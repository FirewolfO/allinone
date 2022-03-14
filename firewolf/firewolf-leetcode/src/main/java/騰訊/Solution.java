package 騰訊;

import static utils.LinkUtil.*;

class Solution {

    public static void main(String[] args) {
        ListNode<Integer> integerListNode = buildLink(4, 2, 1, 3);
        ListNode listNode = new Solution().sortList(integerListNode);
        System.out.println(listNode);
    }

    public ListNode sortList(ListNode<Integer> head) {
        ListNode<Integer> empty = new ListNode();

        while (head != null) {
            ListNode<Integer> tmp = head.next;
            ListNode<Integer> point = empty;
            while (point.next != null && point.next.val < head.val) {
                point = point.next;
            }
            head.next = point.next;
            point.next = head;
            head = tmp;
        }
        return empty.next;
    }
}