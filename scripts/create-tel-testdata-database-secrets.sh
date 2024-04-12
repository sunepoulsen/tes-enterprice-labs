#!/bin/bash

DATABASE=tel-testdata
ADM_PASSWORD_FILE=$DATABASE-admin-password.txt
APP_PASSWORD_FILE=$DATABASE-application-password.txt

mkdir -p $1
cd $1

# Delete previous password files
rm -rf $ADM_PASSWORD_FILE $APP_PASSWORD_FILE

# Generate a passphrase for adm user
openssl rand -base64 48 > $ADM_PASSWORD_FILE

# Generate a passphrase for app user
openssl rand -base64 48 > $APP_PASSWORD_FILE

cd -
