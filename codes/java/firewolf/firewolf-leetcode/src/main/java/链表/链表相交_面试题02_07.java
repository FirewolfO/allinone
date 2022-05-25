package 链表;

import utils.LinkUtil.*;

public class 链表相交_面试题02_07 {

    public static void main(String[] args) {
        // [1,2,3,4,5]
        // [2,4,5]
        ListNode<Integer> a1 = new ListNode<>(1);
        ListNode<Integer> a2 = new ListNode<>(2);
        ListNode<Integer> a3 = new ListNode<>(3);
        ListNode<Integer> a4 = new ListNode<>(4);
        ListNode<Integer> a5 = new ListNode<>(5);
        a1.next = a2;
        a2.next = a3;
        a3.next = a4;
        a4.next = a5;

        ListNode<Integer> b1 = new ListNode<>(2);
        b1.next = a4;

        ListNode intersectionNode = new 链表相交_面试题02_07().getIntersectionNode(a1, b1);
        System.out.println(intersectionNode.val);

    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode pA = headA;
        ListNode pB = headB;
        int lenA = 0;
        int lenB = 0;
        while (pA != null) {
            lenA++;
            pA = pA.next;
        }
        while (pB != null) {
            lenB++;
            pB = pB.next;
        }
        pA = headA;
        pB = headB;
        int subLen = Math.abs(lenA - lenB);
        if (lenA > lenB) {
            while (subLen > 0) {
                pA = pA.next;
                subLen--;
            }
        }
        if (lenA < lenB) {
            while (subLen > 0) {
                pB = pB.next;
                subLen--;
            }
        }
        while (pA != null) {
            if (pA == pB) {
                return pA;
            }
            pA = pA.next;
            pB = pB.next;
        }
        return null;
    }
}
