#!/usr/bin/python3

import pipelinemodule

if __name__ == '__main__':
    pipelinemodule.execute_gradle_tasks("Building the TEL Web Image", [
        [":tel-web:tel-web-docker-image:clean"],
        [":tel-web:tel-web-docker-image:buildImage"],
    ])
