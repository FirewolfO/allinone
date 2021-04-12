package 链表;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode(int x) {
 * val = x;
 * next = null;
 * }
 * }
 */

import java.util.Stack;

import static utils.LinkUtil.*;

public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        Stack<ListNode> stack1 = new Stack<>();
        Stack<ListNode> stack2 = new Stack<>();

        while (headA != null) {
            stack1.push(headA);
            headA = headA.next;
        }

        while (headB != null) {
            stack2.push(headB);
            headB = headB.next;
        }

        ListNode res = null;
        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            ListNode l1 = stack1.pop();
            ListNode l2 = stack2.pop();
            if (l1.val == l2.val) {
                res = l1;
            } else {
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        ListNode l1 = buildLink(new int[]{4, 1, 8, 4, 5});
        ListNode l2 = buildLink(new int[]{5, 0, 1, 8, 4, 5});
        System.out.println(new Solution().getIntersectionNode(l1, l2));
    }
}