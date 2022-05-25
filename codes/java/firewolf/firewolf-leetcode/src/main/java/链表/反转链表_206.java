package 链表;

import utils.LinkUtil;
import utils.LinkUtil.*;

public class 反转链表_206 {

    public static void main(String[] args) {
        ListNode<Integer> integerListNode = LinkUtil.buildLink(1, 2, 3, 4, 5);
        ListNode listNode = new 反转链表_206().reverseList(integerListNode);
        System.out.println(listNode);
    }

    public ListNode reverseList(ListNode head) {
        ListNode empty = new ListNode();

        ListNode p = head;
        while (p != null) {
            ListNode cur = p.next;
            p.next = empty.next;
            empty.next = p;
            p = cur;
        }
        return empty.next;
    }
}
