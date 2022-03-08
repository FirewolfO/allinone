package 排序;

import utils.ArrayUtils;

public class 选择排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                3, 2, 1, 5, 6, 4
        };
        new 选择排序().sort(nums);
        System.out.println(ArrayUtils.array2Str(nums));
    }

    public void sort(int[] array) {
        // 第i次排序找出第i小/大的元素，
        for (int i = 0; i < array.length - 1; i++) {
            int toFindIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[toFindIndex]) {
                    toFindIndex = j; // 更新最大或者小值索引
                }
            }
            array[i] = (array[i] + array[toFindIndex]) - (array[toFindIndex] = array[i]);
        }
    }
}
