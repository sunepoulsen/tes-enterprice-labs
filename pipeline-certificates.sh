#!/bin/bash

echo "Creating required certificates"
./gradlew :makeCertificates || exit
