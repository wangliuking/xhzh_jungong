package leetcode;

import java.util.Arrays;

public class Chapter16 {
    public static void main(String[] args) {
        Chapter16 chapter16 = new Chapter16();
        int[] nums = {0, 2, 1, -3};
        int target = 1;
        System.out.println(chapter16.threeSumClosest(nums, target));
    }

    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int res = nums[0] + nums[1] + nums[nums.length - 1];
        int close = Math.abs(res - target);
        for (int i = 0; i < nums.length; i++) {
            int L = i + 1;
            int R = nums.length - 1;
            while (L < R) {
                int sum = nums[i] + nums[L] + nums[R];
                if (Math.abs(sum - target) == 0) return sum;
                if (Math.abs(sum - target) <= close) {
                    res = sum;
                    close = Math.abs(sum - target);
                }
                if (sum > target) {
                    R--;
                } else {
                    L++;
                }
            }
        }
        return res;
    }
}
