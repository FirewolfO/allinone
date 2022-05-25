package 链表;

import utils.LinkUtil;
import utils.LinkUtil.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Definition for singly-linked list.
 * class ListNode {
 * int val;
 * ListNode next;
 * ListNode(int x) {
 * val = x;
 * next = null;
 * }
 * }
 */
public class 环形链表II_142 {

    public static void main(String[] args) {
        ListNode<Integer> integerListNode = LinkUtil.buildLink(1, 2, 0, 4);

        ListNode node = LinkUtil.findNode(integerListNode, 2, 1);

        ListNode tail = LinkUtil.findNode(integerListNode, 4, 1);

        tail.next = node;

        ListNode listNode = new 环形链表II_142().detectCycle(integerListNode);
        System.out.println(listNode.val);
    }

    public ListNode detectCycle(ListNode head) {
        int pos = 0;
        Map<ListNode, Integer> posMap = new HashMap<>();
        while (head != null) {
            if (posMap.containsKey(head)) {
                return head;
            } else {
                posMap.put(head, pos);
            }
            pos++;
            head = head.next;
        }
        return null;
    }
}