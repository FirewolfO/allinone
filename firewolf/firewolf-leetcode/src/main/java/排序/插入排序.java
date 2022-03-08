package 排序;

import utils.ArrayUtils;

public class 插入排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                3, 2, 1, 5, 6, 4
        };
        new 插入排序().sort(nums);
        System.out.println(ArrayUtils.array2Str(nums));
    }

    public void sort(int[] array) {
        int toInsertIndex;
        int tmp;
        for (int i = 1; i < array.length; i++) { // 第一个元素已经有序，从第二个元素开始，往前面的序列中插入
            toInsertIndex = i;
            tmp = array[i];
            while (toInsertIndex > 0 && array[toInsertIndex - 1] > tmp) { // 前一个元素比 要插入的元素大，继续往后移动
                array[toInsertIndex] = array[toInsertIndex - 1];
                toInsertIndex--;
            }
            array[toInsertIndex] = tmp; // 不能移动了，插入元素
        }
    }
}
