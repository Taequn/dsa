import java.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.lang.Math;

class SemanticSimilarity {
    private HashMap<String, HashMap<String, Integer>> descriptors;
    private Iterable<String> bodyOfText;
    private HashMap<String, Integer> uniqueWords;

    /**
     * Constructor for the SemanticSimilarity program.
     * @param sentences The sentences being used to determine semantic similarity.
     */
    public SemanticSimilarity(Iterable<String> sentences) {
        ArrayList<String> body = new ArrayList<String>();
        for(String sentence : sentences) {
            body.add(sentence.toLowerCase());
        }
        this.bodyOfText = body;
        split();
    }

    /**
     * Creates a HashMap with HashMaps:
     * Each unique words gets a HashMap with semantic descriptors.
     */
    private void split() {
        //initiate HashMap of HashMaps
        HashMap<String, HashMap<String, Integer>> bigD = new HashMap<String, HashMap<String, Integer>>();
        for (String sentence : this.bodyOfText) {
            for (String word : sentence.split(" ")) {
                if(!bigD.containsKey(word)) {
                    bigD.put(word, uniqueValues(word));
                }
            }
        }
        this.descriptors = bigD;
    }

    /**
     * Creates a HashMap with the unique words and their semantic descriptors.
     * A helper function for the split function.
     * @param value
     * @return
     */

    public HashMap<String, Integer> uniqueValues(String value) {
        HashMap<String, Integer> uniques = new HashMap<String, Integer>();
        for (String sentence : bodyOfText) {
            if (sentence.contains(value)) {
                for (String values : sentence.split(" "))
                    if (!values.equals(value)) {
                        if (!uniques.containsKey(values)) {
                            uniques.put(values, 1);
                        } else {
                            uniques.put(values, uniques.get(values) + 1);
                        }
                    }
            }
        }
        return uniques;
    }

    /**
     * Compares the two HashMaps and returns the formula-based similarity.
     * Helper function for the similarity function.
     * @param first The first HashMap<String, Integer>
     * @param second The second HashMap<String, Integer>
     * @return The similarity between the two HashMaps.
     */

    private double compare(HashMap<String, Integer> first, HashMap<String, Integer> second) {
        int sharedNumber = 0;
        for(String key : first.keySet()) {
            if(second.containsKey(key)) {
                sharedNumber += first.get(key) * second.get(key);
            }
        }
        return sharedNumber;
    }

    /**
     * Returns the similarity between the two words.
     * @param w1 The first word.
     * @param w2 The second word.
     * @return The similarity between the two words based on the provided formula.
     */
    public double similarity(String w1, String w2) {
        w1 = w1.toLowerCase();
        w2 = w2.toLowerCase();
        if(!descriptors.containsKey(w1) || !descriptors.containsKey(w2)) {
            return -1.0;
        }
        HashMap<String, Integer> comp1 = descriptors.get(w1);
        HashMap<String, Integer> comp2 = descriptors.get(w2);


        double numerator = compare(comp1, comp2);
        double firstDenom = Math.sqrt(compare(comp1, comp1));
        double secondDenom = Math.sqrt(compare(comp2, comp2));
        double denominator = firstDenom * secondDenom;
        return numerator/denominator;
    }


    public static void main(String[] args) {
        String[] test = {
             "This is a very handsome sentence",
             //"This is a blue dog",
             "This is a blue dog",
               "dog dog dog dog dog is",
             "The blue dog is very happy",
             // "The mannequin is mediocre",
                "This is a suboptimal sentence",
             // "The dog said \"THIS IS A VERY HANDSOME BLUE MANNEQUIN\""
         };


        SemanticSimilarity test1 = new SemanticSimilarity(Arrays.asList(test));
        System.out.println(test1.similarity("This", "is"));

    }
}