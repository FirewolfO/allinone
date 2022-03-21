package Test;

public class Test {

    public static void main(String[] args) {
        System.out.println(new Test().findLongestStrLen(" aaa    da daad "));
    }

    public int findLongestStrLen(String s) {
        int index = 0;
        int maxLen = 0;
        char[] chars = s.toCharArray();
        int curLen = 0;
        while (index < chars.length) {
            if (chars[index] != ' ') {
                curLen++;
                maxLen = Math.max(maxLen, curLen);
                if (maxLen >= chars.length / 2) break;
            } else {
                curLen = 0;
            }
            index++;
        }
        return maxLen;
    }
}
