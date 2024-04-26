#!/usr/bin/python3

import pipelinemodule

if __name__ == '__main__':
    pipelinemodule.execute_gradle_tasks("Running TEL TestData Module Component Tests", [
        [":tel-testdata:tel-testdata-component-tests:clean"],
        [":tel-testdata:tel-testdata-component-tests:check", "-Ptel-testdata-component-tests"]
    ])
