package dk.sunepoulsen.tel.testdata.module.ct

import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealth
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealthStatusCode
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceInfo
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceInfoApp
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceInfoService
import spock.lang.Specification

class ActuatorSpec extends Specification {

    private String moduleVersion

    void setup() {
        Properties deploymentProperties = DeploymentSpockExtension.loadDeploymentProperties()
        moduleVersion = deploymentProperties.getProperty('module.version')
    }

    void "GET /actuator/health returns OK"() {
        given: 'Service is available'
            DeploymentSpockExtension.telTestDataBackendContainer().isHostAccessible()

        when: 'Call GET /actuator/health'
            ServiceHealth result = DeploymentSpockExtension.telTestDataBackendIntegrator().health().blockingGet()

        then: 'Verify health body'
            result == new ServiceHealth(
                status: ServiceHealthStatusCode.UP
            )
    }

    void "GET /actuator/info returns OK"() {
        given: 'Service is available'
            DeploymentSpockExtension.telTestDataBackendContainer().isHostAccessible()

        when: 'Call GET /actuator/info'
            ServiceInfo result = DeploymentSpockExtension.telTestDataBackendIntegrator().info().blockingGet()

        then: 'Verify info body'
            result == new ServiceInfo(
                app: new ServiceInfoApp(
                    name: 'TES Enterprise Labs TestData Module',
                    version: moduleVersion,
                    service: new ServiceInfoService(
                        name: 'tel-testdata-module'
                    )
                )
            )
    }
}
