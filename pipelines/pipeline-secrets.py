#!/usr/bin/python3

import pipelinemodule

if __name__ == '__main__':
    pipelinemodule.execute_gradle_tasks("Creating required secrets", [
        [":makeSecrets"]
    ])
