package main

import (
	"errors"
	"fmt"
	"math"
)

type Vertex struct {
	x int
	y int
}

func scores() {
	var stus [2]string
	stus[0] = "zhangsan"
	stus[1] = "lisi"

	scores := [6]int{60, 70, 80, 90, 100, 110}
	fmt.Println(scores)
	fmt.Println(stus)
}

func cuter() {
	names := [4]string{ //定义数组
		"zhangsan",
		"lisi",
		"wangwu",
		"zhaoliu",
	}

	fmt.Println(names)

	a := names[0:2] //定义切片
	b := names[1:3] //定义切片
	fmt.Println(a)

	b[0] = "wangba" //修改其中一个切片，会导致其他切片以及数组被修改
	fmt.Println(names)
	fmt.Println(a)
}

func cuter2() {
	var a = [4]int{1, 2, 3, 4}
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

func cuter3() {
	s := []int{2, 3, 5, 7, 11, 13}
	printSlice(s)

	s = s[2:]
	printSlice(s)
}

func cuter4() {
	a := make([]int, 4)
	fmt.Println(a) // {0,0,0,0}

	b := make([]int, 1, 4)
	fmt.Println(b) // {0}
}

func list() {
	stus := []string{"zhangsan", "lisi", "wangwu"}
	for i, stu := range stus {
		fmt.Printf("第 %d 个学生名字是 %s", i+1, stu)
	}
}

type Stu struct {
	age  int
	name string
}

var m map[string]Stu //定义映射变量，键是string，值是Stu

var m2 = map[string]Stu{
	"s003": Stu{10, "wanguwu"},
	"s004": Stu{20, "zhaoliu"},
}

func listmap() {
	m := make(map[string]Stu)       //初始化映射
	m["s001"] = Stu{20, "zhangsan"} //添加元素
	m["s002"] = Stu{30, "lisi"}
	fmt.Println(m)
	fmt.Println(m2)

	for key, value := range m {
		fmt.Printf("学号=%s, 姓名=%s\n", key, value.name)
	}

	for _, value := range m {
		fmt.Printf("姓名=%s\n", value.name) //省略key
	}

	for key, _ := range m {
		fmt.Printf("学号=%s\n", key) //省略value
	}
}

// //定义函数，其中有参数是函数
// func compute(computeFunc func(x float64, y float64) float64) float64{
// 	return computeFunc(3,4) //通过传入的函数进行计算
// }

//省略了返回值的名字
func getNameAndAge() (string, int) {
	return "lixuing", 50
}

func countdata(args ...int) {
	for _, i := range args {
		fmt.Printf("%v\n", i)
	}
}

//定义函数，其中有参数是函数类型
func compute(computeFunc func(x float64, y float64) float64) float64 {
	return computeFunc(3, 4) //通过传入的函数进行计算
}

//先定义类型
type compFunc func(float64, float64) float64

func compute2(computeFunc compFunc) float64 {
	return computeFunc(3, 4) //通过传入的函数进行计算
}

func gougu(x float64, y float64) float64 {
	return math.Sqrt(x*x + y*y)
}

func test() func(int, int) int { //函数返回函数
	return func(a int, b int) int {
		return a + b
	}
}

func bibao() func(int) int {
	var x int = 0
	return func(y int) int {
		x = x + y
		return x
	}
}

type Person struct {
	name string
	age  int
}

func (person Person) eat() {
	fmt.Printf("%v eat....", person.name)
}

func (Person) sleepHours() int {
	return 20
}

type Fish interface { // 接口1
	swim()        // 游泳
	sport()       //运动
	name() string // 名字，有返回值
}

type Animal interface { // 接口2
	run() // 奔跑
}

// 复合接口，把多个接口合并成大接口
type Tortoise interface {
	Fish
	Animal
}

//从语法上，我们可以只实现部分接口，从结构上，我们应该是先全部接口
type Shark struct { // 鲨鱼，应该实现Fish的全部接口
	name string
}

func (shark Shark) swim() { // 实现接口方法
	fmt.Printf("%v 横着游泳...\n", shark.name)
}

// 海龟，应该实现Tortoise的全部接口
type SeaToise struct {
	age int
}

func (seaToise SeaToise) run() {
	fmt.Printf("海龟在沙滩上跑，最大年龄是 %d\n", seaToise.age)
}
func (SeaToise) swim() {
	fmt.Printf("海龟在海里用四条腿游泳...\n")
}

type MyFloat float64

func (myFloat MyFloat) show() {
	fmt.Println("just show .... ")
}

type Dog struct {
	name  string
	color string
}

func (dog Dog) changeName() { //实际上，无法改变...
	dog.name = "DogName:" + dog.name
}

func (dog *Dog) changeNameReal() { //因为是指针，所以真的改掉了
	dog.name = "DogName:" + dog.name
}

func num(a *int) {
	fmt.Printf("num = %d\n", *a)
	*a = 300
}

type IPet interface {
	sayHello(string) string
}
type Cat struct {
	color string
}

func (cat Cat) sayHello(name string) string {
	return "Hello " + name + ", I'm a cat"
}

type Pig struct {
	weight int
}

func (pig Pig) sayHello(name string) string {
	return "Hello " + name + ", I'm a pig"
}
func printHello(pet IPet) {
	fmt.Println(pet.sayHello("Tom"))
}

// ------ 继承
type Student struct { // 充当父类
	name string
}

func (stu Student) showName() {
	fmt.Println("my name is " + stu.name)
}

type Senior struct {
	stu Student
	sno string
}

type Car struct {
	color string
	band  string
}

func NewCar(color string, band string) *Car {
	return &Car{color, band}
}

func Sqrt(f float64) (num float64, err error) {
	if f < 0 {
		return 0, errors.New("负数不能开平方")
	}
	// 实现
	return math.Sqrt(f), nil
}

func main() {
	value, error := Sqrt(-100)
	if error != nil {
		fmt.Println(error)
	} else {
		fmt.Println(value)
	}

	// r := gin.Default()
	// r.GET("/ping", func(c *gin.Context) {
	//     c.JSON(200, gin.H{
	//         "message": "pong",
	//     })
	// })
	// r.Run() // listen and serve on 0.0.0.0:8080 (for windows "localhost:8080")

	// car := NewCar("blue", "奔驰")
	// fmt.Println(car.color)

	// senior := Senior{
	// 	Student{"王五"},"s0001",
	// }
	// senior.stu.showName() //顺序初始化的，可以直接省略掉属性

	// pet :=  Cat{"orange"}
	// printHello(pet)

	// pig := Pig{1000}
	// printHello(pig)

	// dog := Dog{"哈士奇","棕色"}
	// dog.changeName()
	// fmt.Printf("name is %s", dog.name)

	// dog.changeNameReal()
	// fmt.Printf("name is %s", dog.name)

	// myFloat := MyFloat(10.33)
	// myFloat.show()

	// shark :=Shark{
	// 	name : "食人鲨",
	// }
	// shark.swim()

	// seaToise := SeaToise{100}
	// seaToise.run()
	// seaToise.swim()

	// person := Person{"zhangsna",20}
	// person.eat()

	// fmt.Println(person.sleepHours())

	// call := bibao()

	// fmt.Println(call(10)) // x的值为0，输出10，
	// fmt.Println(call(20)) // x的值为10，输出30
	// fmt.Println(call(30)) // x的值为30，输出60

	// //用变量接受函数
	// myFun := gougu

	// fmt.Println(myFun(5,12))
	// fmt.Println(compute(myFun)) //传入函数计算
	// fmt.Println(compute(math.Pow)) //传入函数计算

	// m := test() //调用函数，得到返回的函数
	// fmt.Println(m(10,20))

	// for i := 1; i < 10; i++{
	// 	for j := 1; j < 10; j++{
	// 		fmt.Println(i,j);
	// 		if i>=5 {
	// 			goto MY_LABEL
	// 		}
	// 	}
	// }
	// MY_LABEL:
	// fmt.Println("end...")
	// const (
	// 	a = iota // 0
	// 	_ // 1 ， 跳过
	// 	m = 100 // 2   插队
	// 	b = iota //3
	// )
	// const c =  iota // 0
	// fmt.Println(c)
	// fmt.Println(a,b)

	// _,age := getNameAndAge()  //不使用name，可以用_来忽略，_匿名变量
	// fmt.Println(age)

	// var class string = "java"  // 声明的时候初始化
	// var name="zhangsan" // 声明时候初始化，省略类型
	// var hobby string
	// hobby = "篮球"  // 先声明，后面再初始化
	// var i, j int = 1,2  // 多个变量初始化，声明类型
	// var c, python, java = true, false, "no!"  //多个变量初始化，省略类型
	// fmt.Println(class,name,hobby,i,j,c,python,java)

	// //用变量接受函数
	// myFun := func(x float64,y float64) float64{
	// 	return math.Sqrt(x*x+y*y)
	// }

	// fmt.Println(myFun(5,12))
	// fmt.Println(compute(myFun)) //传入函数计算
	// fmt.Println(compute(math.Pow)) //传入函数计算

	// listmap()

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

func pointer() {
	i, j := 100, 30
	p := &i         //指向i
	fmt.Println(*p) //通过指针读取i的值

	*p = 40 //通过指针修改i的值
	fmt.Println(i)

	p = &j         //指向j
	*p = *p / 2    // 通过指针使用j
	fmt.Println(j) //查看j的值
}

func stack(num int) {
	for i := 0; i < num; i++ {
		defer fmt.Printf("i=%d", i)
	}
}

func converAge(age int) string {
	switch {
	case age > 60:
		return "老人"
	case age > 20:
		return "年轻人"
	default:
		return "孩子"
	}
}

func add(x int, y int) int {
	return x + y
}

func swap(x, y int) (int, int) {
	return y, x
}

func forloop(count int) {
	for i := 0; i < count; i++ {
		fmt.Printf("i=%d\n", i)
	}

	j := 1
	for ; j < 5; j++ {
		fmt.Printf("j=%d\n", j)
	}

	x := 1
	for x < 10 {
		fmt.Printf("x=%d\n", x)
		x++
	}
}

func sexJudge(sex int) string {
	if v := 100; sex == 1 {
		fmt.Println(v)
		return "男"
	} else {
		fmt.Println(v + 10)
		return "女"
	}
}

func convertNumToChinese(num int) string {
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
