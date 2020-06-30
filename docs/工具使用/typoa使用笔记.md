## 常用插件

- maven 

- gitee

- localization



## 常用操作



### 更改密码

![image-20200630180150832](https://gitee.com/firewolf/allinone/raw/master/images/image-20200630180150832.png)

### 插件加速设置

系统管理  -> 插件管理 -> 高级 ->  升级站点，填入下面的地址：

```
https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json
```



### 配置Jdk

系统管理 -> 全局工具配置 -> JDK ->新增JDK

![image-20200630180431290](https://gitee.com/firewolf/allinone/raw/master/images/image-20200630180431290.png)



### 配置maven

系统管理 -> 全局工具配置 -> JDK ->新增Maven

![image-20200630181132834](https://gitee.com/firewolf/allinone/raw/master/images/image-20200630181132834.png)



### 配置gitee

系统管理 -> 系统配置 -> Gitee配置

![image-20200630181002474](https://gitee.com/firewolf/allinone/raw/master/images/image-20200630181002474.png)

这里需要一个整数令牌，点击添加，通过下面的界面添加，其中gitee APIV5死人令牌需要在gitee上面生成

![image-20200630180906163](C:/Users/liuxing/AppData/Roaming/Typora/typora-user-images/image-20200630180906163.png)





## 项目构建场景

### 构建Maven项目

点击新建任务，在下面的界面选择maven项目，然后输入名字点击确定。

![image-20200630181306919](https://gitee.com/firewolf/allinone/raw/master/images/image-20200630181306919.png)

在接下来的界面输入git仓库地址

![image-20200630181530804](https://gitee.com/firewolf/allinone/raw/master/images/image-20200630181530804.png)

设置pom文件位置以及mvn命令

![image-20200630181740650](https://gitee.com/firewolf/allinone/raw/master/images/image-20200630181740650.png)

之后就可以进行构建了。

