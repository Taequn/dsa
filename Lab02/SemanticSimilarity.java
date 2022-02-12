import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.lang.Math;

class SemanticSimilarity {
    private HashMap<String, HashMap<String, Integer>> descriptors;

    /**
     * Constructor for the SemanticSimilarity program.
     * @param sentences The sentences being used to determine semantic similarity.
     */
    public SemanticSimilarity(Iterable<String> sentences) {
        descriptors = new HashMap<String, HashMap<String, Integer>>();
        generateDescriptors(sentences);
    }

    /**
     * Method to generate descriptors for each of the words. Each descriptor's values are
     * its count of shared sentences with other words.
     * @param sentences The sentences being used to determine semantic similarity.
     */
    private void generateDescriptors(Iterable<String> sentences) {
        for (String s: sentences) {
            HashSet<String> words = sentenceToWords(s);
            for (String w1 : words) { // Loop through all the unique words.
                for (String w2 : words) {
                    if (w1.equals(w2)) {
                        continue;
                    }
                    if (!descriptors.containsKey(w1)) {
                        descriptors.put(w1, new HashMap<String, Integer>()); // Create new descriptor.
                    }
                    if (!descriptors.containsKey(w2)) {
                        descriptors.put(w2, new HashMap<String, Integer>()); // Create new descriptor.
                    }

                    HashMap<String, Integer> d1 = descriptors.get(w1);

                    if (!d1.containsKey(w2)) {
                        d1.put(w2, 1);
                    } else {
                        d1.put(w2, d1.get(w2) + 1); // Increment sentence count for repeated words.
                    }
                }
            }
        }
    }

    /***
     * Tester method to print contents of descriptors.
     */
    public void printDescriptors() {
        for (Map.Entry<String, HashMap<String, Integer>> word: descriptors.entrySet()) {
            System.out.println(word.getKey() + ":");
            System.out.println("{");
            for (Map.Entry<String, Integer> occurence: word.getValue().entrySet()) {
                System.out.println("\t" + occurence.getKey() + ": " + occurence.getValue());
            }
            System.out.println("}");
        }
    }

    /***
     * Takes a string which contains a sentence and returns an ArrayList containing the
     * words in that sentence, in lowercase (numeric words are not included)
     * @param sentence The sentence to be processed.
     * @return A HashSet with all the unique words.
     */
    private static HashSet<String> sentenceToWords(String sentence) {
        HashSet<String> result = new HashSet<String>();
        String currentWord = "";
        for(int i = 0; i < sentence.length(); i++){
            char currentChar = sentence.charAt(i);
            if(Character.isLetter(currentChar)) {
                currentWord = currentWord + currentChar; // Build word with valid letters.
            }
            else {
                if (currentWord.equals("")) { // End word at invalid character
                    continue;
                }
                else {
                    result.add(currentWord.toLowerCase());
                    currentWord = "";
                }
            }
        }
        if (!currentWord.equals("")) {
            result.add(currentWord.toLowerCase());
        }
        return result;
    }

    /**
     * Calculates the similarity ratio between two words.
     * @param w1 The first word being compared.
     * @param w2 The second word being compared.
     * @return A double indicating the ration between the words.
     */
    public double similarity(String w1, String w2) {
        int numeratorSum = 0;
        int aSquared = 0;
        int bSquared = 0;
        w1 = w1.toLowerCase();
        w2 = w2.toLowerCase();

        if ((!descriptors.containsKey(w1)) || (!descriptors.containsKey(w2))) { // Verification
            return -1.0;
        }

        for (String key : descriptors.get(w1).keySet()) { // Count words in first word descriptor.
            if (descriptors.get(w2).containsKey(key)) {
                numeratorSum += descriptors.get(w1).get(key) * descriptors.get(w2).get(key); // Count shared words.
                aSquared += descriptors.get(w1).get(key) * descriptors.get(w1).get(key);
            } else {
                aSquared += descriptors.get(w1).get(key) * descriptors.get(w1).get(key);
            }
        }

        for (String key : descriptors.get(w2).keySet()) { // Count words in second word descriptor.
            bSquared += descriptors.get(w2).get(key) * descriptors.get(w2).get(key);
        }

        return (numeratorSum/(Math.sqrt(aSquared * bSquared)));
    }

    public static void main(String[] args) {
        // /*
        // System.out.println(sentenceToWords("Hello world i am very cool"));
        // System.out.println(sentenceToWords("This, is, -a- elaborately! punctuated? sentence?"));
        // System.out.println(sentenceToWords("Incorre;ctly punc1tuated !!!/12sentence13 with 123123 numbers"));
        // */
        // String[] test = {
        //     // "This is a very handsome sentence",
        //     "This is a blue dog",
        //     "This is a blue dog",
        //     // "Blue is a blue Blue",
        //       "dog dog dog dog dog is",
        //     // "The blue dog is very happy",
        //     // "The mannequin is mediocre",
        //     // "The dog said \"THIS IS A VERY HANDSOME BLUE MANNEQUIN\""
        // };

        // SemanticSimilarity testtest = new SemanticSimilarity(Arrays.asList(test));
        // testtest.printDescriptors();

        // System.out.println(testtest.similarity("dog", "dog"));
        // System.out.println(testtest.similarity("dog", "is"));
        // System.out.println(testtest.similarity("dog", "blue"));
        // System.out.println(testtest.similarity("mannequin", "very"));
        // System.out.println(testtest.similarity("dog", "The"));
        // System.out.println(testtest.similarity("--", "is"));
        // System.out.println(testtest.similarity(" ", "is"));
    }
}