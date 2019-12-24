package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        /*String s = "爱国;民主;敬业;富强";
        String str = s.replaceAll("([,.;!\\s]+)","-$1-");
        System.out.println(str);*/
        RegexTest regexTest = new RegexTest();
        String str = "leetcodeee";
        System.out.println(regexTest.totalNum(str));
    }

    /**
     * 统计末尾出现e的次数
     */
    public int totalNum(String str){
        Pattern pattern = Pattern.compile("(\\w*?)(e*)");
        Matcher matcher = pattern.matcher(str);
        if(matcher.matches()){
            System.out.println(111);
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            return matcher.group(2).length();
        }
        return 0;
    }
}
