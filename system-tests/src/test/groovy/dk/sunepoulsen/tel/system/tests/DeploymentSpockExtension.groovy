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
import dk.sunepoulsen.tes.utils.PropertyResource
import dk.sunepoulsen.tes.utils.Resources
import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile

import javax.net.ssl.SSLContext
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.temporal.ChronoUnit

@Slf4j
class DeploymentSpockExtension implements IGlobalExtension {
    private static GenericContainer<?> databaseContainer = null
    private DockerImageProvider databaseDockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'database')
    private static TESBackendContainer telTestDataBackendContainer = null
    private static GenericContainer telWebContainer = null

    private DockerImageProvider telTestDataDockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-testdata')
    private DockerImageProvider telWebDockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-web')

    private static PropertyResource deploymentProperties = new PropertyResource(Resources.readResource('/deployment.properties'))

    DeploymentSpockExtension() {
    }

    static TESBackendContainer telTestDataBackendContainer() {
        return telTestDataBackendContainer
    }

    static TechEasySolutionsBackendIntegrator telTestDataBackendIntegrator() {
        SSLContext sslContext = SSLContextFactory.createSSLContext(new File(deploymentProperties.property('ssl.key-store')), deploymentProperties.property('ssl.key-store-password'))

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

        databaseContainer = new GenericContainer<>(databaseDockerImageProvider.dockerImageName())
            .withEnv('POSTGRES_PASSWORD', deploymentProperties.property('database.super.user.password'))
            .withClasspathResourceMapping('db/001-tel-testdata.sh', '/docker-entrypoint-initdb.d/001-tel-testdata.sh', BindMode.READ_ONLY)
            .waitingFor((new LogMessageWaitStrategy())
                .withRegEx(".*database system is ready to accept connections.*\\s")
                .withTimes(2)
                .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS))
            )
            .withNetworkAliases('postgres')
            .withNetwork(network)
        databaseContainer.start()

        telTestDataBackendContainer = new TESBackendContainer(telTestDataDockerImageProvider, new TESContainerSecureProtocol(), 'systemtests')
            .withClasspathResourceMapping('/config/tel-testdata/application-systemtests.properties', '/app/resources/application-systemtests.properties', BindMode.READ_ONLY)
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.property('ssl.key-store')), "/app/certificates/${deploymentProperties.property('ssl.key-store.filename')}")
            .withNetwork(network)
        telTestDataBackendContainer.start()

        telWebContainer = new GenericContainer(telWebDockerImageProvider.dockerImageName())
            .withExposedPorts(new Integer[]{443})
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.property('certificate.pem.file')), "/var/lib/nginx/tes-enterprise-labs.pem")
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.property('certificate.key.file')), "/var/lib/nginx/tes-enterprise-labs.key")
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.property('certificate.passwords.file')), "/var/lib/nginx/tes-enterprise-labs-passwords.txt")
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

        File databaseLogFile = new File('build/logs/postgres.log')
        databaseLogFile.withWriter(StandardCharsets.UTF_8.name()) { w ->
            w << databaseContainer.logs
        }
        databaseContainer.stop()
    }
}
