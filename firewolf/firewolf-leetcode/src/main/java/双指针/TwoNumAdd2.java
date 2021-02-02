package 双指针;

/**
 * 描述：2. 两数相加
 * 连接：https://leetcode-cn.com/problems/add-two-numbers/
 * 这种方式虽然代码较长，但是效率高一些
 * Author：liuxing
 * Date：2021-01-31
 */
public class TwoNumAdd2 {

    public static void main(String[] args) {
        TwoNumAdd2 t = new TwoNumAdd2();
        ListNode l1 = t.buildLink(new int[]{9, 9, 9, 9, 9, 9, 9});
        ListNode l2 = t.buildLink(new int[]{9, 9, 9, 9});
        ListNode listNode = t.addTwoNumbers(l2, l1);
        System.out.println(listNode);
    }

    private ListNode buildLink(int[] nums) {
        ListNode result = new ListNode();
        ListNode p = result;
        int i = 0;
        while (i < nums.length) {
            ListNode l = new ListNode(nums[i]);
            p.next = l;
            p = p.next;
            i++;
        }
        return result.next;
    }


    /**
     * 两数相加逻辑
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        ListNode next = result;
        int count = 0;
        while (l1 != null && l2 != null) {
            ListNode n = new ListNode();
            int a = l1.val + l2.val + count;
            n.val = a % 10;
            count = a / 10;
            next.next = n;
            next = n;
            l1 = l1.next;
            l2 = l2.next;
        }
        if (l1 != null) {
            while (l1 != null) {
                int a = l1.val + count;
                count = a / 10;
                l1.val = a % 10;
                next.next = l1;
                next = l1;
                l1 = l1.next;
            }
        }
        if (l2 != null) {
            next.next = l2;
            while (l2 != null) {
                int a = l2.val + count;
                count = a / 10;
                l2.val = a % 10;
                next.next = l2;
                next = l2;
                l2 = l2.next;
            }
        }
        if (count != 0) {
            ListNode mm = new ListNode();
            mm.val = count;
            next.next = mm;
        }
        return result.next;
    }

    static class ListNode {
        public int val;
        public ListNode next;

        public ListNode() {
        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        public String toString() {
            String s = val + ",";
            if (next != null) {
                s = s + next.toString();
            }
            return s;
        }
    }
}
