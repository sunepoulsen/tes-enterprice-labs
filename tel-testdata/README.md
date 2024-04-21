# TEL TestData

TEL TestData is a backend service with the responsibility to generate and store test data.

These test data can be accessed from other backends or the [Tel-Web](../tel-web/README.md)

## Features

The backend has the following features:

1. Store data sets of data points.

## Tests

### Component Tests

Component Tests are placed in `tel-testdata-component-tests` and can be executed with:

```
./gradlew :tel-testdata:tel-testdata-component-tests:check -Ptel-testdata-component-tests
```
Component tests are not executed as part of a normal build of the entire solution.

### Stress Tests

Stress Tests are placed in `tel-testdata-stress-tests` and can be executed with:

```
./gradlew :tel-testdata:tel-testdata-stress-tests:gatlingRun -Dstress.test.profile=<profile>
```

Stress tests are not executed as part of a normal build of the entire solution.
