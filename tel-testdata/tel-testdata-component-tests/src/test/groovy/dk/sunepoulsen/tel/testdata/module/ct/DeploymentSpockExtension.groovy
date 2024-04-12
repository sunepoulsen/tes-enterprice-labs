package dk.sunepoulsen.tel.testdata.module.ct

import dk.sunepoulsen.tel.testdata.module.integrator.TelTestDataIntegrator
import dk.sunepoulsen.tel.testdata.module.integrator.TelTestDataTestsIntegrator
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
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
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
    private DockerImageProvider dockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-testdata')
    private static Properties deploymentProperties = loadDeploymentProperties()

    static Properties loadDeploymentProperties() {
        Properties props = new Properties()
        props.load(DeploymentSpockExtension.class.getResourceAsStream('/deployment.properties') )
        return props
    }

    static TESBackendContainer telTestDataBackendContainer() {
        return telTestDataBackendContainer
    }

    static TelTestDataIntegrator telTestDataBackendIntegrator() {
        SSLContext sslContext = SSLContextFactory.createSSLContext(new File(deploymentProperties.getProperty('ssl.key-store')), deploymentProperties.getProperty('ssl.key-store-password'))

        TechEasySolutionsClientConfig clientConfig = new DefaultClientConfig(sslContext)
        TechEasySolutionsClient client = telTestDataBackendContainer.createClient(clientConfig)

        return new TelTestDataIntegrator(client)
    }

    static TelTestDataTestsIntegrator telTestDataBackendTestsIntegrator() {
        SSLContext sslContext = SSLContextFactory.createSSLContext(new File(deploymentProperties.getProperty('ssl.key-store')), deploymentProperties.getProperty('ssl.key-store-password'))

        TechEasySolutionsClientConfig clientConfig = new DefaultClientConfig(sslContext)
        TechEasySolutionsClient client = telTestDataBackendContainer.createClient(clientConfig)

        return new TelTestDataTestsIntegrator(client)
    }

    @Override
    void start() {
        Network network = Network.newNetwork()

        databaseContainer = new GenericContainer<>(databaseDockerImageProvider.dockerImageName())
                .withEnv('POSTGRES_PASSWORD', deploymentProperties.getProperty('database.super.user.password'))
                .withClasspathResourceMapping('db/001-tel-testdata.sh', '/docker-entrypoint-initdb.d/001-tel-testdata.sh', BindMode.READ_ONLY)
                .waitingFor((new LogMessageWaitStrategy())
                    .withRegEx(".*database system is ready to accept connections.*\\s")
                    .withTimes(2)
                    .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS))
                )
                .withNetworkAliases('postgres')
                .withNetwork(network)
        databaseContainer.start()

        log.debug("Current directory: {}", new File(".").absolutePath)
        telTestDataBackendContainer = new TESBackendContainer(dockerImageProvider, new TESContainerSecureProtocol(), 'ct')
            .withConfigMapping('application-ct.properties')
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.getProperty('ssl.key-store')), "/app/certificates/${deploymentProperties.getProperty('ssl.key-store.filename')}")
            .withNetwork(network)
        telTestDataBackendContainer.start()

        log.info('TEL TestData Module Exported Port: {}', telTestDataBackendContainer.getMappedPort(8080))
    }

    @Override
    void visitSpec(SpecInfo spec) {
    }

    @Override
    void stop() {
        telTestDataBackendContainer.copyLogFile('build/logs/tel-testdata-module.log')
        telTestDataBackendContainer.stop()

        File databaseLogFile = new File('build/logs/postgres.log')
        databaseLogFile.withWriter(StandardCharsets.UTF_8.name()) { w ->
            w << databaseContainer.logs
        }
        databaseContainer.stop()
    }

}
