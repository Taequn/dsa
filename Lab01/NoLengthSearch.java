import java.util.*;

public class NoLengthSearch {
    
    public static <T extends Comparable<? super T>>


    int find(LabList<T> list, T target){
        //due to the nature of .createRandomList(), there is no need to check for null
        //within this function, since it's impossible to init LabList with nulls or
        //with an empty list (it throws an exception when bounds are invalid)

        //CHECKS
        if(list.get(0) == null){return -1;}
        if(list.get(0).compareTo(target) == 0){return 0;}

        //BODY
        int currExp = 0; //exponent size
        T curr = list.get(0); //position

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

        //binary search
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

        return -1;  // replace with correct return value

    }

    static int exponent(int base, int exponent) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result = result * base;
        }
        return result;
    }

    public static void main(String[] args) {
        //Basic tests
        int higherLength = (int) (Math.random() * 10000);
        int lowerLength = (int) (Math.random() * higherLength);
        int higherBound = (int) (Math.random() * 100000);
        int lowerBound = (int) (Math.random() * higherBound);
        LabList<Integer> labList = LabList.createRandomList(lowerLength, higherLength,
                lowerBound, higherBound);

        /**Test 1 — integer exists within the list*/
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
