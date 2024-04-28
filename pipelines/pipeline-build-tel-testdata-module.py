#!/usr/bin/python3

import pipelinemodule

if __name__ == '__main__':
    pipelinemodule.execute_gradle_tasks("Building TEL TestData Module", [
        [":tel-testdata:tel-testdata-integrations:build"],
        [":tel-testdata:tel-testdata-integrations:JavaDoc"],
        [":tel-testdata:tel-testdata-module:build"],
        [":tel-testdata:tel-testdata-module:JavaDoc"],
    ])
