### 本地环境搭建（非必须）

1. 安装node.js

2. 安装npm

3. 安装gitbook

   ```shell
   sudo npm install -g gitbook-cli
   ```

4. 进入到gitbook项目启动

   ```shell
   gitbook serve 
   ```

5. 访问查看：localhost:4000

### gitbook镜像构建

我这里从node:16进行构建，步骤如下：

1. 启动node:16镜像并进入容器，给安装gitbook环境

   ```shell
   docker run -it node:16 /bin/bash 
   ```

2. 进入之后，安装gitbook

   ```
   npm install -g gitbook-cli
   ```

3. 安装完成后，启动

   ```
   gitbook serve
   ```

   发现会出现以下错误

   ```
   Installing GitBook 3.2.3
   /usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/graceful-fs/polyfills.js:287
         if (cb) cb.apply(this, arguments)
                    ^
   TypeError: cb.apply is not a function
       at /usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/graceful-fs/polyfills.js:287:18
       at FSReqCallback.oncomplete (fs.js:169:5)
   ```

   解决方式如下

   - 修改polyfills.js文件，把内容替换成下面的（具体参看：https://www.cnblogs.com/cyxroot/p/13754475.html）

     ```js
     var fs = require('./fs.js')
     var constants = require('constants')
     
     var origCwd = process.cwd
     var cwd = null
     
     var platform = process.env.GRACEFUL_FS_PLATFORM || process.platform
     
     process.cwd = function() {
       if (!cwd)
         cwd = origCwd.call(process)
       return cwd
     }
     try {
       process.cwd()
     } catch (er) {}
     
     var chdir = process.chdir
     process.chdir = function(d) {
       cwd = null
       chdir.call(process, d)
     }
     
     module.exports = patch
     
     function patch (fs) {
       // (re-)implement some things that are known busted or missing.
     
       // lchmod, broken prior to 0.6.2
       // back-port the fix here.
       if (constants.hasOwnProperty('O_SYMLINK') &&
           process.version.match(/^v0\.6\.[0-2]|^v0\.5\./)) {
         patchLchmod(fs)
       }
     
       // lutimes implementation, or no-op
       if (!fs.lutimes) {
         patchLutimes(fs)
       }
     
       // https://github.com/isaacs/node-graceful-fs/issues/4
       // Chown should not fail on einval or eperm if non-root.
       // It should not fail on enosys ever, as this just indicates
       // that a fs doesn't support the intended operation.
     
       fs.chown = chownFix(fs.chown)
       fs.fchown = chownFix(fs.fchown)
       fs.lchown = chownFix(fs.lchown)
     
       fs.chmod = chmodFix(fs.chmod)
       fs.fchmod = chmodFix(fs.fchmod)
       fs.lchmod = chmodFix(fs.lchmod)
     
       fs.chownSync = chownFixSync(fs.chownSync)
       fs.fchownSync = chownFixSync(fs.fchownSync)
       fs.lchownSync = chownFixSync(fs.lchownSync)
     
       fs.chmodSync = chmodFixSync(fs.chmodSync)
       fs.fchmodSync = chmodFixSync(fs.fchmodSync)
       fs.lchmodSync = chmodFixSync(fs.lchmodSync)
     
       fs.statSync = statFixSync(fs.statSync)
       fs.fstatSync = statFixSync(fs.fstatSync)
       fs.lstatSync = statFixSync(fs.lstatSync)
     
       // if lchmod/lchown do not exist, then make them no-ops
       if (!fs.lchmod) {
         fs.lchmod = function (path, mode, cb) {
           if (cb) process.nextTick(cb)
         }
         fs.lchmodSync = function () {}
       }
       if (!fs.lchown) {
         fs.lchown = function (path, uid, gid, cb) {
           if (cb) process.nextTick(cb)
         }
         fs.lchownSync = function () {}
       }
     
       // on Windows, A/V software can lock the directory, causing this
       // to fail with an EACCES or EPERM if the directory contains newly
       // created files.  Try again on failure, for up to 60 seconds.
     
       // Set the timeout this long because some Windows Anti-Virus, such as Parity
       // bit9, may lock files for up to a minute, causing npm package install
       // failures. Also, take care to yield the scheduler. Windows scheduling gives
       // CPU to a busy looping process, which can cause the program causing the lock
       // contention to be starved of CPU by node, so the contention doesn't resolve.
       if (platform === "win32") {
         fs.rename = (function (fs$rename) { return function (from, to, cb) {
           var start = Date.now()
           var backoff = 0;
           fs$rename(from, to, function CB (er) {
             if (er
                 && (er.code === "EACCES" || er.code === "EPERM")
                 && Date.now() - start < 60000) {
               setTimeout(function() {
                 fs.stat(to, function (stater, st) {
                   if (stater && stater.code === "ENOENT")
                     fs$rename(from, to, CB);
                   else
                     cb(er)
                 })
               }, backoff)
               if (backoff < 100)
                 backoff += 10;
               return;
             }
             if (cb) cb(er)
           })
         }})(fs.rename)
       }
     
       // if read() returns EAGAIN, then just try it again.
       fs.read = (function (fs$read) { return function (fd, buffer, offset, length, position, callback_) {
         var callback
         if (callback_ && typeof callback_ === 'function') {
           var eagCounter = 0
           callback = function (er, _, __) {
             if (er && er.code === 'EAGAIN' && eagCounter < 10) {
               eagCounter ++
               return fs$read.call(fs, fd, buffer, offset, length, position, callback)
             }
             callback_.apply(this, arguments)
           }
         }
         return fs$read.call(fs, fd, buffer, offset, length, position, callback)
       }})(fs.read)
     
       fs.readSync = (function (fs$readSync) { return function (fd, buffer, offset, length, position) {
         var eagCounter = 0
         while (true) {
           try {
             return fs$readSync.call(fs, fd, buffer, offset, length, position)
           } catch (er) {
             if (er.code === 'EAGAIN' && eagCounter < 10) {
               eagCounter ++
               continue
             }
             throw er
           }
         }
       }})(fs.readSync)
     }
     
     function patchLchmod (fs) {
       fs.lchmod = function (path, mode, callback) {
         fs.open( path
                , constants.O_WRONLY | constants.O_SYMLINK
                , mode
                , function (err, fd) {
           if (err) {
             if (callback) callback(err)
             return
           }
           // prefer to return the chmod error, if one occurs,
           // but still try to close, and report closing errors if they occur.
           fs.fchmod(fd, mode, function (err) {
             fs.close(fd, function(err2) {
               if (callback) callback(err || err2)
             })
           })
         })
       }
     
       fs.lchmodSync = function (path, mode) {
         var fd = fs.openSync(path, constants.O_WRONLY | constants.O_SYMLINK, mode)
     
         // prefer to return the chmod error, if one occurs,
         // but still try to close, and report closing errors if they occur.
         var threw = true
         var ret
         try {
           ret = fs.fchmodSync(fd, mode)
           threw = false
         } finally {
           if (threw) {
             try {
               fs.closeSync(fd)
             } catch (er) {}
           } else {
             fs.closeSync(fd)
           }
         }
         return ret
       }
     }
     
     function patchLutimes (fs) {
       if (constants.hasOwnProperty("O_SYMLINK")) {
         fs.lutimes = function (path, at, mt, cb) {
           fs.open(path, constants.O_SYMLINK, function (er, fd) {
             if (er) {
               if (cb) cb(er)
               return
             }
             fs.futimes(fd, at, mt, function (er) {
               fs.close(fd, function (er2) {
                 if (cb) cb(er || er2)
               })
             })
           })
         }
     
         fs.lutimesSync = function (path, at, mt) {
           var fd = fs.openSync(path, constants.O_SYMLINK)
           var ret
           var threw = true
           try {
             ret = fs.futimesSync(fd, at, mt)
             threw = false
           } finally {
             if (threw) {
               try {
                 fs.closeSync(fd)
               } catch (er) {}
             } else {
               fs.closeSync(fd)
             }
           }
           return ret
         }
     
       } else {
         fs.lutimes = function (_a, _b, _c, cb) { if (cb) process.nextTick(cb) }
         fs.lutimesSync = function () {}
       }
     }
     
     function chmodFix (orig) {
       if (!orig) return orig
       return function (target, mode, cb) {
         return orig.call(fs, target, mode, function (er) {
           if (chownErOk(er)) er = null
           if (cb) cb.apply(this, arguments)
         })
       }
     }
     
     function chmodFixSync (orig) {
       if (!orig) return orig
       return function (target, mode) {
         try {
           return orig.call(fs, target, mode)
         } catch (er) {
           if (!chownErOk(er)) throw er
         }
       }
     }
     
     
     function chownFix (orig) {
       if (!orig) return orig
       return function (target, uid, gid, cb) {
         return orig.call(fs, target, uid, gid, function (er) {
           if (chownErOk(er)) er = null
           if (cb) cb.apply(this, arguments)
         })
       }
     }
     
     function chownFixSync (orig) {
       if (!orig) return orig
       return function (target, uid, gid) {
         try {
           return orig.call(fs, target, uid, gid)
         } catch (er) {
           if (!chownErOk(er)) throw er
         }
       }
     }
     
     
     function statFix (orig) {
       if (!orig) return orig
       // Older versions of Node erroneously returned signed integers for
       // uid + gid.
       return function (target, cb) {
         return orig.call(fs, target, function (er, stats) {
           if (!stats) return cb.apply(this, arguments)
           if (stats.uid < 0) stats.uid += 0x100000000
           if (stats.gid < 0) stats.gid += 0x100000000
           if (cb) cb.apply(this, arguments)
         })
       }
     }
     
     function statFixSync (orig) {
       if (!orig) return orig
       // Older versions of Node erroneously returned signed integers for
       // uid + gid.
       return function (target) {
         var stats = orig.call(fs, target)
         if (stats.uid < 0) stats.uid += 0x100000000
         if (stats.gid < 0) stats.gid += 0x100000000
         return stats;
       }
     }
     
     // ENOSYS means that the fs doesn't support the op. Just ignore
     // that, because it doesn't matter.
     //
     // if there's no getuid, or if getuid() is something other
     // than 0, and the error is EINVAL or EPERM, then just ignore
     // it.
     //
     // This specific case is a silent failure in cp, install, tar,
     // and most other unix tools that manage permissions.
     //
     // When running as root, or if other types of errors are
     // encountered, then it's strict.
     function chownErOk (er) {
       if (!er)
         return true
     
       if (er.code === "ENOSYS")
         return true
     
       var nonroot = !process.getuid || process.getuid() !== 0
       if (nonroot) {
         if (er.code === "EINVAL" || er.code === "EPERM")
           return true
       }
     
       return false
     }
     ```

   - 由于这个node镜像里面没有vi命令，无法编辑，我们可以通过重启node镜像，使用挂载目录的方式把修正好的文件映射到容器里面，之后替换错误的文件

     ```
     docker -v 宿主机正确文件目录 容器不存在的目录 -it node:16 /bin/bash
     ```

     之后从第二步开始继续

     就可以正常安装完成

