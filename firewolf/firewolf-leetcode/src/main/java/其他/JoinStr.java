package 其他;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/24 8:05 下午
 */
public class JoinStr {
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        if ((words.length == 0) || (words[0] == null) || (words[0].length() == 0)) {
            return res;
        }
        int wLen = words[0].length();
        Map<String, Integer> map = new HashMap<>();
        for (String w : words) {
            map.put(w, map.getOrDefault(w, 0) + 1);
        }
        for (int i = 0; i < s.length() - words.length * wLen + 1; i++) {
            Map<String, Integer> d = new HashMap<>();
            int k = 0;
            int valid = 0;
            while (k < words.length) {
                String tmp = s.substring(i + k * wLen, i + (k + 1) * wLen);
                if (map.containsKey(tmp) && d.getOrDefault(tmp, 0) + 1 <= map.get(tmp)) {
                    d.put(tmp, d.getOrDefault(tmp, 0) + 1);
                    valid++;
                    k++;
                } else {
                    break;
                }
            }
            if (valid == words.length) {
                res.add(i);
            }
        }
        return res;
    }


    public static void main(String[] args) {
        String s = "barfoothefoobarman";
        String[] nn = {"foo", "bar"};
        System.out.println(new JoinStr().findSubstring(s, nn));
    }
}
