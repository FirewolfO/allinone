import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.LinkUtil.*;

class Solution {
    public static void main(String[] args) {
        int i = new Solution().canCompleteCircuit(new int[]{2, 3, 4}, new int[]{3, 4, 3});
        System.out.println(i);
    }

    public int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        for (int i = 0; i < gas.length; i++) {
            int k = i;
            int other = 0;
            while (k < i + len) {
                int index = (k + len) % len;
                other += (gas[index] - cost[(k + len + 1) % len]);
                if (index == (i - 2 + len) % len) {
                    return i;
                }
                if (other < 0) break;
                k++;
            }
        }
        return -1;
    }

}