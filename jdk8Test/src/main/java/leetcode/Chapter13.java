package leetcode;

import java.util.HashMap;
import java.util.Map;

public class Chapter13 {
    public static void main(String[] args) {
        Chapter13 chapter13 = new Chapter13();
        System.out.println(chapter13.romanToInt("MCMXCIV"));
    }

    public int romanToInt(String s) {
        if(s == null || "".equals(s)) return 0;
        String[] strArr = {"IV", "IX", "XL", "XC", "CD", "CM"};
        int[] val = {-2, -2, -20, -20, -200, -200};
        Map<Character, Integer> map = new HashMap<Character, Integer>() {{
            put('I', 1);
            put('V', 5);
            put('X', 10);
            put('L', 50);
            put('C', 100);
            put('D', 500);
            put('M', 1000);
        }};
        char[] arr = s.toCharArray();
        int res = 0;
        for (int i = 0; i < arr.length; i++) {
            res += map.get(arr[i]);
        }
        for (int i = 0; i < strArr.length; i++) {
            if (s.contains(strArr[i])) {
                res += val[i];
            }
        }
        return res;
    }
}
