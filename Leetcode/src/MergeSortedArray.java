
public class MergeSortedArray {

    public MergeSortedArray(){}

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int a = m -1;
        int b = m + n -1;
        int c = n - 1;
       while(c >= 0){
           if(b >= 0 && a>=0 &&  nums1[a]< nums2[c]){
               nums1[b] = nums2[c];
               b--;
               c--;
           }else if(a < 0){
               nums1[c] = nums2[c];
               c--;
           }
           else{
               nums1[b] = nums1[a];
               nums1[a] = 0;
               b--;
               a--;
           }
       }

        for(int i = 0; i < m+n; i++){
            System.out.println(nums1[i]);
        }
    }
}
