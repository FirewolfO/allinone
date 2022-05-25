package 贪心;

class 柠檬水找零_860 {

    public static void main(String[] args) {
        int[] bills = new int[]{5, 5, 5, 10, 20};
        boolean b = new 柠檬水找零_860().lemonadeChange(bills);
        System.out.println(b);
    }

    public boolean lemonadeChange(int[] bills) {
        int five = 0; //5块钱个数
        int ten = 0; //10块钱个数
        for (int i = 0; i < bills.length; i++) {
            if (bills[i] == 5) {
                five += 1;
            } else if (bills[i] == 10) {
                five -= 1;
                ten += 1;
                if (five < 0) return false;
            } else {
                if (ten > 0 && five > 0) { //通过10+5找零
                    ten -= 1;
                    five -= 1;
                } else if (five > 2) { // 通过3个5找零
                    five -= 3;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}