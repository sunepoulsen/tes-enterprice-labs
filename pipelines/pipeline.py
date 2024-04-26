#!/usr/bin/python3

import argparse

import pipelinemodule

if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        prog='./pipeline.py',
        description='Pipeline to build, test and publish all artifacts',
        epilog="Analyzing with sonarqube will be ignore if '-r' is missing.")

    parser.add_argument('-r', '--remote', action='store_true',
                        help="Analyses the code with a remote sonarqube.")

    args = parser.parse_args()

    scripts = [
        ["./pipeline-clean.py"],
        ["./pipeline-secrets.py"],
        ["./pipeline-build-tel-testdata-module.py"],
        ["./pipeline-run-tel-testdata-component-tests.py"],
        ["./pipeline-build-tel-web-module.py"],
        ["./pipeline-build-tel-web-image.py"],
        ["./pipeline-run-tel-web-component-tests.py"],
        ["./pipeline-run-system-tests.py"],
        ["./pipeline-analyze.py"],
    ]

    pipelinemodule.extend_script_arguments(scripts, "./pipeline-analyze.py", args.remote, ["--remote"])

    pipelinemodule.execute_scripts("Building the pipeline", scripts)
