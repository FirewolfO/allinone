package 回溯;

import java.util.ArrayList;
import java.util.List;


class 分割回文串_131 {

    public static void main(String[] args) {
        List<List<String>> aab = new 分割回文串_131().partition("aab");
        System.out.println(aab);
    }

    List<List<String>> res = new ArrayList<>();
    List<String> one = new ArrayList<>();

    public List<List<String>> partition(String s) {
        partitionHelper(s, 0);
        return res;
    }

    private void partitionHelper(String s, int start) {
        if (start >= s.length()) { //到最后一个位置了，结束，开始收集
            res.add(new ArrayList<>(one));
            return;
        }
        for (int i = start; i < s.length(); i++) { // 当前字符串为 start ~ i ,其他的是 i ~ s.length()-1;
            if (isHuiwen(s, start, i)) { // 如果当前串是回文，则看后面的能不能都被切割成回文
                one.add(s.substring(start, i + 1));
                partitionHelper(s, i + 1);
                one.remove(one.size() - 1); //回溯
            }
        }
    }

    /**
     * 判断那是不是回文串
     *
     * @param s
     * @param start
     * @param end
     * @return
     */
    private boolean isHuiwen(String s, int start, int end) {
        while (start <= end && s.charAt(start) == s.charAt(end)) {
            start++;
            end--;
        }
        if (start > end) return true;
        return false;
    }
}