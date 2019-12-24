package leetcode;

import java.util.*;

public class Chapter17 {
    public static void main(String[] args) {
        Chapter17 chapter17 = new Chapter17();
        System.out.println(chapter17.letterCombinationsTest("234"));
    }

    Map<String, String> map = new HashMap<String, String>() {{
        put("2", "abc");
        put("3", "def");
        put("4", "ghi");
        put("5", "jkl");
        put("6", "mno");
        put("7", "pqrs");
        put("8", "tuv");
        put("9", "wxyz");
    }};

    List<String> resList = new ArrayList<>();

    public void backTrack(String combination, String digits) {
        if (digits.length() == 0) {
            resList.add(combination);
        } else {
            String d = digits.substring(0, 1);
            String words = map.get(d);
            for (int i = 0; i < words.length(); i++) {
                String word = words.substring(i, i + 1);
                backTrack(combination + word, digits.substring(1));
            }
        }
    }

    public List<String> letterCombinationsTest(String digits) {
        if (digits.length() != 0)
            backTrack("", digits);
        return resList;
    }

    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if ("".equals(digits)) return res;
        res.add("");
        Map<Character, List<String>> map = new HashMap<>();
        map.put('2', Arrays.asList("a", "b", "c"));
        map.put('3', Arrays.asList("d", "e", "f"));
        map.put('4', Arrays.asList("g", "h", "i"));
        map.put('5', Arrays.asList("j", "k", "l"));
        map.put('6', Arrays.asList("m", "n", "o"));
        map.put('7', Arrays.asList("p", "q", "r", "s"));
        map.put('8', Arrays.asList("t", "u", "v"));
        map.put('9', Arrays.asList("w", "x", "y", "z"));
        for (int i = 0; i < digits.length(); i++) {
            char c = digits.charAt(i);
            List<String> l = map.get(c);
            List<String> temp = new ArrayList<>();
            for (int j = 0; j < l.size(); j++) {
                for (int z = 0; z < res.size(); z++) {
                    temp.add(res.get(z) + l.get(j));
                }
            }
            res = temp;
        }
        return res;
    }
}
