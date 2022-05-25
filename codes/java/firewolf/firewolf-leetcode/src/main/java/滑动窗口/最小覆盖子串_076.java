package 滑动窗口;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class 最小覆盖子串_076 {

    public static void main(String[] args) {
        String s = "ADOBECODEBANC";
        String t = "ABC";
        String s1 = new 最小覆盖子串_076().minWindow2(s, t);
        System.out.println(s1);
    }

    public String minWindow(String s, String t) {
        Map<Character, Integer> tMap = new HashMap<>();
        Map<Character, Integer> resMap = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            putVal(tMap, t.charAt(i));
        }
        int left = 0;
        int right = 0;
        int resStart = 0;
        int length = 1000000;
        while (right < s.length()) {
            if (tMap.containsKey(s.charAt(right))) {
                putVal(resMap, s.charAt(right));
                while (check(tMap, resMap)) {
                    if (right - left + 1 < length) {
                        resStart = left;
                        length = right - left + 1;
                    }
                    delVal(resMap, s.charAt(left++));
                }
            }
            right++;
        }
        if (length == 1000000) {
            return "";
        }
        return s.substring(resStart, resStart + length);
    }


    private boolean check(Map<Character, Integer> tMap, Map<Character, Integer> resMap) {
        if (tMap.size() != resMap.size()) {
            return false;
        }
        Set<Character> keys = tMap.keySet();
        for (Character key : keys) {
            if (tMap.get(key) > resMap.get(key)) {
                return false;
            }
        }
        return true;
    }

    private void delVal(Map<Character, Integer> map, Character c) {
        if (map.containsKey(c)) {
            int count = map.get(c);
            if (count <= 1) {
                map.remove(c);
            } else {
                map.put(c, count - 1);
            }
        }
    }

    private void putVal(Map<Character, Integer> map, Character c) {
        if (map.containsKey(c)) {
            map.put(c, map.get(c) + 1);
        } else {
            map.put(c, 1);
        }
    }

    //------------------- 使用数组替代map优化操作-------
    public String minWindow2(String s, String t) {
        int[] tCount = new int[128];
        int[] sCount = new int[128];
        for (int i = 0; i < t.length(); i++) {
            tCount[t.charAt(i)]++;
        }
        int left = 0;
        int right = 0;
        int resStart = 0;
        int length = 1000000;
        while (right < s.length()) {
            char c = s.charAt(right);
            if (tCount[c] != 0) {
                sCount[c]++;
                while (checkArray(tCount, sCount)) {
                    if (right - left + 1 < length) {
                        resStart = left;
                        length = right - left + 1;
                    }
                    char c1 = s.charAt(left);
                    if (sCount[c1] > 0) { // 防止数量为负
                        sCount[c1]--;
                    }
                    left++;
                }
            }
            right++;
        }
        if (length == 1000000) {
            return "";
        }
        return s.substring(resStart, resStart + length);
    }

    private boolean checkArray(int[] tCount, int[] sCount) {
        for (int i = 65; i < tCount.length; i++) {
            if (tCount[i] > sCount[i]) {
                return false;
            }
        }
        return true;
    }
}