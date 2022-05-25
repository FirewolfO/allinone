package 排序;

import utils.ArrayUtils;

import java.util.Arrays;

/***
 * # 1、算法思想
 *
 * 归并排序是建立在归并操作上的一种有效的排序算法。该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为2-路归并。
 *
 * **<font color=red>核心思想：通过递归分治的方式，先让少数元素有序，然后让所有元素有序</font>**
 *
 * # 2、算法描述
 *
 * - 把长度为n的输入序列分成两个长度为n/2的子序列；
 * - 对这两个子序列分别采用归并排序；
 * - 将两个排序好的子序列合并成一个最终的排序序列。
 */
public class sort05_归并排序 {
    public static void main(String[] args) {
        int[] nums = new int[]{
                3, 2, 1, 5, 6, 4
        };
        new sort05_归并排序().sort2(nums);
        System.out.println(ArrayUtils.array2Str(nums));
    }


    /************************提前开辟tmp，节省空间*****************/
    public void sort2(int[] array) {
        int[] tmp = new int[array.length]; //提前开辟空间，避免空间浪费
        sortRecurise(array, 0, array.length - 1, tmp);
    }

    private void sortRecurise(int[] array, int start, int end, int[] tmp) {
        if (end - start == 0) {
            return;
        }
        int middle = (end + start) / 2;
        sortRecurise(array, start, middle, tmp); // 排左边的
        sortRecurise(array, middle + 1, end, tmp); // 排右边的
        merge2(array, start, end, middle, tmp); // 合并
    }

    private void merge2(int[] array, int start, int end, int middle, int[] tmp) {
        int i = start;
        int j = middle + 1;
        int index = 0;
        while (i <= middle && j <= end) {
            if (array[i] < array[j]) {
                tmp[index++] = array[i++];
            } else {
                tmp[index++] = array[j++];
            }
        }
        while (i <= middle) {
            tmp[index++] = array[i++];
        }
        while (j <= end) {
            tmp[index++] = array[j++];
        }
        int tIndex = 0;
        for (int aIndex = start; aIndex <= end; aIndex++) { //先利用tmp进行排序，之后，拷贝回来
            array[aIndex] = tmp[tIndex++];
        }
    }


    /******************** 中间过程开辟了很多内存空间 ************************/
    public void sort(int[] array) {
        int[] sorted = sortWithReturn(array);
        for (int i = 0; i < array.length; i++) {
            array[i] = sorted[i];
        }
    }

    private int[] sortWithReturn(int[] array) {
        if (array.length <= 1) return array;
        int middle = array.length / 2;
        return merge(sortWithReturn(Arrays.copyOfRange(array, 0, middle)), sortWithReturn(Arrays.copyOfRange(array, middle, array.length)));
    }

    // 合并两个有序数组
    private int[] merge(int[] array1, int[] array2) {
        int newArray[] = new int[array1.length + array2.length];
        int index1 = 0;
        int index2 = 0;
        int index = 0;
        while (index1 < array1.length && index2 < array2.length) {
            if (array1[index1] < array2[index2]) {
                newArray[index++] = array1[index1++];
            } else {
                newArray[index++] = array2[index2++];
            }
        }
        while (index1 < array1.length) {
            newArray[index++] = array1[index1++];
        }
        while (index2 < array2.length) {
            newArray[index++] = array2[index2++];
        }
        return newArray;
    }
}
