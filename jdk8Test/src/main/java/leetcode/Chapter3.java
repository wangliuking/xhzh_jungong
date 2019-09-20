package leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Chapter3 {
    public static void main(String[] args) {
        Chapter3 chapter3 = new Chapter3();
        int l = chapter3.lengthOfLongestSubstring2("abcdefef");
        System.out.println(l);
    }

    /**
     * 暴力法
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int len = 0;
        for (int i = 0; i <= s.length(); i++) {
            for (int j = i + 1; j <= s.length(); j++) {
                if (allUnique(s, i, j)) {
                    len = Math.max(j - i, len);
                }
            }
        }
        return len;
    }

    public static boolean allUnique(String str, int start, int end) {
        Set<Character> set = new HashSet<>();
        for (int i = start; i < end; i++) {
            Character c = str.charAt(i);
            if (set.contains(c)) {
                return false;
            }
            set.add(c);
        }
        return true;
    }

    /**
     * 滑动窗口加hashset
     */
    public int lengthOfLongestSubstring1(String s) {
        Set<Character> set = new HashSet<>();
        int len = 0, i = 0, j = 0;
        while (i < s.length() && j < s.length()) {
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                len = Math.max(len, j - i);
            } else {
                set.remove(s.charAt(i++));
            }
        }
        return len;
    }

    /**
     * 优化滑动窗口+map
     */
    public int lengthOfLongestSubstring2(String s) {
        int len = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0, j = 0; j < s.length(); j++) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(map.get(s.charAt(j)), i);
            }
            len = Math.max(len, j - i + 1);
            map.put(s.charAt(j), j + 1);
        }
        return len;
    }


}
