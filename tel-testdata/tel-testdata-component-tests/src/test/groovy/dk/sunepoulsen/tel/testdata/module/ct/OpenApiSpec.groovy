package dk.sunepoulsen.tel.testdata.module.ct

import dk.sunepoulsen.tes.selenium.WebDriverFactory
import dk.sunepoulsen.tes.selenium.verifiers.OpenApiVerifier
import org.openqa.selenium.WebDriver
import spock.lang.Specification
import spock.lang.Unroll

class OpenApiSpec extends Specification {

    private static WebDriver webDriver
    private OpenApiVerifier openApiVerifier

    void setup() {
        openApiVerifier = new OpenApiVerifier(webDriver, DeploymentSpockExtension.telTestDataBackendContainer().baseUrl())
    }

    void setupSpec() {
        webDriver = new WebDriverFactory().createWebDriver()
    }

    void cleanupSpec() {
        webDriver.quit()
    }

    void "Verify title of the OpenApi documentation"() {
        given: 'TEL Metrics Module is available'
            DeploymentSpockExtension.telTestDataBackendContainer().isHostAccessible()

        when: 'Call GET /swagger-ui.html'
            openApiVerifier.visit('/swagger-ui.html')

        then: 'Verify application title'
            openApiVerifier.verifyCurrentUrlPath('/swagger-ui/index.html')
            openApiVerifier.verifyTitle('''OpenAPI definition
 v0 
OAS 3.0''')
    }

    @Unroll
    void "Has endpoint #_method #_path"() {
        given: 'TEL Metrics Module is available'
            DeploymentSpockExtension.telTestDataBackendContainer().isHostAccessible()

        when: 'Call GET /swagger-ui.html'
            openApiVerifier.visit('/swagger-ui.html')

        then: 'Verify application title'
            openApiVerifier.verifyCurrentUrlPath('/swagger-ui/index.html')
            openApiVerifier.verifyEndpoint(_id, _method, _path)

        where:
            _id                                  | _method  | _path
            'Actuator-links'                     | 'GET'    | '/actuator'
            'Actuator-info'                      | 'GET'    | '/actuator/info'
            'Actuator-health'                    | 'GET'    | '/actuator/health'
            'Actuator-health-path'               | 'GET'    | '/actuator/health/**'
            'Data_Points-createDataPointDataSet' | 'POST'   | '/datasets/data-points'
            'Tests-deletePersistence'            | 'DELETE' | '/tests/persistence'
    }

}
