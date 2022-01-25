package 贪心;

import java.util.Arrays;

class 分发饼干_455 {
    
    public static void main(String[] args) {
        int[] g = new int[]{1, 2, 3};
        int[] s = new int[]{1, 1};
        int contentChildren = new 分发饼干_455().findContentChildren(g, s);
        System.out.println(contentChildren);
    }


    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int count = 0;
        int gIndex = 0;
        for (int i = 0; i < s.length; i++) {
            if (gIndex < g.length && s[i] >= g[gIndex]) {
                gIndex++;
                count++;
            }
        }
        return count;
    }
}