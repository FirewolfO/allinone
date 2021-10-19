package 链表;

import utils.LinkUtil;
import utils.LinkUtil.ListNode;

import java.util.List;

/**
 * 25. K 个一组翻转链表
 * 给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
 * <p>
 * k 是一个正整数，它的值小于或等于链表的长度。
 * <p>
 * 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 * <p>
 * 进阶：
 * <p>
 * 你可以设计一个只使用常数额外空间的算法来解决此问题吗？
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 */
public class K个一组翻转链表_025 {

    public static void main(String[] args) {
        ListNode n = LinkUtil.buildLink(1, 2, 3, 4, 5);
        System.out.println(new K个一组翻转链表_025().reverseKGroup(n, 3));
    }

    public ListNode reverseKGroup(ListNode head, int k) {

        ListNode res = new ListNode();
        ListNode t = res;
        ListNode current = head;
        ListNode tail = head;
        int count = 0;
        while (head != null) {
            count++;
            tail = head;
            if (count == k) {
                ListNode nextHead = head.next;
                ListNode[] headTail = reverseLink(current, tail);
                t.next = headTail[0]; // 连接头，连接尾部
                t = headTail[1];
                count = 0;
                current = nextHead;
                head = nextHead;
                continue;
            }
            head = head.next;
        }
        if (count != 0) {
            t.next = current;
        }
        return res.next;
    }

    /**
     * @param head 需要被反转的链表的头节点
     * @param tail 下一段链表的头节点
     * @return 反转后的头节点
     */
    public ListNode[] reverseLink(ListNode head, ListNode tail) {
        ListNode t = head;
        ListNode cur = head;
        ListNode empty = new ListNode();
        ListNode nextHead = tail.next;
        while (cur != nextHead) {
            ListNode curNext = cur.next;
            cur.next = empty.next;
            empty.next = cur;
            cur = curNext;
        }
        ListNode[] headTail = new ListNode[2];
        headTail[0] = empty.next; //头
        t.next = null;
        headTail[1] = t;
        return headTail;
    }
}
