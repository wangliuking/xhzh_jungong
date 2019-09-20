package leetcode;

/**
 * 字符串最大回文子串
 */
public class Chapter5 {
    public static void main(String[] args) {
        Chapter5 chapter5 = new Chapter5();
        String res = chapter5.longestPalindrome1("babad");
        System.out.println(res);
    }

    public String longestPalindrome(String s) {
        int len = 0, n = s.length();
        String finalStr = "";
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                String tempStr;
                if (j == n) {
                    tempStr = s.substring(i);
                } else {
                    tempStr = s.substring(i, j + 1);
                }
                if (tempStr.length() > len) {
                    String newStr = new StringBuilder(tempStr).reverse().toString();
                    if (tempStr.equals(newStr)) {
                        len = tempStr.length();
                        finalStr = tempStr;
                    }
                }
            }
        }
        return finalStr;
    }

    public String longestPalindrome1(String s) {
        if (s == null || s.length() < 1) return "";
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    public int expandAroundCenter(String s, int left, int right) {
        int L = left,R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }
}
