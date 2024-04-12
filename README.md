# TES Enterprise Labs

TES Enterprise Labs is an enterprise solution to test out enterprise technologies such as Spring Boot, Kafka, Machine Learning, etc.  

## Domains

The solution has the following domains:

- [Tel TestData](tel-testdata/README.md): Backend module to generate and store test data.
- [Tel Web](tel-web/README.md): Website to visualise features from TES Enterprise Labs.

## Building

The project is built with Gradle and is requiring Java 21.

The project is integrated with [Sonatype Nexus Repository](https://www.sonatype.com/products/sonatype-nexus-repository)
and [SonarQube](https://www.sonarsource.com/products/sonarqube/) and is using the following properties from
`~/.gradle/gradle.properties`:

```properties
# System properties for Nexus

systemProp.maven.repository.base.url=<url to Nexus>
systemProp.maven.repository.snapshots=maven-snapshots
systemProp.maven.repository.releases=maven-releases

systemProp.maven.repository.username=<Username for the account to connect to Nexus>
systemProp.maven.repository.password=<Password for the user account>

# System properties for SonarQube

systemProp.sonar.host.url=<URL to SonarQube>
systemProp.sonar.login=<Auth token to SonarQube>

# System properties for TES Enterprise Labs
systemProp.tel.database.super.user.password=<password for the database super user>
systemProp.tel.database.admin.password=<password for the database admin user>
systemProp.tel.database.application.password=<password for the database application user>
```
The `tel.database.admin.password` and `tel.database.application.password` is used for all databases
in the solution.

### Pipeline

A complete pipeline to build the project can be used with:

```shell
./pipeline.sh
```

This pipeline has the following steps:

1. Clean the repository.
2. Build the `TEL TestData` backend including build a docker image.
3. Run the component tests against the `TEL TestData` backend with the docker image from step 2.
5. Build the `TEL Web` frontend.
6. Build a docker image of the `TEL Web` frontend.
7. Run the component tests against the `TEL Web` frontend using the docker image from step 6.
8. Run a system test with all the deployable components of the solution.
9. Analyzing the project with SonarQube - including check of dependency vulnerabilities.

The pipeline selects the required Java version to build the project. To get it to work the
developer needs:

1. A working installation of [SDK Man](https://sdkman.io/)
2. Installed the java distribution of `21.0.1-tem` with `sdk`. This distribution does not need to be active,
   but it needs to be installed.

If you want to change the Java distribution being used then you can overwrite the variable `JAVA_VERSION` in
`pipeline-tools.sh`

## Running

The different backend modules can be run by a normal Spring Boot Run configuration in IntelliJ.

There are created some Docker Compose configurations to run the entire solution with Docker. The documentation of these
configurations can be found here: [Docker Compose Configurations](deployment/docker/README.md)

## Code Analysing

The project can be analyzed with [SonarQube](https://www.sonarsource.com/products/sonarqube/) with:

```shell
source pipeline-tools.sh
./pipeline-analyze.sh
```

This will also analyze all dependencies for vulnerabilities. This is done with the
[org.owasp:dependency-check-gradle](https://github.com/dependency-check/dependency-check-gradle) plugin.

Analysing the vulnerabilities without the SonarQube analysis can be done with:

```shell
source pipeline-tools.sh
./gradlew dependencyCheckAggregate
```

The result of the scan can be found here: `build/reports/dependency-check-*`
