package map;

public class 赎金信_383 {


    public static void main(String[] args) {
        String ransomNote = "aa";
        String magazine = "aab";
        boolean b = new 赎金信_383().canConstruct(ransomNote, magazine);
        System.out.println(b);
    }

    public boolean canConstruct(String ransomNote, String magazine) {
        int[] ransomNoteArray = new int[123];
        int[] magazineArray = new int[123];
        char[] ransomNoteChars = ransomNote.toCharArray();
        char[] magazineChars = magazine.toCharArray();
        for (char c : ransomNoteChars) {
            ransomNoteArray[c]++;
        }
        for (char c : magazineChars) {
            magazineArray[c]++;
        }
        for (int i = 97; i < 123; i++) {
            if (ransomNoteArray[i] > magazineArray[i]) {
                return false;
            }
        }
        return true;
    }
}
