# Acme Air Sample and Benchmark [![Build Status](https://travis-ci.org/WillemJiang/acmeair.svg?branch=master)](https://travis-ci.org/WillemJiang/acmeair)

This application shows an implementation of a fictitious airline called "Acme Air".  The application was built with the some key business requirements: the ability to scale to billions of web API calls per day, the need to develop and deploy the application in public clouds (as opposed to dedicated pre-allocated infrastructure), and the need to support multiple channels for user interaction (with mobile enablement first and browser/Web 2.0 second).

There are two implementations of the application tier. Each application implementation, supports multiple data tiers.  They are:

## Repository Contents

Source:

- **acmeair-common**: The Java entities used throughout the application
- **acmeair-customer-common**: The common files of customer service
- **acmeair-customer-service**: The micro service of customer service 
- **acmeair-loader**:  A tool to load the Java implementation data store
- **acmeair-services**:  The Java data services interface definitions
- **acmeair-service-morphia**:  A mongodb data service implementation
- **acmeair-webapp**:  The Web 2.0 application and associated Java REST services which access the customer service for login 

## How to get started

* Instructions for [setting up and building the codebase](Documentation/Build_Instructions.md)
* Deploying the sample application to [Websphere Liberty](Documentation/Liberty_Instructions.md)
* Extending Acme Air by [adding additional data services.](Documentation/Extending_AcmeAir_Services.md)

## Ask Questions

Questions about the Acme Air Open Source Project can be directed to our Google Groups.

* Acme Air Users: [https://groups.google.com/forum/?fromgroups#!forum/acmeair-users](https://groups.google.com/forum/?fromgroups#!forum/acmeair-users)

## Submit a bug report

We use github issues to report and handle bug reports.

## OSS Contributions

We accept contributions via pull requests.

CLA agreements needed for us to accept pull requests soon can be found in the [CLAs directory](CLAs) of the repository.
