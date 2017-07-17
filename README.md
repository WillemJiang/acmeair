# Acme Air Sample and Benchmark [![Build Status](https://travis-ci.org/TankTian/acmeair.svg?branch=master)](https://travis-ci.org/TankTian/acmeair)[![Coverage Status](https://coveralls.io/repos/github/TankTian/acmeair/badge.svg)](https://coveralls.io/github/TankTian/acmeair)

This application shows an implementation of a fictitious airline called "Acme Air".  The application was built with the some key business requirements: the ability to scale to billions of web API calls per day, the need to develop and deploy the application in public clouds (as opposed to dedicated pre-allocated infrastructure), and the need to support multiple channels for user interaction (with mobile enablement first and browser/Web 2.0 second).

## Repository Contents

Source:

- **acmeair-common**: The Java entities used throughout the application.
- **acmeair-customer-common**: The common files of customer service which could be used by other module who want to use customer service.
- **acmeair-customer-service**: The micro service of customer service. 
- **acmeair-loader**:  A tool to load the Java implementation data store.
- **acmeair-services**:  The Java data services interface definitions.
- **acmeair-service-morphia**:  A mongodb data service implementation.
- **acmeair-booking-service**: The micro service of booking service.
- **acmeair-website**:  The Web 2.0 application frontend which accesses the customer service for login and booking service for booking flight. 

## How to build

* Development Environment
  
  * Install [Maven](https://maven.apache.org/) to build the code.
  * We use [Docker](https://www.docker.com/) to run the integration test.
   
* Instructions for build the code base

  You can build the code by using maven from the root without running the test
        
      mvn clean install -Dtest=false -DfailIfNoTests=false 
  
  You can build the code by using maven from the root without using docker
      
      mvn clean install

  If you want to build the docker image and run the integration tests with docker, you can use the Profile of Docker just like this 
  
      mvn clean install -Pdocker
    
      
