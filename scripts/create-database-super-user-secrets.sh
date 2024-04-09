#!/bin/bash

SUPER_USER_PASSWORD_FILE=database-super-user-password.txt

mkdir -p $1
cd $1

# Delete previous password files
rm -rf $SUPER_USER_PASSWORD_FILE

# Generate a passphrase for super user
openssl rand -base64 48 > $SUPER_USER_PASSWORD_FILE

cd -
