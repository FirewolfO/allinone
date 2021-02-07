/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/2/3 10:46 下午
 */
public class LastWordLen {

    public static void main(String[] args) {
        System.out.println(new LastWordLen().lengthOfLastWord("a"));
    }

    public int lengthOfLastWord(String s) {
        if(s ==null || s.length() == 0){
            return 0;
        }
        int len = 0;
        int index = s.length()-1;
        while(index >-1){
            if(s.charAt(index) == ' '){
                break;
            }else{
                len++;
                index--;
            }
        }
        return len;
    }
}
