package leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Chapter20 {
    public static void main(String[] args) {
        //Chapter20 chapter20 = new Chapter20();
        //System.out.println(chapter20.isValid("]"));
        String a = "\u4f18\u79c0";
        System.out.println(a);
    }

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> mapping = new HashMap<Character, Character>() {{
            put('(', ')');
            put('[', ']');
            put('{', '}');
        }};
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!mapping.containsKey(c)) {
                char topEl = stack.isEmpty() ? '*' : stack.pop();
                char temp = mapping.get(topEl) == null ? '$' : mapping.get(topEl);
                if (temp != c) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }
}
