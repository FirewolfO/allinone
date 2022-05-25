package 贪心;

class 单调递增的数字_738 {
    public static void main(String[] args) {
        int i = new 单调递增的数字_738().monotoneIncreasingDigits(10);
        System.out.println(i);
    }

    public int monotoneIncreasingDigits(int n) {
        if (n < 10) return n;
        char[] chars = (n + "").toCharArray();
        int i = 1; // 找到第一个不满足单调递增的位置
        while (i < chars.length && chars[i] >= chars[i - 1]) {
            i++;
        }
        int j = i >= chars.length - 1 ? chars.length - 1 : i;
        while (j > 0 && chars[j] < chars[j - 1]) { // 单调递增的这部分，由于第i位-1后可能大于第i-1位，所以需要判断，如果是的话，第i-1也要减1
            chars[j - 1] -= 1;
            j -= 1;
        }
        j += 1;
        while (j < chars.length) { //第0~j位满足单调递增了，后面的直接全部设置为9
            chars[j++] = '9';
        }
        return Integer.parseInt(new String(chars));
    }
}