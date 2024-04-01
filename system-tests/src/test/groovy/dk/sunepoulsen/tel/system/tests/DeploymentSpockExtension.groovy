package dk.sunepoulsen.tel.system.tests

import dk.sunepoulsen.tes.docker.containers.ClasspathPropertiesDockerImageProvider
import dk.sunepoulsen.tes.docker.containers.DockerImageProvider
import dk.sunepoulsen.tes.docker.containers.TESBackendContainer
import dk.sunepoulsen.tes.docker.containers.TESContainerSecureProtocol
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsClient
import dk.sunepoulsen.tes.rest.integrations.config.DefaultClientConfig
import dk.sunepoulsen.tes.rest.integrations.config.TechEasySolutionsClientConfig
import dk.sunepoulsen.tes.security.net.ssl.SSLContextFactory
import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile

import javax.net.ssl.SSLContext

@Slf4j
class DeploymentSpockExtension implements IGlobalExtension {
    private static TESBackendContainer telTestDataBackendContainer = null
    private static GenericContainer telWebContainer = null

    private DockerImageProvider telTestDataDockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-testdata')
    private DockerImageProvider telWebDockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-web')

    private static Properties deploymentProperties = loadDeploymentProperties()

    DeploymentSpockExtension() {
    }

    static Properties loadDeploymentProperties() {
        Properties props = new Properties()
        props.load(DeploymentSpockExtension.class.getResourceAsStream('/deployment.properties') )
        return props
    }

    static TESBackendContainer telTestDataBackendContainer() {
        return telTestDataBackendContainer
    }

    static TechEasySolutionsBackendIntegrator telTestDataBackendIntegrator() {
        SSLContext sslContext = SSLContextFactory.createSSLContext(new File(deploymentProperties.getProperty('ssl.key-store')), deploymentProperties.getProperty('ssl.key-store-password'))

        TechEasySolutionsClientConfig clientConfig = new DefaultClientConfig(sslContext)
        TechEasySolutionsClient client = telTestDataBackendContainer.createClient(clientConfig)

        return new TechEasySolutionsBackendIntegrator(client)
    }

    static GenericContainer telWebContainer() {
        return telWebContainer
    }

    static String telWebContainerBaseUrl() {
        return "https://${telWebContainer.getHost()}:${telWebContainer.getMappedPort(443)}"
    }

    @Override
    void start() {
        Network network = Network.newNetwork()

        telTestDataBackendContainer = new TESBackendContainer(telTestDataDockerImageProvider, new TESContainerSecureProtocol(), 'systemtests')
            .withClasspathResourceMapping('/config/tel-testdata/application-systemtests.properties', '/app/resources/application-systemtests.properties', BindMode.READ_ONLY)
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.getProperty('ssl.key-store')), "/app/certificates/${deploymentProperties.getProperty('ssl.key-store.filename')}")
            .withNetwork(network)
        telTestDataBackendContainer.start()

        telWebContainer = new GenericContainer(telWebDockerImageProvider.dockerImageName())
            .withExposedPorts(new Integer[]{443})
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.getProperty('certificate.pem.file')), "/var/lib/nginx/tes-enterprise-labs.pem")
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.getProperty('certificate.key.file')), "/var/lib/nginx/tes-enterprise-labs.key")
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.getProperty('certificate.passwords.file')), "/var/lib/nginx/tes-enterprise-labs-passwords.txt")
            .waitingFor(Wait.forHttps("/").allowInsecure().forStatusCode(200))
            .withNetwork(network)
        telWebContainer.start()

        log.info('TEL TestData Module Exported Port: {}', telTestDataBackendContainer.getMappedPort(8080))
        log.info('TEL Web Module Exported Port: {}', telWebContainer.getMappedPort(443))
    }

    @Override
    void visitSpec(SpecInfo spec) {
    }

    @Override
    void stop() {
        telTestDataBackendContainer.copyLogFile('build/logs/tel-testdata-module.log')
        telTestDataBackendContainer.stop()

        telWebContainer.copyFileFromContainer('/var/log/nginx/access.log', 'build/logs/tel-web-module-access.log')
        telWebContainer.copyFileFromContainer('/var/log/nginx/error.log', 'build/logs/tel-web-module-error.log')
        telWebContainer.stop()
    }
}
