package 集合;

import java.util.HashSet;
import java.util.Set;

class 快乐数_202 {

    public static void main(String[] args) {
        boolean happy = new 快乐数_202().isHappy(3);
        System.out.println(happy);
    }

    public boolean isHappy(int n) {
        Set<Integer> sumSet = new HashSet<>(); // 保存已经计算过的和
        while (true) {
            int sum = 0;
            while (n != 0) {
                int mod = n % 10;
                sum += mod * mod;
                n = n / 10;
            }
            if (sum == 1) {
                return true;
            }
            if (sumSet.contains(sum)) { // 如果和已经出现过，后面会无限循环，所有返回false
                return false;
            }
            sumSet.add(sum);
            n = sum;
        }
    }
}