### shell脚本

```shell
#!/bin/bash
res=`curl -i -k -X POST -H "Content-type:application/json" -d $2 $1`
echo $res
```



## 业务数据记录订阅

- 查询 

  ```shell
  ./openapi.sh "https://10.122.94.176/v1/api/pubsub/busi/record/query" '{"appKey":"appkey1"}'
  ```

- 添加

  ```shell
  ./openapi.sh "https://10.122.94.176/v1/api/pubsub/busi/record/add" '{"pushUrl":"http://10.170.161.125:9999/event/record/push","appKey":"appkey1","busiTypes":[1,2,3,5,6,7,12]}'
  ```

- 解绑

  ```shell
  ./openapi.sh "https://10.122.94.176/v1/api/pubsub/busi/record/unbind" '{"appKey":"appkey1"}'
  ```



## 事件记录

- 查询

  ```shell
  ./openapi.sh "https://10.122.94.176/v1/api/event/record/list" '{"pageSize":10,"pageNum":1,"eventTypeId":326}'
  ```

  

