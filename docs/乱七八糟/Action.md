# 2021/12/20

### 索引review



### 复盘文档准备



### 130沟通



### OnCall记录

- 视频预览地址web可以，openAPI不可以
  - 中台返回的uri是 /gmp/....格式
  - web是前端拼接的：ws://${location.host}${uri}
  - openAPI 是通过后端拼接的：GMP_SERVER_ADDR + uri 
  - 视频预览的gmp请求示是通过Nginx转发到gmp服务的，GMP_SERVER_ADDR配置的是gmp服务所在地址，如果gmp没有和nginx（front服务）部署在一起，那么就无法正确访问
  - 解决：把GMP_SERVER_ADDR 的值改成front服务所在地址



```
xx: Please initialize the log4cxx system properly.
2021-Dec-20 15:11:49.050 INFO  [http-nio-18201-exec-2] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.web.vehicle.VehicleRuleController - queryById request,reqDto:60
2021-Dec-20 15:11:49.060 INFO  [http-nio-18201-exec-3] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.web.vehicle.VehicleRuleController - deleteVehicleRule request,reqDto:{"id":60}
2021-Dec-20 15:11:49.060 INFO  [http-nio-18201-exec-3] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.s.impl.StructureRuleServiceImpl - delete bind control reqDto:VehicleRuleIdReqDto(id=60, vehicleRuleUuId=null)
2021-Dec-20 15:11:49.061 INFO  [http-nio-18201-exec-3] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.s.impl.StructureRuleServiceImpl - delete structure control. deviceSetId:139, deviceSetUuid:3eb3e75db0804060bd1cc7e10f68c89f, ruleId:60
2021-Dec-20 15:11:52.229 INFO  [http-nio-18201-exec-3] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.s.impl.StructureRuleServiceImpl - delete structure control. deviceSetId:140, deviceSetUuid:5923c6f60e7049068f76ad119a6e9d8d, ruleId:60
2021-Dec-20 15:11:55.942 INFO  [http-nio-18201-exec-3] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.s.impl.StructureRuleServiceImpl - delete structure control. deviceSetId:142, deviceSetUuid:64db9b840c01460aa7e016e974464c54, ruleId:60
2021-Dec-20 15:11:59.283 INFO  [http-nio-18201-exec-3] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.s.impl.StructureRuleServiceImpl - call middle delete control cost time:10222， reqDto:VehicleRuleIdReqDto(id=60, vehicleRuleUuId=null)
2021-Dec-20 15:11:59.284 INFO  [http-nio-18201-exec-3] [TID:8e0795d78aab4754bad070184377eae3] c.m.e.p.m.s.p.s.impl.StructureRuleServiceImpl - delete control is finish. reqDto:VehicleRuleIdReqDto(id=60, vehicleRuleUuId=null)
```

