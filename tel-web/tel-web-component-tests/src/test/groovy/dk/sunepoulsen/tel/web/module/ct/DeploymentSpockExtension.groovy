package dk.sunepoulsen.tel.web.module.ct

import dk.sunepoulsen.tes.docker.containers.ClasspathPropertiesDockerImageProvider
import dk.sunepoulsen.tes.docker.containers.DockerImageProvider
import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile

@Slf4j
class DeploymentSpockExtension implements IGlobalExtension {
    private static GenericContainer telWebContainer = null
    private DockerImageProvider dockerImageProvider = new ClasspathPropertiesDockerImageProvider('/deployment.properties', 'tel-web')

    DeploymentSpockExtension() {
    }

    static GenericContainer frontendContainer() {
        return telWebContainer
    }

    static String frontendContainerBaseUrl() {
        return "https://${telWebContainer.getHost()}:${telWebContainer.getMappedPort(443)}"
    }

    @Override
    void start() {
        Network network = Network.newNetwork()

        log.debug("Current directory: {}", new File(".").absolutePath)
        telWebContainer = new GenericContainer(this.dockerImageProvider.dockerImageName())
            .withExposedPorts(new Integer[]{443})
            .withCopyFileToContainer(MountableFile.forHostPath("../../certificates/tes-enterprise-labs.pem"), "/var/lib/nginx/tes-enterprise-labs.pem")
            .withCopyFileToContainer(MountableFile.forHostPath("../../certificates/tes-enterprise-labs.key"), "/var/lib/nginx/tes-enterprise-labs.key")
            .withCopyFileToContainer(MountableFile.forHostPath("../../certificates/tes-enterprise-labs-password.txt"), "/var/lib/nginx/tes-enterprise-labs-passwords.txt")
            .waitingFor(Wait.forHttps("/").allowInsecure().forStatusCode(200))
            .withNetwork(network)
        telWebContainer.start()

        log.info('TEL Web Module Exported Port: {}', telWebContainer.getMappedPort(443))
    }

    @Override
    void visitSpec(SpecInfo spec) {
    }

    @Override
    void stop() {
        telWebContainer.copyFileFromContainer('/var/log/nginx/access.log', 'build/logs/tel-web-module-access.log')
        telWebContainer.copyFileFromContainer('/var/log/nginx/error.log', 'build/logs/tel-web-module-error.log')
        telWebContainer.stop()
    }
}
