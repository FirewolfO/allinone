import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.LinkUtil.*;

class Solution {

    public static void main(String[] args) {
        int res = new Solution().minCut("aaba");
        System.out.println(res);
    }

    boolean[][] dp;
    int minCount;
    public int minCut(String s) {
        minCount = s.length();
        int len = s.length();
        dp = new boolean[len][len];
        for(int i = 0; i < len; i++){
            dp[i][i] = true;
        }
        for(int Len = 2; Len <= len; Len++){
            for(int i = 0; i < len; i++){
                int j = Len + i - 1;
                if(j >= len) break;
                if(s.charAt(i) != s.charAt(j)){
                    dp[i][j] = false;
                }else{
                    if(Len <= 3){
                        dp[i][j] = true;
                    }else{
                        dp[i][j] = dp[i+1][j-1];
                    }
                }
            }
        }
        dfs(s,0,0);
        return minCount-1;
    }

    private void dfs(String s, int index,int count){
        if(index == s.length()){
            minCount = Math.min(minCount,count);
            return;
        }
        int i = s.length()-1;
        while(i >= index){
            if(dp[index][i]){
                dfs(s,i+1,count+1);
                break;
            }
            i--;
        }
    }
}