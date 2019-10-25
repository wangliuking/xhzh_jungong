package leetcode;

public class Chapter9 {
    public static void main(String[] args) {
        Chapter9 chapter9 = new Chapter9();
        boolean b = chapter9.isPalindrome(-100);
        System.out.println(b);
    }

    public boolean isPalindrome(int x) {
        String str = x+"";
        String finalStr = new StringBuilder(str).reverse().toString();
        if(str.equals(finalStr)){
            return true;
        }else{
            return false;
        }
    }
}
