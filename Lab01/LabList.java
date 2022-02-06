import java.util.*;

public class LabList<T extends Comparable<? super T>>
{
    // the items
    private ArrayList<T> items;

    // Constructors

    // from a collection
    public LabList(Collection<? extends T> c) {
        items = new ArrayList<T>(c);
        Collections.sort(items);
    }

    // from an array
    public LabList(T[] arr) {
        items = new ArrayList<T>(arr.length);
        for (T item : arr) {
            items.add(item);
        }
        Collections.sort(items);
    }

    // Create a random list of integers
    public static LabList<Integer> createRandomList(int lengthLower, int lengthHigher, int valueLower, int valueHigher) {
        Random rng = new Random();
        int length = lengthLower + rng.nextInt(lengthHigher - lengthLower);
        Integer[] randomItems = new Integer[length];

        int bound = valueHigher - valueLower;
        for (int i = 0; i < length; i++) {
            randomItems[i] = valueLower + rng.nextInt(bound);
        }

        return new LabList<Integer>(randomItems);
    }

    @Override
    public String toString() {
        return items.toString();
    }

    /** ADDED FOR TESTING PURPOSES */
    public int size() {
        return items.size();
    }


    // query the data structure
    public T get(int index) {
        if (index < items.size())
            return items.get(index);
        else
            return null;
    }
}