package 每日一题;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Day20220314_两个列表的最小索引总和_599 {


    public static void main(String[] args) {
        String[] strings1 = new String[]{"Shogun", "Tapioca Express", "Burger King", "KFC"};
        String[] strings2 = new String[]{"KFC", "Shogun", "Burger King"};
        String[] restaurant = new Day20220314_两个列表的最小索引总和_599().findRestaurant(strings1, strings2);
        System.out.println(Arrays.toString(restaurant));
    }

    public String[] findRestaurant(String[] list1, String[] list2) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < list1.length; i++) {
            map.put(list1[i], i);
        }
        Map<String, Integer> resMap = new HashMap<>();
        for (int i = 0; i < list2.length; i++) {
            if (map.containsKey(list2[i])) {
                int indexSum = map.get(list2[i]) + i;
                Integer exsitMinIndexSum = resMap.isEmpty() ? -1 : resMap.entrySet().iterator().next().getValue();
                if (indexSum < exsitMinIndexSum) {
                    resMap.clear();
                }
                if (indexSum <= exsitMinIndexSum || exsitMinIndexSum == -1) {
                    resMap.put(list2[i], indexSum);
                }
            }
        }
        String[] resArray = new String[resMap.size()];
        int i = 0;
        for (String s : resMap.keySet()) {
            resArray[i++] = s;
        }
        return resArray;
    }
}