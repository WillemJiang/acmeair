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
  * We use [ServiceCenter](https://github.com/ServiceComb/service-center) as service discovery registry. 
  * We use [MongoDB](https://www.mongodb.com/) as Data Service (it is optional.)
   
* Instructions to build the code base

  You can build the code by using maven from the root without running the test
        
      mvn clean install -Dtest=false -DfailIfNoTests=false 
  
  You can build the code by using maven from the root without using docker
      
      mvn clean install

  If you want to build the docker image and run the integration tests with docker, you can use the Profile of Docker just like this 
  
      mvn clean install -Pdocker
      
  If you are using docker machine, please use the following command
  
      mvn clean install -Pdocker -Pdocker-machine
      
* Running Application

  The Acmeair Application have three separated services process: acmeair-customer-service, acmeair-booking-service and acmeair-webapp.
  Acmeair Application also need to use the Service Registry [servcie-center](https://github.com/ServiceComb/service-center) to find out the services which it dependents. 
  acmeair-booking-service and acmeair-customer-service can use the outside mongoDB service or use the in memory DB by using active profile.
    
  Here are the dependencies of these services:
  
      acmeair-webapp -----> acmeair-booking-service (DB)  -----+
           |        |               |                          |
           |        |               |                          |
           |        |               v                          |
           |        +--> acmeair-customer-service (DB)-----+   |
           |                                               |   |
           |                                               V   V
           +-------------------------------------------->Service Registry             
  
  
* Running Application with docker-compose
    
      cd docker-compose/service-comb; docker-compose up

* Running Application with docker-compose and pre-loaded customers/flights data
    
      cd docker-compose/service-comb; docker-compose -f docker-compose.yml -f docker-compose.perf.yml up
  
* Running Application with java command
  
  1.Running ServiceCenter with docker
  
      docker run -d -p 30100:30100 servicecomb/service-center
      
  2.Running MongoDB With docker (optional)
     
      docker run -p 27017:27017 mongo
      
  3.Starting acmeair-customer-service 
     
      #Running the customer service with in memory DB
      java -Dspring.profiles.active=jpa -Dcse.rest.address=0.0.0.0:8082 -Dcse.service.registry.address=http://localhost:30100 -jar acmeair-customer-service/target/acmeair/acmeair-customer-service-exec.jar
        
      #Running the customer service with mongoDB service
      java -Dspring.profiles.active=mongodb -Dcse.rest.address=0.0.0.0:8082 -Dspring.data.mongodb.host=localhost -Dcse.service.registry.address=http://localhost:30100 -jar acmeair-customer-service/target/acmeair/acmeair-customer-service-exec.jar
                   
  4.Starting acmeair-booking-service 
   
      #Running the booking service with in memory DB
      java -Dspring.profiles.active=jpa -Dcse.rest.address=0.0.0.0:8081 -Dcse.service.registry.address=http://localhost:30100 -jar acmeair-booking-service/target/acmeair/acmeair-booking-service-exec.jar
        
      # Running the booking service with mongoDB service
      java -Dspring.profiles.active=mongodb -Dcse.rest.address=0.0.0.0:8081 -Dspring.data.mongodb.host=localhost -Dcse.service.registry.address=http://localhost:30100 -jar acmeair-booking-service/target/acmeair/acmeair-booking-service-exec.jar
                
  5.Starting acmeair-webapp
      
      java -Dcse.rest.address=0.0.0.0:8080 -Dspring.profiles.active=sc -Dcse.service.registry.address=http://localhost:30100 -jar acmeair-webapp/target/acmeair/acmeair-webapp-exec.jar
       
  6.Access the acmeair-webapp from browser with below address
  
      http://localhost:8080/index.html

* Running AcmeAir on [Google Compute Engine](https://cloud.google.com/compute/)
  
  The default Java version is 1.7 on Google Compute Engine but AcmeAir is using 1.8. Run the following command to set up Java 8 on Google Compute Engine.
      
      sudo update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
      export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre/

* Auto deploy on [Huawei Cloud][huawei_cloud]  
  To auto compile, build, deploy and run this demo on Huawei Cloud's [Service Stage Platform][service_stage], you need the following steps:

  1.A registered [Service Stage][service_stage] account.  
  2.Auto build and publish your docker image to Huawei's Image Warehouse, details can refer to [auto publish guide][publish_guide].  
  3.Auto deploy using Huawei Cloud's orchestration feature, details can refer to [orchestration guide][orchestration_guide].   

[huawei_cloud]: http://www.hwclouds.com
[publish_guide]: docs/how-to-auto-publish-images-to-huawei-cloud.md
[orchestration_guide]: docs/how-to-auto-deploy-on-huawei-cloud.md

* 在华为云上自动部署  

  本章节介绍基于华为微服务云应用平台[Service Stage ][service_stage]，实现自动编译、构建、部署和运行的步骤。

  1.一个已注册的[Service Stage][service_stage]帐号。  
  2.自动编译、构建和发布Docker镜像到华为的镜像仓库，详情可见[自动发布指南][publish_guide_cn] 。  
  3.使用华为云的编排功能自动部署微服务，详情可见[自动部署指南][orchestration_guide_cn] 。  

[service_stage]: https://servicestage.hwclouds.com/servicestage
[publish_guide_cn]: docs/how-to-auto-publish-images-to-huawei-cloud-cn.md
[orchestration_guide_cn]: docs/how-to-auto-deploy-on-huawei-cloud-cn.md