4. 通过运行的node容器生成一个gitbook镜像

   ```
   docker commit -a "liuxing" -m "gitbook image" node容器id ampregistry:5000/gitbook:1.0
   ```

5. 在需要gitbook文档的项目中，添加dockerfile来构建镜像

   ```dockerfile
   FROM ampregistry:5000/gitbook:1.0
   
   #maintainer
   MAINTAINER liuxing liuxing@megvii.com
   WORKDIR .
   RUN mkdir $WORKDIR/gitbook
   COPY . $WORKDIR/gitbook
   CMD cd $WORKDIR/gitbook &&  gitbook serve
   ```

6. 构建镜像

   - 本地构建

     在Dockerfile所在目录执行如下命令

     ```shell
     docker build -t ampregistry:5000/pangu-openapi:1.1.0 .
     ```

   - 利用jenkins打通按照分支选择构建

     ![jenkins2](https://gitee.com/firewolf/allinone/raw/master/images/jenkins2.gif)

     其中shell命令如下：

     ```shell
     #!/bin/bash
     echo "================start build docker==========="
     echo "branch="${branch}
     cd ${root_pom} 
     if [[ $branch == origin* ]]; then
        t=${branch:7}
     else
        t=${branch}
     fi
     docker_tag=${t//\//-}
     docker build -t ampregistry:5000/pangu-openapi:${docker_tag} .
     docker push ampregistry:5000/pangu-openapi:${docker_tag}
     ```

7. 启动镜像

   - 本地启动

     ```
     docker run -p 4000:4000 ampregistry:5000/pangu-openapi:1.1.0
     ```

   - devops启动

     - 添加如下编排引入openapi服务

       ```
         pangu-openapi:
           rank: 10
           hostname: MEGVII219427206
           docker:
             image: ampregistry:5000/pangu-openapi:feature-pangu-openapi-v1.1.0
             kind: daemon
             net: host
             log_max_gigabytes: 1
             envs:
             - TZ='Asia/Shanghai'
             stop_wait_secs: "30"
             start_retries: "20"
             start_secs: "5"
             image_check_policy: registry
           health_check:
             protocol: tcp
             port: "4000"
             command: "" 
       ```

     - 部署启动服务即可，

8. 访问

   ip:4000



### 文件编写与效果预览

1. 在gitbook仓库中更新文档
2. 重新构建镜像（本地或jenkins）
3. 重启镜像或devops服务即可
4. 公司中请直接在下面地址构建即可：http://jenkins.inu.megvii-inc.com/view/pangu/job/pangu-1.0.2/job/gitbook-penapi/







# 人员管理

## 上传人员图片
<a id=上传人员图片> </a>
### 基本信息

**Path：** /v1/api/person/uploadImage

**Method：** POST

**接口描述：**上传人脸图片文件，接口内部包含了人脸质量判断，图片压缩等处理，适用于需要上传人脸的所有场景,


### 请求参数
**Headers**

| 参数名称     | 参数值              | 是否必须 | 示例 | 备注 |
| ------------ | ------------------- | -------- | ---- | ---- |
| Content-Type | multipart/form-data | 是       |      |      |
| **Body**     |                     |          |      |      |

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注                                                         |
| -------- | -------- | -------- | ---- | ------------------------------------------------------------ |
| file     | file     | 是       |      | 上传人脸图片文件，接口内部包含了人脸质量判断，图片压缩等处理，适用于需要上传人脸的所有场景, 最大10m,支持格式:jpg/jpeg/bmp/png/JPG/JPEG/BMP/PNG |



### 返回数据

| 名称 | 类型   | 是否必须 | 备注          |
| ---- | ------ | -------- | ------------- |
| uri  | string | 非必须   | 上传返回的uri |

### HTTP请求示例

#### 请求 path

```
 /v1/api/person/uploadImage
```


#### 请求 body

```json
upload file stream (OMITTED)
```


### HTTP响应示例

#### 响应 200

```json
{
    "code": 0,
    "data": {
        "uri": "_ZzEwMF9mb3JldmVyQnVja2V0_9a00bafda96b4829a9ff6cd470b1c675"
    },
    "msg": "成功"
}
```

## 上传人员图片（不卡质量）

### 基本信息

**Path：** /v1/api/person/config/uploadImage

**Method：** POST

**接口描述：**上传人员图片(使用非系统入库参数,适用于1：N图片上传等场景，不建议入库)

### 请求参数

**Headers**

| 参数名称     | 参数值              | 是否必须 | 示例 | 备注 |
| ------------ | ------------------- | -------- | ---- | ---- |
| Content-Type | multipart/form-data | 是       |      |      |

**Body**

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注                                                         |
| -------- | -------- | -------- | ---- | ------------------------------------------------------------ |
| file     | file     | 是       |      | 上传人脸图片文件，接口内部包含了人脸质量判断(按照最低标准进行判断)，图片压缩等处理，适用于需要上传人脸等场景，不建议入库使用, 最大10m,支持格式:jpg/jpeg/bmp/png/JPG/JPEG/BMP/PNG |

### 返回数据

| 名称 | 类型   | 是否必须 | 备注          |
| ---- | ------ | -------- | ------------- |
| uri  | string | 非必须   | 上传返回的uri |

### HTTP请求示例

#### 请求 path

```json
/v1/api/person/config/uploadImage
```

#### 请求 body

```json
upload file stream (OMITTED)
```

### HTTP响应示例

#### 响应 200

```json
{
    "code": 0,
    "data": {
        "uri": "_ZzEwMF9mb3JldmVyQnVja2V0_9a00bafda96b4829a9ff6cd470b1c675"
    },
    "msg": "成功"
}
```



## 入库质量检测

### 基本信息

**Path：** /v1/api/misc/file/qualityJudge

**Method：** POST

**接口描述：**上传人脸图片文件质量检测，接口内部包含了人脸质量判断，不做上传，只是个质量判断。

### 请求参数

**Headers**

| 参数名称     | 参数值              | 是否必须 | 示例 | 备注 |
| ------------ | ------------------- | -------- | ---- | ---- |
| Content-Type | multipart/form-data | 是       |      |      |

**Body**

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注                                                         |
| -------- | -------- | -------- | ---- | ------------------------------------------------------------ |
| file     | file     | 是       |      | 上传人脸图片文件质量检测，接口内部包含了人脸质量判断，不做上传，只是个质量判断,最大10m,支持格式:jpg/jpeg/bmp/png/JPG/JPEG/BMP/PNG |

### 返回数据

```json
OK
```

### 质量不过关相关错误码

| code   | msg                |
| ------ | ------------------ |
| 101082 | 图片大小超过10MB   |
| 101066 | 图片格式或名称错误 |
| 101050 | 模糊度不通过       |
| 101052 | 垂直角度不通过     |
| 101053 | 水平角度不通过     |
| 101054 | 旋转角度不通过     |
| 101055 | 亮度不通过         |
| 101056 | 标准差亮度不通过   |
| 101051 | 人脸大小不通过     |
| 101101 | 面部存在遮挡       |



### HTTP请求示例

#### 请求 path

```json
 /v1/api/misc/file/qualityJudge
```

#### 请求 body

```json
upload file stream (OMITTED)
```

### HTTP响应示例

#### 响应 200

```json
{
    "code": 0,
    "msg": "成功"
}
```

相关错误码



## 批量添加人员

<a id=批量添加人员> </a>
### 基本信息

**Path：** /v1/api/person/batchAdd

**Method：** POST

**接口描述：**一次添加多个人员


### 请求参数
**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |
| **Body**     |                  |          |      |      |



<table>
  <thead class="ant-table-thead">
    <tr>
      <th key="name">名称</th>
      <th key="type">类型</th>
      <th key="required">是否必须</th>
      <th key="desc">备注</th>
    </tr>
  </thead>
  <tbody classname="ant-table-tbody">
    <tr key="0-0">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>personList</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户列表</span>
      </td>
    </tr>
    <tr key="0-0-0">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户名, 长度:[1,40]</span>
      </td>
    </tr>
    <tr key="0-0-1">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>type</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户类别, 1 员工 2访客 3 重点人员</span>
      </td>
    </tr>
    <tr key="0-0-2">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>sex</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">性别, 0 未知, 1 男, 2 女</span>
      </td>
    </tr>
    <tr key="0-0-3">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">uuid, 若填写需保证唯一, 最大长度32位</span>
      </td>
    </tr>
    <tr key="0-0-4">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>imageUri</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户识别照片的uri</span>
      </td>
    </tr>
    <tr key="0-0-5">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>code</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">员工编码,员工可填,若填写需保证唯一, 格式: 允许大小写英文字母,数字, 长度:[1,32]</span>
      </td>
    </tr>
    <tr key="0-0-6">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>identifyNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">身份证号, 格式:允许大小写英文字母,数字, 长度:[1,32]</span>
      </td>
    </tr>
    <tr key="0-0-7">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>uniqueIdentify</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">唯一标识, 若填写需要保证唯一, 格式:最大40位数字、字母和ascii码的字符</span>
      </td>
    </tr>
    <tr key="0-0-8">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitFirm</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客所属单位, 访客可填, 格式:汉字,大小写英文字母,数字, 长度: [1,40]</span>
      </td>
    </tr>
    <tr key="0-0-9">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitStartTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访起始时间(时间戳, 毫秒), 访客必填</span>
      </td>
    </tr>
    <tr key="0-0-10">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitEndTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访结束时间(时间戳, 毫秒), 访客必填</span>
      </td>
    </tr>
    <tr key="0-0-11">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitReason</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访原因, 访客可填,格式: 任意字符, 长度: [1, 255]</span>
      </td>
    </tr>
    <tr key="0-0-12">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitedUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的uuid, 访客必填</span>
      </td>
    </tr>
    <tr key="0-0-13">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitType</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客类型, 访客可填, 1 普通访客, 2 VIP（当为2，此字段必填）</span>
      </td>
    </tr>
    <tr key="0-0-14">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>email</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">邮箱, 格式: 符合一般邮箱格式即可</span>
      </td>
    </tr>
    <tr key="0-0-15">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>phone</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap;">手机号, 格式:数字, 长度:[6,18]，访客类型手机号必填，其他人员类型非必填</span>
        <br>
      </td>
    </tr>
    <tr key="0-0-16">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡号,若填写需保证唯一, 格式: 数字, 长度:[1,20]</span>
      </td>
    </tr>
    <tr key="0-0-17">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>password</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">密码,</span>仅对员工生效
        <span style="white-space: pre-wrap">&nbsp;，</span>
      </td>
    </tr>
    <tr key="0-0-18">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>entryTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">入职时间(时间戳, 毫秒),</span>员工填写生效，其他填写不生效</td>
    </tr>
    <tr key="0-0-19">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>postion</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">职位, 员工可填,格式: 任意字符, 长度: [0,64]，</span>员工填写生效，其他填写不生效</td>
    </tr>
    <tr key="0-0-20">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>birthdayStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">生日(时间戳, 毫秒)，</span>员工填写生效，其他填写不生效</td>
    </tr>
    <tr key="0-0-21">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>ext</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap;">用户自定义说明</span>
      </td>
    </tr>
    <tr key="0-0-22">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>orgUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap;">组织ID</span>
      </td>
    </tr>
    <tr key="0-0-22">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardBegin</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡片生效日期，精确到天，格式为yyyy-MM-dd</span>
      </td>
    </tr>
    <tr key="0-0-23">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>cardEnd</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡片失效日期，精确到天，格式为yyyy-MM-dd</span>
      </td>
    </tr>
    <tr key="0-0-24">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>groupList</span>
      </td>
      <td key="1">
        <span>string []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">加入的组uuid集合</span>
      </td>
    </tr>
    <tr key="array-4">
      <td key="0">
        <span style="padding-left: 40px">
          <span style="color: #8c8a8a">&nbsp;├─</span>
        </span>
      </td>
      <td key="1">
        <span>
        </span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">
        </span>
      </td>
    </tr>
  </tbody>
</table>



### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key="name">名称</th>
      <th key="type">类型</th>
      <th key="required">是否必须</th>
      <th key="desc">备注</th>
    </tr>
  </thead>
  <tbody classname="ant-table-tbody">
    <tr key="0-0">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>successes</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">成功列表</span>
      </td>
    </tr>
    <tr key="0-0-0">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户名</span>
      </td>
    </tr>
    <tr key="0-0-1">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>code</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果状态码</span>
      </td>
    </tr>
    <tr key="0-0-2">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>type</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户类别, 1 员工 2访客 3重点人员</span>
      </td>
    </tr>
    <tr key="0-0-3">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>msg</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果信息提示</span>
      </td>
    </tr>
    <tr key="0-0-4">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>sex</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">性别, 0 未知, 1 男, 2 女</span>
      </td>
    </tr>
    <tr key="0-0-5">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">uuid, 若填写需保证唯一, 最大长度32位</span>
      </td>
    </tr>
    <tr key="0-0-6">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>imageUri</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户识别照片的uri</span>
      </td>
    </tr>
    <tr key="0-0-7">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>staffCode</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">员工编码,员工可填,若填写需保证唯一, 格式: 允许大小写英文字母,数字, 长度:[1,32]</span>
      </td>
    </tr>
    <tr key="0-0-8">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>identifyNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">身份证号, 格式:允许大小写英文字母,数字, 长度:[1,32]</span>
      </td>
    </tr>
    <tr key="0-0-9">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitStartTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访起始时间(时间戳,毫秒), 访客必填</span>
      </td>
    </tr>
    <tr key="0-0-10">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitEndTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访结束时间(时间戳,毫秒), 访客必填</span>
      </td>
    </tr>
    <tr key="0-0-11">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitReason</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访原因, 访客可填,格式: 任意字符, 长度: [1, 255]</span>
      </td>
    </tr>
    <tr key="0-0-12">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitedUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的uuid, 访客必填</span>
      </td>
    </tr>
    <tr key="0-0-13">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitType</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客类型, 访客可填, 1 普通访客, 2 VIP</span>
      </td>
    </tr>
    <tr key="0-0-14">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>email</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">邮箱, 格式: 符合一般邮箱格式即可</span>
      </td>
    </tr>
    <tr key="0-0-15">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>phone</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">手机号, 格式:数字, 长度:[6,18]</span>
      </td>
    </tr>
    <tr key="0-0-16">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡号,若填写需保证唯一, 格式: 数字, 长度:[1,16]</span>
      </td>
    </tr>
    <tr key="0-0-17">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>password</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">密码, 员工可填,格式: 数字, 长度:[4,6]</span>
      </td>
    </tr>
    <tr key="0-0-18">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>entryTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">入职时间(时间戳,毫秒), 员工可填</span>
      </td>
    </tr>
    <tr key="0-0-19">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>postion</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">职位, 员工可填,格式: 任意字符, 长度: [1,64]</span>
      </td>
    </tr>
    <tr key="0-0-20">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>birthdayStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">生日(时间戳,毫秒)</span>
      </td>
    </tr>
    <tr key="0-0-21">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>ext</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">备注, 格式: 任意字符, 长度:[1,255]</span>
      </td>
    </tr>
     <tr key="0-1-22">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>orgUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织ID</span>
      </td>
    </tr>
    <tr key="0-0-23">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>groupList</span>
      </td>
      <td key="1">
        <span>string []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">加入的组uuid集合</span>
      </td>
    </tr>
    <tr key="array-5">
      <td key="0">
        <span style="padding-left: 40px">
          <span style="color: #8c8a8a">&nbsp;├─</span>
        </span>
      </td>
      <td key="1">
        <span>
        </span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">
        </span>
      </td>
    </tr>
    <tr key="0-1">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>failures</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">失败列表</span>
      </td>
    </tr>
    <tr key="0-1-0">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户名</span>
      </td>
    </tr>
    <tr key="0-1-1">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>code</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果状态码</span>
      </td>
    </tr>
    <tr key="0-1-2">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>type</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户类别, 1 员工 2访客 3重点人员</span>
      </td>
    </tr>
    <tr key="0-1-3">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>msg</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果信息提示</span>
      </td>
    </tr>
    <tr key="0-1-4">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>sex</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">性别, 0 未知, 1 男, 2 女</span>
      </td>
    </tr>
    <tr key="0-1-5">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">uuid, 若填写需保证唯一, 最大长度32位</span>
      </td>
    </tr>
    <tr key="0-1-6">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>imageUri</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户识别照片的uri</span>
      </td>
    </tr>
    <tr key="0-1-7">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>staffCode</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">员工编码,员工可填,若填写需保证唯一, 格式: 允许大小写英文字母,数字, 长度:[1,32]</span>
      </td>
    </tr>
    <tr key="0-1-8">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>identifyNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">身份证号, 格式:允许大小写英文字母,数字, 长度:[1,32]</span>
      </td>
    </tr>
    <tr key="0-1-9">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitStartTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访起始时间(时间戳,毫秒), 访客必填</span>
      </td>
    </tr>
    <tr key="0-1-10">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitEndTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访结束时间(时间戳,毫秒), 访客必填</span>
      </td>
    </tr>
    <tr key="0-1-11">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitReason</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访原因, 访客可填,格式: 任意字符, 长度: [1, 255]</span>
      </td>
    </tr>
    <tr key="0-1-12">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitedUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的uuid, 访客必填</span>
      </td>
    </tr>
    <tr key="0-1-13">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitType</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客类型, 访客可填, 1 普通访客, 2 VIP</span>
      </td>
    </tr>
    <tr key="0-1-14">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>email</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">邮箱, 格式: 符合一般邮箱格式即可</span>
      </td>
    </tr>
    <tr key="0-1-15">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>phone</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">手机号, 格式:数字, 长度:[6,18]</span>
      </td>
    </tr>
    <tr key="0-1-16">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡号,若填写需保证唯一, 格式: 数字, 长度:[1,16]</span>
      </td>
    </tr>
    <tr key="0-1-17">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>password</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">密码, 员工可填,格式: 数字, 长度:[4,6]</span>
      </td>
    </tr>
    <tr key="0-1-18">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>entryTimeStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">入职时间(时间戳,毫秒), 员工可填</span>
      </td>
    </tr>
    <tr key="0-1-19">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>postion</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">职位, 员工可填,格式: 任意字符, 长度: [1,64]</span>
      </td>
    </tr>
    <tr key="0-1-20">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>birthdayStamp</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">生日(时间戳,毫秒)</span>
      </td>
    </tr>
    <tr key="0-1-21">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>ext</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">备注, 格式: 任意字符, 长度:[1,255]</span>
      </td>
    </tr>
    <tr key="0-1-22">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>orgUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织ID</span>
      </td>
    </tr>
    <tr key="0-1-23">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>groupList</span>
      </td>
      <td key="1">
        <span>string []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">加入的组uuid集合</span>
      </td>
    </tr>
    <tr key="array-6">
      <td key="0">
        <br>
      </td>
      <td key="1">
      </td>
      <td key="2">&nbsp;</td>
      <td key="4">
        <span style="white-space: pre-wrap">
        </span>
      </td>
    </tr>
  </tbody>
</table>



### HTTP请求示例

#### 请求 path

```
 /v1/api/person/batchAdd
```


#### 请求 body

```json
 {
    "personList": [
        {
            "name": "我是员工",
            "type": 1,
            "sex": 1,
            "uuid": "ngtwr",
            "imageUri": "_ZzEwMF9mb3JldmVyQnVja2V0_91b2035456f9407ea1b8d944003bbe06",
            "code": "877849",
            "identifyNum": "911102",
            "email": "MgpbsXyhik@126.com",
            "phone": "17792842739",
            "cardNum": "220306",
            "password": "255075",
            "entryTimeStamp": 1622535057000,
            "postion": "海龙",
            "birthdayStamp": 1591113600000,
            "uniqueIdentify": "650677",
            "ext": "备注",
            "orgUuid":"507cf621f4db4d0dac14e10baad2d442",
            "groupList": [
                "976055aae31445c5a583ebc1a53c7514"
            ]
        }
    ]
}
```


### HTTP响应示例

#### 响应 200

```json
 {
    "code": 0,
    "data": {
        "failures": [],
        "successes": [
            {
                "birthdayStamp": 1591113600000,
                "cardNum": "220306",
                "email": "MgpbsXyhik@126.com",
                "entryTimeStamp": 1622535057000,
                "ext": "备注",
                "groupList": [
                    "976055aae31445c5a583ebc1a53c7514"
                ],
               "identifyNum": "911102",
                "imageUri": "_ZzEwMF9mb3JldmVyQnVja2V0_91b2035456f9407ea1b8d944003bbe06",
                "name": "我是员工",
                "password": "255075",
                "phone": "17792842739",
                "postion": "海龙",
                "sex": 1,
                "staffCode": "877849",
                "type": 1,
                "uuid": "ngtwr",
                "orgUuid":"507cf621f4db4d0dac14e10baad2d442"
            }
        ]
    },
    "msg": "成功"
}
```





## 批量删除人员

<a id=批量删除人员> </a>

### 基本信息

**Path：** /v1/api/person/batchDelete

**Method：** POST

**接口描述：**一次删除多个人员


### 请求参数
**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |
| **Body**     |                  |          |      |      |

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th>
      <th key=type>类型</th>
      <th key=required>是否必须</th>
      <th key=desc>备注</th>
    </tr>
  </thead>
  <tbody className="ant-table-tbody">
    <tr key=0-0>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>uuidList</span>
      </td>
      <td key=1>
        <span>string []</span>
      </td>
      <td key=2>必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">用户UUID列表</span>
      </td>
    </tr>
    <tr key=array-3>
      <td key=0>
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>
        </span>
      </td>
      <td key=1>
        <span>
        </span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">
        </span>
      </td>
    </tr>
  </tbody>
</table>

### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key="name">名称</th>
      <th key="type">类型</th>
      <th key="required">是否必须</th>
      <th key="desc">备注</th>
    </tr>
  </thead>
  <tbody classname="ant-table-tbody">
    <tr key="0-0">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>successes</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">成功列表</span>
      </td>
    </tr>
    <tr key="0-0-0">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">uuid</span>
      </td>
    </tr>
    <tr key="0-0-1">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>code</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果状态码</span>
      </td>
    </tr>
    <tr key="0-0-2">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>msg</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果信息提示</span>
      </td>
    </tr>
    <tr key="0-1">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>failures</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">失败列表</span>
      </td>
    </tr>
    <tr key="0-1-0">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">uuid</span>
      </td>
    </tr>
    <tr key="0-1-1">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>code</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果状态码</span>
      </td>
    </tr>
    <tr key="0-1-2">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>msg</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">结果信息提示</span>
      </td>
    </tr>
  </tbody>
</table>





### HTTP请求示例

#### 请求 path

```
  /v1/api/person/batchDelete
```


#### 请求 body

```json
{
    "uuidList": [
        "7e6916df6a05444692b0d48621892551",
        "d3eef33925544fac9c83f650be8e727b"
    ]
} 
```


### HTTP响应示例

#### 响应 200

```json
{
    "code": 0,
    "data": {
        "failures": [],
        "successes": [
            {
                "uuid": "7e6916df6a05444692b0d48621892551"
            },
            {
                "uuid": "d3eef33925544fac9c83f650be8e727b"
            }
        ]
    },
    "msg": "成功"
} 
```





## 修改人员

<a id=修改人员> </a>

### 基本信息

**Path：** /v1/api/person/update

**Method：** POST

**接口描述：**修改人员信息


### 请求参数
**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |
| **Body**     |                  |          |      |      |


<table>
               <thead class="ant-table-thead">
                              <tr>
                                             <th key=name>名称</th>
                                             <th key=type>类型</th>
                                             <th key=required>是否必须</th>
                                             <th key=desc>备注</th>
                              </tr>
               </thead>
               <tbody className="ant-table-tbody">
                              <tr key=0-0>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> uuid</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>必须</td>
                                             <td key=4><span style="white-space: pre-wrap">用户UUID</span>
                                             </td>
                              </tr>
                              <tr key=0-1>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> name</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">用户名, 长度:[1,40]</span>
                                             </td>
                              </tr>
                              <tr key=0-2>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> sex</span>
                                             </td>
                                             <td key=1><span>integer</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">性别, 0 未知, 1 男, 2 女</span>
                                             </td>
                              </tr>
                              <tr key=0-3>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> imageUri</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">用户识别照片的uri</span>
                                             </td>
                              </tr>
                              <tr key=0-4>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> code</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">员工编码,员工可填,若填写需保证唯一, 格式: 允许大小写英文字母,数字, 长度:[1,32]</span>
                                             </td>
                              </tr>
                              <tr key=0-5>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> identifyNum</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">身份证号, 格式:允许大小写英文字母,数字, 长度:[1,32]</span>
                                             </td>
                              </tr>
                              <tr key=0-6>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> uniqueIdentify</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">唯一标识, 若填写需要保证唯一, 格式:最大40位数字、字母和ascii码的字符</span>
                                             </td>
                              </tr>
                              <tr key=0-7>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> visitFirm</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">访客所属单位, 访客可填, 格式:汉字,大小写英文字母,数字, 长度: [1,40]</span>
                                             </td>
                              </tr>
                              <tr key=0-8>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> visitStartTimeStamp</span>
                                             </td>
                                             <td key=1><span>integer</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">拜访起始时间(时间戳,毫秒), 访客必填</span>
                                             </td>
                              </tr>
                              <tr key=0-9>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> visitEndTimeStamp</span>
                                             </td>
                                             <td key=1><span>integer</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">拜访结束时间(时间戳,毫秒), 访客必填</span>
                                             </td>
                              </tr>
                              <tr key=0-10>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> visitReason</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">拜访原因, 访客可填,格式: 任意字符, 长度: [1, 255]</span>
                                             </td>
                              </tr>
                              <tr key=0-11>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> visitedUuid</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">受访人的uuid, 访客必填</span>
                                             </td>
                              </tr>
                              <tr key=0-12>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> visitType</span>
                                             </td>
                                             <td key=1><span>integer</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">访客类型, 访客可填, 1 普通访客, 2 VIP</span>
                                             </td>
                              </tr>
                              <tr key=0-13>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> email</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">邮箱, 格式: 符合一般邮箱格式即可</span>
                                             </td>
                              </tr>
                              <tr key=0-14>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> phone</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">手机号, 格式:数字, 长度:[6,18]，访客类型手机号必填，其他人员类型非必填</span>
                                             </td>
                              </tr>
                              <tr key=0-15>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> cardNum</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">卡号,员工可填,若填写需保证唯一, 格式: 数字, 长度:[1,20]</span>
                                             </td>
                              </tr>
                              <tr key=0-16>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> password</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">密码, 员工可填,格式: 数字, 长度:[4,6]</span>
                                             </td>
                              </tr>
                              <tr key=0-17>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> entryTimeStamp</span>
                                             </td>
                                             <td key=1><span>integer</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">入职时间(时间戳,毫秒), 员工可填</span>
                                             </td>
                              </tr>
                              <tr key=0-18>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> postion</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">职位, 员工可填,格式: 任意字符, 长度: [0,64]</span>
                                             </td>
                              </tr>
                              <tr key=0-19>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> birthdayStamp</span>
                                             </td>
                                             <td key=1><span>integer</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">生日(时间戳,毫秒)</span>
                                             </td>
                              </tr>
                              <tr key=0-20>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> ext</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">备注, 格式: 任意字符, 长度:[0,255]</span>
                                             </td>
                              </tr>
                        	 <tr key=0-20>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> orgUuid</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">组织ID</span>
                                             </td>
                              </tr>
                              <tr key=0-21>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> groupList</span>
                                             </td>
                                             <td key=1><span>string []</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">加入的组集合</span>
                                             </td>
                              </tr>
                              <tr key=array-2>
                                             <td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> </span>
                                             </td>
                                             <td key=1><span></span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap"></span>
                                             </td>
                              </tr>
                              <tr key=0-22>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> cardBegin</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">卡片生效日期，精确到天，格式为yyyy-MM-dd</span>
                                             </td>
                              </tr>
                              <tr key=0-23>
                                             <td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> cardEnd</span>
                                             </td>
                                             <td key=1><span>string</span>
                                             </td>
                                             <td key=2>非必须</td>
                                             <td key=4><span style="white-space: pre-wrap">卡片失效日期，精确到天，格式为yyyy-MM-dd</span>
                                             </td>
                              </tr>
               </tbody>
</table>


### 返回数据

```javascript
OK
```



### HTTP请求示例

#### 请求 path

```
  /v1/api/person/update
```


#### 请求 body

```json
 {
    "uuid": "363227b5360f4a29b28d0a9543c14914",
    "name": "我是update员工",
    "type": 1,
    "sex": 1,
    "imageUri": "_ZzEwMF9mb3JldmVyQnVja2V0_2fb0ce763b5c4cb29a7d64af407a2f43",
    "code": "910025",
    "identifyNum": "185864",
    "email": "OpMJvwJc62@126.com",
    "phone": "13403743852",
    "cardNum": "1234",
    "cardBegin": "2021-08-24",
    "cardEnd": "2031-08-24",
    "password": "451548",
    "entryTimeStamp": 1622535055000,
    "postion": "海龙",
    "birthdayStamp": 1591113600000,
    "uniqueIdentify": "992514",
    "ext": "备注",
    "orgUuid":"507cf621f4db4d0dac14e10baad2d442",
    "groupList": [
        "976055aae31445c5a583ebc1a53c7514"
    ]
}
```


### HTTP响应示例

#### 响应 200

```json
  {
    "code": 0,
    "msg": "成功"
}
```



## 搜索人员列表

<a id=搜索人员列表> </a>
### 基本信息

**Path：** /v1/api/person/list

**Method：** POST

**接口描述：**搜索人员, 下列字段支持模糊搜索：name、phone、identifyNum、email、code、cardNum 


### 请求参数
**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |
| **Body**     |                  |          |      |      |

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th>
      <th key=type>类型</th>
      <th key=required>是否必须</th>
      <th key=desc>备注</th>
    </tr>
  </thead>
  <tbody className="ant-table-tbody">
    <tr key=0-0>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>pageNum</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">当前页码,不传默认1</span>
      </td>
    </tr>
    <tr key=0-1>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>pageSize</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">分页大小，不传默认20</span>
      </td>
    </tr>
    <tr key=0-2>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>type</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">用户类别, 1 员工 2访客 3重点人员</span>
      </td>
    </tr>
    <tr key=0-3>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>name</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">用户姓名</span>
      </td>
    </tr>
    <tr key=0-4>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>phone</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">手机号</span>
      </td>
    </tr>
    <tr key=0-5>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>identifyNum</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">身份证号</span>
      </td>
    </tr>
    <tr key=0-6>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>email</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">邮箱</span>
      </td>
    </tr>
    <tr key=0-7>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>code</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">员工编号</span>
      </td>
    </tr>
    <tr key=0-8>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>cardNum</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">卡号</span>
      </td>
    </tr>
    <tr key=0-9>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>uniqueIdentify</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">唯一标识</span>
      </td>
    </tr>
    <tr key=0-10>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>staffEntryMinStamp</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">员工入职日期最小值(时间戳,毫秒)</span>
      </td>
    </tr>
    <tr key=0-11>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>staffEntryMaxStamp</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">员工入职日期最大值(时间戳,毫秒)</span>
      </td>
    </tr>
    <tr key=0-12>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>visitorCreateMinStamp</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">访客创建日期最小值(时间戳,毫秒)</span>
      </td>
    </tr>
    <tr key=0-13>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>visitorCreateMaxStamp</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">访客创建日期最大值(时间戳,毫秒)</span>
      </td>
    </tr>
    <tr key=0-14>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>filterVisitedType</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">筛选访客状态类型, 0 不筛选, 1 有效, 2 无效</span>
      </td>
    </tr>
    <tr key=0-15>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>simpleInfo</span>
      </td>
      <td key=1>
        <span>boolean</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">是否仅返回简单信息(id+name), 默认false</span>
      </td>
    </tr>
    <tr key=0-16>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>isPinyinOrder</span>
      </td>
      <td key=1>
        <span>boolean</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">是否按拼音排序, 默认true</span>
      </td>
    </tr>
    <tr key=0-17>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>visitType</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">访客类型(1 普通访客, 2 VIP访客)</span>
      </td>
    </tr>
    <tr key=0-18>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>visitedName</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">受访员工姓名</span>
      </td>
    </tr>
    <tr key=0-19>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>orgUuid</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">组织uuid</span>
      </td>
    </tr>
  </tbody>
</table>



### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key="name">名称</th>
      <th key="type">类型</th>
      <th key="required">是否必须</th>
      <th key="desc">备注</th>
    </tr>
  </thead>
  <tbody classname="ant-table-tbody">
    <tr key="0-0">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>pageNum</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">当前页码</span>
      </td>
    </tr>
    <tr key="0-1">
      <td key="0">
        <span style="padding-left: 0px">&nbsp;pageSize</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">分页大小</span>
      </td>
    </tr>
    <tr key="0-2">
      <td key="0">
        <span style="padding-left: 0px">&nbsp;total</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">总数</span>
      </td>
    </tr>
    <tr key="0-3">
      <td key="0">
        <span style="padding-left: 0px">&nbsp;list</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">
        </span>
      </td>
    </tr>
    <tr key="0-3-0">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户UUID</span>
      </td>
    </tr>
    <tr key="0-3-1">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户名</span>
      </td>
    </tr>
    <tr key="0-3-2">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>type</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户类别, 1 员工 2访客 3重点人员</span>
      </td>
    </tr>
    <tr key="0-3-3">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>sex</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">性别, 0 未知, 1 男, 2 女</span>
      </td>
    </tr>
    <tr key="0-3-4">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>imageUri</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户识别照片的url</span>
      </td>
    </tr>
    <tr key="0-3-5">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>code</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">员工编码,员工非必须,唯一</span>
      </td>
    </tr>
    <tr key="0-3-6">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>identifyNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">身份证号</span>
      </td>
    </tr>
    <tr key="0-3-7">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitStartTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访起始时间(时间戳,毫秒), 访客必须</span>
      </td>
    </tr>
    <tr key="0-3-8">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitEndTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访结束时间(时间戳,毫秒), 访客必须</span>
      </td>
    </tr>
    <tr key="0-3-9">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitReason</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访原因, 访客非必须</span>
      </td>
    </tr>
    <tr key="0-3-10">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitedUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的UUID,访客必须</span>
      </td>
    </tr>
    <tr key="0-3-11">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitedName</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的姓名, 访客必须</span>
      </td>
    </tr>
    <tr key="0-3-12">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitType</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客类型, 访客必须, 1 普通访客, 2 VIP</span>
      </td>
    </tr>
    <tr key="0-3-13">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitedStatus</span>
      </td>
      <td key="1">
        <span>boolean</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客状态, 访客必须, true 有效, false 无效</span>
      </td>
    </tr>
    <tr key="0-3-14">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>email</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">邮箱</span>
      </td>
    </tr>
    <tr key="0-3-15">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>phone</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">手机号</span>
      </td>
    </tr>
    <tr key="0-3-16">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡号,员工非必须, 唯一</span>
      </td>
    </tr>
    <tr key="0-3-17">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>password</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">密码, 员工非必须</span>
      </td>
    </tr>
    <tr key="0-3-18">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>entryTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">入职时间(时间戳, 毫秒), 员工非必须</span>
      </td>
    </tr>
    <tr key="0-3-19">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>postion</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">职位, 员工非必须</span>
      </td>
    </tr>
    <tr key="0-3-20">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>birthday</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">生日(时间戳,毫秒), 员工非必须</span>
      </td>
    </tr>
    <tr key="0-3-21">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uniqueIdentify</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">唯一标识, 唯一</span>
      </td>
    </tr>
    <tr key="0-3-22">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>ext</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">备注</span>
      </td>
    </tr>
    <tr key="0-3-23">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>groupList</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">人员所属组列表</span>
      </td>
    </tr>
    <tr key="0-3-23-0">
      <td key="0">
        <span style="padding-left: 40px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组uuid</span>
      </td>
    </tr>
    <tr key="0-3-23-1">
      <td key="0">
        <span style="padding-left: 40px">
          <span style="color: #8c8a8a">├─</span>name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组名</span>
      </td>
    </tr>
    <tr key="0-3-24">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitFirm</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客所属单位, 访客非必须</span>
      </td>
    </tr>
    <tr key="0-3-25">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>isPassRecord</span>
      </td>
      <td key="1">
        <span>boolean</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">是否有通行记录</span>
      </td>
    </tr>
    <tr key="0-3-26">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>isAttendanceRecord</span>
      </td>
      <td key="1">
        <span>boolean</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">是否有考勤记录</span>
      </td>
    </tr>
    <tr key="0-3-27">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>orgUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织uuid</span>
      </td>
    </tr>
    <tr key="0-3-28">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>orgName</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织名称</span>
      </td>
    </tr>
    <tr key="0-3-26">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardBegin</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡片生效日期，精确到天，格式为yyyy-MM-dd</span>
      </td>
    </tr>
    <tr key="0-3-26">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardEnd</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡片失效日期，精确到天，格式为yyyy-MM-dd</span>
      </td>
    </tr>
  </tbody>
</table>





### HTTP请求示例

#### 请求 path

```
/v1/api/person/list
```


#### 请求 body

```json
{
    "type": 1,
    "name": "备用员工1"
} 
```


### HTTP响应示例

#### 响应 200

```json
{
	"code": 0,
	"data": {
		"list": [
			{
				"code": "13925810",
				"ext": "",
				"groupList": [
					{
						"name": "员工默认组"
					}
				],
				"identifyNum": "1234",
				"isAttendanceRecord": false,
				"isPassRecord": false,
				"name": "123456",
				"password": "123456",
				"postion": "",
				"sex": 0,
				"type": 1,
				"orgUuid": "950d737b2051410986698adb493965d5",
				"orgName": "一级组织",
				"uuid": "950d737b2051410986698adb493965d5"
			}
		],
		"pageNum": 1,
		"pageSize": 20,
		"total": 1
	},
	"msg": "成功"
}
```



 ## 根据uuid列表返回人员详细信息列表

<a id=根据uuid列表返回人员详细信息列表> </a>

### 基本信息

**Path：** /v1/api/person/uuid/list

**Method：** POST

**接口描述：**通过uuid列表一次获取多个人员信息


### 请求参数
**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |
| **Body**     |                  |          |      |      |

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key="name">名称</th>
      <th key="type">类型</th>
      <th key="required">是否必须</th>
      <th key="desc">备注</th>
    </tr>
  </thead>
  <tbody classname="ant-table-tbody">
    <tr key="0-0">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>uuidList</span>
      </td>
      <td key="1">
        <span>string []</span>
      </td>
      <td key="2">必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">人员UUID列表</span>
      </td>
    </tr>
    <tr key="array-7">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>
        </span>
      </td>
      <td key="1">
        <span>
        </span>
      </td>
      <td key="2">&nbsp;</td>
      <td key="4">
        <span style="white-space: pre-wrap">
        </span>
      </td>
    </tr>
  </tbody>
</table>


### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key="name">名称</th>
      <th key="type">类型</th>
      <th key="required">是否必须</th>
      <th key="desc">备注</th>
    </tr>
  </thead>
  <tbody classname="ant-table-tbody">
    <tr key="0">
      <td key="0">
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>
        </span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">
        </span>
      </td>
    </tr>
    <tr key="0-0">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户UUID</span>
      </td>
    </tr>
    <tr key="0-1">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户名</span>
      </td>
    </tr>
    <tr key="0-2">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>type</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户类别, 1 员工 2访客 3重点人员</span>
      </td>
    </tr>
    <tr key="0-3">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>sex</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">性别, 0 未知, 1 男, 2 女</span>
      </td>
    </tr>
    <tr key="0-4">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>imageUri</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户识别照片的url</span>
      </td>
    </tr>
    <tr key="0-5">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>code</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">员工编码,员工非必须,唯一</span>
      </td>
    </tr>
    <tr key="0-6">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>identifyNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">身份证号</span>
      </td>
    </tr>
    <tr key="0-7">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitStartTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访起始时间(时间戳,毫秒), 访客必须</span>
      </td>
    </tr>
    <tr key="0-8">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitEndTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访结束时间(时间戳,毫秒), 访客必须</span>
      </td>
    </tr>
    <tr key="0-9">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitReason</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访原因, 访客非必须</span>
      </td>
    </tr>
    <tr key="0-10">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitedUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的UUID,访客必须</span>
      </td>
    </tr>
    <tr key="0-11">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitedName</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的姓名, 访客必须</span>
      </td>
    </tr>
    <tr key="0-12">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitType</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客类型, 访客必须, 1 普通访客, 2 VIP</span>
      </td>
    </tr>
    <tr key="0-13">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>visitedStatus</span>
      </td>
      <td key="1">
        <span>boolean</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客状态, 访客必须, true 有效, false 无效</span>
      </td>
    </tr>
    <tr key="0-14">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>email</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">邮箱</span>
      </td>
    </tr>
    <tr key="0-15">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>phone</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">手机号</span>
      </td>
    </tr>
    <tr key="0-16">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡号,员工非必须, 唯一</span>
      </td>
    </tr>
    <tr key="0-17">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>password</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">密码, 员工非必须</span>
      </td>
    </tr>
    <tr key="0-18">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>entryTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">入职时间(时间戳, 毫秒), 员工非必须</span>
      </td>
    </tr>
    <tr key="0-19">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>postion</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">职位, 员工非必须</span>
      </td>
    </tr>
    <tr key="0-20">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>birthday</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">生日(时间戳,毫秒), 员工非必须</span>
      </td>
    </tr>
    <tr key="0-21">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uniqueIdentify</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">唯一标识, 唯一</span>
      </td>
    </tr>
    <tr key="0-22">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>ext</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">备注</span>
      </td>
    </tr>
    <tr key="0-23">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>groupList</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">人员所属组列表</span>
      </td>
    </tr>
    <tr key="0-23-0">
      <td key="0">
        <span style="padding-left: 40px">
          <span style="color: #8c8a8a">&nbsp;├─</span>uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组uuid</span>
      </td>
    </tr>
    <tr key="0-23-1">
      <td key="0">
        <span style="padding-left: 40px">
          <span style="color: #8c8a8a">├─</span>name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组名</span>
      </td>
    </tr>
    <tr key="0-24">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>visitFirm</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客所属单位, 访客非必须</span>
      </td>
    </tr>
    <tr key="0-25">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>orgUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织uuid</span>
      </td>
    </tr>
    <tr key="0-25">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>orgName</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织名称</span>
      </td>
    </tr>
    <tr key="0-25">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardBegin</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡片生效日期，精确到天，格式为yyyy-MM-dd</span>
      </td>
    </tr>
    <tr key="0-26">
      <td key="0">
        <span style="padding-left: 20px">
          <span style="color: #8c8a8a">├─</span>cardEnd</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡片失效日期，精确到天，格式为yyyy-MM-dd</span>
      </td>
    </tr>
  </tbody>
</table>




### HTTP请求示例

#### 请求 path

```
  /v1/api/person/uuid/list
```


#### 请求 body

```json
 {
    "uuidList": [
        "0ff02877cceb4b2eab1a737854165b21"
    ]
}
```


### HTTP响应示例

#### 响应 200

```json
{
    "code": 0,
    "data": [
        {
            "birthday": 1591113600000,
            "cardNum": "717277",
            "code": "158138",
            "email": "zcsEou3S0G@126.com",
            "entryTime": 1622535059000,
            "ext": "备注",
            "groupList": [
                {
                    "name": "员工默认组"
                }
            ],
            "identifyNum": "840369",
            "orgUuid": "950d737b2051410986698adb493965d5",
			"orgName": "一级组织"
} 
```



## 根据uuid返回人员信息

<a id=根据uuid返回人员信息> </a>
### 基本信息

**Path：** /v1/api/person/query

**Method：** POST

**接口描述：通过uuid获取个人员信息**


### 请求参数
**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |
| **Body**     |                  |          |      |      |

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th>
      <th key=type>类型</th>
      <th key=required>是否必须</th>
      <th key=desc>备注</th>
    </tr>
  </thead>
  <tbody className="ant-table-tbody">
    <tr key=0-0>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>uuid</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">员工、访客uuid</span>
      </td>
    </tr>
  </tbody>
</table>


### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key="name">名称</th>
      <th key="type">类型</th>
      <th key="required">是否必须</th>
      <th key="desc">备注</th>
    </tr>
  </thead>
  <tbody classname="ant-table-tbody">
    <tr key="0-0">
      <td key="0">
        <span style="padding-left: 20px">
          uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户UUID</span>
      </td>
    </tr>
    <tr key="0-1">
      <td key="0">
        <span style="padding-left: 20px">
          name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户名</span>
      </td>
    </tr>
    <tr key="0-2">
      <td key="0">
        <span style="padding-left: 20px">
          type</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户类别, 1 员工 2访客 3重点人员</span>
      </td>
    </tr>
    <tr key="0-3">
      <td key="0">
        <span style="padding-left: 20px">
          sex</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">性别, 0 未知, 1 男, 2 女</span>
      </td>
    </tr>
    <tr key="0-4">
      <td key="0">
        <span style="padding-left: 20px">
          imageUri</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">用户识别照片的url</span>
      </td>
    </tr>
    <tr key="0-5">
      <td key="0">
        <span style="padding-left: 20px">
          code</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">员工编码,员工非必须,唯一</span>
      </td>
    </tr>
    <tr key="0-6">
      <td key="0">
        <span style="padding-left: 20px">
          identifyNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">身份证号</span>
      </td>
    </tr>
    <tr key="0-7">
      <td key="0">
        <span style="padding-left: 20px">
          visitStartTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访起始时间(时间戳,毫秒), 访客必须</span>
      </td>
    </tr>
    <tr key="0-8">
      <td key="0">
        <span style="padding-left: 20px">
          visitEndTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访结束时间(时间戳,毫秒), 访客必须</span>
      </td>
    </tr>
    <tr key="0-9">
      <td key="0">
        <span style="padding-left: 20px">
          visitReason</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">拜访原因, 访客非必须</span>
      </td>
    </tr>
    <tr key="0-10">
      <td key="0">
        <span style="padding-left: 20px">
          visitedUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的UUID,访客必须</span>
      </td>
    </tr>
    <tr key="0-11">
      <td key="0">
        <span style="padding-left: 20px">
          visitedName</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">受访人的姓名, 访客必须</span>
      </td>
    </tr>
    <tr key="0-12">
      <td key="0">
        <span style="padding-left: 20px">
          visitType</span>
      </td>
      <td key="1">
        <span>integer</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客类型, 访客必须, 1 普通访客, 2 VIP</span>
      </td>
    </tr>
    <tr key="0-13">
      <td key="0">
        <span style="padding-left: 20px">
          visitedStatus</span>
      </td>
      <td key="1">
        <span>boolean</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客状态, 访客必须, true 有效, false 无效</span>
      </td>
    </tr>
    <tr key="0-14">
      <td key="0">
        <span style="padding-left: 20px">
          email</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">邮箱</span>
      </td>
    </tr>
    <tr key="0-15">
      <td key="0">
        <span style="padding-left: 20px">
          phone</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">手机号</span>
      </td>
    </tr>
    <tr key="0-16">
      <td key="0">
        <span style="padding-left: 20px">
          cardNum</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">卡号,员工非必须, 唯一</span>
      </td>
    </tr>
    <tr key="0-17">
      <td key="0">
        <span style="padding-left: 20px">
          password</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">密码, 员工非必须</span>
      </td>
    </tr>
    <tr key="0-18">
      <td key="0">
        <span style="padding-left: 20px">
          entryTime</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">入职时间(时间戳, 毫秒), 员工非必须</span>
      </td>
    </tr>
    <tr key="0-19">
      <td key="0">
        <span style="padding-left: 20px">
          postion</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">职位, 员工非必须</span>
      </td>
    </tr>
    <tr key="0-20">
      <td key="0">
        <span style="padding-left: 20px">
          birthday</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">生日(时间戳,毫秒), 员工非必须</span>
      </td>
    </tr>
    <tr key="0-21">
      <td key="0">
        <span style="padding-left: 20px">
          uniqueIdentify</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">唯一标识, 唯一</span>
      </td>
    </tr>
    <tr key="0-22">
      <td key="0">
        <span style="padding-left: 20px">
          ext</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">备注</span>
      </td>
    </tr>
    <tr key="0-23">
      <td key="0">
        <span style="padding-left: 20px">
          groupList</span>
      </td>
      <td key="1">
        <span>object []</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">人员所属组列表</span>
      </td>
    </tr>
    <tr key="0-23-0">
      <td key="0">
        <span style="padding-left: 40px">
          uuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组uuid</span>
      </td>
    </tr>
    <tr key="0-23-1">
      <td key="0">
        <span style="padding-left: 40px">
          name</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组名</span>
      </td>
    </tr>
    <tr key="0-24">
      <td key="0">
        <span style="padding-left: 20px">
          visitFirm</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">访客所属单位, 访客非必须</span>
      </td>
    </tr>
    <tr key="0-25">
      <td key="0">
        <span style="padding-left: 20px">
          orgUuid</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织uuid</span>
      </td>
    </tr>
    <tr key="0-25">
      <td key="0">
        <span style="padding-left: 20px">
          orgName</span>
      </td>
      <td key="1">
        <span>string</span>
      </td>
      <td key="2">非必须</td>
      <td key="4">
        <span style="white-space: pre-wrap">组织名称</span>
      </td>
    </tr>
  </tbody>
</table>




### HTTP请求示例

#### 请求 path

```
   /v1/api/person/query
```


#### 请求 body

```json
 {
    "uuid": "72ec703308e840cfb05287ac9f268be5"
}
```



### HTTP响应示例

#### 响应 200

```json
 {
    "code": 0,
    "data": {
        "birthday": 1591113600000,
        "cardNum": "006910",
        "code": "480914",
        "email": "BSSQPEa7Jk@126.com",
        "entryTime": 1622535059000,
        "ext": "备注",
        "groupList": [
            {
                "name": "员工默认组",
                "uuid": "976055aae31445c5a583ebc1a53c7514"
            }
        ],
        "identifyNum": "347876",
        "imageUri": "http://10.171.4.130/pub/_ZzEwMF9mb3JldmVyQnVja2V0_a53fe0cb76074585ba477a910a3c9b3f?timestamp=1623139902&sig=d5f91198ebbe3fe74c06a56adc7faf22",
        "name": "备用员工1",
        "password": "060844",
        "phone": "13262729364",
        "cardNum": "1234",
        "cardBegin": "2021-08-24",
        "cardEnd": "2031-08-24",
        "postion": "海龙",
        "sex": 1,
        "type": 1,
        "uuid": "72ec703308e840cfb05287ac9f268be5"
    },
    "msg": "成功"
}
```





## 根据员工、访客UUID获取二维码

<a id=根据员工、访客UUID获取二维码> </a>

### 基本信息

**Path：** /v1/api/person/visitorCode

**Method：** POST

**接口描述：**员工、访客获取二维码使用


### 请求参数
**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |
| ** Body**    |                  |          |      |      |

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th>
      <th key=type>类型</th>
      <th key=required>是否必须</th>
      <th key=desc>备注</th>
    </tr>
  </thead>
  <tbody className="ant-table-tbody">
    <tr key=0-0>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>uuid</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">员工、访客uuid</span>
      </td>
    </tr>
  </tbody>
</table>


### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th>
      <th key=type>类型</th>
      <th key=required>是否必须</th>
      <th key=desc>备注</th>
    </tr>
  </thead>
  <tbody className="ant-table-tbody">
    <tr key=0-0>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>qrCode</span>
      </td>
      <td key=1>
        <span>string</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">员工、访客二维码图片(base64编码的字符串)</span>
      </td>
    </tr>
    <tr key=0-1>
      <td key=0>
        <span style="padding-left: 0px">
          <span style="color: #8c8a8a">
          </span>expireTime</span>
      </td>
      <td key=1>
        <span>integer</span>
      </td>
      <td key=2>非必须</td>
      <td key=4>
        <span style="white-space: pre-wrap">二维码图片的过期分钟数</span>
      </td>
    </tr>
  </tbody>
</table>



### HTTP请求示例

#### 请求 path

```
/v1/api/person/visitorCode
```

#### 请求 body

```json
  {
    "uuid": "72ec703308e840cfb05287ac9f268be5"
}
```


### HTTP响应示例

#### 响应 200

```json
 {
    "code": 0,
    "data": {
        "expireTime": 10,
        "qrCode": "data:image/png;base64,R0lGODlhyADIAPAAAAAAAP///ywAAAAAyADIAEAC/4yPqcvtD6OctNpbgd68+/8lHQaGTQlgIso57PtOo3rMdXuW6+bqCNrj/XARG834QMI0O+YQ1vREabelr2rCpp4x7rZIPIYDVupCuQw6tbIxyZ2zytcGtJDtneb3+H59bDdXJgEU1wV29weiJveWSAYI5wcpaJZ0pXgIEShl+HUWCWVWyIDEGGV6kTp56mmph0inQJr5ebnouVrb9kh7m5WRRiispfsriGuBzAK7m4tJqRnN/MrqxenLaOxrzAt8zIkqCfrYfGzJPT7dWqy+TVy93D3cGyrr3ThqbysuGst/DVq/e3zIfQvnquC6gMmcWVMGb1o6eXPQIRvYqV1Dif/qHFHMxpCgxo/uBD6rh/KbtpIb8a0sl3ASyYwOa00UKSYlzVkREco0+c+lQnbVihrcaS7pUHoVl5YCWhPcTJayZnq0tQyjOZBUsEHzCnMkQLFZTx6cGjZn1Z5Uxx6dR7ZjyKla5wrtuu+dTow3kcY8SvSc0cGECxs+jDix4sWMG8PdtO/l2rQcdeqdjJMn5buQLX/N2/aq0n99t7LdrHDQQ5ug/QF22vns3rk+Y7ulAFalPs8tU8tdjft3Y9u5b6uOOnq4cru0hS8UG9i0axVckf/VLBvzbdHXa1fO3Pey38XiwzuvLrX309nrdbcfz5ry4+fxM9eFOF3yTvR8P/P/bgqcdNrN551499kW3XVXeZccfd8Z1xZ/qkDl4HGvlDZhJcy9l2BRaMnXGniRbRhVeRQyqGBzIKpnHXS+raidgNT8hBp+MaboYoAvSqOfjJw52KNZ2+FY35A0DkiSjBgaZh6L1DmnZIj7hQZbcB/19x9W/lH0o34WHrjcewYSCSRT+eiY42tB7YbkdCay1+JbEQWZpmBxwYmdkUXiI+GdXJL44I8WzmdRjXsKWVxgKJL2oZYbJVoloTtOCSOl7oWJaaaabsppp55+CmqoojL6YYNeRggloqiWamahN4o5IqxZKnpeqhy2quKrh95qo6P2qYpncYuqWWSBp2WnZ3oA/+5a4rHJChtrsVKaamZZdUKLJ61urtomj5MuGG2FznaZYbfwPZnlmGjGaeWvfsrj47fyHulrvVGyOqqcswKra575/ksns3kuSSzAxHF77r0iathrvLxK66Srls6IIKANYuvuwZWSe224EmvbL3pNLnypvxaXC6GhZa7b7rMIU0uvpA+bvObMH+Mqa7+OIQwpqRkPTKHD+v4s8om17utzslgS7azMEC/bslXzDj3xyAmjPKiUWatsbMRTr9ys1+92/SfLZF9N78XT4kz10i47STC6JYPZc8U2k8mnx5Guffe7gxWdrq0a61xt01T2PfiZatlb5ali282utYl/ibLQUf/PWfneDMfTKMjnnn224yQzXjPVb44esLgbf7014b1ajTFdbCr+YOxgcgfc6aRDXvvRyMIc9s+edygwgVr7PjHwqgtfOL5t8wuvkHkHG/TJdXIs8PC50m5747N376Hgy1e9Zcquc4606eGCLr7x2eI9++3bW153y4IiL7Hkvacf6PZ9sus2wM0tTAI0nwHBtS2uGU2BjTrc0yhmP//xLXhoC4bKPNc6A4aued7KWb3cx7yZ6U93lhNdBft3ve6My3rZk2B+5ufCDi7ObWpj4AD9xkHZdexx44sbYkD4OcOtLm2Zwwv/6HcRFtKsYMoD4s1SSMSxTdBOTPSe2Ro4xfr/qcuC5qohspxmsDCKcYxkLKMZz4jGNKpxjWxsYxmxqLekkfB7C4Tj9NCiPBpKj45wgyPT/JhHGHowf3XEIvYimLvypa6ATRRfIy+owkoNC4UtjCLvcMe7DMqPZSXM4QZxeMXAUa+PkLTk5R61vhWWbn8hKyQmwaas5E3Kh5GUBviK58gYkm+UtsSfIClotUB6MIPsQ6UNP6jIPUpxiABUJgX1GEpzTRJ2rrTi80ypSbaxcndAE2XJJrnIVCbwjyEsGOWMKE0HwlKYVUQcIh/4t+Mxc53WpGIspYZNcbaSl9yMnf5MOEJ99lOgSORnA++ow2eyjqALNWjnyBhM6HlR/2lu/GH5dDfNXIKqgP/rIc+qJ8I4AhNzg5SoNjlK0kyK1KMvLOkwfQnFB4Lxk+ZMIpMuCtKRzlNhJ0wdO0+ZzmNuMnLyLGc3UadNuq30lQXtojMpedS3jY6mF1JnMac6xaEuMaqdROcBG/pNnEoSppXk4ldX+VPibVWpQlXiIZ0Iv4YlFVBgfKL6jvhUhMI1kTyM51iNecOy3pOssOyoX7EK2BMa9pf2nGPSfFrP+kFWjsiL6GPzGtmsYpayx7SsWfH52SR9DZrwZOxhEwrU6JmStNtk7WlBm9qAvg+UpaVtVTfHUqjtVafkHOg8OyrboPayrTEjbEbx6k5qdvZl9f+c7DV5K1UNFjW6jm3mZa8L3beKNbBQZatRtepdZBK3neRN7UQxqE7Xxi+u1k3ceWsZxL9yF6Ho7WsOfxrc+CL1dVZNqdxkyd9x6vesof2tfxF4vsEmdotr9eSBT7pU6NV1tLrU64NXCU4Ja1ShL7UvZ6HGU+3tMMHn5DD68AhfBmtxpSUe32tFG1JS7vN3mpOhi+dqW61W98YxnjEEM5xc/zLyqcf1MWuBPLnpBhDGiyPmdndpVCcv2I8U7fAXnyxZB3tTvHbk43C//OOcNja9dDXuhXE525qWUs37fS49i8vDuO32zQVGsUvvWjY3hxO5TX7onaMKW/CWmc//Pej/ZmVqUz0TWbN/FnSQ4yxQ2N6Szlw9tD0dbeJtZtO2E8Zua40y0yyqks1V7vOWT/tK5ZI4wuGbsocvCWFX07jHpX5ndxlL1cVGk5tqteutOc3QmF4avoe0sIA9C9VAs1fThQRifpGtagj2Wpc7BiiWRS1r2k17mWF9dWKG/OgoJ5rS4P5UuRvMyQpnVsbU9TOp/xlr3SZT2AhGLU9bXF8wX7XW1sbtvc+81WdHesPoNuuS05xsViN6uYRO6yxHXenVapjhTsUwxFEqX9Ximcsav61wfdvtkNsb1+72OMf37Vxmt7S2C7dxqiH+SO7im9pCrLhKBbxtwarXyh1Hdr4BZKwYZ3+U0FS9H84ZI/SMZzmfa84t0L9d2aHbPOAKL7itz+1PRpeXwTmn9M9F3HKOa3eny6ay2B+O8FDzb8fApXJ4377sENcYyu12XsLXTmaLJ3bP862o3/8O+MALfvCEL/zgCwAAOw=="
    },
    "msg": "成功"
}
```

