import java.util.HashMap;
import java.util.ArrayList;

class SemanticSimilarity {
    HashMap<String, HashMap<String, Integer>> descriptors;

    public SemanticSimilarity(Iterable<String> sentences) {
        //TO-DO: Implement constructor
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
        return result;
    }

    public static void main(String[] args) {
        System.out.println(sentenceToWords("Hello world i am very cool"));
    }
}