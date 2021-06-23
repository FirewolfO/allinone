package 中心扩散;

class 最长回文串 {

    public static void main(String[] args) {


        String m = new 最长回文串().longestPalindrome("cbbd");
        System.out.println(m);
    }

    public String longestPalindrome(String s) {
        if(s.length() <= 1){
            return s;
        }
        int maxLen = 0;
        int start = 0;
        char[] chars = s.toCharArray();
        for(int i = 0;i<chars.length-1;i++){
            int even = maxLen(i,i,chars);
            int old = maxLen(i,i+1,chars);
            if(even > old && even > maxLen){
                maxLen = even;
                start = i - maxLen / 2;
            }else if(even < old && old > maxLen){
                maxLen = old;
                start = i - maxLen / 2 + 1;
            }
        }
        return s.substring(start,start + maxLen);
    }

    private int maxLen(int left,int right,char[] chars){
        int maxLen = (left == right) ? -1 : 0;
        while(left >= 0 && right < chars.length && chars[left--] == chars[right++]){
            maxLen += 2;
        }
        return maxLen;
    }
}