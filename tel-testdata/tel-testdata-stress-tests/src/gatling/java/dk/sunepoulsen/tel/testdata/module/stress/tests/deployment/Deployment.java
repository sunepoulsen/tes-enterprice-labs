package dk.sunepoulsen.tel.testdata.module.stress.tests.deployment;

import dk.sunepoulsen.tes.docker.containers.ClasspathPropertiesDockerImageProvider;
import dk.sunepoulsen.tes.docker.containers.DockerImageProvider;
import dk.sunepoulsen.tes.docker.containers.TESBackendContainer;
import dk.sunepoulsen.tes.docker.containers.TESContainerSecureProtocol;
import dk.sunepoulsen.tes.docker.exceptions.DockerImageProviderException;
import dk.sunepoulsen.tes.utils.PropertyResource;
import dk.sunepoulsen.tes.utils.Resources;
import dk.sunepoulsen.tes.utils.exceptions.PropertyResourceException;
import dk.sunepoulsen.tes.utils.exceptions.ResourceException;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
public class Deployment {

    private static final String MODULE_BUILD_DIR_PROPERTY = "module.build.dir";
    private static final String LOGS_DIRNAME = "logs";

    private GenericContainer<?> databaseContainer;
    private DockerImageProvider databaseDockerImageProvider;
    private TESBackendContainer telTestDataBackendContainer;
    private DockerImageProvider dockerImageProvider;
    private PropertyResource deploymentProperties;

    public Deployment() throws ResourceException {
        databaseContainer = null;
        databaseDockerImageProvider = new ClasspathPropertiesDockerImageProvider("/deployment/deployment.properties", "database");
        telTestDataBackendContainer = null;
        dockerImageProvider = new ClasspathPropertiesDockerImageProvider("/deployment/deployment.properties", "tel-testdata");
        deploymentProperties = new PropertyResource(Resources.readResource("/deployment/deployment.properties"));
    }

    public String containerBaseUrl() throws URISyntaxException {
        return telTestDataBackendContainer.baseUrl().toString();
    }

    public void start() throws DockerImageProviderException, PropertyResourceException, URISyntaxException {
        log.info("Starting deployment of the Gatling Stress Tests:");

        Network network = Network.newNetwork();

        databaseContainer = new GenericContainer<>(databaseDockerImageProvider.dockerImageName())
            .withEnv("POSTGRES_PASSWORD", deploymentProperties.property("database.super.user.password"))
            .withClasspathResourceMapping("db/001-tel-testdata.sh", "/docker-entrypoint-initdb.d/001-tel-testdata.sh", BindMode.READ_ONLY)
            .waitingFor((new LogMessageWaitStrategy())
                .withRegEx(".*database system is ready to accept connections.*\\s")
                .withTimes(2)
                .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS))
            )
            .withNetworkAliases("postgres")
            .withNetwork(network);
        log.info("   Starting PostgreSQL container");
        databaseContainer.start();
        log.info("   Started PostgreSQL container");

        telTestDataBackendContainer = new TESBackendContainer(dockerImageProvider, new TESContainerSecureProtocol(), "stresstest")
            .withClasspathResourceMapping("/deployment/application-stresstest.properties", "/app/resources/application-stresstest.properties", BindMode.READ_ONLY)
            .withCopyFileToContainer(MountableFile.forHostPath(deploymentProperties.property("ssl.key-store")), "/app/certificates/" + deploymentProperties.property("ssl.key-store.filename"))
            .withNetwork(network);

        log.info("   Starting TEL-TestData container");
        telTestDataBackendContainer.start();
        log.info("   Started TEL-TestData container. It is available at {}", containerBaseUrl());
    }

    public void shutdown() throws IOException, PropertyResourceException {
        log.info("Shutting down deployment of the Gatling Stress Tests");
        log.debug("Current directory: {}", new File(".").getAbsolutePath());
        Path logDirectory = FileSystems.getDefault().getPath(deploymentProperties.property(MODULE_BUILD_DIR_PROPERTY), LOGS_DIRNAME);
        Files.createDirectories(logDirectory);

        telTestDataBackendContainer.copyLogFile(deploymentProperties.property(MODULE_BUILD_DIR_PROPERTY) + "/" + LOGS_DIRNAME + "/tel-testdata-module.log");
        telTestDataBackendContainer.stop();

        Path databaseLogFile = FileSystems.getDefault().getPath(deploymentProperties.property(MODULE_BUILD_DIR_PROPERTY), LOGS_DIRNAME, "postgres.log");
        Files.writeString(databaseLogFile, databaseContainer.getLogs());

        databaseContainer.stop();
    }
}
