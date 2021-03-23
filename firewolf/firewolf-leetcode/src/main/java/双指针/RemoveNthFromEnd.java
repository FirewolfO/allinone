package 双指针;

/**
 * Description: 19. 删除链表的倒数第 N 个结点
 * https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/23 11:58 上午
 */

import utils.LinkUtil;

import static utils.LinkUtil.*;


public class RemoveNthFromEnd {

    public static void main(String[] args) {
        ListNode l = LinkUtil.buildLink(new int[]{1});
        System.out.println(new RemoveNthFromEnd().removeNthFromEnd(l,2));
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode duoyu = new ListNode(0, head);
        ListNode first = duoyu;
        ListNode second = duoyu;
        int i = 0;
        while (i <= n && first != null) {
            first = first.next;
            i++;
        }

        while (first != null) {
            first = first.next;
            second = second.next;
        }
        if (second != null) {
            second.next = second.next.next;
        }
        return duoyu.next;
    }
}
