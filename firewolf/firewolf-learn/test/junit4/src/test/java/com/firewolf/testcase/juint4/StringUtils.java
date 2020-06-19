package com.firewolf.testcase.juint4;

public class StringUtils {
    /**
     * 把每个单词的第一个字母大写，其他的小写
     *
     * @param string
     * @return
     */
    public static String firstChar2UpCase(String string) {
        if (string == null) {
            return null;
        }
        String[] words = string.split(" ");
        StringBuffer newStr = new StringBuffer();
        for (String word : words) {
            newStr.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        return newStr.substring(0, newStr.length() - 1);
    }
}