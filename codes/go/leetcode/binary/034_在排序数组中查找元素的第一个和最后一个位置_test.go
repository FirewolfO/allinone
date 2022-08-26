package binary

import (
	"errors"
	"fmt"
	"os"
	"strings"
	"testing"
)

//https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/

func TestSearchRange(t *testing.T) {
	nums := []int{1, 3, 4, 4, 4}
	left := searchLeft(nums, 4)
	fmt.Println(left)

}
func searchRange(nums []int, target int) []int {
	return []int{-1, -1}
}

func searchLeft(nums []int, target int) int {
	low := 0
	high := len(nums) - 1
	rest := -1
	for {
		if low > high {
			return rest
		}
		mid := (low + high) / 2
		if nums[mid] == target {
			rest = mid
			low = mid - 1
		} else if nums[mid] < target {
			low = mid + 1
		} else {
			high = mid - 1
		}
	}
}

func Tx(function func(...interface{}) error, args ...interface{}) {
	var err error // 事务开启封装
	defer func() {
		if err == nil {
			fmt.Println("没有错误")
		} else {
			fmt.Println("有错误")
		}
	}()
	err = function(args)
	fmt.Println("xxx")
}

func TestTx(t *testing.T) {
	age := 30
	Tx(func(...interface{}) error {
		if age == 10 {
			return errors.New("不能是10岁")
		}
		return nil
	}, age)
}

func TestName(t *testing.T) {
	var t1 interface{} = 2

	v, ok := t1.(int)
	if ok {
		fmt.Println("int:", v)
	} else {
		fmt.Println("v:", v)
	}
}

func TestFiles(t *testing.T) {
	content, _ := os.ReadFile("/Users/bytedance/Desktop/test.sql")
	sqls := strings.Split(string(content), "),(")
	res := "INSERT INTO `action` VALUES ("
	for _, sql := range sqls {
		if strings.Contains(sql, "'iam'") {
			res = res + sql + "),("
		}
	}
	res = strings.TrimSuffix(res, "),(")

	fmt.Println(res)
}
