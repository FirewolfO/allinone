package 贪心;

class 跳跃游戏II_45 {
    public static void main(String[] args) {
        int[] steps = new int[]{2, 3, 1, 1, 4};
        int jump = new 跳跃游戏II_45().jump(steps);
        System.out.println(jump);
    }

    public int jump(int[] nums) {
        if (nums.length <= 1) return 0;
        int count = 0; // 跳跃参数
        int maxDistance = 0;
        int curDistance = 0; // 当前可以跳跃到的最大区域
        for (int i = 0; i < nums.length; i++) {
            maxDistance = Math.max(maxDistance, i + nums[i]);
            if (maxDistance >= nums.length - 1) {
                count++;
                return count;
            }
            // 移动到了最后一个位置，需要多加一步
            if (curDistance == i) {
                curDistance = maxDistance;
                count++;
            }
        }
        return count;
    }
}