package 分治;

/**
 * 14. 最长公共前缀
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 * <p>
 * 如果不存在公共前缀，返回空字符串 ""
 */
public class 最长公共前缀_014 {

    public static void main(String[] args) {
        String s = new 最长公共前缀_014().longestCommonPrefix(new String[]{"aaa", "aa", "aaa"});
        System.out.println(s);
    }

    public String longestCommonPrefix(String[] strs) {
        return longestCommonPrefix(strs, 0, strs.length - 1);
    }


    private String longestCommonPrefix(String[] strs, int start, int end) {
        if (end - start <= 1) { //低于两个了，直接求
            return longestCommonPrefixOf2Strs(strs[start], strs[end]);
        } else {
            int mid = (end + start) / 2;
            String left = longestCommonPrefix(strs, start, mid - 1);
            String right = longestCommonPrefix(strs, mid, end);
            return longestCommonPrefixOf2Strs(left, right);
        }
    }

    private String longestCommonPrefixOf2Strs(String str1, String str2) {
        if (str1.length() == 0 || str2.length() == 0) {
            return "";
        }
        int i = 0;
        int j = 0;
        while (i < str1.length() && j < str2.length() && str1.charAt(i) == str2.charAt(j)) {
            i++;
            j++;
        }
        return str1.substring(0, i);
    }
}
