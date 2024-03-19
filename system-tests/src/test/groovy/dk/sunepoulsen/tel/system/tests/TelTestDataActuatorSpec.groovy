package dk.sunepoulsen.tel.system.tests

import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealth
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealthStatusCode
import spock.lang.Specification

class TelTestDataActuatorSpec extends Specification {

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
}
