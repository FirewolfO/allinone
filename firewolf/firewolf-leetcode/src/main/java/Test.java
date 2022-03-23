public class Test {


    public static void main(String[] args) {
        int x = 50;
        int cont = 0;
        while (x > 0) {
            cont++;
            x = x&(x-1);
        }
        System.out.println(cont);
    }

    public int findLongestWrod(String str) {
        int start = 0, end = 0;
        int maxLen = 0;
        int strLen = str.length();
        char[] chars = str.toCharArray();
        while (end < strLen) {
            if (chars[end] == ' ') {
                int curLen = end - start;
                if (curLen > maxLen) {
                    maxLen = curLen;
                    if (maxLen > (strLen - start) / 2) break;
                }
                start = end + 1;
            }
            end++;
        }
        if (end == strLen && maxLen == 0) return strLen - start;
        return maxLen;
    }
}
