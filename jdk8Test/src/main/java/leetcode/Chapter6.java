package leetcode;

import java.util.LinkedList;
import java.util.List;

/**
 * z字形变换
 */
public class Chapter6 {
    public static void main(String[] args) {
        Chapter6 chapter6 = new Chapter6();
        String res = chapter6.convert("LEETCODEISHIRING",3);
        System.out.println(res);
    }

    public String convert(String s, int numRows) {
        if (numRows == 1) return s;
        List<StringBuilder> strList = new LinkedList<>();
        for (int i = 0; i < Math.min(s.length(), numRows); i++) {
            strList.add(new StringBuilder());
        }
        int curRow = 0;
        boolean goingDown = false;
        for (char c : s.toCharArray()) {
            strList.get(curRow).append(c);
            if (curRow == 0 || curRow == numRows - 1) {
                goingDown = !goingDown;
            }
            curRow += goingDown ? 1 : -1;
        }
        StringBuilder res = new StringBuilder();
        for(StringBuilder temp : strList){
            res.append(temp);
        }
        return res.toString();
    }
}
