package 滑动窗口;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：76. 最小覆盖子串
 * 连接：https://leetcode-cn.com/problems/minimum-window-substring/
 * Author：liuxing
 * Date：2021-02-07
 */
public class ShortestCoverSubString {

    Map<Character, Integer> tMap = new HashMap<>();
    Map<Character, Integer> sMap = new HashMap<>();

    public static void main(String[] args) {
        String s = "ADOBECODEBANC";
        String t = "ABC";
        System.out.println(new ShortestCoverSubString().minWindow(s, t));
    }

    public String minWindow(String s, String t) {
        char[] tChars = t.toCharArray();
        char[] sChars = s.toCharArray();
        int minLen = s.length() + 1;
        for (char c : tChars) {
            tMap.put(c, tMap.getOrDefault(c, 0) + 1);
        }

        int right = 0;
        int left = -1;
        int start = 0;
        while (right < s.length()) {
            char currentC = sChars[right];
            if (tMap.containsKey(currentC)) {
                sMap.put(currentC, sMap.getOrDefault(currentC, 0) + 1);
            }
            if (check()) {
                while (check() && left < right) {
                    ++left;
                    char key = sChars[left];
                    if (tMap.containsKey(key)) {
                        sMap.put(key, sMap.getOrDefault(key, 0) - 1);
                    }
                }
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }
            }
            right++;
        }
        return minLen == s.length() + 1 ? "" : new String(sChars, start, minLen);
    }

    private boolean check() {
        if (sMap.size() < tMap.size()) {
            return false;
        }
        for (Map.Entry<Character, Integer> entry : tMap.entrySet()) {
            if (sMap.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
}
