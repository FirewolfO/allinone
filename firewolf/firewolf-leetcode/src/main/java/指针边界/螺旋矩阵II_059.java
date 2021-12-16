package 指针边界;

class 螺旋矩阵II_059 {

    public static void main(String[] args) {
        int[][] ints = new 螺旋矩阵II_059().generateMatrix(5);
        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[i].length; j++) {
                System.out.print(ints[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public int[][] generateMatrix(int n) {
        int[][] ss = new int[n][n];
        int offset = 1;
        int num = 1;
        int loop = n / 2;  // 每个圈循环几次，例如n为奇数3，那么loop = 1 只是循环一圈，矩阵中间的值需要单独处理
        while (loop > 0) {
            for (int i = offset - 1; i < n - offset; i++) {
                ss[offset - 1][i] = num++;
            }

            for (int i = offset - 1; i < n - offset; i++) {
                ss[i][n - offset] = num++;
            }

            for (int i = n - offset; i >= offset; i--) {
                ss[n - offset][i] = num++;
            }

            for (int i = n - offset; i >= offset; i--) {
                ss[i][offset - 1] = num++;
            }
            offset++;
            loop--;
        }

        if (n % 2 == 1) { // 单独处理中间的
            int mid = n / 2;
            ss[mid][mid] = n * n;
        }
        return ss;
    }
}