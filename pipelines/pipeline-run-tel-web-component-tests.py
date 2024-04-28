#!/usr/bin/python3

import pipelinemodule

if __name__ == '__main__':
    pipelinemodule.execute_gradle_tasks("Running TEL Web Module Component Tests", [
        [":tel-web:tel-web-component-tests:clean"],
        [":tel-web:tel-web-component-tests:check", "-Ptel-web-component-tests"]
    ])
