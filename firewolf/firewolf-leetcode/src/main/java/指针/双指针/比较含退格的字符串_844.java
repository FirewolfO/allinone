package 指针.双指针;

/**
 * 844. 比较含退格的字符串
 * 给定 s 和 t 两个字符串，当它们分别被输入到空白的文本编辑器后，请你判断二者是否相等。# 代表退格字符。
 * <p>
 * 如果相等，返回 true ；否则，返回 false 。
 * <p>
 * 注意：如果对空文本输入退格字符，文本继续为空。
 */
public class 比较含退格的字符串_844 {

    public static void main(String[] args) {
        String s = "ab#c";
        String t = "ad#c";

    }

    public boolean backspaceCompare(String s, String t) {
        int sIndex = s.length() - 1;
        int tIndex = t.length() - 1;
        int sSkip = 0;
        int tSkip = 0;
        while (sIndex >= 0 && tIndex >= 0) {
            while (s.charAt(sIndex) == '#' && sIndex >= 0) {
                sSkip++;
                sIndex--;
            }
            while (t.charAt(tIndex) == '#' && tIndex >= 0) {
                tSkip++;
                tIndex--;
            }
            if ((sIndex >= 0 && tIndex >= 0) && s.charAt(sIndex) != t.charAt(tIndex)) {
                return false;
            }
        }
        return false;
    }

}
