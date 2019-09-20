package leetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串转整数
 */
public class Chapter8 {
    public static void main(String[] args) {
        Chapter8 chapter8 = new Chapter8();
        int res = chapter8.myAtoi("-23456666666666666asdfe1");
        System.out.println(res);
    }

    public int myAtoi(String str) {
        str = str.trim();
        Pattern p = Pattern.compile("^[\\+\\-]?\\d+");
        Matcher m = p.matcher(str);
        int value = 0;
        if(m.find()){
            System.out.println("m : "+m);
            try{
                value = Integer.parseInt(str.substring(m.start(),m.end()));
                return value;
            }catch (Exception e){
                return str.substring(m.start(),m.start()+1).equals("-")?Integer.MIN_VALUE:Integer.MAX_VALUE;
            }

        }
        return value;
    }
}
