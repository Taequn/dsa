import java.util.*;

public class MissingVars {

    public static Collection<Boolean[]> findMissing(VarCombos v) {
        LinkedList<Boolean[]> result = new LinkedList<Boolean[]>();
        int size = v.size();
        int total = size + v.numMissing();

        //dont do recursion if table is already full
        if (size == total) {
            return result;
        }

        Queue<Integer> toCheck = new LinkedList<Integer>();
        for (int i = 0; i < size; i++) {
            toCheck.add(i);
        }
        Boolean[] startingSequence = new Boolean[v.numVars()];
        findMissingHelper(
            v,
            toCheck,
            0,
            startingSequence,
            result
        );
        return result;

    }

    private static void findMissingHelper(
        VarCombos v, 
        Queue<Integer> toCheck,
        int step,
        Boolean[] sequenceSoFar,
        LinkedList<Boolean[]> result
    ) {
        
        //the number of true-false values we expect to see on a given step is the
        //total size n divided by 2 to the power of step + 1 (that is, n/2 for
        //step 0, n/4 for step 1, n/8  for step 2 etc.)
        int expectedTrue = (v.size() + v.numMissing()) / (int)Math.pow(2, step + 1);
        int expectedFalse = expectedTrue;

        Queue<Integer> toCheckTrue = new LinkedList<Integer>();
        Queue<Integer> toCheckFalse = new LinkedList<Integer>();
        
        while (toCheck.peek() != null) {
            int currentRow = toCheck.remove();
            boolean currentValue = v.queryTable(currentRow, step);
            if (currentValue) {
                expectedTrue--;
                toCheckTrue.add(currentRow);
            }
            else {
                expectedFalse--;
                toCheckFalse.add(currentRow);
            }
        }
        if (expectedTrue > 0) {
            Boolean[] currentSequence = sequenceSoFar.clone();
            currentSequence[step] = true;
            //if step is 1 less than numVars, we just populated the last var in a sequence. 
            //add this sequence to result
            if (step == v.numVars() - 1) {
                result.add(currentSequence);
            }
            else {
                findMissingHelper(
                    v,
                    toCheckTrue,
                    step + 1,
                    currentSequence,
                    result
                );
            }
        }
        if (expectedFalse > 0) {
            Boolean[] currentSequence = sequenceSoFar.clone();
            currentSequence[step] = false;
            if (step == v.numVars() - 1) {
                result.add(currentSequence);
            }
            else {
                findMissingHelper(
                    v,
                    toCheckFalse,
                    step + 1,
                    currentSequence,
                    result
                );
            }
        }
    }
    
}
