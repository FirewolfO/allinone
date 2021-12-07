### 处理devops关闭容器不正常问题

问题如下：

```shell
sudo docker network disconnect --force host megvii.pangu110-80-core.core-core.meta_master-1
```

```shell
 sudo docker network disconnect --force 网络方式 服务名
```

### 构建镜像并推送

```shell
sudo docker build -t ampregistry:5000/pangu-facade-openapi:poc-taihao .
sudo docker push ampregistry:5000/aipark-adapter:1.0
```

### 引入Skywalking

1.  Dockerfile

   ```dockerfile
   FROM ampregistry:5000/sng-biz-base:2.0.0.1
   
   # maintainer
   MAINTAINER wenjianrui wenjianrui@megvii.com
   
   ENV TZ Asia/Shanghai
   
   ENV SW_AGENT_NAME agent-pangu-ms-base
   
   WORKDIR $PRO_PATH
   
   ADD pangu-ms-base-provider-1.0-SNAPSHOT-exec.jar $PRO_PATH
   
   CMD mkdir -p /data/{log,errorlog}
   
   CMD java -Xms1G -Xmx1G -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=30 -XX:+DisableExplicitGC -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=25 -XX:G1MaxNewSizePercent=40 -XX:G1MixedGCLiveThresholdPercent=35 -XX:+AlwaysPreTouch -XX:+ParallelRefProcEnabled $SKYWALKING -jar pangu-ms-base-provider-1.0-SNAPSHOT-exec.jar
   ```

   

2. 环境变量

   ```properties
   SW_AGENT_COLLECTOR_BACKEND_SERVICES=KSSCS4000H31907020002:11800
   SKYWALKING=-javaagent:skywaling-agent/skywalking-agent.jar
   ```

   

### 证书

```shell
sudo ETCDCTL_API=3 devops-etcdctl --endpoints localhost:5430 del /devops/license
```





### 清除历史告警记录

```shell
sudo supervisorctl restart devops-manager-core:devops-prometheus
sudo supervisorctl restart devops-manager-extra:devops-alertmanager
```

