import java.util.*;
public class Uppermost {
    public static Line[] lines;

    /**
    * Method to create the array for the visible lines. The indices in slope and intercepts correspond to each other.
    * @param slopes A given array that contains the slopes of our lines.
    * @param intercepts A given array that contains the intercepts of our lines.
    */
    public static int[] visibleLines(double[] slopes, double[] intercepts){
        if(slopes.length != intercepts.length){
            throw new IllegalArgumentException("slopes and intercepts must be the same length");
        }

        lines = new Line[slopes.length]; // create space for the total lines
        for(int i = 0; i < slopes.length; i++){
            lines[i] = new Line(slopes[i], intercepts[i], i); // connect the data for each line
        }
        Arrays.sort(lines);

        Line[] answer = recursive(lines); // look for the visible lines
        
        int[] indexes = new int[answer.length];
        for(int i = 0; i < answer.length; i++){
            indexes[i] = answer[i].index; // convert visible lines to int []
            // System.out.println(indexes[i]);
        }

        return indexes;
    }

    /**
     * Print helper method. It prints the lines.
     * @param lines The lines made from connecting the slopes and intercepts.
     */
    public static void print(Line[] lines){
        for(int i = 0; i < lines.length; i++){
            System.out.println(lines[i]);
        }
    }

    /**
     * Print helper method. It prints the lines and an identifier word.
     * @param word An identifier word to help read the code.
     * @param lines The lines made from connecting the slopes and intercepts.
     */
    public static void print(String word, Line[] lines){
        for(int i = 0; i < lines.length; i++){
            System.out.println(word + " " + lines[i]);
        }
    }

    /**
     * Recursive function for the code. It breaks the array of lines
     * into halves at each step of the recursion. Then, it merges the pieces
     * into the array of visible lines. If there are any parallel lines,
     * the lower one is removed.
     * @param list The list of all the lines.
     * @return An array of visible lines.
     */
    public static Line[] recursive(Line[] list){
        if(list.length <= 2){
            if(list.length <= 1){ // base case
                return list;
            } else {
                if(list[0].slope == list[1].slope){ // account for equal slopes
                    Line[] answer = new Line[1];
                    if(list[0].intercept > list[1].intercept){
                        answer[0] = list[0]; // remove list[1]
                    } else {
                        answer[0] = list[1]; // remove list[0]
                    }
                    return answer;
                }
            }
        }

        int start = 0;
        int end = list.length - 1;

        if(start < end){
            int mid = (start + end) / 2; // split the list in half
            Line[] left = new Line[mid - start + 1];
            Line[] right = new Line[end - mid];

            for(int i = 0; i < left.length; i++){
                left[i] = list[start + i];
            }
            for(int i = 0; i < right.length; i++){
                right[i] = list[mid + 1 + i];
            }

            Line[] firstHalf = recursive(left);
            //print(firstHalf);
            Line[] secondHalf = recursive(right);
            //print(secondHalf);

            Line[] merged = merge(firstHalf, secondHalf);
            return merged;
        }

        return list;
    }

    /**
     * Merge the two arrays.
     * @param left The array containing all the smaller slopes. Sorted.
     * @param right The array containing all the larger slopes. Sorted.
     * @return merged array
     */
    public static Line[] merge(Line[] left, Line[] right) {
        int idxInLeftArr = 0;
        int idxInRightArr = 0;
        
        ArrayList<Line> topLines = new ArrayList<Line>();

        if (left.length > 1) { // add the first two lines from the left array
            topLines.add(left[idxInLeftArr++]);
            topLines.add(left[idxInLeftArr++]);
        } else { // if there is only one line in left array, add it and one from the right array
            topLines.add(left[idxInLeftArr++]);
            topLines.add(right[idxInRightArr++]);
        }

        int visibleLines = 2;

        for (Line l: allLines) { // TODO: Instead of looping over the whole thing, loop through both arrays
            Line lastVisible = topLines.get(visibleLines - 1);
            Line secondToLastVisible = topLines.get(visibleLines - 2);

            //if THIS line intersects the SECOND TO LAST one before the LAST one,
            // the LAST one is not visible as it is overshadowed by THIS ONE
            double lastIntersect = lastVisible.findIntersection(secondToLastVisible);
            double thisIntersect = l.findIntersection(secondToLastVisible);
            //we do this in a while loop, because THIS line might also be overshadowing
            //the lines we have previously added to the list and assumed to be visible.
            //so keep doing this until THIS line no longer overshadows anything, or there
            //are no lines left to check
            while (thisIntersect < lastIntersect && visibleLines > 1) {
                topLines.remove(visibleLines - 1);
                visibleLines--;

                if (visibleLines > 1) {
                    lastVisible = topLines.get(visibleLines - 1);
                    secondToLastVisible = topLines.get(visibleLines - 2);
                    lastIntersect = lastVisible.findIntersection(secondToLastVisible);
                    thisIntersect = l.findIntersection(secondToLastVisible);
                }
            }
            topLines.add(l);
            visibleLines++;
        }

        Line[] answer = new Line[topLines.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = topLines.get(i);
        }
        return answer;
    }


    public static void printLines(){
        for(int i = 0; i < lines.length; i++){
            System.out.println(lines[i]);
        }
    }


    /**
     *
     * TESTING IS HERE
     *
     */

    public static void main(String[] args) {
        //visibleLines(new double[]{-2, -1, -0.5, -0.25}, new double[]{1, -4, 5, 2});
        //visibleLines(new double[]{0.25, 0.5, 1, 2}, new double[]{-3, 2, 3, -3});
        //visibleLines(new double[]{0.25, 0.5, 1, 2, -2, -1, -0.5, -0.25},
        //       new double[]{-3, 2, 3, -3, 1, -4, 5, 2});
        //visibleLines(new double[]{-2, -1, 1, 2}, new double[]{-4, -2, -2, -4});
        //visibleLines(new double[]{-2, -1, 1, 2}, new double[]{-4, -2, -2, 6});
        //visibleLines(new double[]{0, -1, 1}, new double[]{5, -5, -5});
//        visibleLines(new double[]{-4, -6, 0, 4, 1, -1, -9, -7, -8, 3, 2, -5, -10, -3, -2},
//                new double[]{-1189.0, -523.0, -3335.0, -6634.0,-4068.0, -2679.0, -41, -305, -153, -5753, -4878, -826,
//                0, -1611, -2103});

    }
}


/**
 * Line helper class to collect all the line data into one class
 */
class Line implements Comparable<Line> {
    double slope;
    double intercept;
    int index;

    Line(double slope, double intercept, int index) {
        this.slope = slope;
        this.intercept = intercept;
        this.index = index;
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public int getIndex() {
        return index;
    }

    public double evaluateAtX(double x) {
        return slope * x + intercept;
    }

    public double findIntersection(Line other) {
        return (other.intercept - intercept) / (slope - other.slope);
    }


    public boolean equals(Line other){
        return this.slope == other.slope && this.intercept == other.intercept;
    }

    @Override
    public int compareTo(Line other){
        if(this.slope == other.slope){
            return Double.compare(this.intercept, other.intercept);
        } else {
            return Double.compare(this.slope, other.slope);
        }
    }

    public String toString() {
        return "y = " + slope + "x + " + intercept;
    }
}

