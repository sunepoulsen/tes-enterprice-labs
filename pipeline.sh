#!/bin/bash

if [[ "$1" != "--ignore-tools" ]]
then
  echo "Using tools defined in file: ./pipeline-tools.sh"
  source ./pipeline-tools.sh || exit
fi

echo
./pipeline-clean-repo.sh

echo
./pipeline-secrets.sh

echo
./pipeline-build-tel-testdata-module.sh

echo
./pipeline-run-tel-testdata-component-tests.sh

echo
./pipeline-run-tel-testdata-stress-tests.sh

echo
./pipeline-build-tel-web-module.sh

echo
./pipeline-build-tel-web-image.sh

echo
./pipeline-run-tel-web-component-tests.sh

echo
./pipeline-run-system-tests.sh

echo
./pipeline-deployment-tests.sh

echo
./pipeline-analyze.sh
