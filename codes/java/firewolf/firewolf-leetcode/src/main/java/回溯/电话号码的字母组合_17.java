package 回溯;

import java.util.ArrayList;
import java.util.List;

class 电话号码的字母组合_17 {

    public static void main(String[] args) {
        List<String> strings = new 电话号码的字母组合_17().letterCombinations("23");
        System.out.println(strings);
    }

    String[] numStrings = new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
    List<String> res = new ArrayList<>();
    StringBuffer sb = new StringBuffer();

    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return res;
        }
        letterCombinationsHelper(0, digits);
        return res;
    }

    private void letterCombinationsHelper(int index, String digits) {
        if (sb.length() == digits.length()) {//长度够了，收集
            res.add(sb.toString());
            return;
        }
        int numIndex = digits.charAt(index) - '0';
        String str = numStrings[numIndex];
        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i));
            letterCombinationsHelper(index + 1, digits);
            sb.deleteCharAt(sb.length() - 1); //回溯
        }
    }
}