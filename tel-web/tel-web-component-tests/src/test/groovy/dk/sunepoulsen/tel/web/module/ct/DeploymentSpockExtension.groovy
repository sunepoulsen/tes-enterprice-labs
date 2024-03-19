package dk.sunepoulsen.tel.web.module.ct

import groovy.util.logging.Slf4j
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

@Slf4j
class DeploymentSpockExtension implements IGlobalExtension {
    private static GenericContainer telWebContainer = null
    private DockerImageName telWebDockerImageName

    DeploymentSpockExtension() {
        Properties deploymentProperties = loadDeploymentProperties()
        String frontendImageName = deploymentProperties.getProperty('image.name')
        String frontendImageTag = deploymentProperties.getProperty('image.tag')

        this.telWebDockerImageName = DockerImageName.parse(frontendImageName + ":" + frontendImageTag)
    }

    static GenericContainer frontendContainer() {
        return telWebContainer
    }

    static String frontendContainerBaseUrl() {
        return "http://${telWebContainer.getHost()}:${telWebContainer.getMappedPort(8080)}"
    }

    @Override
    void start() {
        Network network = Network.newNetwork()

        telWebContainer = new GenericContainer(this.telWebDockerImageName)
            .withExposedPorts(new Integer[]{8080})
            .waitingFor(Wait.forHttp("/").forStatusCode(200))
            .withNetwork(network)
        telWebContainer.start()

        log.info('ViDA DDR Reportings Web Exported Port: {}', telWebContainer.getMappedPort(8080))
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

    private static Properties loadDeploymentProperties() {
        Properties props = new Properties()
        props.load(DeploymentSpockExtension.class.getResourceAsStream('/deployment.properties') )
        return props
    }
}
