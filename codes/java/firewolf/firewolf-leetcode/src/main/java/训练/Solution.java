package 训练;

class Solution {
    public static void main(String[] args) {
        System.out.println(new Solution().countAndSay(4));
    }

    public String countAndSay(int n) {
        if (n == 1) return "1";
        int i = 2;
        String last = "1";
        while (i <= n) {
            StringBuilder sb = new StringBuilder();
            char[] lastCharArray = last.toCharArray();
            char c = lastCharArray[0];
            int count = 1;
            int index = 1;
            while (index < last.length()) {
                if (lastCharArray[index] == c) {
                    count++;
                } else {
                    sb.append(count + "").append(c + "");
                    count = 1;
                    c = lastCharArray[index];
                }
                index++;
            }
            sb.append(count + "").append(c + "");
            last = sb.toString();
        }
        return last;

    }
}