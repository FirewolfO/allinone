class Solution {
    public String addStrings(String num1, String num2) {
        int i = num1.length()-1;
        int j = num2.length()-1;
        String s = "";
        int next = 0;
        while(i >= 0 && j >= 0){
            int sum = num1.charAt(i)-'0' + num2.charAt(j) - '0' + next;
            s = sum % 10 + s;
            next = sum / 10;
            i--;
            j--;
        }
        String shengyu = (i==0) ? num2 : num1;
        int index = i == 0 ? j : i;
        while(index >= 0){
            int sum = shengyu.charAt(index)-'0' + next;
            s = sum % 10 + s;
            next = sum / 10;
            index--;
        }
        if(next != 0){
            s = next + s;
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().addStrings("11","123"));
    }
}