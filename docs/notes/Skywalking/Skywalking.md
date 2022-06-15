```bash
firewall-cmd --zone=public --add-port=11800/tcp --permanent  
```

```java
-javaagent:D:\software\skywalking-agent.jar -Dskywalking.agent.service_name=localtest -Dskywalking.collector.backend_service=45.134.83.37:11800
```

