package leetcode;

import java.util.ArrayList;
import java.util.List;

public class Chapter15 {
    public static void main(String[] args) {
        Chapter15 chapter15 = new Chapter15();
        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> res = chapter15.threeSum(nums);
        for(List<Integer> l : res){
            System.out.println(l);
        }

    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        for(int i=0;i<nums.length-2;i++){
            for(int j=i+1;j<nums.length-1;j++){
                for(int z=j+1;z<nums.length;z++){
                    int sum = nums[i]+nums[j]+nums[z];
                    if(sum == 0){
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[z]);



                        res.add(list);
                    }
                }
            }
        }
        return res;
    }
}
