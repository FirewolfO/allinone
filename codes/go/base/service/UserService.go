package service

import (
	"base/model"
	"fmt"
)

func AddUser(user model.User) {
	fmt.Printf("user info = %v \n", user)
}
