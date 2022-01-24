package 回溯;

import java.util.*;

class 重新安排行程_332 {


    public static void main(String[] args) {
        List<List<String>> tickets = new ArrayList<>();
        tickets.add(Arrays.asList("MUC", "LHR"));
        tickets.add(Arrays.asList("JFK", "MUC"));
        tickets.add(Arrays.asList("SFO", "SJC"));
        tickets.add(Arrays.asList("LHR", "SFO"));
        List<String> itinerary = new 重新安排行程_332().findItinerary(tickets);
        System.out.println(itinerary);
    }

    private List<String> res = new ArrayList<>();
    private Map<String, Map<String, Integer>> map = new HashMap<>();

    public List<String> findItinerary(List<List<String>> tickets) {
        tickets.forEach(ticket -> {
            if (!map.containsKey(ticket.get(0))) {
                map.put(ticket.get(0), new TreeMap<String, Integer>()); //必须使用TreeMap，才能排序
            }
            int count = map.get(ticket.get(0)).getOrDefault(ticket.get(1), 0);
            map.get(ticket.get(0)).put(ticket.get(1), count + 1);
        });
        res.add("JFK");
        findItineraryHelper("JFK", tickets.size());
        return res;
    }

    private boolean findItineraryHelper(String from, int ticketNum) {
        if (res.size() == ticketNum + 1) { // 证明所有的票都用到了
            return true;
        }
        if (map.containsKey(from)) {
            Set<Map.Entry<String, Integer>> entrySet = map.get(from).entrySet();
            for (Map.Entry<String, Integer> entry : entrySet) {
                int count = entry.getValue();
                if (count > 0) { //还能去的地方，才去
                    entry.setValue(count - 1);
                    res.add(entry.getKey());
                    if (findItineraryHelper(entry.getKey(), ticketNum)) return true;
                    entry.setValue(count);
                    res.remove(res.size() - 1);
                }
            }
        }
        return false;
    }
}