package utils;

/**
 * 链表工具
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/23 11:59 上午
 */
public class LinkUtil {

    /**
     * 构建链表
     *
     * @param nums
     * @param <T>
     * @return
     */
    public static <T> ListNode<T> buildLink(T... nums) {
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
     * 找出第 n 个值为nodeVal的节点
     *
     * @param head
     * @param nodeVal
     * @param count
     * @param <T>
     * @return
     */
    public static <T> ListNode findNode(ListNode<T> head, T nodeVal, int count) {
        ListNode<T> p = head;
        int c = 0;
        while (p != null) {
            if (p.val == nodeVal) {
                c++;
                if (c == count) {
                    return p;
                }
            }
            p = p.next;
        }
        return null;
    }

    public static class ListNode<T> {
        public T val;
        public ListNode<T> next;

        public ListNode() {
        }

        public ListNode(T val) {
            this.val = val;
        }

        public ListNode(T val, ListNode<T> next) {
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
