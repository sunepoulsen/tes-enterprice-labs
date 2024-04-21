package dk.sunepoulsen.tel.testdata.module.stress.tests.simulations;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class OperationsChains {

    private ScenarioConfig config;

    public OperationsChains(ScenarioConfig config) {
        this.config = config;
    }

    ChainBuilder healthSample() {
        return sample(
            exec(
                http("Health check")
                    .get("/actuator/health")
                    .check(
                        status().is(200),
                        jmesPath("status").is("UP")
                    )
            )
        );
    }

    private ChainBuilder sample(ChainBuilder chainBuilder) {
        return chainBuilder.exec(
            pause(config.getRequestsPause())
        );
    }
}
