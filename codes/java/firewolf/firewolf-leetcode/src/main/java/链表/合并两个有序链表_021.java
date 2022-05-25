package 链表;

import utils.LinkUtil;

import static utils.LinkUtil.ListNode;


public class 合并两个有序链表_021 {

    public static void main(String[] args) {
        ListNode<Integer> l1 = LinkUtil.buildLink(1, 2, 4);
        ListNode<Integer> l2 = LinkUtil.buildLink(1, 3, 4);
        ListNode listNode = new 合并两个有序链表_021().mergeTwoLists(l1, l2);
        System.out.println(listNode);
    }

    public ListNode mergeTwoLists(ListNode<Integer> l1, ListNode<Integer> l2) {
        ListNode empty = new ListNode(0);
        ListNode head = empty;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                head.next = l1;
                l1 = l1.next;
            } else {
                head.next = l2;
                l2 = l2.next;
            }
            head = head.next;
        }
        if (l1 != null) {
            head.next = l1;
        }
        if (l2 != null) {
            head.next = l2;
        }
        return empty.next;
    }
}
