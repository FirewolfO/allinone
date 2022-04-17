# 类初始化过程

- 加载
- 连接
  - 验证
  - 准备
  - 解析
- 初始化

# 类加载器

- BootStrap类加载器：加载rt.jar、resource.jar等，只加载特定包下面的类
- ExtClassLoader：加载jar/lib/ext下的包
- AppClassLoader：加载classpath环境变量下的包



# 双亲委派

1. 防止类重复加载
2. 避免核心API被篡改

# 两个类相同的条件

- 类的完整类名必须一样，包括包名；
- 加载它的类加载器必须相同；
