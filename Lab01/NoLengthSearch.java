public class NoLengthSearch {
    
    public static <T extends Comparable<? super T>>

    //raise to the exponent)

    int find(LabList<T> list, T target){
        //due to the nature of .createRandomList(), there is no need to check for null
        //However, we still need to check for empty list

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
        // TODO: Your testing code goes here

        LabList<Integer> labList = LabList.createRandomList(100, 200, 1000, 2000);
        //random whole number between 0 and 200
        int target = (int) (Math.random() * 200);
        int position = find(labList, labList.get(target));

        System.out.println(position);
        if (position >= 0)
            System.out.println(position==target);
    }

}
