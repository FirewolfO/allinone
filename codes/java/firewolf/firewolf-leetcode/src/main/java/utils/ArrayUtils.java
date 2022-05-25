package utils;

import java.util.Arrays;

public class ArrayUtils {

    /***
     * [[1,100],[11,22],[1,11],[2,12]]  转成数组
     * {
     *      {1,100},{11,22},{1,11},{2,12}
     * }
     *
     * @param str
     * @param split
     * @return
     */
    public static int[][] to2Array(String str, String split) {
        String s = str.replaceAll(" ", "").replaceAll("]" + split + "\\[", ";");
        s = s.substring(2, s.length() - 2);
        String[] datas = s.split(";");
        int[][] res = new int[datas.length][];
        for (int i = 0; i < datas.length; i++) {
            res[i] = Arrays.stream(datas[i].split(split)).mapToInt(x -> Integer.parseInt(x)).toArray();
        }
        return res;
    }

    /***
     * [1,2,3,4]  --> {1,2,3,4}
     * @param str
     * @param split
     * @return
     */
    public static int[] toArray(String str, String split) {
        String s = str.replaceAll(" ", "");
        s = s.substring(1, s.length() - 1);
        return Arrays.stream(s.split(split)).mapToInt(x -> Integer.parseInt(x)).toArray();
    }

    public static String array2Str(int[][] array, boolean pretty) {
        if (array == null || array.length == 0) return "{}";
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        if (pretty) sb.append("\n\r");
        for (int i = 0; i < array.length - 1; i++) {
            if (pretty) sb.append("\t");
            sb.append(array2Str(array[i]));
            if (pretty) sb.append(",\n\r");

        }
        if (pretty) sb.append("\t");
        sb.append(array2Str(array[array.length - 1]));
        if (pretty) sb.append("\n\r");
        sb.append("}");
        return sb.toString();
    }

    public static String array2Str(int[] array) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        if (array != null || array.length != 0) {
            for (int i = 0; i < array.length - 1; i++) {
                sb.append(array[i]).append(",");
            }
            sb.append(array[array.length - 1]);
        }
        sb.append("}");
        return sb.toString();
    }
}
