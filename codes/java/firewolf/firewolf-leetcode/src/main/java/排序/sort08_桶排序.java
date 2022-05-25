package 排序;

import utils.ArrayUtils;

/***
 桶排序是计数排序的扩展版本，计数排序可以看成每个桶只存储相同元素，而桶排序每个桶存储一定范围的元素，通过映射函数
 ，将待排序数组中的元素映射到各个对应的桶中，对每个桶中的元素进行排序，最后将非空桶中的元素逐个放入原序列中。
 */
public class sort08_桶排序 {

    public static void main(String[] args) {
        int[] nums = new int[]{
                1, 2, 3, 73, 45, 23, 10, -12
        };
        int bucket_size = 5;
        new sort08_桶排序().sort(nums, bucket_size);
        System.out.println(ArrayUtils.array2Str(nums));
    }

    public void sort(int[] array, int bucketSize) {
        
    }
}
