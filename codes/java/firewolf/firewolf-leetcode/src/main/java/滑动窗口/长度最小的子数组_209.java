package 滑动窗口;

public class 长度最小的子数组_209 {


    public static void main(String[] args) {
        int[] nums = new int[]{2,3,1,2,4,3};
        int i = new 长度最小的子数组_209().minSubArrayLen(7, nums);
        System.out.println(i);
    }

    public int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int right = 0;
        int sum = 0;
        int length = nums.length+1;
        while(right < nums.length){
            sum += nums[right];
            if(sum >= target){
                while(left <= right && sum >= target){
                    if(right-left+1 <length){
                        length = right-left+1;
                    }
                    sum -= nums[left];
                    left++;
                }
            }
            right++;
        }
        return length == nums.length+1 ?0 :length;
    }
}
