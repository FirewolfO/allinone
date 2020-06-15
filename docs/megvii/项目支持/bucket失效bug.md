imageId : 5-AAABcpjNUqIp-HD7AAAAAg==

底库图片：weed://1,3de85d2265ebfc

存储图片：weed://8,3de8593c559364

对象存储接口：[/objectStorage/{bucketName}](http://10.122.101.59:8080/doc/v5/#/operations/objectStorage/GetBucket) 获取bucket信息，这个接口可以获取到bucket的ttlSeconds

monitor类型的底库，会自动创建一个和底库id一样的bucket，这个bucket的ttl是0，表示永久存储。