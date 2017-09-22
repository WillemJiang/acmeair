# 在华为云上自动部署微服务
## 背景
这个 acmeair 的例子包含了3个服务（即网关、订票业务和顾客业务）。此外，我们还使用了 [MongoDB](https://www.mongodb.com) 来为订票业务提供持久化存储能力。因此，本指南将阐述在华为公有云上自动部署这4个服务的步骤，最终编排结构如图1所示。
![图1 编排结构](images/cluster_info.png)  
图1 编排结构

## 导入编排模板
要导入编排模板，需要以下步骤：
1. 访问Service Stage上的 [设计器](https://servicestage.hwclouds.com/servicestage/#/stage/doodle/new/topology)
2. 点击如图2所示的 *导入* 按钮
3. 点击如图2所示的 *选择本地模板*
4. 在弹出的文件框中选择模板文件 `scripts/acmeair.tar.gz`
5. 输入模板文件名, 如： acmeair, 然后点击 *导入*
  ![图2 导入模板步骤](images/import_template_cn.png)  
  图2 导入模板步骤  
  现在在网页上应该能看到类似于图1所示的编排结构图了。

## 自动部署
在部署集群之前，可能需要对一些属性进行更改。部署的步骤为：
1. 更新集群名. 如果你的集群名称已经是 acmeair 了，则可以跳过这一步。  
  如图3所示，首先你需要双击 *cluster-resource* 按钮，然后选取你的集群名。
  ![图3 选取集群](images/update_cluster_name_cn.png)  
  图3 选取集群
2. 更新镜像地址。如果你没有构建并发布镜像到镜像仓库，则可以跳过这一步，直接使用我们提供的公共镜像。  
  (1) 访问 [华为镜像仓库](https://servicestage.hwclouds.com/servicestage/#/stage/softRepository/mirrorCenter/myMirrorPack), 然后选择 *webapp* 镜像，点击如图4所示 *复制地址* 按钮。
  ![图4 复制镜像地址](images/copy_image_address_cn.png)  
  图4 复制镜像地址  
  (2) 如图5所示，首先你需要点击*输入输出*按钮，然后在输入参数名称一栏中找到*webapp\_container\_image*并在该栏中点击*编辑*按钮，然后在默认值所在列粘贴步骤(1)中复制的镜像地址，并点击保存按钮。
  ![图5 更新镜像地址](images/update_image_address_cn.png)  
  图5 更新镜像地址  
  对*customer*, *booking*和*mongodb*重复步骤(1)和步骤(2)来更新镜像地址。
3. 部署。如图6所示，点击*保存模板*和*部署*按钮。然后点击*创建*按钮。  
  ![图6 保存并部署](images/save_and_deploy_cn.png)  
  图6 保存并部署  
4. 访问 [堆栈](https://servicestage.hwclouds.com/servicestage/#/stage/stack/stacklist) 界面，检查堆栈运行状态。当集群颜色由蓝色变为绿色时，就说明集群已成功部署。然后就按图7所示进入*webapp*服务详情页面，获取图8所示位置的服务地址，然后就可以访问该地址来使用订票业务。
  ![图7 查看webapp服务详情](images/view_webapp_details_cn.png)  
  图7 查看webapp服务详情  
  ![图8 获取服务地址](images/get_host_address_cn.png)  
  图8 获取服务地址  
