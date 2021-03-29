package 其他;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/26 3:50 下午
 */
public class Test {


    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) {
            return new int[]{-1, -1};
        }
        return binarySearch(nums, 0, nums.length - 1, target);
    }

    public int[] binarySearch(int[] nums, int left, int right, int target) {
        if (right - left < 2) {
            int lIndex = nums[left] == target ? left : (nums[right] == target ? right : -1);
            int rIndex = nums[right] == target ? right : (nums[left] == target ? left : -1);
            return new int[]{lIndex, rIndex};
        }
        int middle = (left + right) / 2;
        if (nums[middle] < target) {
            return binarySearch(nums, middle + 1, right, target);
        } else if (nums[middle] > target) {
            return binarySearch(nums, left, middle - 1, target);
        } else {
            int[] r = binarySearch(nums, middle + 1, right, target);
            int[] l = binarySearch(nums, left, middle - 1, target);
            int l1 = l[0] != -1 ? l[0] : (l[1] != -1 ? l[1] : middle);
            int r1 = r[1] != -1 ? r[1] : (r[0] != -1 ? r[0] : middle);
            return new int[]{l1, r1};
        }
    }

    public static void main(String[] args) {
        int[] nums = {5, 7, 7, 8, 8, 10};
        int targe = 6;
        int[] res = new Test().searchRange(nums, targe);
        System.out.println(res);
    }
}
