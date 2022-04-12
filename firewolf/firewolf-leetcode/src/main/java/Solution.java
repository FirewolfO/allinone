import java.util.ArrayList;
import java.util.List;

import static utils.LinkUtil.*;

class Solution {
    List<String> res = new ArrayList<>();
    List<String> one = new ArrayList<>();


    public static void main(String[] args) {
        List<String> strings = new Solution().restoreIpAddresses("25525511135");
        System.out.println("xx");
    }

    public List<String> restoreIpAddresses(String s) {
        dfs(s, 0);
        return res;
    }

    private void dfs(String s, int index) {
        if (index >= s.length()) {
            if (one.size() == 4) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < one.size() - 1; i++) {
                    sb.append(one.get(i)).append(".");
                }
                sb.append(one.get(one.size() - 1));
                res.add(sb.toString());
            }
            return;
        }
        if (one.size() > 4) return;
        one.add(s.substring(index, index + 1));
        dfs(s, index + 1);
        one.remove(one.size() - 1);

        if (s.charAt(index) == '0') return;
        if (index < s.length() - 1) {
            one.add(s.substring(index, index + 2));
            dfs(s, index + 2);
            one.remove(one.size() - 1);
        }


        if (index < s.length() - 2) {
            String item = s.substring(index, index + 3);
            int count = Integer.parseInt(item);
            if (count <= 255) {
                one.add(s.substring(index, index + 3));
                dfs(s, index + 3);
                one.remove(one.size() - 1);
            }
        }
    }
}