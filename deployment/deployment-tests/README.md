# Deployment Test

## Execution

```bash
./gradlew :deployment:deployment-tests:check -Pdeployment-tests -Ddeployment.test.profile=<profile>
```

## Profiles

- **local**: Tests against the local docker deployment.
