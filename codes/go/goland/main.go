package main

import (
	"github.com/gin-gonic/gin"
	"goland/dao"
	"goland/domain"
	"goland/service"
)

func main() {
	u := domain.User{"zhangsan",100,true}
	dao.AddUser(u)

	service.AddUser()
	r := gin.Default()
	r.GET("/", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "Hello,World",
		})
	})
	r.Run(":9000") // listen and serve on 0.0.0.0:8080 (for windows "localhost:8080")
}
