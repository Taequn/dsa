import java.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.lang.Math;

class SemanticSimilarity {
    /**
     * Change to private later
     */
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
     * Method to generate descriptors for each of the words. Each descriptor's values are
     * its count of shared sentences with other words.
     * @param sentences The sentences being used to determine semantic similarity.
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
     * Calculates the similarity ratio between two words.
     * @param w1 The first word being compared.
     * @param w2 The second word being compared.
     * @return A double indicating the ration between the words.
     */
//    public double similarity(String w1, String w2) {
//        int numeratorSum = 0;
//        int aSquared = 0;
//        int bSquared = 0;
//        w1 = w1.toLowerCase();
//        w2 = w2.toLowerCase();
//
//        if ((!descriptors.containsKey(w1)) || (!descriptors.containsKey(w2))) { // Verification
//            return -1.0;
//        }
//
//        for (String key : descriptors.get(w1).keySet()) { // Count words in first word descriptor.
//            if (descriptors.get(w2).containsKey(key)) {
//                numeratorSum += descriptors.get(w1).get(key) * descriptors.get(w2).get(key); // Count shared words.
//                aSquared += descriptors.get(w1).get(key) * descriptors.get(w1).get(key);
//            } else {
//                aSquared += descriptors.get(w1).get(key) * descriptors.get(w1).get(key);
//            }
//        }
//
//        for (String key : descriptors.get(w2).keySet()) { // Count words in second word descriptor.
//            bSquared += descriptors.get(w2).get(key) * descriptors.get(w2).get(key);
//        }
//
//        return (numeratorSum/(Math.sqrt(aSquared * bSquared)));
//    }

    private double compare(HashMap<String, Integer> first, HashMap<String, Integer> second) {
        int sharedNumber = 0;
        for(String key : first.keySet()) {
            if(second.containsKey(key)) {
                sharedNumber += first.get(key) * second.get(key);
            }
        }
        return sharedNumber;
    }

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

        //System.out.println(test1.bodyOfText);
        //System.out.println(test1.descriptors);

        System.out.println(test1.similarity("This", "is"));

    }
}