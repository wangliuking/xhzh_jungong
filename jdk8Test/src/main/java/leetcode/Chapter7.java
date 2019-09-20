package leetcode;

import java.util.regex.Pattern;

/**
 * 整数反转
 */
public class Chapter7 {
    public static void main(String[] args) {
        Chapter7 chapter7 = new Chapter7();
        int res = chapter7.reverse1(123);
        System.out.println(res);
    }

    /**
     * 字符串反转
     *
     * @param x
     * @return
     */
    public int reverse(int x) {
        if (x < -Math.pow(2, 31) || x > Math.pow(2, 31)) return 0;
        String str = x + "";
        StringBuilder s = new StringBuilder();
        String temp = s.append(str.charAt(0)).toString();
        if (temp.matches("^[0-9]*$")) {
            //整数
            long l = Long.parseLong(new StringBuilder(str).reverse().toString());
            return l < -Math.pow(2, 31) || l > Math.pow(2, 31) - 1 ? 0 : (int) l;
        } else {
            //有符号位
            long l = Long.parseLong(new StringBuilder(str.substring(1)).reverse().toString());
            return l < -Math.pow(2, 31) || l > Math.pow(2, 31) - 1 ? 0 : -(int) l;
        }
    }

    /**
     * 弹入推出数字
     */
    public int reverse1(int x) {
        int res = 0;
        while (x != 0) {
            int pop = x % 10;
            x = x / 10;
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }
            res = res * 10 + pop;
        }
        return res;
    }
}
