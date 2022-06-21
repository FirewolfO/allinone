package main

import (
	"base/model"
	"base/service"
)

func main() {
	user := model.User{
		"zhangsan", 100, true,
	}

	service.AddUser(user)
}
