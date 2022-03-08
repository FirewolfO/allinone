package 排序;

import utils.ArrayUtils;

public class 希尔排序 {
    public static void main(String[] args) {
        int[] nums = new int[]{
                3, 2, 1, 5, 6, 4
        };
        new 希尔排序().sort(nums);
        System.out.println(ArrayUtils.array2Str(nums));
    }

    public void sort(int[] array) {
        int h = 1;// 计算间隔h
        while (h * 3 + 1 < array.length) {
            h = h * 3 + 1;
        }
        int toInsertIndex;
        int tmp;
        while (h > 0) {
            for (int i = h; i < array.length; i++) {
                toInsertIndex = i;
                tmp = array[i];
                while (toInsertIndex > h - 1 && array[toInsertIndex - h] > tmp) { // 下一个元素比要插入元素大， 移动
                    array[toInsertIndex] = array[toInsertIndex - h];
                    toInsertIndex -= h;
                }
                array[toInsertIndex] = tmp;
            }
            h = (h - 1) / 3;
        }
    }
}
