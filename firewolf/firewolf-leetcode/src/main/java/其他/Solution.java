package 其他;

class Solution {

    public static void main(String[] args) {
        System.out.println(new Solution().myAtoi("00000-42a1234"));
    }

    public int myAtoi(String s) {
        if (s.length() == 0) {
            return 0;
        }
        int res = 0;
        int flag = 0; // 正数
        char[] chars = s.toCharArray();
        int i = 0;
        while (chars[i] == ' ') {
            i++;
        }
        int x = i;
        for (; i < chars.length; i++) {
            if (chars[i] == '-' || chars[i] == '+') {
                if (x == i) {
                    if (flag != 0) {
                        break;
                    }
                    if (chars[i] == '-') {
                        flag = -1;
                    } else {
                        flag = 1;
                    }
                    continue;
                } else {
                    break;
                }
            }
            if (chars[i] == '+') {
                continue;
            }
            if (chars[i] < '0' || chars[i] > '9') {
                break;
            }
            int m = chars[i] - '0';
            if (flag == 1 || flag == 0) {
                if (res > (Integer.MAX_VALUE / 10 - m / 10)) {
                    return Integer.MAX_VALUE;
                }
            } else {
                if (res * -1 < (Integer.MIN_VALUE / 10 - m / 10)) {
                    return Integer.MIN_VALUE;
                }
            }
            res = res * 10 + m;
        }
        return res * ((flag == 0) ? 1 : flag);
    }
}