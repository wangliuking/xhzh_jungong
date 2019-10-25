package leetcode;

public class Chapter11 {
    public static void main(String[] args) {
        Chapter11 chapter11 = new Chapter11();

    }

    public int maxArea(int[] height) {
        int length = height.length;
        int res = 0;
        for(int i=0;i<length;i++){
            for(int j=i+1;j<length;j++){
                int high = Math.min(height[i],height[j]);
                int size = high * (j-i);
                if(size > res){
                    res = size;
                }
            }
        }
        return res;
    }
}
