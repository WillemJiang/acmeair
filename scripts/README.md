# Acme Air AUTO CI/CD
本文介绍如何基于华为微服务云应用平台（[Service Stage](https://servicestage.hwclouds.com)），实现自动编译、构建、部署、运行。

## auto build
###  环境准备 
linux  
docker 1.11.2(当前只支持该版本)   
mvn 3.x  
jdk 1.8+  
注册Service Stage账号，创建集群（比如：acmeair）,添加集群节点资源，创建好各微服务镜像对应的软件仓库地址。  
###  步骤  
1、下载acmeair代码到linux机器。   
2、修改根目录下脚本/scripts/release_images_to_huaweicloud.sh，设置该脚本依赖的环境变量,参考该脚本中注释进行修改。  
3、执行脚本release_images_to_huaweicloud.sh，完成编译、镜像制作、镜像上传到Service Stage仓库。  

## auto deploy  
**如果想直接部署看到效果，可以不用自己build，直接使用已有镜像，参考下面步骤实现acmeair的部署和应用访问。**  

1、导入自动部署模板，进入Service Stage系统，点击进入：应用上线>应用编排>模板，然后点击‘创建模板’，将写好的acmeair-blueprint-deploy-template-v1.tar.gz自动部署模板导入即可。  
2、创建完成模板后，点击模板上部署菜单即可自动完成部署（前提是你的集群名字为：acmeair，部署的镜像地址和模板中一致；如果集群和镜像地址自定义，请修改模板中对应的字段）。  
3、应用列表界面，查看acmeair-website应用详细页面，点击‘访问地址’中的应用访问地址即可进行acmeeair系统。  
