# Acme Air Sample and Benchmark [![Build Status](https://travis-ci.org/WillemJiang/acmeair.svg?branch=master)](https://travis-ci.org/WillemJiang/acmeair)[![Coverage Status](https://coveralls.io/repos/github/WillemJiang/acmeair/badge.svg)](https://coveralls.io/github/WillemJiang/acmeair)

This application shows an implementation of a fictitious airline called "Acme Air".  The application was built with the some key business requirements: the ability to scale to billions of web API calls per day, the need to develop and deploy the application in public clouds (as opposed to dedicated pre-allocated infrastructure), and the need to support multiple channels for user interaction (with mobile enablement first and browser/Web 2.0 second).

There are two implementations of the application tier. Each application implementation, supports multiple data tiers.  They are:

## Repository Contents

Source:

- **acmeair-common**: The Java entities used throughout the application.
- **acmeair-customer-common**: The common files of customer service which could be used by other module who want to use customer service.
- **acmeair-customer-service**: The micro service of customer service. 
- **acmeair-loader**:  A tool to load the Java implementation data store.
- **acmeair-services**:  The Java data services interface definitions.
- **acmeair-service-morphia**:  A mongodb data service implementation.
- **acmeair-booking-service**: The micro service of booking service.
- **acmeair-webapp**:  The Web 2.0 application frontend which accesses the customer service for login and booking service for booking flight. 

## How to get started

* Development Environment
  
  * Install [Maven](https://maven.apache.org/) to build the code.
  * We use [Docker](https://www.docker.com/) to run the integration test.
  * We use [Consul](https://www.consul.io) as service discovery registry. 
  * We use [MongoDB](https://www.mongodb.com/) as Data Service (it is optional.)
   
* Instructions for build the code base


  You can build the code by using mvn command from the root without using docker
      
      mvn clean install

  If you want to build the docker image and run the integration tests with docker, you can use the Profile of Docker just like this 
  
      mvn clean install -Pdocker
      
* Running Application

  The Acmeair Application have three separated services process: acmeair-customer-service, acmeair-booking-service and acmeair-webapp.
  Acmeair Application also need to use the Service Registry [consul](https://www.consul.io/) to find out the services which it dependents. 
  acmeair-booking-service and acmeair-customer-service can use the outside mongoDB service or use the in memory DB by using active profile.
    
  Here are the dependencies of these service:
  
      acmeair-web -----> acmeair-booking-service (DB)  --------+
           |        |               |                          |
           |        |               |                          |
           |        |               v                          |
           |        +--> acmeair-customer-service (DB)-----+   |
           |                                               |   |
           |                                               V   V
           +-------------------------------------------->Service Registry             
                    
  
  1.Running Consul with docker
  
      docker run -p 8500:8500 consul
      
  2.Running MongoDb With docker (optional)
     
      docker run -p 27017:27017 mongo
      
  3.Staring acmeair-customer-service 
     
      #Running the customer service with in memory db
      java -jar -Dspring.profiles.active=jpa -Dserver.port=8082 -jar acmeaire-customer-service/target/acmeair/acmeair-customer-service-exec.jar
        
      #Running the customer service with mongoDB service
      java -jar -Dspring.profiles.active=mongodb -Dspring.data.mongodb.host=localhost -Dserver.port=8082 -jar acmeaire-customer-service/target/acmeair/acmeair-customer-service-exec.jar
              
        
  4.Staring acmeair-booking-service 
   
      #Running the booking service with in memory db
      java -jar -Dspring.profiles.active=jpa -Dserver.port=8081 -jar acmeaire-booking-service/target/acmeair/acmeair-booking-service-exec.jar
        
      # Running the booking service with mongoDB service
      java -jar -Dspring.profiles.active=mongodb -Dspring.data.mongodb.host=localhost -Dserver.port=8081 -jar acmeaire-booking-service/target/acmeair/acmeair-booking-service-exec.jar
             
      
  5.Starting acmeair-webapp
      
      java -jar -Dserver.port=8080 -jar /maven/acmeair/acmeair-customer-service-exec.jar
       
  6.Access the acmeair-webapp from browser with below address
  
      http://localhost:8080/rest/index.html
