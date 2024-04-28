#!/usr/bin/python3

import pipelinemodule

if __name__ == '__main__':
    pipelinemodule.execute_gradle_tasks("Running System Tests", [
        [":system-tests:clean"],
        [":system-tests:check", "-Psystem-tests"]
    ])
