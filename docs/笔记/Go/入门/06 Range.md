# 基本规则

`for` 循环的 `range` 形式可遍历切片或映射。

## 遍历切片

- 基本格式：`for i, v := range 切片`
- 每次迭代都会返回两个值。第一个值为当前元素的下标，第二个值为该下标所对应元素的一份副本。
- 可以将下标或值赋予 `_` 来忽略它
  - 只需要索引：`for i, _ := range 切片`  或者 `for i := range 切片 `
  - 只需要值：`for _, value := range 切片`

## 遍历映射

- `for` 循环的 `range` 形式可遍历切片或映射。`for i, v := range 映射`
- 每次迭代都会返回两个值。第一个值为当前元素的key，第二个值为为value
- 只需要key：`for key, _ := range 映射`  
- 只需要value：`for _, value := range 映射`



# 使用示例

## 遍历切片

```go
func list(){
	stus := []string{"zhangsan","lisi","wangwu"}
	for i,stu := range stus {
		fmt.Printf("第 %d 个学生名字是 %s", i+1, stu)
	}
}
```

## 遍历映射

```go
func listmap(){
	m := make(map[string]Stu)  //初始化映射
	m["s001"] = Stu{20,"zhangsan"} //添加元素
	m["s002"] = Stu{30,"lisi"}
	for key,value := range m{
		fmt.Printf("学号=%s, 姓名=%s", key,value.name)
	}
}
```

