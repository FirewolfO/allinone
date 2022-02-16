package 贪心;

import java.util.ArrayList;
import java.util.List;

class 划分字母区间_763 {
    public static void main(String[] args) {
        String s = "ababcbacadefegdehijhklij";
        List<Integer> integers = new 划分字母区间_763().partitionLabels(s);
        System.out.println(integers);
    }

    public List<Integer> partitionLabels(String s) {
        char[] chars = s.toCharArray();
        int[] last = new int[26];
        // 记录每个字符最后出现的位置，同一个字符，只能在同一个段，所以从这个字符开始到最后出现的位置，一定是在同一个段里面
        for (int i = 0; i < chars.length; i++) {
            last[chars[i] - 'a'] = i;
        }
        int start = 0;
        int end = 0;
        List<Integer> res = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            end = Math.max(end, last[chars[i] - 'a']); //找到段的最大位置
            if (i == end) {
                res.add(end - start + 1); //
                start = i + 1; // 下一个段的开始位置
                if (start < s.length()) {
                    end = last[chars[start] - 'a'];
                }
            }
        }
        return res;
    }
}