package 双指针;

import utils.LinkUtil;

import static utils.LinkUtil.ListNode;

/**
 * 2. 两个相加
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * <p>
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * <p>
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class 两数相加 {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode root = new ListNode();
        ListNode p = root;
        int next = 0;
        while (l1 != null || l2 != null) {
            int total = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + next;
            next = total / 10;
            p.next = new ListNode(total % 10);
            p = p.next;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (next != 0) {
            while (l1 != null) {
                int n = l1.val + next;
                p.next = new ListNode(n % 10);
                p = p.next;
                next = n / 10;
                l1 = l1.next;
            }

            while (l2 != null) {
                int n = l2.val + next;
                p.next = new ListNode(n % 10);
                p = p.next;
                next = n / 10;
                l2 = l2.next;
            }
        }
        if (next != 0) {
            p.next = new ListNode(next);
        }
        return root.next;
    }
}