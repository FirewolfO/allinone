package dao

import (
	"fmt"
	"goland/domain"
)
func AddUser(user domain.User)  {
	fmt.Printf("add user, user = %v \n", user)
}