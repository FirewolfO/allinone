package binary

import (
	"fmt"
	"testing"
)

//https://leetcode.cn/problems/search-insert-position/
func TestSearchInsert(t *testing.T) {
	nums := []int{1, 2}
	index := searchInsert(nums, -10)
	fmt.Println(index)
}

func searchInsert(nums []int, target int) int {
	low := 0
	high := len(nums) - 1
	for {
		if low > high { // 超出范围，跳出
			return low
		}
		mid := (low + high) / 2
		if nums[mid] < target { // 中间值比target小，那么位置一定是mid+1之后，
			low = mid + 1
		} else { //不小于target，那么就是这个位置，或者是之前的位置
			high = mid - 1
		}
	}
}
