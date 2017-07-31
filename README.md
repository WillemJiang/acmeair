# Acme Air Sample and Benchmark [![Build Status](https://travis-ci.org/TankTian/acmeair.svg?branch=master)](https://travis-ci.org/TankTian/acmeair)[![Coverage Status](https://coveralls.io/repos/github/TankTian/acmeair/badge.svg)](https://coveralls.io/github/TankTian/acmeair)

This application shows an implementation of a fictitious airline called "Acme Air".  The application was built with the some key business requirements: the ability to scale to billions of web API calls per day, the need to develop and deploy the application in public clouds (as opposed to dedicated pre-allocated infrastructure).
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
      
      
## Acme Air AUTO CI/CD(based on the Service Stage )
本章节介绍基于华为微服务云应用平台（[Service Stage](https://servicestage.hwclouds.com)），实现自动编译、构建、部署、运行。

### auto build
####  环境准备 
* linux  
* docker 1.11.2(当前只支持该版本)   
* mvn 3.x  
* jdk 1.8+  
* 注册Service Stage账号，创建集群（比如：acmeair）,添加集群节点资源，创建好各微服务镜像对应的软件仓库地址。  
####  步骤  
* 下载acmeair代码到linux机器。   
* 修改根目录下脚本./scripts/release_images_to_huaweicloud.sh，设置该脚本依赖的环境变量,参考该脚本中注释进行修改。  
* 执行脚本release_images_to_huaweicloud.sh，完成编译、镜像制作、镜像上传到Service Stage仓库。  

### auto deploy  
**如果想直接部署看到效果，可以不用自己build，直接使用已有镜像，参考下面步骤实现acmeair的部署和应用访问。**  

* 导入自动部署模板，进入Service Stage系统，点击进入：应用上线>应用编排>模板，然后点击‘创建模板’，将写好的./scripts/acmeair-blueprint-deploy-template-v1.tar.gz自动部署模板导入即可。  
* 创建完成模板后，点击模板上部署菜单即可自动完成部署（前提是你的集群名字为：acmeair，部署的镜像地址和模板中一致；如果集群和镜像地址自定义，请修改模板中对应的字段）。  
* 应用列表界面，查看acmeair-website应用详细页面，点击‘访问地址’中的应用访问地址即可进行acmeeair系统。   

## Acme Air微服务治理   
###  负载均衡
* 增加应用实例个数：应用上线>应用列表，点击‘acmeair-customer’，进入应用详情，点击‘升级’，配置实例数量2，保存容器配置，点击‘升级’，升级成功后，实例页面增加新创建的实例。  
* 查看仪表盘：应用开发>微服务管理>仪表盘，点击‘customerServiceApp'，展示该服务的两个实例以及每个实例的实时数据（总请求成功数、每秒请求速率、CPU占用率等）。
* 进入acmeair系统，选择去程航班和回程航班，点击‘预定选定航班’，同时查看仪表盘总请求成功数，每预定一次机票，每个实例的总请求成功数会实时增加（注意：acmeair-website没有配置handler，
由它发出的服务调用并不会遵循负载均衡策略，在订购机票这一行为中，acmeair-website会调用一次acmeair-customer，acmeair-booking会调用两次acmeair-customer，所以仪表盘中某个实例的总请求成功数总会加2）。

###  熔断
* 查看仪表盘：应用开发>微服务管理>仪表盘，点击‘bookingServiceApp’进入实例展示层，点击实例名称进入实例方法层，找到Consumer.customerServiceApp.customer_REST.getCustomer，此时请求速率趋势曲线为一条直线（注意对比配置熔断策略之前和之后的趋势）。  
* 使用uid3用户登录acmeair系统，选择去程航班和回程航班，连续点击‘预定选定航班’，同时查看仪表盘请求速率趋势曲线，会呈波状趋势，订购机票成功，总请求成功数会增加，订购机票失败，请求失败数会增加。  
* 配置熔断策略：应用开发>微服务管理>微服务治理，为bookingServiceApp配置针对customerServiceApp customer_REST.getCustomer方法的熔断策略（为直观感受熔断效果，建议熔断时间窗设置大于等于20s）。  
* 继续订购机票行为，当请求失败率高于熔断配置的失败率，请求速率趋势会下滑并变成直线，总请求成功数不再随订购行为增加，请求失败数变为0，在熔断时间窗之内订购机票结果会一直失败。  

###  容错
* 登录uid3用户订票，查看仪表盘实例请求成功数，配置容错策略之前和之后增加的个数不一样

###  限流
* 第一种方案：登录uid0用户订票，配置QPS=1，两个页面同时订购机票，查看仪表盘请求成功数量的变化
* 第二种方案：新增一个用户uid4，修改后台booking调用customer的方法，订购一次机票多次调用customer，配置限流数小于调用次数，查看仪表盘总请求成功数
   
###  降级
* 登录uid0用户订票，查看方法层仪表盘getCustomer这个方法的实时数据，针对该方法配置降级策略，继续订购机票则这个方法的请求趋势曲线不会发生变化，一直呈一条直线，并且请求成功数也不会增加
    
      
