package 滑动窗口;

import java.util.HashSet;
import java.util.Set;

/**
 * 3. 无重复字符的最长子串
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 */
class 最长无重复子串 {
    public static void main(String[] args) {
        int s = new 最长无重复子串().lengthOfLongestSubstring("abcabcbb");
        System.out.println(s);
    }

    public int lengthOfLongestSubstring(String s) {
        if (s.length() <= 1) {
            return s.length();
        }
        char[] cs = s.toCharArray();
        Set<Character> cSets = new HashSet<>();
        int left = 0;
        int right = 0;
        int max = 0;
        while (right < s.length()) {
            while (right < s.length() && !cSets.contains(cs[right])) {
                cSets.add(cs[right++]);
            }
            if (right - left > max) {
                max = right - left;
            }
            if (s.length() - left + 1 <= max) {
                break;
            }
            cSets.remove(cs[left++]);
        }
        return max;
    }
}
