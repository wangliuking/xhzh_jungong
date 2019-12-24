package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chapter15 {
    public static void main(String[] args) {
        Chapter15 chapter15 = new Chapter15();
        int[] nums = {-1,0,1,2,-1,-4};
        List<List<Integer>> res = chapter15.threeSumTest(nums);
        for (List<Integer> l : res) {
            System.out.println(l);
        }

    }

    public List<List<Integer>> threeSum(int[] nums) {
        boolean bl = true;
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                for (int z = j + 1; z < nums.length; z++) {
                    int sum = nums[i] + nums[j] + nums[z];
                    if (sum == 0) {
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[z]);
                        if (nums[i] == 0 && nums[j] == 0 && bl) {
                            res.add(list);
                            bl = false;
                        }

                        boolean status = true;

                        for (int x = 0; x < res.size(); x++) {
                            List<Integer> l = res.get(x);
                            int a = l.get(0);
                            int b = l.get(1);
                            int c = l.get(2);
                            boolean numBoolean1 = nums[i] != a && nums[i] != b && nums[i] != c;
                            boolean numBoolean2 = nums[j] != a && nums[j] != b && nums[j] != c;
                            boolean numBoolean3 = nums[z] != a && nums[z] != b && nums[z] != c;
                            if (!numBoolean1 && !numBoolean2 && !numBoolean3) {
                                status = false;
                            }
                        }

                        if (status) {
                            res.add(list);
                        }
                    }
                }
            }
        }
        return res;
    }

    public List<List<Integer>> threeSumTest(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) return res;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int L = i + 1;
            int R = nums.length-1;
            if (nums[i] > 0) break;
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            while (L < R) {
                int sum = nums[i] + nums[L] + nums[R];
                if (sum == 0) {
                    res.add(Arrays.asList(nums[i], nums[L], nums[R]));
                    while(L<R && nums[L+1] == nums[L]) L++;
                    while(L<R && nums[R-1] == nums[R]) R--;
                    L++;
                    R--;
                }else if(sum < 0){
                    L++;
                }else if(sum > 0){
                    R--;
                }
            }
        }
        return res;
    }
}
