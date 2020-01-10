package sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortDemo {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>(){{
            add("wlk");
            add("xxx");
            add("yyy");
            add("zzz");
        }};
        SortDemo sortDemo = new SortDemo();
        sortDemo.jdk8Sort(list1);
        System.out.println(list1);

    }

    public void jdk7Sort(List<String> list){
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public void jdk8Sort(List<String> list){
        Collections.sort(list, (o1, o2) -> o1.compareTo(o2));
    }
}
