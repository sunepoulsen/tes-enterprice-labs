package dk.sunepoulsen.tel.deployment.tests

import dk.sunepoulsen.tel.deployment.tests.config.DeploymentConfig
import dk.sunepoulsen.tel.deployment.tests.integrations.BackendIntegrations
import dk.sunepoulsen.tes.selenium.WebDriverFactory
import dk.sunepoulsen.tes.selenium.verifiers.WebVerifier
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import spock.lang.Specification

class TelWebSpec extends Specification {

    private DeploymentConfig config
    private BackendIntegrations integrations
    private static WebDriver webDriver
    private WebVerifier webVerifier

    void setup() {
        this.config = new DeploymentConfig()
        webVerifier = new WebVerifier(webDriver, this.config.telWebUrl, this.config.waitDuration)
    }

    void setupSpec() {
        webDriver = new WebDriverFactory().createWebDriver()
    }

    void cleanupSpec() {
        webDriver.quit()
    }

    void "GET / returns OK"() {
        when: 'Call GET /'
            webVerifier.visit('/')

        then: 'Verify application title'
            webVerifier.verifyVisibleTextElement(By.cssSelector('span.app-title'), 'TES Enterprise Labs')
    }

}
