package dk.sunepoulsen.tel.system.tests

import dk.sunepoulsen.tes.selenium.WebDriverFactory
import dk.sunepoulsen.tes.selenium.verifiers.WebVerifier
import groovy.util.logging.Slf4j
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import spock.lang.Specification

@Slf4j
class TelWebHomePageSpec extends Specification {

    private static WebDriver webDriver
    private WebVerifier webVerifier

    void setup() {
        webVerifier = new WebVerifier(webDriver, new URI(DeploymentSpockExtension.telWebContainerBaseUrl()))
    }

    void setupSpec() {
        webDriver = new WebDriverFactory().createWebDriver()
    }

    void cleanupSpec() {
        webDriver.quit()
    }

    void "GET / returns OK"() {
        given: 'Frontend service is available'
            DeploymentSpockExtension.telWebContainer().isHostAccessible()

        when: 'Call GET /'
            webVerifier.visit('/')

        then: 'Verify application title'
            webVerifier.verifyVisibleTextElement(By.cssSelector('span.app-title'), 'TES Enterprise Labs')
    }
}
