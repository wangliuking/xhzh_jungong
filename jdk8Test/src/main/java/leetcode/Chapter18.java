package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chapter18 {
    public static void main(String[] args) {
        Chapter18 chapter18 = new Chapter18();
        int[] nums = {1, 0, -1, 0, -2, 2};
        int target = 0;
        List<List<Integer>> list = chapter18.fourSum(nums, target);
        for (List<Integer> l : list) {
            System.out.println(l);
        }
    }

    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length < 4) return res;
        if (nums.length == 4 && nums[0] + nums[1] + nums[2] + nums[3] == target) {
            res.add(Arrays.asList(nums[0], nums[1], nums[2], nums[3]));
            return res;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int min1 = nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3];
            if (min1 > target) break;
            int max1 = nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1];
            if (max1 < target) continue;
            for (int j = i + 1; j < nums.length - 2; j++) {
                int L = j + 1;
                int R = nums.length - 1;
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                int min2 = nums[i] + nums[j] + nums[L] + nums[L + 1];
                if (min2 > target) break;
                int max2 = nums[i] + nums[j] + nums[nums.length - 2] + nums[nums.length - 1];
                if (max2 < target) continue;
                while (L < R) {
                    int sum = nums[i] + nums[j] + nums[L] + nums[R];
                    if (sum == target) {
                        res.add(Arrays.asList(nums[i], nums[j], nums[L], nums[R]));
                        while (L < R && nums[L + 1] == nums[L]) L++;
                        while (L < R && nums[R - 1] == nums[R]) R--;
                        L++;
                        R--;
                    } else if (sum < target) {
                        L++;
                    } else if (sum > target) {
                        R--;
                    }
                }
            }
        }
        return res;
    }
}
