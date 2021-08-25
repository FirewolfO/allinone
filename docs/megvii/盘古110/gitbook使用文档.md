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

