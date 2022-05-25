package 回溯;

import java.util.ArrayList;
import java.util.List;

class 复原IP地址_93 {

    public static void main(String[] args) {
        String ipStr = "25525511135";
        System.out.println(new 复原IP地址_93().restoreIpAddresses(ipStr));
    }


    List<String> res = new ArrayList<>();
    List<String> one = new ArrayList<>();

    public List<String> restoreIpAddresses(String s) {
        restoreIpAddressesHelper(s, 0);
        return res;
    }

    private void restoreIpAddressesHelper(String s, int start) {
        if (one.size() == 4) {
            if (start >= s.length()) { // 遍历完了，正好被截成了4段，收集
                StringBuffer sb = new StringBuffer();
                int i = 0;
                for (; i < one.size() - 1; i++) {
                    sb.append(one.get(i)).append(".");
                }
                sb.append(one.get(i));
                res.add(sb.toString());
            }
            return;
        }
        for (int i = start; i < s.length(); i++) {
            if ((i - start) <= 3 && isValidIp(s, start, i)) { // (i-start)<=3 用于剪枝，不会有4个字符的ip段
                one.add(s.substring(start, i + 1));
                restoreIpAddressesHelper(s, i + 1);
                one.remove(one.size() - 1);
            }
        }
    }

    private boolean isValidIp(String s, int start, int end) {
        String oneS = s.substring(start, end + 1);
        if (oneS.charAt(0) == '0' && oneS.length() != 1) return false;
        int val = Integer.parseInt(oneS);
        if (val > 255) return false;
        return true;
    }
}