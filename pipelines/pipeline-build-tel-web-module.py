#!/usr/bin/python3

import pipelinemodule

if __name__ == '__main__':
    pipelinemodule.execute_gradle_tasks("Clean TEL Web Module", [
        [":tel-web:tel-web-module:clean"]
    ])

    pipelinemodule.execute_npm_tasks("Building TEL Web Module", "tel-web/tel-web-module", [
        ["install"],
        ["run", "test:unit:coverage"],
        ["run", "lint"],
        ["run", "build"],
    ])
