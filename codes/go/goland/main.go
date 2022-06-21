package main

import (
	"fmt"

	"github.com/gin-gonic/gin"
)

type Mover interface {
	move()
}

type dog struct{}

func (d dog) move() {
	fmt.Println("狗会动")
}

// 多种响应方式
func main() {
	r := gin.Default()
	r.Use(Middle1("中间件1"))
	r.Use(Middle1("中间件2"))
	r.GET("/test", func(context *gin.Context) {
		context.JSON(200, gin.H{"name": "liuxing"})
	})
	r.Run(":8001")
}

func Middle1(name string) gin.HandlerFunc {
	return func(context *gin.Context) {
		fmt.Printf("%s start  \n", name)
		//context.Next()
		fmt.Printf("%s end  \n", name)
	}
}

func Middle2(name string) gin.HandlerFunc {
	return func(context *gin.Context) {
		fmt.Printf("%s start \n", name)
		context.Next()
		fmt.Printf("%s end  \n", name)
	}
}
