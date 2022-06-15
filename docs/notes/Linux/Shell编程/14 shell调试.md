任何一个Shell开发者，都必须掌握所需要的调试技术，对于初学者来说，这显得尤为重要。因为在编写Shell脚本的时候，经常会遇到各种各样的错误，通过调试技术，可以快速地排除错误。

# 使用echo调试

echo命令是Shell编程中最简单的调试技术。当用户需要验证程序中某个变量的值时，就可以直接使用echo命令将该变量的值输出到屏幕。



# 使用trap命令调试

trap命令可以捕获指定的信号，并且执行预定的命令，其基本语法如下：

```powershell
trap 'command' signal
```

其中，参数command表示捕获指定的信号后要执行的命令，而参数signal表示指定的信号

在Shell脚本执行的时候，会产生3个所谓的伪信号，分别为EXIT、ERR以及DEBUG。其中，EXIT信号在退出某个函数或者某个脚本执行完成时触发，ERR信号在某条命令返回非0状态时触发，DEBUG信号在脚本的每一条命令执行之前触发。

如：

```powershell
#!/bin/bash
error()
{
        echo "Line $1 Error：Commond or function exited with status code $?"
}
func()
{
        return 1;
}
trap 'error $LINENO' ERR
abc
func
```

这个脚本中定义了信号处理函数，在函数中，使用位置变量$1获取用户传递给函数的参数，使用系统变量$?获取上一个命令的退出状态码。

之后使用trap命令捕获ERR信号，并且将前面定义的ERRTRAP()函数作为ERR信号被触发时的响应函数，传递给ERRTRAP()函数的参数为Shell内置变量$LINENO，该变量代表当前执行的行号。

执行结果如下：

```powershell
trap.sh: line 11: abc: command not found
Line 11 Error：Commond or function exited with status code 127
Line 8 Error：Commond or function exited with status code 1
```



# 使用tee命令调试

在普通的语句中，用户使用echo和trap命令就可以非常轻松地完成调试，但是对于管道或者重定向来说，使用上面两种方法就显得心有余而力不足，因为在管道的作用下，一些命令的输出结果将会直接成为下一个命令的输入，中间结果并不会显示在屏幕上。

tee命令就可以轻松地完成任务。tee命令会从标准输入读取数据，将其内容输出到标准输出设备，同时又可将内容保存成文件。

如：

```powershell
#!/bin/bash
fcount=`ls -l | tee fList.txt | wc -l`
echo $fcount
```



# 调试钩子

我们一在调试程序的时候都可以设定一个开关变量，当该变量的值为真时，才输出调试信息；否则，不输出调试信息。

```powershell
#!/bin/bash
d=true
debug()
{
        if [ "$d" == "true" ];then
                $@
        fi
}
#调用调试函数
debug echo "a=$a"
a=10
debug echo "a=$a"
```

这样，我们可以通过改变d的值来决定是否调试。
