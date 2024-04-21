package dk.sunepoulsen.tel.testdata.module.stress.tests.simulations;

import dk.sunepoulsen.tel.testdata.module.stress.tests.deployment.Deployment;
import dk.sunepoulsen.tes.docker.exceptions.DockerImageProviderException;
import dk.sunepoulsen.tes.utils.exceptions.PropertyResourceException;
import dk.sunepoulsen.tes.utils.exceptions.ResourceException;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.io.IOException;
import java.net.URISyntaxException;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class OperationsSimulation extends Simulation {

    private Deployment deployment;
    private Config config;
    private OperationsChains operationsChains;

    public OperationsSimulation() throws PropertyResourceException, ResourceException, DockerImageProviderException, URISyntaxException {
        this.config = new Config();
        this.operationsChains = new OperationsChains(this.config.getOperationsConfig());

        deployment = new Deployment();
        deployment.start();

        initSimulation();
    }

    @Override
    public void after() {
        try {
            deployment.shutdown();
        } catch (IOException | PropertyResourceException ex) {
            throw new RuntimeException(ex);
        }
    }

    void initSimulation() throws URISyntaxException {
        setUp(
            adminScenario().injectOpen(
                nothingFor(this.config.getOperationsConfig().getStartupDuration()),
                atOnceUsers(this.config.getOperationsConfig().getAtOnceUsers()),
                rampUsers(this.config.getOperationsConfig().getRampUsers())
                    .during(this.config.getOperationsConfig().getRampDuration())
            )
        ).protocols(
            http.baseUrl(deployment.containerBaseUrl())
                .userAgentHeader(
                    "Gatling " + this.config.getGatlingVersion()
                ));

    }

    ScenarioBuilder adminScenario() {
        return scenario("Admins")
            .during(this.config.getOperationsConfig().getUsersDuration()).on(
                exec(
                    this.operationsChains.healthSample()
                )
            );
    }

    /*
    ChainBuilder health = exec(
        http("Health check")
            .get("/actuator/health")
            .check(
                status().is(200),
                jmesPath("status").shouldBe("UP")
            ),
        pause(Duration.ofMillis(50))
    );


    ScenarioBuilder admins = scenario("Admins")
        .during(Duration.ofSeconds(30)).on(
            exec(
                health
            )
        );
     */

}
