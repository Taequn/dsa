import java.io.*;
import java.util.*;

public class Trie {

    // node class
    private static class TrieNode {
        boolean isWord;
        HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    }

    // root node
    private TrieNode root = new TrieNode();

    // method to implement
    public HashMap<String, Integer> suggestions(String target, int dist)
    {
        target = target.toLowerCase();
        HashMap<String, Integer> words = new HashMap<String, Integer>();

        words = suggestionsRecursiveHelper(target, target, dist, dist, root.children, root, "", 0);

        return words;
    }

    public HashMap<String, Integer> suggestionsRecursiveHelper (String origTarget, String currTarget, int origDist, int currDist, HashMap<Character, TrieNode> children, TrieNode RootNode, String current_word, int accuracy) {
        HashMap<String, Integer> solution = new HashMap<>();

        if (currTarget.length() == 0) { // not a base case but add correct words
            if (RootNode.isWord == true) {
                if(this.contains(current_word)) {
                    solution.put(current_word, origDist - currDist);
                }
            }
        } else if (currDist == 0) { // base case for no more changes
            if (RootNode.isWord == true) {
                if(this.contains(current_word)) {
                    solution.put(current_word, origDist - currDist);
                }
            }
            return solution;
        } else if (RootNode.isWord == true) { // base case for existing word
            if(this.contains(current_word)) {
                solution.put(current_word, origDist - currDist);
            }

            return solution;
        } else { // recursion
            for (Character c : children.keySet()) {
                TrieNode childRoot = children.get(c);
                int tempDist = currDist;
                String currTargetWithoutFirstLetter = currTarget;
                String newCurrentWord = (new StringBuilder()).append(current_word).append(c).toString();
                if (currTarget.length() > 0) {
                    currTargetWithoutFirstLetter = currTarget.substring(1);

                    if (c == currTarget.charAt(0)) { // decrement based on accuracy
                        solution.putAll(suggestionsRecursiveHelper(origTarget, currTargetWithoutFirstLetter, origDist, tempDist, childRoot.children, childRoot, newCurrentWord, accuracy)); // correct
                    } else {
                        tempDist--;
                        solution.putAll(suggestionsRecursiveHelper(origTarget, currTargetWithoutFirstLetter, origDist, tempDist, childRoot.children, childRoot, current_word, accuracy)); // deletion
                        solution.putAll(suggestionsRecursiveHelper(origTarget, currTarget, origDist, tempDist, childRoot.children, childRoot, newCurrentWord, accuracy)); // addition
    
                        if (!c.equals(currTarget.charAt(0))) {
                            solution.putAll(suggestionsRecursiveHelper(origTarget, currTargetWithoutFirstLetter, origDist, tempDist, childRoot.children, childRoot, newCurrentWord, accuracy)); // substitution
                        }
                    }
                } else if (currDist > 0) {
                    tempDist--;
                    solution.putAll(suggestionsRecursiveHelper(origTarget, currTarget, origDist, tempDist, childRoot.children, childRoot, newCurrentWord, accuracy)); // assume addition at the end
                }
            }
        }

        return solution;
    }

    // method to add a string
    public boolean add(String s) {
        s = s.trim().toLowerCase();

        TrieNode current = root;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                TrieNode child = current.children.get(c);
                if (child == null) {
                    child = new TrieNode();
                    current.children.put(c, child);
                }
                current = child;
            }
        }

        if (current.isWord)
            return false;

        current.isWord = true;
        return true;
    }

    // method to check if a string has been added
    public boolean contains(String s) {
        s = s.trim().toLowerCase();

        TrieNode current = root;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                TrieNode child = current.children.get(c);
                if (child == null) {
                    return false;
                }
                current = child;
            }
        }

        return current.isWord;
    }

    // empty constructor
    public Trie() {
        super();
    }

    // constructor to add words from a stream, like standard input
    public Trie(InputStream source) {
        Scanner scan = new Scanner(source);
        addWords(scan);
        scan.close();
    }

    // constructor to add words from a file
    public Trie(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        addWords(scan);
        scan.close();
    }

    // helper function to add words from a scanner
    private void addWords(Scanner scan) {
        while (scan.hasNext()) {
            add(scan.next());
        }
    }

    // main function for testing
    public static void main(String[] args) {

        Trie dictionary;

        if (args.length > 0) {
            try {
                dictionary = new Trie(args[0]);
            } catch (FileNotFoundException e) {
                System.err.printf("could not open file %s for reading\n", args[0]);
                return;
            }
        }
        else {
            dictionary = new Trie(System.in);
        }

        System.out.println(dictionary.contains("cat"));

        HashMap<String, Integer> suggestions = dictionary.suggestions("cat", 3);
        for (String suggestion : suggestions.keySet()) {
            System.out.print("  " + suggestion + " = " + suggestions.get(suggestion));
        }
    }

}
