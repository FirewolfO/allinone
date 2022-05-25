package 贪心;

import java.util.Arrays;

class 用最少数量的箭引爆气球_452 {

    public static void main(String[] args) {
        int[][] points = new int[][]{
                {10, 16},
                {2, 8},
                {1, 6},
                {7, 12}
        };

        int minArrowShots = new 用最少数量的箭引爆气球_452().findMinArrowShots(points);
        System.out.println(minArrowShots);
    }

    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));
        int count = 1;
        int lastRight = points[0][1];
        for (int i = 1; i < points.length; i++) {
            if (points[i][0] > lastRight) {
                count++;
                lastRight = points[i][1]; // 更换下一组之后，初始值设置为第一个区间的右边界
            } else {
                lastRight = Math.min(points[i][1], lastRight); // 在同一组里面，右边界是边界里面最小的
            }
        }
        return count;
    }
}   