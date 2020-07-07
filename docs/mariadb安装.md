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

