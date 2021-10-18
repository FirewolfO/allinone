package KMP;

/**
 * 关于KMP算法，精讲地址为：https://www.zhihu.com/question/21923021
 * 28. 实现 strStr()
 * 实现 strStr() 函数。
 * <p>
 * 给你两个字符串 haystack 和 needle ，请你在 haystack 字符串中找出 needle 字符串出现的第一个位置（下标从 0 开始）。如果不存在，则返回  -1 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/implement-strstr
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class 实现strStr_028 {

    public static void main(String[] args) {
        String hayStack = "aaa";
        String needle = "aaa";
        System.out.println(new 实现strStr_028().strStr(hayStack, needle));
        System.out.println(new 实现strStr_028().foceFind("abc", "c"));
    }

    public int strStr(String haystack, String needle) {
        if (haystack.length() < needle.length()) { //原串长度比匹配串短，不可能匹配
            return -1;
        }
        if (haystack.length() == 0 || needle.length() == 0) { // 空串，直接返回0
            return 0;
        }

        int[] next = getNext(needle);
        int j = 0;
        for (int i = 0; i < haystack.length(); i++) {
            while (haystack.charAt(i) != needle.charAt(j) && j > 0) { // 如果不匹配的话，就回退 j
                j = next[j - 1];
            }
            if (haystack.charAt(i) == needle.charAt(j)) { //j位置的已经和i匹配上了，后移 i 和 j
                j++;
                if (j == needle.length()) { // 匹配完所有的了，直接返回
                    return i - needle.length() + 1;
                }
            }
        }

        // 已经扫描到了最后一位，证明满足条件
        return -1;
    }

    private int[] getNext(String needle) {
        int[] next = new int[needle.length()];
        next[0] = 0; // 初始化next的第一个值

        char[] chars = needle.toCharArray();
        for (int i = 1; i < chars.length; i++) {
            int now = next[i - 1]; // now表示的是下标[0 ~ i-1]构成的子串的相等前后缀的长度，同时也是前缀的最后一个字符的下一个下标，也就是说，前缀下标为now-1的字符，对比的是后缀下标 i-1的字符，
            // 于是在 0 ~ i 区间，需要使用 now 来和 i 对比
            while (now > 0 && chars[now] != chars[i]) { // 不相等的时候，就回退now， 由于 i 之前的一段字符串，是 [0 ~ i-1]区间的后缀，那么，next[now - 1] 对应的下标，就是要继续匹配的字符
                now = next[now - 1];
            }
            if (chars[now] == chars[i]) { // 找到和 i 位置字符一样的了，长度增加
                now++;
            }
            next[i] = now; // 赋值
        }
        return next;
    }


    // 以下是暴力匹配逻辑
    public int foceFind(String haystack, String needle) {
        if (haystack.length() < needle.length()) { //原串长度比匹配串短，不可能匹配
            return -1;
        }
        if (haystack.length() == 0 || needle.length() == 0) { // 空串，直接返回0
            return 0;
        }
        char[] haystackChars = haystack.toCharArray();
        char[] needleChars = needle.toCharArray();

        for (int i = 0; i <= haystackChars.length - needleChars.length; i++) { // 超过 haystackChars.length - needleChars.length的，长度不够，不用遍历
            int j = 0; // 模式串从第一个开始扫描
            int k = i;
            while (j < needleChars.length && needleChars[j] == haystackChars[k]) {
                j++;
                k++;
            }
            if (j == needleChars.length) { //所有的字符都相等
                return i;
            }
        }
        return -1;
    }
}
