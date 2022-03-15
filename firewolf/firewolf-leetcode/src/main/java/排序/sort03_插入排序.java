package 排序;

import utils.ArrayUtils;

/***
 * # 1、算法介绍
 *
 * 插入排序（Insertion-Sort）的算法描述是一种简单直观的排序算法。它的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。
 *
 * **<font color=red>核心思想：每次选择一个元素，插入已经拍好序的数组中合适的位置，让数组仍然有序</font>**
 *
 * # 2、算法描述
 *
 * 一般来说，插入排序都采用in-place在数组上实现。具体算法描述如下：
 *
 * - 从第一个元素开始，该元素可以认为已经被排序；
 * - 取出下一个元素，在已经排序的元素序列中从后向前扫描；
 * - 如果该元素（已排序）大于新元素，将该元素移到下一位置；
 * - 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置；
 * - 将新元素插入到该位置后；
 * - 重复步骤2~5。
 */
public class sort03_插入排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                3, 2, 1, 5, 6, 4
        };
        new sort03_插入排序().sort(nums);
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
