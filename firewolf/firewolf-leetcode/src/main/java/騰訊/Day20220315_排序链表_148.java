package 騰訊;

import java.util.concurrent.TimeUnit;

import static utils.LinkUtil.*;

class Day20220315_排序链表_148 {

    public static void main(String[] args) {
        // 插入排序
        ListNode<Integer> node1 = buildLink(4, 2, 1, 3);
        ListNode listNode = new Day20220315_排序链表_148().sortList(node1);
        System.out.println(listNode);

        //归并
        ListNode<Integer> node2 = buildLink(4, 2, 1, 3);
        ListNode listNode2 = new Day20220315_排序链表_148().sortListMerger(node2);
        System.out.println(listNode2);

        // 优化归并排序
        ListNode<Integer> node3 = buildLink(4, 2, 1, 3);
        ListNode listNode3 = new Day20220315_排序链表_148().sortListMerger2(node3);
        System.out.println(listNode3);
    }

    /*******************插入排序**********************/
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

    /******************归并,自顶向下********************/
    public ListNode sortListMerger(ListNode<Integer> head) {
        ListNode<Integer> pointer = head;
        int linkLen = 0;
        while (pointer != null) {
            linkLen++;
            pointer = pointer.next;
        }
        return _sortListMerger(head, linkLen);
    }

    private ListNode _sortListMerger(ListNode<Integer> node, int len) {
        if (len <= 1) {
            return node;
        }
        int halfLen = len / 2; // 一半长度
        ListNode<Integer> secondLink = node;
        ListNode<Integer> fistTail = node;
        int firstLen = 0;
        while (firstLen < halfLen && secondLink != null) {
            firstLen++;
            fistTail = secondLink;
            secondLink = secondLink.next;
        }
        fistTail.next = null; // 截断
        ListNode<Integer> node11 = _sortListMerger(node, halfLen);
        ListNode<Integer> node12 = _sortListMerger(secondLink, len - halfLen);
        return mergeSortedList(node11, node12);
    }


    /*** 合并有序链表*/
    private ListNode mergeSortedList(ListNode<Integer> node1, ListNode<Integer> node2) {

        ListNode<Integer> empty = new ListNode<>();
        ListNode<Integer> pointer = empty;
        ListNode<Integer> smallerNode;
        while (node1 != null && node2 != null) {
            if (node1.val < node2.val) { //取小的
                smallerNode = node1;
                node1 = node1.next;
            } else {
                smallerNode = node2;
                node2 = node2.next;
            }
            smallerNode.next = null;
            pointer.next = smallerNode;
            pointer = pointer.next;
        }
        ListNode remainingNode = node1 == null ? node2 : node1; // 剩余的节点
        pointer.next = remainingNode;
        return empty.next;
    }

    /***********************优化，自顶向下，去掉长度扫描************************/
    public ListNode sortListMerger2(ListNode<Integer> head) {
        return _sortListMerger2(head, null);
    }

    //对头尾节点之间的元素进行排序，不包括尾结点
    public ListNode _sortListMerger2(ListNode<Integer> head, ListNode<Integer> tail) {
        if (head == null) return null;
        if (head.next == tail) { //不包含尾结点
            head.next = null;
            return head;
        }
        if (head == tail || head == null) {
            return head;
        }
        // 使用快慢指针分断链表，快的走两步，慢的走一步
        ListNode<Integer> slow = head;
        ListNode<Integer> fast = head;
        while (fast != tail) {
            slow = slow.next;
            fast = fast.next;
            if (fast != tail) { // 多走一步
                fast = fast.next;
            }
        }

        ListNode<Integer> node1 = _sortListMerger2(head, slow); // 不包含slow
        ListNode<Integer> node2 = _sortListMerger2(slow, tail);
        return mergeSortedList(node1, node2);
    }
}