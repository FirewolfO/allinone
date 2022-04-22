import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.LinkUtil.*;

class Solution {

    public static void main(String[] args) {
        new Solution().compareVersion("0.1","1.1");
    }

    public int compareVersion(String version1, String version2) {
        String[] versionToken1 = version1.split("\\.");
        String[] versionToken2 = version2.split("\\.");
        int index = 0;
        while (index < versionToken1.length && index < versionToken2.length) {
            int v1 = Integer.parseInt(versionToken1[index]);
            int v2 = Integer.parseInt(versionToken2[index]);
            if (v1 < v2) {
                return -1;
            } else if (v1 > v2) {
                return 1;
            }
            index++;
        }
        return 0;
    }
}