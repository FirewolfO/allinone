package 每日一题;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class AllOne {
    private Map<String, Integer> map = new HashMap<>();
    LinkedList<String> link = new LinkedList<>();

    public AllOne() {

    }

    public void inc(String key) {
        int count = map.getOrDefault(key, 0);
        map.put(key, count + 1);
        if (count == 0) { // 第一次添加
            link.addFirst(key);
        } else {
            link.remove(key);
            int index = 0;
            while (index < link.size() && map.get(link.get(index)) < count + 1) {
                index++;
            }
            link.add(index, key);
        }
    }

    public void dec(String key) {
        Integer count = map.getOrDefault(key, 0);
        if (count == 0) return;
        if (count == 1) map.remove(key);
        else map.put(key, count - 1);
        link.remove(key);
        if (count == 1) return;
        int index = 0;
        while (index < link.size() && map.get(link.get(index)) < count - 1) {
            index++;
        }
        link.add(index, key);
    }

    public String getMaxKey() {
        if (map.size() == 0) return "";
        return link.getLast();
    }

    public String getMinKey() {
        if (map.size() == 0) return "";
        return link.getFirst();
    }
}

/**
 * Your AllOne object will be instantiated and called as such:
 * AllOne obj = new AllOne();
 * obj.inc(key);
 * obj.dec(key);
 * String param_3 = obj.getMaxKey();
 * String param_4 = obj.getMinKey();
 */