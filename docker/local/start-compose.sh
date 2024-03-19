#!/bin/bash

rm -rf logs
docker compose up -d
docker compose images
docker compose ps
