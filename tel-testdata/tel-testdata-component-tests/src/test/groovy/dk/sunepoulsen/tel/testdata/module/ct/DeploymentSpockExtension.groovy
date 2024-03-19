package dk.sunepoulsen.tel.testdata.module.ct

import dk.sunepoulsen.tes.docker.containers.TESBackendContainer
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator
import groovy.util.logging.Slf4j
import org.slf4j.bridge.SLF4JBridgeHandler
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo
import org.testcontainers.containers.Network

@Slf4j
class DeploymentSpockExtension implements IGlobalExtension {
    private static TESBackendContainer telTestDataBackendContainer = null

    static Properties loadDeploymentProperties() {
        Properties props = new Properties()
        props.load(DeploymentSpockExtension.class.getResourceAsStream('/deployment.properties') )
        return props
    }

    static TESBackendContainer telTestDataBackendContainer() {
        return telTestDataBackendContainer
    }

    static TechEasySolutionsBackendIntegrator telTestDataBackendIntegrator() {
        return new TechEasySolutionsBackendIntegrator(telTestDataBackendContainer.createClient())
    }

    static String storeBackendBaseUrl() {
        return "http://${telTestDataBackendContainer.getHost()}:${telTestDataBackendContainer.getMappedPort(8080)}"
    }

    @Override
    void start() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        System.setProperty('jdk.httpclient.HttpClient.log', 'errors,requests,headers,content,frames,ssl,trace,channel')

        Properties deploymentProperties = loadDeploymentProperties()
        String imageName = deploymentProperties.getProperty('image.name')
        String imageTag = deploymentProperties.getProperty('image.tag')

        Network network = Network.newNetwork()

        telTestDataBackendContainer = new TESBackendContainer(imageName, imageTag, 'ct')
            .withConfigMapping('application-ct.properties')
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
    }

}
