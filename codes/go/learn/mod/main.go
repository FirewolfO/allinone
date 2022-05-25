package main

import (
	"user"
)

func main() {
	name := user.Name()
	fmt.Printf("name:%s", name)
}