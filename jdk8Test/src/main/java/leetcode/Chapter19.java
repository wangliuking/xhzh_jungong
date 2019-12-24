package leetcode;

import java.util.List;

public class Chapter19 {

    public static void main(String[] args) {

    }

    public ListNode removeNthFromEndTest(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        int length = 0;
        ListNode top1 = dummy;
        ListNode top2 = dummy;
        while (top1 != null) {
            if (length == n + 1) {
                top2 = top2.next;
                top1 = top1.next;
            } else {
                length++;
                top1 = top1.next;
            }
        }
        top2.next = top2.next.next;
        return dummy.next;
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        int length = 0;
        ListNode top = head;
        while (top != null) {
            length++;
            top = top.next;
        }
        length -= n;
        ListNode temp = dummy;
        while (length > 0) {
            length--;
            temp = temp.next;
        }
        temp.next = temp.next.next;
        return dummy.next;
    }
}
