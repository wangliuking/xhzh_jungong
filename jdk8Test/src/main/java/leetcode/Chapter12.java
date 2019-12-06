package leetcode;

public class Chapter12 {
    public static void main(String[] args) {
        Chapter12 chapter12 = new Chapter12();
        System.out.println(chapter12.intToRomanTest(1994));
    }

    public String intToRomanTest(int num) {
        int[] intArr = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] strArr = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        String result = "";
        for (int i = 0; i < intArr.length; i++) {
            while (num >= intArr[i]) {
                result += strArr[i];
                num -= intArr[i];
            }
        }
        return result;
    }

    public String intToRoman(int num) {
        if (num < 1 || num > 3999) return "";
        switch (num) {
            case 4:
                return "IV";
            case 9:
                return "IX";
            case 40:
                return "XL";
            case 90:
                return "XC";
            case 400:
                return "CD";
            case 900:
                return "CM";
            default:
                int m = num / 1000;
                int cm = (num - m * 1000) / 900;
                int d = (num - m * 1000 - cm * 900) / 500;
                int cd = (num - m * 1000 - cm * 900 - d * 500) / 400;
                int c = (num - m * 1000 - cm * 900 - d * 500 - cd * 400) / 100;
                int xc = (num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100) / 90;
                int l = (num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100 - xc * 90) / 50;
                int xl = (num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100 - xc * 90 - l * 50) / 40;
                int x = (num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100 - xc * 90 - l * 50 - xl * 40) / 10;
                int ix = (num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100 - xc * 90 - l * 50 - xl * 40 - x * 10) / 9;
                int v = (num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100 - xc * 90 - l * 50 - xl * 40 - x * 10 - ix * 9) / 5;
                int iv = (num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100 - xc * 90 - l * 50 - xl * 40 - x * 10 - ix * 9 - v * 5) / 4;
                int i = num - m * 1000 - cm * 900 - d * 500 - cd * 400 - c * 100 - xc * 90 - l * 50 - xl * 40 - x * 10 - ix * 9 - v * 5 - iv * 4;
                return append(m, "M") + append(cm, "CM") + append(d, "D") + append(cd, "CD") + append(c, "C") +
                        append(xc, "XC") + append(l, "L") + append(xl, "XL") + append(x, "X") + append(ix, "IX") +
                        append(v, "V") + append(iv, "IV") + append(i, "I");
        }
    }

    public String append(int size, String s) {
        if (size == 0) return "";
        String str = "";
        for (int i = 0; i < size; i++) {
            str += s;
        }
        return str;
    }
}
