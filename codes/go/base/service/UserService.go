package service

import (
	"base/model"
	"fmt"
)

func init() {
	fmt.Println("service.init")
}

func AddUser(user model.User) {
	fmt.Printf("user info = %v \n", user)
}
