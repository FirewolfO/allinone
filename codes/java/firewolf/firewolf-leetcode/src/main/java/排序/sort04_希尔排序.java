package 排序;

import utils.ArrayUtils;

/***
 * # 1、算法介绍
 *
 * 1959年Shell发明，第一个突破O(n2)的排序算法，是简单插入排序的改进版。它与插入排序的不同之处在于，它会优先比较距离较远的元素。希尔排序又叫**缩小增量排序**。
 *
 * **<font color=red>核心思想：仍然是插入排序</font>**
 *
 * # 2、算法描述
 *
 * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，具体算法描述：
 *
 * - 选择一个增量序列t1，t2，…，tk，其中ti>tj，tk=1；
 * - 按增量序列个数k，对序列进行k 趟排序；
 * - 每趟排序，根据对应的增量ti，将待排序列分割成若干长度为m 的子序列，分别对各子表进行直接插入排序。仅增量因子为1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。
 */
public class sort04_希尔排序 {
    public static void main(String[] args) {
        int[] nums = new int[]{
                3, 2, 1, 5, 6, 4
        };
        new sort04_希尔排序().sort(nums);
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
