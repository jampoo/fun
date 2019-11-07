import java.util.Arrays;

public class DianMian2 {

    public static int findLowestUnassigned(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        Arrays.sort(nums);

        for (int i=0; i<nums.length; i++) {
            if (nums[i] != i+1) {
                return i+1;
            }
        }
        return nums.length + 1;
    }


}
