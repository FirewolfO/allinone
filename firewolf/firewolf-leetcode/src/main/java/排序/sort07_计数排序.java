package 排序;

import utils.ArrayUtils;

/***
 * # 1、算法介绍
 *
 * 计数排序不是基于比较的排序算法，其核心在于将输入的数据值转化为键存储在额外开辟的数组空间中。 作为一种线性时间复杂度的排序，计数排序要求输入的数据必须是有确定范围的整数。
 *
 * # 2、算法描述
 *
 * - 找出待排序的数组中最大和最小的元素；
 * - 统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
 * - 对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）；
 * - 反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1。
 */
public class sort07_计数排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                1, 2, 3, 73, 45, 23, 10, -12
        };
        new sort07_计数排序().sort(nums);
        System.out.println(ArrayUtils.array2Str(nums));
    }

    public void sort(int[] array) {
        int min, max;
        min = max = array[0];
        for (int i = 1; i < array.length; i++) { // 找出数据覆盖范围
            if (array[i] > max) max = array[i];
            if (array[i] < min) min = array[i];
        }
        int arrLen = max - min + 1;
        byte[] countArray = new byte[arrLen]; // 这里如果每个元素的重复次数不超过127，则可以使byte，否则，需要改成int或更大的
        for (int i = 0; i < array.length; i++) {
            countArray[array[i] - min]++;  // 映射数据到数组上，减去偏移量
        }
        int index = 0;
        for (int i = 0; i < countArray.length; i++) { // 把数据取出来，当countArray[i]不等于0，表示i出现过
            while (countArray[i] > 0) {
                array[index++] = i + min; // 重新把偏移加回来
                countArray[i]--;
            }
        }
    }
}
