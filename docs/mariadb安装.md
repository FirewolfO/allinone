## docker 安装单机

```powershell
docker run --name mariadbtest -v %CD%/conf1:/etc/mysql -e MYSQL_ROOT_PASSWORD=mypass -p 3366:3306 -d mariadb/server:10.3
```

--wsrep-cluster-address

--wsrep-new-cluster





```powershell
docker run --name mariadbtest2 -v %CD%/conf2:/etc/mysql -e MYSQL_ROOT_PASSWORD=mypass -p 3377:3306 -d mariadb/server:10.3
```



```powershell
docker run --name mariadbtest3 -v %CD%/conf3:/etc/mysql -e MYSQL_ROOT_PASSWORD=mypass -p 3388:3306 -d mariadb/server:10.3
```

event_record_platform_notify







<img width="70" height="70" class="x-image" src="http://10.122.101.181:12029/pub/_ZzEwMF9mb3JldmVyQnVja2V0_4d093f30065f4b8285d18ea4fe60c71a?timestamp=1594727118&amp;sig=3042827823e2eeb87e7ca7dfa291d078" alt="" style="width: 70px; height: 39.375px; transform: translate(0px, 15.3125px);">

AC110001000820AD9418232335051C01





