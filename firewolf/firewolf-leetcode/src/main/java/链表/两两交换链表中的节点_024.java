package 链表;

import utils.LinkUtil;
import utils.LinkUtil.ListNode;

/**
 * 24. 两两交换链表中的节点
 * 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
 * <p>
 * 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 */
public class 两两交换链表中的节点_024 {

    public static void main(String[] args) {
        ListNode l = LinkUtil.buildLink(1,2,3,4);
        System.out.println(new 两两交换链表中的节点_024().swapPairs(l));
    }

    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode empty = new ListNode();
        empty.next = head;
        ListNode point = empty;

        while (point.next != null && point.next.next != null) {
            ListNode tmp = point.next;
            point.next = tmp.next;
            tmp.next = point.next.next;
            point.next.next = tmp;
            point = tmp;
        }
        return empty.next;
    }

}
