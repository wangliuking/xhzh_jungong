package leetcode;

import java.util.*;

/**
 * 两数之和
 */
public class Chapter1 {
    public static void main(String[] args) {
        Chapter1 chapter1 = new Chapter1();
        int[] a = {1, 2, 3, 4, 5};
        int b = 6;
        int[] res = chapter1.twoSumHash1(a, b);
        System.out.println(Arrays.toString(res));
    }

    /**
     * 简单暴力
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        List<int[]> res = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    /**
     * 两次循环，利用哈希表空间换时间
     */
    public int[] twoSumHash2(int[] nums, int target){
        Map<Integer,Integer> map = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            map.put(nums[i],i);
        }
        for(int i=0;i<nums.length;i++){
            int key = target-nums[i];
            if(map.containsKey(key) && map.get(key) != i){
                return new int[]{i,map.get(key)};
            }
        }
        return new int[]{};
    }

    /**
     * 一遍哈希表，遍历的同时进行查找
     */
    public int[] twoSumHash1(int[] nums, int target){
        Map<Integer,Integer> map = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            int key = target-nums[i];
            if(map.containsKey(key) && map.get(key) != i){
                return new int[]{i,map.get(key)};
            }
            map.put(nums[i],i);
        }
        return new int[]{};
    }
}
