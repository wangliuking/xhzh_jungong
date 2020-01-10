package lambda;

public class LambdaDemo {
    private static String val = "Good!";

    public static void main(String[] args) {
        MathOperation add = (a, b) -> {
            if (a < b) {
                return a + b;
            }
            return 0;
        };
        MathOperation sub = (a, b) -> {
            if (a < b) {
                return a - b;
            }
            return 0;
        };
        LambdaDemo lambdaDemo = new LambdaDemo();
        int res = lambdaDemo.operate(2, 1, sub);
        System.out.println(res);

        LambdaHello lambdaHello = (mes) -> {
            System.out.println(mes);
            System.out.println(val);
        };
        val = "zzz";
        lambdaHello.sayHello("wlk");

    }

    interface MathOperation {
        int operation(int a, int b);
    }

    interface LambdaHello {
        void sayHello(String mes);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }
}
