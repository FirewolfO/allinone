package map;

import java.util.*;

public class 字母异位词分组_049 {

    public static void main(String[] args) {
        String s1 = "bdddddddddd";
        String s2 = "bbbbbbbbbbc";
        System.out.println(new 字母异位词分组_049().groupAnagrams(new String[]{s1, s2}));
    }

    public List<List<String>> groupAnagrams(String[] strs) {

        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            String sTag = str2IntStr(s);
            List<String> sList = map.getOrDefault(sTag, new ArrayList<String>());
            sList.add(s);
            map.put(sTag, sList);
        }
        List<List<String>> resList = new ArrayList<>();
        for (List<String> v : map.values()) {
            resList.add(v);
        }
        return resList;
    }

    private String str2IntStr(String s) {
        int[] sArrays = new int[123];
        char[] sChars = s.toCharArray();
        for (char c : sChars) {
            sArrays[c]++;
        }
        String res = "";
        for (int i = 97; i < 123; i++) {
            res = res + sArrays[i] + "_";
        }
        return res;
    }
}
