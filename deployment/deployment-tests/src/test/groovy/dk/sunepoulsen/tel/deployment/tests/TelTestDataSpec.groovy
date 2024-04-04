package dk.sunepoulsen.tel.deployment.tests

import dk.sunepoulsen.tel.deployment.tests.config.DeploymentConfig
import dk.sunepoulsen.tel.deployment.tests.integrations.BackendIntegrations
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientNotFoundException
import dk.sunepoulsen.tes.rest.models.HashMapModel
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceInfo
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceInfoApp
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceInfoService
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceMetrics
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification

class TelTestDataSpec extends Specification {

    private DeploymentConfig config
    private BackendIntegrations integrations
    private TechEasySolutionsBackendIntegrator telTestDataIntegrator

    void setup() {
        this.config = new DeploymentConfig()
        this.integrations = new BackendIntegrations(this.config)
        this.telTestDataIntegrator = this.integrations.telTestDataIntegrator()
    }

    void "GET /actuator/info returns OK"() {
        given: 'TEL TestData is available'
            this.integrations.waitFor(this.telTestDataIntegrator)

        when: 'Call GET /actuator/info'
            ServiceInfo result = this.telTestDataIntegrator.info().blockingGet()

        then: 'Verify info body'
            result == new ServiceInfo(
                app: new ServiceInfoApp(
                    name: 'TES Enterprise Labs TestData Module',
                    version: this.config.moduleVersion,
                    service: new ServiceInfoService(
                        name: 'tel-testdata-module'
                    )
                )
            )

    }

    @IgnoreIf({ DeploymentConfig.instance().envEnabled })
    void "GET /actuator/env returns 404"() {
        given: 'TEL TestData is available'
            this.integrations.waitFor(this.telTestDataIntegrator)

        when: 'Call GET /actuator/env'
            this.telTestDataIntegrator.env().blockingGet()

        then: 'Verify response code 404'
            thrown(ClientNotFoundException)
    }

    @Requires({ DeploymentConfig.instance().envEnabled })
    void "GET /actuator/env returns OK"() {
        given: 'TEL TestData is available'
            this.integrations.waitFor(this.telTestDataIntegrator)

        when: 'Call GET /actuator/env'
            HashMapModel model = this.telTestDataIntegrator.env().blockingGet()

        then: 'Verify response code 200'
            model.size() == 2
            model.containsKey('activeProfiles')
            model.containsKey('propertySources')
    }

    @IgnoreIf({ DeploymentConfig.instance().metricsEnabled })
    void "GET /actuator/metrics returns 404"() {
        given: 'TEL TestData is available'
            this.integrations.waitFor(this.telTestDataIntegrator)

        when: 'Call GET /actuator/metrics'
            this.telTestDataIntegrator.metrics().blockingGet()

        then: 'Verify response code 404'
            thrown(ClientNotFoundException)
    }

    @Requires({ DeploymentConfig.instance().metricsEnabled })
    void "GET /actuator/metrics returns OK"() {
        given: 'TEL TestData is available'
            this.integrations.waitFor(this.telTestDataIntegrator)

        when: 'Call GET /actuator/metrics'
            ServiceMetrics model = this.telTestDataIntegrator.metrics().blockingGet()

        then: 'Verify response code 200'
            model.names.size() > 0
    }

    @IgnoreIf({ DeploymentConfig.instance().metricsEnabled })
    void "GET /actuator/metrics/application.ready.time returns 404"() {
        given: 'TEL TestData is available'
            this.integrations.waitFor(this.telTestDataIntegrator)

        when: 'Call GET /actuator/metrics/application.ready.time'
            this.telTestDataIntegrator.metric('application.ready.time').blockingGet()

        then: 'Verify response code 404'
            thrown(ClientNotFoundException)
    }

    @Requires({ DeploymentConfig.instance().metricsEnabled })
    void "GET /actuator/metrics/application.ready.time returns OK"() {
        given: 'TEL TestData is available'
            this.integrations.waitFor(this.telTestDataIntegrator)

        when: 'Call GET /actuator/metrics/application.ready.time'
            HashMapModel model = this.telTestDataIntegrator.metric('application.ready.time').blockingGet()

        then: 'Verify response code 200'
            model.name == 'application.ready.time'
            model.baseUnit == 'seconds'
            model.measurements[0].value < 20.0
    }
}
