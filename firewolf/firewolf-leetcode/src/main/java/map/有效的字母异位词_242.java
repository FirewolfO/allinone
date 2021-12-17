package map;

public class 有效的字母异位词_242 {

    public static void main(String[] args) {
        String s = "anagram";
        String t = "nagaram";
        boolean anagram = new 有效的字母异位词_242().isAnagram(s, t);
        System.out.println(anagram);
    }

    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] sArray = new int[123];
        int[] tArray = new int[123];
        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();
        for (char c : sChars) {
            sArray[c]++;
        }
        for (char c : tChars) {
            tArray[c]++;
        }
        for (int i = 97; i < 123; i++) {
            if (sArray[i] != tArray[i]) {
                return false;
            }
        }
        return true;
    }
}
