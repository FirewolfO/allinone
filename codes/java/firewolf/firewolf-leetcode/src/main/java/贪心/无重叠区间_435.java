package 贪心;

import utils.ArrayUtils;

import java.util.Arrays;

class 无重叠区间_435 {

    public static void main(String[] args) {
        String s = "[[1,100],[11,22],[1,11],[2,12]]";
        int[][] datas = ArrayUtils.to2Array(s, ",");
        System.out.println(new 无重叠区间_435().eraseOverlapIntervals(datas));
    }

    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]); //按照左边界排序
        int lastRight = intervals[0][1];
        int count = 0;
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < lastRight) { // 如果重叠了，移除靠右覆盖更大的
                count++; // 移除数量+1
                lastRight = Math.min(lastRight, intervals[i][1]); // 更新最右值为最靠左的
            } else {
                lastRight = intervals[i][1]; // 没有重叠，更新右边界
            }
        }
        return count;
    }
}