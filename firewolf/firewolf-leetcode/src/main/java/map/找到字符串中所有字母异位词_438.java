package map;

import java.util.ArrayList;
import java.util.List;

class 找到字符串中所有字母异位词_438 {


    public static void main(String[] args) {
        String s = "cbaebabacd";
        String p = "abc";
        List<Integer> anagrams = new 找到字符串中所有字母异位词_438().findAnagrams(s, p);
        System.out.println(anagrams);
    }

    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> resList = new ArrayList<>();
        if (s.length() < p.length()) {
            return resList;
        }
        int pLen = p.length();
        int[] pArray = getArray(p);
        int[] sArray = getArray(s.substring(0, pLen));
        char[] sChars = s.toCharArray();
        for (int i = 0; i <= s.length() - pLen; i++) {
            if (i > 0) {
                sArray[sChars[i - 1]]--;
                sArray[sChars[i + pLen - 1]]++;
            }
            if (isSampe(sArray, pArray)) {
                resList.add(i);
            }
        }
        return resList;
    }


    private boolean isSampe(int[] a, int[] b) {
        for (int i = 97; i < 123; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private int[] getArray(String s) {
        char[] sChars = s.toCharArray();
        int[] res = new int[123];
        for (char c : sChars) {
            res[c]++;
        }
        return res;
    }
}