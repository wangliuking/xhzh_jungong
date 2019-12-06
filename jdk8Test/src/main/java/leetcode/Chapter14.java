package leetcode;

public class Chapter14 {
    public static void main(String[] args) {
        Chapter14 chapter14 = new Chapter14();
        String[] arr = {"","b"};
        System.out.println(chapter14.longestCommonPrefix(arr));
    }

    public String longestCommonPrefix(String[] strs) {
        int minLength = 0;
        for (int i = 0; i < strs.length; i++) {
            if (i == 0 || strs[i].length() < minLength) {
                minLength = strs[i].length();
            }
        }
        if (minLength == 0) return "";
        String res = "";
        for (int i = 0; i < minLength; i++) {
            for (int j = 0; j < strs.length; j++) {
                String temp = strs[j];
                if (strs[j].length() - 1 > i) {
                    temp = strs[j].substring(0, i + 1);
                }
                if (temp.equals(res) || j == 0) {
                    res = temp;
                } else {
                    return res.substring(0, res.length() - 1);
                }
            }
        }
        return res;
    }
}
