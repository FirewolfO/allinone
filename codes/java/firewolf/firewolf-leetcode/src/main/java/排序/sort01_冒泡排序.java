package 排序;

import utils.ArrayUtils;

/***
 * # 1、算法介绍
 *
 * 冒泡排序是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来。走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。
 *
 * **<font color=red>核心思想：通过交换相邻元素，把最大/小的元素往一端移动，从而让一端排好序</font>**
 *
 * # 2、算法描述
 *
 * - 比较相邻的元素。如果第一个比第二个大，就交换它们两个；
 * - 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数；
 * - 针对所有的元素重复以上的步骤，除了最后一个；
 * - 重复步骤1~3，直到排序完成。
 *
 * # 3、动图演示
 */
public class sort01_冒泡排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                1, 2, 3, 73, 45, 23, 10, -12
        };
        new sort01_冒泡排序().sort(nums);
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
