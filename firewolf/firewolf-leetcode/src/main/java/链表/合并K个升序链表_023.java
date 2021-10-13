package 链表;

import utils.LinkUtil;

/**
 * https://leetcode-cn.com/problems/merge-k-sorted-lists/
 * 23. 合并K个升序链表
 * 给你一个链表数组，每个链表都已经按升序排列。
 *
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 */
import static utils.LinkUtil.ListNode;

/**
 * 给你一个链表数组，每个链表都已经按升序排列。
 * <p>
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 */
public class 合并K个升序链表_023 {

    public static void main(String[] args) {
        ListNode l1 = LinkUtil.buildLink(1, 4, 5);
        ListNode l2 = LinkUtil.buildLink(1, 3, 4);
        ListNode l3 = LinkUtil.buildLink(2, 6);
        System.out.println(new 合并K个升序链表_023().mergeKLists(new ListNode[]{l1, l2, l3}));
    }

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        return mergeKLists(lists, 0, lists.length - 1);
    }

    public ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[start];
        } else if (start + 1 == end) {
            return merge2List(lists[start], lists[end]);
        } else {
            int middle = (start + end) / 2;
            ListNode left = mergeKLists(lists, start, middle);
            ListNode right = mergeKLists(lists, middle + 1, end);
            return merge2List(left, right);
        }
    }

    private ListNode merge2List(ListNode<Integer> l1, ListNode<Integer> l2) {
        ListNode head = new ListNode(0);
        ListNode curr = head;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        if (l1 != null) {
            curr.next = l1;
        }
        if (l2 != null) {
            curr.next = l2;
        }
        return head.next;
    }

}
