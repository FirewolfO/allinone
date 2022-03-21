package Test;

class Solution {
    public static void main(String[] args) {
        System.out.println(new Solution().isPalindrome("OP"));
    }
    public boolean isPalindrome(String s) {
        char[] chars = s.toLowerCase().toCharArray();
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (chars[left] < 'a' || chars[left] > 'z') {
                left++;
                continue;
            }
            if (chars[right] < 'a' || chars[right] > 'z') {
                right--;
                continue;
            }
            if (chars[left++] != chars[right--]) return false;
        }
        return true;
    }
}