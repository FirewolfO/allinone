package 排序;

import utils.ArrayUtils;

public class 冒泡排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                1, 2, 3, 73, 45, 23, 10, -12
        };
        new 冒泡排序().sort(nums);
        System.out.println(ArrayUtils.array2Str(nums));
    }

    public void sort(int[] array) {
        // 外层控制遍历的次数，由于后面的n-1个元素排好序之后，最后一个自然是最小的，所以，不用再排了，顾只需要遍历 array.length-1次
        for (int i = 0; i < array.length - 1; i++) {
            // 相邻元素对比，把最大的元素调到最后面
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
        }
    }
}
