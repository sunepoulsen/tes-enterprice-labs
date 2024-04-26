# DevOps

## Building pipelines

`TES Enterprise Labs` does not specify a CI/CD pipeline for building the libraries. But it defines
a set of Python and Bash scripts that can be used to build a "local pipeline".

### Prerequisites

To use the local pipeline the following tools are required

#### SDK Man

[SDK Man](https://sdkman.io/) is used to install the required Java version. It the moment this repo
required Java 21.

#### Python 3

The pipeline is written in python 3 and bash.

### Local pipeline

This local pipeline can be executed with:

```bash
./pipeline.sh
```

Each step of the pipeline is implemented as a python script. This is done so its possible to share the
execution of a pipeline step between `Jenkins` and local execution.

The pipeline defines the following steps that is executed in order:

| **Step**                             | **Script**                                     | **Description**                                                                            |
|--------------------------------------|------------------------------------------------|--------------------------------------------------------------------------------------------|
| **Clean**                            | `pipeline-clean.py`                            | Clean the repository.                                                                      |
| **Make secrets**                     | `pipeline-secrets.py`                          | Generate temporary secrets necessary for tests                                             |
| **Build Tel TestData**               | `pipeline-build-tel-testdata-module.py`        | Build `tes-testdata` module.                                                               |
| **Run Tel TestData Component Tests** | `pipeline-run-tel-testdata-component-tests.py` | Runs `tes-testdata` component tests.                                                       |
| **Build Tel Web**                    | `pipeline-build-tel-web-module.py`             | Build `tes-web` module.                                                                    |
| **Build Tel Web Docker Image**       | `pipeline-build-tel-web-image.py`              | Build `tes-web` docker image.                                                              |
| **Run Tel Web Component Tests**      | `pipeline-run-tel-web-component-tests.py`      | Runs `tes-web` component tests.                                                            |
| **Run System Tests**                 | `pipeline-run-system-tests.py`                 | Runs system tests.                                                                         |
| **Analyze**                          | `pipeline-analyze.py`                          | Analyse the artifacts for test coverage, vulnerabilities and code analysis with SonarQube. |

#### Additional scripts

Two additional scripts has also be introduced:

1. `pipeline-tools.sh`: Exports the used versions of `Java` in the console, so the pipeline always uses
   the current `Java` version.
2. `pipeline.sh`: Sources `pipeline-tools.sh` before it calls `pipeline.py`. All extra arguments that is passed
   to `pipeline.sh` is also passed to `pipeline.py`.

These scripts are created to make it possible to emulate the `tools` section of a `Jenkins` pipeline.
