#!/usr/bin/env bash

# Compile
mvn clean compile

# Package
mvn clean package

# Install
mvn clean install

# Deploy
mvn clean deploy -P sonatype-oss-release