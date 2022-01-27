package 贪心;

class 摆动序列_376 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 7, 4, 9, 2, 5};
        System.out.println(new 摆动序列_376().wiggleMaxLength(nums));
    }

    public int wiggleMaxLength(int[] nums) {
        int preDiff = 0;
        int curDiff = 0;
        int res = 1;
        for (int i = 0; i < nums.length - 1; i++) {
            curDiff = nums[i + 1] - nums[i];
            if (curDiff > 0 && preDiff <= 0 || curDiff < 0 && preDiff >= 0) {
                res++;
                preDiff = curDiff;
            }
        }
        return res;
    }
}