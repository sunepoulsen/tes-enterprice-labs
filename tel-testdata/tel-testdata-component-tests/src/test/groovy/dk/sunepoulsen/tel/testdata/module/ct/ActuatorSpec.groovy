package dk.sunepoulsen.tel.testdata.module.ct

import dk.sunepoulsen.tes.rest.models.monitoring.*
import dk.sunepoulsen.tes.utils.PropertyResource
import spock.lang.Specification

class ActuatorSpec extends Specification {

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
                    version: PropertyResource.readProperty('/deployment.properties', 'module.version'),
                    service: new ServiceInfoService(
                        name: 'tel-testdata-module'
                    )
                )
            )
    }
}
