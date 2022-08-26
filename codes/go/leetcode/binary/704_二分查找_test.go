package binary

import (
	"fmt"
	"testing"
)

// https://leetcode.cn/problems/binary-search/
func TestBinarySearch(t *testing.T) {
	arr := []int{1, 5, 10, 11, 77, 89}
	index := BinarySearch(arr, 77)
	fmt.Println(index)

}

func BinarySearch(array []int, target int) int {
	low := 0
	high := len(array) - 1
	for {
		if low > high {
			break
		}
		mid := (low + high) / 2
		if array[mid] == target {
			return mid
		} else if array[mid] < target {
			low = mid + 1
		} else {
			high = mid - 1
		}
	}
	return -1
}
