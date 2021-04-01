package 指针;

/**
 * https://leetcode-cn.com/problems/count-and-say/
 * 描述：38. 外观数列
 */
class CountAndSay {
    public String countAndSay(int n) {
        if (n == 1) {
            return "1";
        }
        String s = countAndSay(n - 1);

        StringBuffer sb = new StringBuffer();
        int count = 1;
        char c = s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            } else {
                sb.append(count + "" + c);
                count = 1;
                c = s.charAt(i);
            }
        }
        sb.append(count + "" + s.charAt(s.length() - 1));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new CountAndSay().countAndSay(5));
    }
}