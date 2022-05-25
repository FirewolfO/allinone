package 指针.快慢指针;

/**
 * 26. 删除有序数组中的重复项
 * 给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
 * <p>
 * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
 */
public class 删除有序数组中的重复项_026 {

    public static void main(String[] args) {
        int i = new 删除有序数组中的重复项_026().removeDuplicates(new int[]{0,0,1,1,1,2,2,3,3,4});
        System.out.println(i);
    }

    public int removeDuplicates(int[] nums) {
        if(nums.length == 1){
            return 1;
        }
        int slow = 0;
        int fast = 1;
        while(fast < nums.length){
            if(nums[slow] == nums[fast]){
                fast++;
            }else{
                nums[++slow] = nums[fast];
            }
        }
        return slow+1;
    }
}
