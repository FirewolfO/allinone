package 回溯;

import java.util.*;

class Solution {
    public static void main(String[] args) {
        List<List<String>> tickets = new ArrayList<>();
        tickets.add(Arrays.asList("JFK", "SFO"));
        tickets.add(Arrays.asList("JFK", "ATL"));
        tickets.add(Arrays.asList("SFO", "ATL"));
        tickets.add(Arrays.asList("ATL", "JFK"));
        tickets.add(Arrays.asList("ATL", "SFO"));
        System.out.println(new Solution().findItinerary(tickets));
    }

    private List<String> res = new ArrayList<>();
    private List<String> current = new ArrayList<>();
    private Map<String, List<String>> ticketMap = new HashMap<>();
    private List<String> used = new ArrayList<>();

    public List<String> findItinerary(List<List<String>> tickets) {

        tickets.forEach(ticket -> {
            if (!ticketMap.containsKey(ticket.get(0))) ticketMap.put(ticket.get(0), new ArrayList<>());
            ticketMap.get(ticket.get(0)).add(ticket.get(1));
        });
//        Set<String> fromSet = ticketMap.keySet();
//        for (String from : fromSet) {
//            used.clear();
//            current.add(from);
//            findItineraryHelp(tickets, from);
//            current.remove(current.size() - 1);
//        }
        current.add("JFK");
        findItineraryHelp(tickets, "JFK");
        return res;
    }

    private void findItineraryHelp(List<List<String>> tickets, String from) {
        if (current.size() == tickets.size() + 1) {
            if (res.size() == 0 || isBetter()) {// 更优的方式
                res = new ArrayList<>(current);
            }
            return;
        }
        List<String> toList = ticketMap.get(from);
        if (toList == null) return;
        for (String to : toList) {
            String onePath = from + to;
            if (used.contains(onePath)) {
                continue;
            }
            current.add(to);
            used.add(onePath);
            findItineraryHelp(tickets, to);
            current.remove(current.size() - 1);
            used.remove(used.size() - 1);
        }

    }

    private boolean isBetter() {
        for (int i = 0; i < res.size(); i++) {
            if (current.get(i).compareTo(res.get(i)) == 0) {
                continue;
            } else if (current.get(i).compareTo(res.get(i)) < 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}