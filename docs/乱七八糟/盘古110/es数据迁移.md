### es迁移方案调研

#### 粗暴迁移

1. 停止ai-converge服务
2. 拷贝原es所在机器上 /mnt/data/pangu/infra/es/data 到新的机器 ，新机器如果没有相应目录，请手动创建
3. 修改目录权限：sudo chmod -R 777  /mnt/data/pangu/infra/es
4. 导出pangu-safeguard-infra的yaml文件
   - 修改pangu-elasticsearch-master服务的hostname为新的主机名
   - 修改env 的network.publish_host和discovery.seed_hosts后面的主机为新的主机名
5. 重新导入yaml
6. 调整环境变量：ES_HOST  值改为新pangu-elasticsearch-master所在机器地址
7. 重启pangu-elasticsearch-master服务
8. 启动ai-converge服务
9. 重启所有盘古业务

> 结论：公司可以，现场失败



#### Reindex方式

1. 新服务器搭建es服务

2. 停止业务及aiot服务

3. 编辑新服务器elasticsearch.yml配置文件

   - 拷贝容器配置文件到宿主机

     ```
     sudo docker cp es容器id：/usr/share/elasticsearch/config/elasticsearch.yml 宿主机目录
     ```

4. 编辑 

   vim  elasticsearch.yml，追加如下内容：

   ```
   reindex.remote.whitelist: "192.168.11.150:19200"
   ```

   后面是原es服务地址，注意whitelist:后面有空格

5. 修改新es挂载

   添加如下内容：

   宿主机elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml

6. 重启新机器es容器

7. 在新机器创建重建索引脚本start.sh，内容如下：

   ```shell
   #!/bin/bash
   # $1:原服务器地址,$2:新服务器地址
   # 示例：./start.sh http://10.122.94.176:19200 http://192.11.11.11:19200
   echo $1  $2
   echo "">indexNames.txt
   curl -XGET $1/_cat/indices | awk -F ' ' '{print $3}' | egrep -v '^\.' > indexNames.txt
   for line in `cat indexNames.txt`
   do
   time=`date`
   echo "" > exe.log 
   echo "$time start reindex $line" >> exe.log
   curl -X POST "$2/_reindex?pretty" -H 'Content-Type: application/json' -d '{ "source": { "remote": { "host": ""'"$1"'""},"index": "'"$line"'"},"dest": {"index": "'"$line"'"}}'
   echo "$time finsh reindex $line" >> exe.log
   done
   echo "all success"
   ```

8. 执行脚本

   ```shell
   ./start.sh http://10.122.94.176:19200 http://192.11.11.11:19200
   ```

   可以在exe.log 中查看重建进度

>结论：现场成功

参考文档：

https://www.elastic.co/guide/en/elasticsearch/reference/current/reindex-upgrade-remote.html