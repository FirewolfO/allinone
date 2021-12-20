package 指针.双指针;

class 删除字符串中的所有相邻重复项_1047 {

    public static void main(String[] args) {
        String s = "abbaca";
        String s1 = new 删除字符串中的所有相邻重复项_1047().removeDuplicates(s);
        System.out.println(s1);
    }

    public String removeDuplicates(String s) {
        char[] chars = s.toCharArray();
        int slow = 0;
        int fast = 1;
        while (fast < chars.length) {
            if (slow >= 0 && chars[slow] == chars[fast]) {
                slow--;
            } else {
                chars[++slow] = chars[fast];
            }
            fast++;
        }
        return new String(chars, 0, slow + 1);
    }
}