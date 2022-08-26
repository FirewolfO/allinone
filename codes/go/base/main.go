package main

import "fmt"

type MyInt = int
type AAA struct {
	name string
	age  int
}

func main() {
	mapa := map[int]string{}
	mapa[1] = "aaa"
	name, ok := mapa[21]
	fmt.Println("aaa", name, ok)
}
