package hard;

/**
 * 描述：剑指 Offer 04. 二维数组中的查找
 * 链接：https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof/
 * Author：liuxing
 * Date：2021-01-26
 */
public class LinkMerge {

    /**
     * 合并多个连表
     *
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        return mergeKLists(lists, 0, lists.length - 1);
    }

    /**
     * 合并第start到第end个连表
     *
     * @param lists
     * @param start
     * @param end
     * @return
     */
    ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[end];
        }
        if (end - start == 1) {
            // 合并两个连表
            return mergeTwo(lists[start], lists[end]);
        }
        int middile = (start + end) / 2;
        ListNode left = mergeKLists(lists, start, middile);
        ListNode right = mergeKLists(lists, middile + 1, end);
        return mergeTwo(left, right);
    }

    /**
     * 合并两个连表
     *
     * @param list1
     * @param list2
     * @return
     */
    ListNode mergeTwo(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        if (list1.val <= list2.val) {
            list1.next = mergeTwo(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwo(list1, list2.next);
            return list2;
        }
    }

    static class ListNode {
        public int val;
        public ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
