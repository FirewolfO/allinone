package 链表;

class 设计链表_707 {

    private ListNode head;
    private int size = 0;

    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public 设计链表_707() {
        head = new ListNode(0);
        size = 0;
    }
    
    public int get(int index) {
        if(index < 0 || index >= size ){
            return -1;
        }
        ListNode p = head.next;
        int c = 0;
        while(p != null){
            if(c == index){
                return p.val;
            }
            c++;
            p = p.next;
        }
        return -1;
    }
    
    public void addAtHead(int val) {
        ListNode node = new ListNode(val);
        node.next = head.next;
        head.next = node;
        size++;
    }
    
    public void addAtTail(int val) {
        ListNode node = new ListNode(val);
        ListNode p = head;
        while(p.next != null){
            p = p.next;
        }
        p.next = node;
        size++;
    }
    
    public void addAtIndex(int index, int val) {
      
        ListNode node = new ListNode(val);
        if(index <= 0){
            addAtHead(val);
            return;
        }
        if(index == size){
            addAtTail(val);
            return;
        }
        if(index > size ){
            return;
        }
        
        ListNode p = head;
        int c = 0;
        while(p != null){
            if(c == index){
                node.next = p.next;
                p.next = node;
                size++;
                return;
            }
            c++;
            p = p.next;
        }
    }
    
    public void deleteAtIndex(int index) {
        if(index <0 || index >= size){
            return;
        }

        ListNode p = head;
        int c = 0;
        while(p!=null){
            if(c == index){
                if(p.next != null){
                    p.next = p.next.next;
                    size--;
                }
            }
            c++;
            p = p.next;
        }
    }
}

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * MyLinkedList obj = new MyLinkedList();
 * int param_1 = obj.get(index);
 * obj.addAtHead(val);
 * obj.addAtTail(val);
 * obj.addAtIndex(index,val);
 * obj.deleteAtIndex(index);
 */