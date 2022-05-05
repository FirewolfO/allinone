import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        int[] nums = new int[]{1,1,2,1,2,2,1};
        new Test().wiggleSort(nums);
        System.out.println("xxx");
    }
    public void wiggleSort(int[] nums) {
        Arrays.sort(nums);
        int[] left = Arrays.copyOf(nums,nums.length / 2);
        int[] right = Arrays.copyOfRange(nums,nums.length - left.length ,nums.length);
        int l = 0;
        int r =0;
        for(int i = 0; i < nums.length;i++){
            if(i % 2== 0){
                nums[i] = left[l++];
            }else{
                nums[i] = right[r++];
            }
        }
    }
}
