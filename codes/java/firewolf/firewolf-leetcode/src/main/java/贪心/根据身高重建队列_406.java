package 贪心;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class 根据身高重建队列_406 {

    public static void main(String[] args) {
        int[][] people = new int[][]{
                {7, 0},
                {4, 4},
                {7, 1},
                {5, 0},
                {6, 1},
                {5, 2}
        };
        int[][] ints = new 根据身高重建队列_406().reconstructQueue(people);
        System.out.println("xxx");
    }

    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (a, b) -> {
            if (a[0] == b[0]) return a[1] - b[1];  // 位置升序排
            return b[0] - a[0]; // 身高降序排
        });
        List<int[]> resList = new ArrayList<>();

        for (int i = 0; i < people.length; i++) {
            resList.add(people[i][1], people[i]);
        }
        return resList.toArray(new int[people.length][2]);
    }
}