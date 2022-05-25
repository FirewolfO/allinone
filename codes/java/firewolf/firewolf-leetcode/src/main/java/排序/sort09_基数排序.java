package 排序;

import utils.ArrayUtils;

import java.util.Arrays;

/***
 * # 1、算法介绍
 *
 * 基数排序是一种非比较型整数排序算法，其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较。由于整数也可以表达字符串（比如名字或日期）和特定格式的浮点数，所以基数排序也不是只能使用于整数。
 *
 *
 *
 * # 2、算法描述
 *
 * 1. 先找十个桶：0~9
 * 2. 第一轮按照元素的个位数排序
 *    - 按照元素的个位数，然后按照数组元素的顺序把元素依次存放进去
 *    - 之后，按照从左向右，从上到下的顺序依次取出元素，组成新的数组。
 * 3. 在新的数组中，进行第二轮，按照十位数排序，依次存放于桶中
 *    - 按照元素的百位数，然后按照数组元素的顺序把元素依次存放进去
 *    - 之后，按照从左向右，从上到下的顺序依次取出元素，组成新的数组。
 * 4. 一直到所有的位都处理完，算法结束
 */
public class sort09_基数排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                3, 2, 3, 1, 2, 4, 5, 5, 6, 7, 7, 8, 2, 3, 1, 1, 1, 10, 11, 5, 6, 2, 4, 7, 8, 5, 6
        };
        new sort09_基数排序().sort2(nums);
        System.out.println(ArrayUtils.array2Str(nums));
    }

    /*************全部都是正数的基数排序***********/
    public void sort(int[] array) {
        int mod = 10;
        int dev = 1;
        while (true) {
            int count = 0;
            int[][] counters = new int[10][0];
            for (int i = 0; i < array.length; i++) {
                int counterIndex = (array[i] % mod) / dev;
                if (array[i] > mod) {
                    count++;
                }
                counters[counterIndex] = append(counters[counterIndex], array[i]);
            }
            int index = 0;
            for (int[] counter : counters) {
                for (int value : counter) {
                    array[index++] = value;
                }
            }
            if (count == 0) {
                break;
            }
            mod *= 10;
            dev *= 10;
        }
    }

    public void sort2(int[] array) {
        int mod = 10;
        int dev = 1;
        while (true) {
            int count = 0;
            int[][] counters = new int[20][0]; // 分配桶，桶是从
            for (int i = 0; i < array.length; i++) {
                int counterIndex = (array[i] % mod) / dev + 10; //找到数据该放到的桶
                if (array[i] > mod) { //证明还需要下一位的排序
                    count++;
                }
                counters[counterIndex] = append(counters[counterIndex], array[i]);
            }
            int index = 0;
            for (int[] counter : counters) { // 把数据取回来
                for (int value : counter) {
                    array[index++] = value;
                }
            }
            if (count == 0) {
                break;
            }
            mod *= 10;
            dev *= 10;
        }
    }

    private int[] append(int[] array, int value) {
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }
}
