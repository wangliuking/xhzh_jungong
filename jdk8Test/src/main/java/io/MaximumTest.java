package io;

import java.util.List;

public class MaximumTest {

    public static void main(String[] args) {
        Model model1 = new Model("aaa",111);
        Model model2 = new Model("ccc",222);
        Model model3 = new Model("bbb",333);
        System.out.println(maximum(model1, model2, model3));
    }

    public static <T extends Comparable<T>> T maximum(T a, T b, T c) {
        T max = a;
        if (b.compareTo(max) > 0) {
            max = b;
        }
        if (c.compareTo(max) > 0) {
            max = c;
        }
        return max;
    }

    public static void getUperNumber(List<? extends Comparable<?>> data) {
        System.out.println("data :" + data.get(0));
    }

}

class Model implements Comparable<Model> {
    private String name;
    private int val;

    public Model(String name,int val){
        this.name = name;
        this.val = val;
    }

    @Override
    public int compareTo(Model model) {
        return this.val > model.val ? 1 : (this.val == model.val ? 0 : -1);
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", val=" + val +
                '}';
    }
}
