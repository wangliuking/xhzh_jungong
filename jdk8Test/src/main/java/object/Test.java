package object;

import java.util.AbstractList;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        Base a = new Base();
        System.out.println(a.baseMethod1());
        Base b = new Child("a","b");
        System.out.println(b.baseMethod1());


    }
}

class Base{
    public String baseMethod1(){
        return "baseMethod1";
    }

    public String baseMethod2(){
        return "baseMethod2";
    }
}

class Child extends Base{
    private String name;
    private String val;
    Child(String name,String val){
        this.name = name;
        this.val = val;
    }

    public String baseMethod1(){
        return "Override_baseMethod1";
    }

    public String childMethod(){
        return "childMethod";
    }
}

class Children extends Base{
    public String baseMethod1(){
        return "Override_baseMethod1";
    }
}
