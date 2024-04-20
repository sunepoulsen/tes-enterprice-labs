package dk.sunepoulsen.tel.deployment.tests.integrations

import dk.sunepoulsen.tel.deployment.tests.config.DeploymentConfig
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsClient
import dk.sunepoulsen.tes.rest.integrations.config.DefaultClientConfig
import dk.sunepoulsen.tes.rest.integrations.config.TechEasySolutionsClientConfig
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealthStatusCode
import dk.sunepoulsen.tes.security.net.ssl.SSLContextFactory
import dk.sunepoulsen.tes.utils.Waits
import groovy.util.logging.Slf4j

import javax.net.ssl.SSLContext
import java.util.concurrent.TimeoutException

@Slf4j
class BackendIntegrations {

    private DeploymentConfig config

    BackendIntegrations(DeploymentConfig config) {
        this.config = config
    }

    TechEasySolutionsClient telTestDataClient() {
        SSLContext sslContext = SSLContextFactory.createSSLContext(config.certificateFile, config.certificatePassword)

        TechEasySolutionsClientConfig clientConfig = new DefaultClientConfig(sslContext)
        return new TechEasySolutionsClient(config.telTestDataUrl, clientConfig)
    }

    TechEasySolutionsBackendIntegrator telTestDataIntegrator() {
        return new TechEasySolutionsBackendIntegrator(telTestDataClient())
    }

    void waitFor(TechEasySolutionsBackendIntegrator integrator) {
        long start = System.currentTimeMillis()
        boolean isAvailable = Waits.waitFor(config.waitDuration, Waits.DEFAULT_SLEEP_DURATION) {
            integrator.health().blockingGet().status == ServiceHealthStatusCode.UP
        }

        if (isAvailable) {
            long end = System.currentTimeMillis()
            log.info('{} is available in {} seconds', integrator.httpClient.uri.toString(), Math.floorDiv(end - start, 1000) + 1)
            return
        }

        throw new TimeoutException("The service ${integrator.httpClient.uri.toString()} was not available with in ${config.waitDuration.toSeconds()} seconds")
    }
}
