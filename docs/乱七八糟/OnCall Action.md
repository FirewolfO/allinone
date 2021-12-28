外部问题：

​	确认	

​	拉群



patch：

​	步骤	

​	

​	名称规范



A版本

​	alpha：产品

B版本

​	beta：产品测试





```
<img width="130" height="130" class="x-image" src="http://192.168.1.251:80/pub/_ZzEwMF82bQ==_a5557d0baa614deabb6c423ada8864a0?timestamp=1640877410&amp;sig=9f920d65bbf976ac4984b4257aa38fef" alt="" style="width: 100%; height: 100%;">
```

```

root@M014302002102000015:/mnt/data/pangu/safeguard/log/person# cat pangu-ms-person.log | grep 'bfdc0a38-2bf0-4056-887c-eb7770e07eb7'
2021-Dec-24 09:22:52.380 INFO  [ConsumeMessageThread_5] [TID:bfdc0a38-2bf0-4056-887c-eb7770e07eb7] c.m.e.p.m.p.provider.mq.BlackPipelineMqListener - black pipeline consumer mq trackId:bfdc0a38-2bf0-4056-887c-eb7770e07eb7,groupId:GID_aiot_safeguard,topic:TP_biz_iot,body:"{\"record_list\":[{\"person_id\":\"\",\"person_name\":\"\",\"snapshot_uri\":\"-1_ZzEwMF82bQ==_fb5bf3e8d8474ba8b9b4d4e2c45053aa\",\"recognition_type\":4,\"liveness_type\":1,\"verification_mode\":1,\"pass_type\":0,\"recognition_score\":1,\"liveness_score\":99,\"temperature\":0,\"temperature_type\":0,\"mask_type\":0,\"timestamp\":1640308972000,\"device_token\":\"7b46063a188c41a5b3d98fc4f802adf8\"}],\"device_set_id\":\"\"}"
```

````
80 INFO  [ConsumeMessageThread_30] [TID:bfdc0a38-2bf0-4056-887c-eb7770e07eb7] c.m.e.p.ms.person.provider.mq.StrangerMqListener - start stranger checkMsgContent,msg:MessageEntity(trackId=null, recordList=[MessageEntity.Message(is_capture=false, personId=, personName=, snapshotUri=-1_ZzEwMF82bQ==_fb5bf3e8d8474ba8b9b4d4e2c45053aa, recognitionType=4, livenessType=1, verificationMode=1, passType=0, recognitionScore=1.0, livenessScore=99.0, frameUri=null, timestamp=1640308972000, featureData=null, featureVersion=null, candidates=null, attributes=null, alert_id=null, track_id=null, quality=null)], captureList=null, deviceSetId=, redis_key=null, securityExtra=null) 
````



```
vel=info msg="[GIN] 2021/12/24 - 09:22:52.310 | 200 |   38.102269ms |    192.168.2.60 | POST     \"/meglink/7b46063a188c41a5b3d98fc4f802adf8/file/upload\" [TraceId: 88941efd-e2e9-4e58-a60e-e1e079a683f4]\n\tresponse {\"code\":100000,\"msg\":\"\",\"data\":{\"key\":\"-1_ZzEwMF82bQ==_fb5bf3e8d8474ba8b9b4d4e2c45053aa\"},\"time\":1640308972310}\n\n"
time="2021-12-24T09:22:52.378" level=info msg="rocketmq SendMessageSync [Topic: TP_biz_iot, Tags: record_batch_upload, Keys: [], Body: {\"record_list\":[{\"person_id\":\"\",\"person_name\":\"\",\"snapshot_uri\":\"-1_ZzEwMF82bQ==_fb5bf3e8d8474ba8b9b4d4e2c45053aa\",\"recognition_type\":4,\"liveness_type\":1,\"verification_mode\":1,\"pass_type\":0,\"recognition_score\":1,\"liveness_score\":99,\"temperature\":0,\"temperature_type\":0,\"mask_type\":0,\"timestamp\":1640308972000,\"device_token\":\"7b46063a188c41a5b3d98fc4f802adf8\"}],\"device_set_id\":\"\"}, Property: map[TAGS:record_batch_upload TraceId:bfdc0a38-2bf0-4056-887c-eb7770e07eb7 UNIQ_KEY:C0A801FB000800000000787595e0003c]] result SendResult [sendStatus=0, msgIds=C0A801FB000800000000787595e0003c, offsetMsgId=C0A801FB00002A9F000000000D1CC111, queueOffset=13404, messageQueue=MessageQueue [topic=TP_biz_iot, brokerName=mq-broker-master, queueId=3]]"
time="2021-12-24T09:22:52.378" level=info msg="rocketmq SendMessageSync [Topic: TP_biz_iot_capture, Tags: capture_batch_upload, Keys: [], Body: {\"record_list\":[{\"person_id\":\"\",\"person_name\":\"\",\"snapshot_uri\":\"-1_ZzEwMF82bQ==_fb5bf3e8d8474ba8b9b4d4e2c45053aa\",\"recognition_type\":4,\"liveness_type\":1,\"verification_mode\":1,\"pass_type\":0,\"recognition_score\":1,\"liveness_score\":99,\"temperature\":0,\"temperature_type\":0,\"mask_type\":0,\"timestamp\":1640308972000,\"device_token\":\"7b46063a188c41a5b3d98fc4f802adf8\"}],\"device_set_id\":\"\"}, Property: map[TAGS:capture_batch_upload TraceId:bfdc0a38-2bf0-4056-887c-eb7770e07eb7 UNIQ_KEY:C0A801FB000800000000787595e0003d]] result SendResult [sendStatus=0, msgIds=C0A801FB000800000000787595e0003d, offsetMsgId=C0A801FB00002A9F000000000D1CC392, queueOffset=268, messageQueue=MessageQueue [topic=TP_biz_iot_capture, brokerName=mq-broker-master, queueId=2]]"
level=info msg="[GIN] 2021/12/24 - 09:22:52.378 | 200 |   12.736131ms |    192.168.2.60 | POST     \"/meglink/7b46063a188c41a5b3d98fc4f802adf8/record/batch_upload\" [TraceId: bfdc0a38-2bf0-4056-887c-eb7770e07eb7]\n\trequest {\n\t\"record_list\":\t[{\n\t\t\t\"person_id\":\t\"\",\n\t\t\t\"person_name\":\t\"\",\n\t\t\t\"snapshot_uri\":\t\"-1_ZzEwMF82bQ==_fb5bf3e8d8474ba8b9b4d4e2c45053aa\",\n\t\t\t\"recognition_type\":\t4,\n\t\t\t\"liveness_type\":\t1,\n\t\t\t\"verification_mode\":\t1,\n\t\t\t\"pass_type\":\t0,\n\t\t\t\"recognition_score\":\t1,\n\t\t\t\"liveness_score\":\t99,\n\t\t\t\"temperature\":\t0,\n\t\t\t\"temperature_type\":\t0,\n\t\t\t\"mask_type\":\t0,\n\t\t\t\"timestamp\":\t1640308972000\n\t\t}]\n}\n\tresponse {\"code\":100000,\"msg\":\"\",\"time\":1640308972378}\n\n"
```

