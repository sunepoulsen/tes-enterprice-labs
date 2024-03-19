#!/bin/bash

FRONTEND_DIR=tel-web/tel-web-module

echo "Building Reportings Web Module"
cd $FRONTEND_DIR || exit

npm install || exit
npm run test:unit:coverage || exit
npm run lint || exit

cd - || exit
