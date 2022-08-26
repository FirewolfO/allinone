package main

import (
	"fmt"
	"testing"
)

func TestBase(t *testing.T) {

	var a int = 10
	fmt.Println(a)

	changeValue(&a) // 指针传参，函数里面可以修改值

	fmt.Println(a) // 20

	changeValue2(a) // 值传参，函数里面无法修改值
	fmt.Println(a)  // 20
}

func changeValue(a *int) {
	*a = 20
}

func changeValue2(a int) {
	a = 30
}
