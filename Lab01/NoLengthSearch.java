import java.util.*;

public class NoLengthSearch {
    /**
     * Finds the position of a target in a list
     * @param list — list of type LabList<T> to search
     * @param target — target of type T to search for
     * @param <T> — type of elements in list
     * @return — position of target in list, or -1 if target is not in list
     */
    public static <T extends Comparable<? super T>>
    int find(LabList<T> list, T target){
        /**due to the nature of .createRandomList(), there is no need to check for null
        within this function, since it's impossible to init LabList with nulls or
        with an empty list (it throws an exception when bounds are invalid)
         */

        //CHECKS
        if(list.get(0) == null){return -1;}
        if(list.get(0).compareTo(target) == 0){return 0;}
        /** Due to the nature of the list, the elements are sorted in ascending order,
         * If the first element is greater than the target, the target cannot be in the list
         * So we can return -1
         * */
        if(list.get(0).compareTo(target) > 0){return -1;}

        //BODY
        int currExp = 0; //exponent size
        T curr = list.get(0); //position

        /**Implement exponentially incrementing jumps of size
         * 2^currExp until the upper bound is established
         */
        while(curr != null){
            if(curr.compareTo(target) > 0){
                break;
            }
            else{
                curr = list.get(exponent(2, ++currExp));
            }
        }

        int lower = exponent(2, currExp-1);
        int upper = exponent(2, currExp);

        /**
         * With established higher and lower bounds, we can now check if the target is in the list
         * via binary search
         */
        return binarySearch(list, target, lower, upper);
    }

    /**
     * Helper function to perform binary search on a list
     * @param list — list of type LabList<T> to search
     * @param target — target of type T to search for
     * @param lower — lower bound of type int
     * @param upper — upper bound of type int
     * @param <T> — type of elements in list
     * @return — position of target in list, or -1 if target is not in list
     */
    public static <T extends Comparable<? super T>>
    int binarySearch(LabList<T> list, T target, int lower, int upper){
        while(lower <= upper) {
            int mid = (lower + upper) / 2;
            T midVal = list.get(mid);

            if (midVal == null || midVal.compareTo(target) > 0) {
                upper = mid - 1;
            } else if (midVal.compareTo(target) < 0) {
                lower = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**Helper function to exponentiate 2 to a certain power
     * @param base
     * @param exponent
     * @return base^exponent
     */
    static int exponent(int base, int exponent) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result = result * base;
        }
        return result;
    }

    public static void main(String[] args) {
        //TEST CASES
        int higherLength = (int) (Math.random() * 10000);
        int lowerLength = (int) (Math.random() * higherLength);
        int higherBound = (int) (Math.random() * 100000);
        int lowerBound = (int) (Math.random() * higherBound);
        LabList<Integer> labList = LabList.createRandomList(lowerLength, higherLength,
                lowerBound, higherBound);

        /**Test 1 — integer exists within the list*/
        //* Refer to line #45 of LabList.java fo implementation of the .size() function*/
        /*int target = (int) (Math.random() * labList.size());
        int position = find(labList, labList.get(target));*/

        /**Test 2 - integer is guranted to not exist within the list*/
        //int target = 100000;
        //int position = find(labList, target);

        /**Test 3 — integer might be within the list of max size 10000*/
        /*Random rng = new Random();
        int counter = 0;
        int valueBound = higherBound-lowerBound;
        int bound = 100;
        for (int i = 0; i < bound; i++) {
            int target = lowerBound + rng.nextInt(valueBound);
            int position = find(labList, target);

            if (position != -1 && labList.get(position).compareTo(target) == 0) {
                counter++;
            }
        }
        System.out.println("TEST 3:\n"+"Number of times target was found: " + counter + " out of " + bound + " tests\n" +
                "Length of list: " + labList.size()+"\n");*/



        /*System.out.println(position);
        if (position >= 0)
            System.out.println(position==target);*/
    }

}
