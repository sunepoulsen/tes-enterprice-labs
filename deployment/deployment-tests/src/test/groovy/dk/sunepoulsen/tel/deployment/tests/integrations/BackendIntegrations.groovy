package dk.sunepoulsen.tel.deployment.tests.integrations

import dk.sunepoulsen.tel.deployment.tests.config.DeploymentConfig
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsClient
import dk.sunepoulsen.tes.rest.integrations.config.DefaultClientConfig
import dk.sunepoulsen.tes.rest.integrations.config.TechEasySolutionsClientConfig
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealthStatusCode
import dk.sunepoulsen.tes.security.net.ssl.SSLContextFactory
import groovy.util.logging.Slf4j

import javax.net.ssl.SSLContext
import java.net.http.HttpClient

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
        long end = System.currentTimeMillis()

        while((end - start) < config.waitDuration.toMillis()) {
            try {
                if (integrator.health().blockingGet().status == ServiceHealthStatusCode.UP) {
                    log.info('{} was available in {} seconds', integrator.httpClient.uri.toString(), Math.floorDiv(end - start, 1000) + 1)
                    return
                }
            } catch( Exception ignored ) {
            }

            Thread.sleep(200)
            end = System.currentTimeMillis()
        }

        log.error('Unable to connect to {} after {} seconds', integrator.httpClient.uri.toString(), config.waitDuration.toSeconds())
    }
}
