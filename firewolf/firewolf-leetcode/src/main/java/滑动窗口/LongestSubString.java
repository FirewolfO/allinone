package 滑动窗口;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：3. 无重复字符的最长子串
 * 连接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 * Author：liuxing
 * Date：2021-02-02
 */
public class LongestSubString {

    public static void main(String[] args) {
        String s = " ";
        System.out.println(new LongestSubString().lengthOfLongestSubstring(s));
    }

    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int left = 0;
        int res = 0;
        Map<Character, Integer> slid = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (slid.containsKey(s.charAt(i))) {
                left = Math.max(left, slid.get(s.charAt(i)) + 1);
            }
            slid.put(s.charAt(i), i);
            res = Math.max(res, i - left + 1);
        }
        return res;
    }
}
