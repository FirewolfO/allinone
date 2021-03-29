package 双指针;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/24 11:02 上午
 */
public class StrStr {

    public int strStr(String haystack, String needle) {
        if ("".equals(needle)) {
            return 0;
        }
        if (haystack.length() < needle.length()) {
            return -1;
        }
        int left = 0;
        int max = haystack.length() - needle.length();
        while (left <= max) {
            int right = left;
            int start = 0;
            while (start < needle.length() && needle.charAt(start) == haystack.charAt(right)){
                start++;
                right++;
            }
            if (start == needle.length()) {
                return left;
            }
            left++;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(new StrStr().strStr("abc","c"));
    }
}
