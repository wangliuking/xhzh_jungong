package leetcode;

import java.util.List;

/**
 * 两数相加，链表结构
 */
public class Chapter2 {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(9);
        l1.next = new ListNode(9);
        //l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(1);
        //l2.next = new ListNode(1);
        //l2.next.next = new ListNode(2);

        Chapter2 chapter2 = new Chapter2();
        ListNode res = chapter2.addTwoNumbers(l1,l2);
        while(res != null){
            System.out.println(res.val);
            res = res.next;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode p = l1, q = l2, curr = dummyHead;
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = x + y + carry;
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (carry > 0) {
            curr.next = new ListNode(carry);
        }
        return dummyHead.next;
    }
}

class ListNode {
    int val;
    ListNode next;   // 下一个链表对象

    ListNode(int x) {
        val = x;
    }  //赋值链表的值
}
