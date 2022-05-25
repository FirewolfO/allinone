package 贪心;

class 加油站_134 {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int curRest = 0; // 当前剩余油量
        int totalRest = 0; // 总共剩余油量
        int index = 0;
        for(int i=0; i < gas.length; i++){
            int tmpRest = gas[i] - cost[i]; // 经过某个站的加油量-耗油量
            curRest += tmpRest;
            totalRest += tmpRest;
            if( curRest < 0 ){ //小于0了，重新计算
                curRest = 0;
                index = (i+1) % gas.length;
            }
        }
        if(totalRest < 0 ) return -1; // 总油量不够，肯定不可以跑一圈，
        // 总油量够，肯定可以跑一圈，
        return index;
    }
}