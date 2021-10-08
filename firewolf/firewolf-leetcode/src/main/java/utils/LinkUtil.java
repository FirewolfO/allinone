package utils;

/**
 * 链表工具
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/23 11:59 上午
 */
public class LinkUtil {

    public static ListNode buildLink(int... nums) {
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

    public static class ListNode<T> {
        public T val;
        public ListNode next;

        public ListNode() {
        }

        public ListNode(T val) {
            this.val = val;
        }

        public ListNode(T val, ListNode next) {
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
