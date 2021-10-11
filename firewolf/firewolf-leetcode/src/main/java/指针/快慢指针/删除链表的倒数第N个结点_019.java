package 指针.快慢指针;

import utils.LinkUtil;

import static utils.LinkUtil.ListNode;

/**
 * 19. 删除链表的倒数第 N 个结点
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 * <p>
 * 进阶：你能尝试使用一趟扫描实现吗？
 */
public class 删除链表的倒数第N个结点_019 {
    public static void main(String[] args) {
        ListNode<Integer> head = LinkUtil.buildLink(new int[]{1, 2});
        ListNode listNode = new 删除链表的倒数第N个结点_019().removeNthFromEnd(head, 3);
        System.out.println(listNode);
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode empty = new ListNode(0); // 创建一个虚拟节点
        empty.next = head;
        ListNode slow = empty;
        ListNode fast = empty;
        int step = 0; //
        while (fast.next != null) {
            fast = fast.next; // 移动快指针
            if (step == n) { // 当长度够了之后，慢指针也开始移动
                slow = slow.next;
            } else {
                step++; // 累计长度，看是否达到了n个节点
            }
        }
        if (step == n) { //链表长度不小于n，删除节点
            slow.next = slow.next.next;
        }
        return empty.next;
    }
}
