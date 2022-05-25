package 每日一题;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Day20220314_词典中最长的单词_720 {
    public static void main(String[] args) {
        String[] words = new String[]{"w", "wo", "wor", "worl", "world"};
        String res = new Day20220314_词典中最长的单词_720().longestWord(words);
        System.out.println(res);
    }

    public String longestWord(String[] words) {
        Arrays.sort(words, (s1, s2) -> {
            if (s1.length() == s2.length()) {
                return s2.compareTo(s1);
            } else {
                return s1.length() - s2.length();
            }
        });
        Set<String> preWords = new HashSet<>();
        preWords.add("");
        String longestStr = "";
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (preWords.contains(word.substring(0, word.length() - 1))) {
                longestStr = word;
                preWords.add(word);
            }
        }
        return longestStr;
    }
}