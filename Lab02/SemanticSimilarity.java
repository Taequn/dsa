import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

class SemanticSimilarity {
    private HashMap<String, HashMap<String, Integer>> descriptors;

    public SemanticSimilarity(Iterable<String> sentences) {
        descriptors = new HashMap<String, HashMap<String, Integer>>();
        generateDescriptors(sentences);
    }

    private void generateDescriptors(Iterable<String> sentences) {
        for (String s: sentences) {
            ArrayList<String> words = sentenceToWords(s);
            for (int i = 0; i < words.size() - 1; i++) {
                for (int j = i + 1; j < words.size(); j++) {
                    String w1 = words.get(i);
                    String w2 = words.get(j);
                    if (w1.equals(w2)) {
                        continue;
                    }
                    if (!descriptors.containsKey(w1)) {
                        descriptors.put(w1, new HashMap<String, Integer>());
                    }
                    if (!descriptors.containsKey(w2)) {
                        descriptors.put(w2, new HashMap<String, Integer>());
                    }

                    HashMap<String, Integer> d1 = descriptors.get(w1);
                    HashMap<String, Integer> d2 = descriptors.get(w2);

                    if (!d1.containsKey(w2)) {
                        d1.put(w2, 1);
                    }
                    else {
                        d1.put(w2, d1.get(w2) + 1);
                    }

                    if (!d2.containsKey(w1)) {
                        d2.put(w1, 1);
                    }
                    else {
                        d2.put(w1, d2.get(w1) + 1);
                    }
                }
            }
        }
    }

    /***
     * Tester method to print contents of descriptors
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
     * @param sentence: the sentence to be processed
     * @return          the Arraylist containing the words
     */
    private static ArrayList<String> sentenceToWords(String sentence) {
        ArrayList<String> result = new ArrayList<String>();
        String currentWord = "";
        for(int i = 0; i < sentence.length(); i++){
            char currentChar = sentence.charAt(i);
            if(Character.isLetter(currentChar)) {
                currentWord = currentWord + currentChar;
            }
            else {
                if (currentWord.equals("")) {
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

    public static void main(String[] args) {
        /*
        System.out.println(sentenceToWords("Hello world i am very cool"));
        System.out.println(sentenceToWords("This, is, -a- elaborately! punctuated? sentence?"));
        System.out.println(sentenceToWords("Incorre;ctly punc1tuated !!!/12sentence13 with 123123 numbers"));
        */
        String[] test = {
            "This is a very handsome sentence",
            "This is a blue dog",
            "The blue dog is very happy",
            "The mannequin is mediocre",
            "The dog said \"THIS IS A VERY HANDSOME BLUE MANNEQUIN\""
        };

        SemanticSimilarity testtest = new SemanticSimilarity(Arrays.asList(test));
        testtest.printDescriptors();
    }
}