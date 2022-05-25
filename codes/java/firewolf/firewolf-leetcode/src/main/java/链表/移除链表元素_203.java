package 链表;

import utils.LinkUtil;
import utils.LinkUtil.*;

public class 移除链表元素_203 {

    public static void main(String[] args) {
        ListNode<Integer> integerListNode = LinkUtil.buildLink(1, 2, 6, 3, 4, 5, 6);
        ListNode listNode = new 移除链表元素_203().removeElements(integerListNode, 6);
        System.out.println(listNode);
    }

    public ListNode removeElements(ListNode<Integer> head, int val) {
        ListNode empty = new ListNode();
        empty.next = head;
        ListNode<Integer> fast = head;
        ListNode<Integer> slow = empty;
        while (fast != null) {
            if (fast.val == val) {
                slow.next = fast.next;
                fast = fast.next;
                continue;
            }
            fast = fast.next;
            slow = slow.next;
        }
        return empty.next;
    }
}