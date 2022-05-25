package main

import (
	"fmt"
)


type Vertex struct{
	x int 
	y int
}

func scores(){
	var stus [2]string
	stus[0] = "zhangsan"
	stus[1] = "lisi"

	scores := [6]int{60,70,80,90,100,110}
	fmt.Println(scores)
	fmt.Println(stus)
}

func cuter(){
	names := [4]string{  //定义数组
		"zhangsan",
		"lisi",
		"wangwu",
		"zhaoliu",
	}

	fmt.Println(names)

	a := names[0:2]  //定义切片
	b := names[1:3]  //定义切片
	fmt.Println(a)

	b[0] = "wangba"  //修改其中一个切片，会导致其他切片以及数组被修改
	fmt.Println(names)
	fmt.Println(a)
}

func cuter2(){
	var a = [4]int{1,2,3,4}
	fmt.Println(a)
	var s = a[0:4]
	fmt.Println(s)
	s = a[:4]
	fmt.Println(s)
	s = a[0:]
	fmt.Println(s)
	s = a[:]	
	fmt.Println(s)
}

func printSlice(s []int) {
	fmt.Printf("len=%d cap=%d %v\n", len(s), cap(s), s)
}

func cuter3(){
	s := []int{2, 3, 5, 7, 11, 13};
	printSlice(s)

	s = s[2:]
	printSlice(s)
}

func cuter4(){
	a := make([]int,4) 
	fmt.Println(a) // {0,0,0,0}

	b := make([]int , 1, 4) 
	fmt.Println(b) // {0}
}

func list(){
	stus := []string{"zhangsan","lisi","wangwu"}
	for i,stu := range stus {
		fmt.Printf("第 %d 个学生名字是 %s", i+1, stu)
	}
}

type Stu struct {
	age int
	name string
}
var m map[string]Stu //定义映射变量，键是string，值是Stu

var m2 = map[string]Stu{
	"s003":Stu{10,"wanguwu"},
	"s004":Stu{20,"zhaoliu"},
}
func listmap(){
	m := make(map[string]Stu)  //初始化映射
	m["s001"] = Stu{20,"zhangsan"} //添加元素
	m["s002"] = Stu{30,"lisi"}
	fmt.Println(m)
	fmt.Println(m2)

	for key,value := range m{
		fmt.Printf("学号=%s, 姓名=%s\n", key,value.name)
	}

	for _,value := range m{
		fmt.Printf("姓名=%s\n",value.name) //省略key
	}

	for key,_ := range m{
		fmt.Printf("学号=%s\n",key) //省略value
	}
}


func main() {

	listmap()

	// list()

	// v := Vertex{1,2}
	// p := &v //指向结构体
	// fmt.Println(p.x) //指针访问字段
	// p.x = 10 //指针修改结构体字段
	// fmt.Println(v)  
	// n := Vertex{x:100}
	// fmt.Println(n)

	// scores()
	// cuter4()



	// p = &Vertex{1000,1000} //修改指向
	// fmt.Println(p.y) 

	// var i int = 42
	// var f float64 = float64(i)
	// var u uint = uint(f)

	// k := float64(i)
	// m := uint(k)
	// fmt.Println(i,f,u,m,k)


	// fmt.Println("HelloWorld!!!")


	// fmt.Println(add(11,33))

	// fmt.Println(swap(1,2))

	// forloop(10)

	// sex := sexJudge(2)
	// fmt.Println(sex)

	// fmt.Println(convertNumToChinese(10))
	// fmt.Println(converAge(13))

	// stack(5)

	// pointer()

}

func pointer(){
	i,j := 100,30
	p := &i //指向i
	fmt.Println(*p) //通过指针读取i的值

	*p = 40 //通过指针修改i的值
	fmt.Println(i)

	p = &j //指向j
	*p = *p/2 // 通过指针使用j
	fmt.Println(j) //查看j的值
}

func stack(num int){
	for i:=0; i< num; i++{
		defer fmt.Printf("i=%d", i)
	}
}


func converAge(age int) string{
	switch{
	case age >60:
		return "老人"
	case age > 20:
		return "年轻人"
	default:
		return "孩子"
	}
}

func add( x int , y int) int {
	return x+y
}


func swap(x, y int) (int, int){
	return y, x 
}

func forloop(count int){
	for i := 0; i<count; i++ {
		fmt.Printf("i=%d\n", i)
	}

	j := 1
	for ; j < 5; j++{
		fmt.Printf("j=%d\n", j)
	}

	x := 1
	for x < 10 {
		fmt.Printf("x=%d\n",x)
		x++
	}
}

func sexJudge(sex int) string{
	if v := 100 ; sex == 1 {
		fmt.Println(v)
		return "男"
	} else {
		fmt.Println(v+10)
		return "女"
	}
}

func convertNumToChinese(num int) string{
	switch num {
	case 10:
		return "十"
	case 100:
		return "百"
	case 1000:
		return "千"
	default:
		return "其他"
	}
}