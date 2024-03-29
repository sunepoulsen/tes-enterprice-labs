package dk.sunepoulsen.tel.system.tests

import dk.sunepoulsen.tes.docker.containers.ClasspathPropertiesDockerImageProvider
import dk.sunepoulsen.tes.docker.containers.DockerImageProvider
import dk.sunepoulsen.tes.docker.containers.TESBackendContainer
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator
import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait

@Slf4j
class DeploymentSpockExtension implements IGlobalExtension {
    private static TESBackendContainer telTestDataBackendContainer = null
    private static GenericContainer telWebContainer = null

    private DockerImageProvider telTestDataDockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-testdata')
    private DockerImageProvider telWebDockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-web')

    DeploymentSpockExtension() {
    }

    static TESBackendContainer telTestDataBackendContainer() {
        return telTestDataBackendContainer
    }

    static TechEasySolutionsBackendIntegrator telTestDataBackendIntegrator() {
        return new TechEasySolutionsBackendIntegrator(telTestDataBackendContainer.createClient())
    }

    static GenericContainer telWebContainer() {
        return telWebContainer
    }

    static String telWebContainerBaseUrl() {
        return "http://${telWebContainer.getHost()}:${telWebContainer.getMappedPort(8080)}"
    }

    @Override
    void start() {
        Network network = Network.newNetwork()

        telTestDataBackendContainer = new TESBackendContainer(telTestDataDockerImageProvider, 'systemtests')
            .withClasspathResourceMapping('/config/tel-testdata/application-systemtests.properties', '/app/resources/application-systemtests.properties', BindMode.READ_ONLY)
            .withNetwork(network)
        telTestDataBackendContainer.start()

        telWebContainer = new GenericContainer(telWebDockerImageProvider.dockerImageName())
            .withExposedPorts(new Integer[]{8080})
            .waitingFor(Wait.forHttp("/").forStatusCode(200))
            .withNetwork(network)
        telWebContainer.start()

        log.info('TEL TestData Module Exported Port: {}', telTestDataBackendContainer.getMappedPort(8080))
        log.info('TEL Web Module Exported Port: {}', telWebContainer.getMappedPort(8080))
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
