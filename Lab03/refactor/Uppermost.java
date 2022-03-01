import java.util.*;

public class Uppermost {
    public static Line[] lines;

    /**
    * Method to create the array for the visible lines. The indices in slope and intercepts correspond to each other.
    * There are two methods for solving the problem. One that looks at the left and right array separately.
    * One that looks at all the slope values for the given step.
    * @param slopes A given array that contains the slopes of our lines.
    * @param intercepts A given array that contains the intercepts of our lines.
    * @return An integer array with the indices of the visible lines in order.`
    */
    public static int[] visibleLines(double[] slopes, double[] intercepts){
        if(slopes.length != intercepts.length){ // verification
            throw new IllegalArgumentException("slopes and intercepts must be the same length");
        }

        lines = new Line[slopes.length]; // create space for the total lines
        for(int i = 0; i < slopes.length; i++){
            lines[i] = new Line(slopes[i], intercepts[i], i); // connect the data for each line
        }
        Arrays.sort(lines);

        //Direct approach:
        //Line[] answer = alternativeMerge(lines);

        Line[] answer = recursive(lines); // recursive approach


        int[] indexes = new int[answer.length];
        for(int i = 0; i < answer.length; i++){ // convert visible lines to int []
            indexes[i] = answer[i].index;
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
     * Partition the array.
     * @param arr The array to sort.
     * @param smallestIdx The smallestIdx in the array.
     * @param largestIdx The largestIdx in the array.
     * @return A sorted array.
     */
    static int partition(Line arr[], int smallestIdx, int largestIdx) {
        Line pivot = arr[largestIdx]; // set the end as the pivot point
        int higherValPointer = (smallestIdx - 1); // set your pointer

        for (int lowerValPointer = smallestIdx; lowerValPointer < largestIdx; lowerValPointer++) { // traverse your array with your small value pointer
            if (arr[lowerValPointer].getSlope() <= pivot.getSlope()) { // until you find a value smaller than your pivot
                higherValPointer++; // then traverse your array with your higher value pointer

                Line temp = arr[higherValPointer]; // keep the location of your higher value
                arr[higherValPointer] = arr[lowerValPointer]; // swap their positions
                arr[lowerValPointer] = temp;
            }
        }

        Line temp = arr[higherValPointer + 1];
        arr[higherValPointer + 1] = arr[largestIdx];
        arr[largestIdx] = temp;

        return (higherValPointer + 1);
    }

    /**
     * An implementation of quicksort for Line[].
     * @param arr The array to sort.
     * @param smallestIdx The smallestIdx in the array.
     * @param largestIdx The largestIdx in the array.
     * @return A sorted array.
     */
    static void quickSort(Line arr[], int smallestIdx, int largestIdx) {
        if (smallestIdx < largestIdx) {
          int partitioned = partition(arr, smallestIdx, largestIdx);
          quickSort(arr, smallestIdx, partitioned - 1);
          quickSort(arr, partitioned + 1, largestIdx);
        }
    }

    /**
     * Recursive function for the code. First, it sorts the given list by slope.
     * It breaks the array of lines into halves at each step of the recursion.
     * Then, it merges the pieces into the array of visible lines.
     * If there are any parallel lines, the lower one is removed.
     * @param list The list of all the lines.
     * @return An array of visible lines.
     */
    public static Line[] recursive(Line[] list){
        if(list.length <= 2){
            if(list.length <= 1){ // for one line, return the line
                return list;
            } else {
                if(list[0].slope == list[1].slope){ // for two lines, return higher y-int if parallel
                    Line[] answer = new Line[1];
                    if(list[0].intercept > list[1].intercept){
                        answer[0] = list[0];
                    } else {
                        answer[0] = list[1];
                    }
                    return answer;
                }
            }
        }

        // sort the list
        //quickSort(list, 0, list.length-1);

        int start = 0;
        int end = list.length - 1;

        if(start < end){
            //split the list in half
            int mid = (start + end) / 2;
            Line[] left = new Line[mid - start + 1];
            Line[] right = new Line[end - mid];

            for(int i = 0; i < left.length; i++){
                left[i] = list[start + i];
            }
            for(int i = 0; i < right.length; i++){
                right[i] = list[mid + 1 + i];
            }

            Line[] firstHalf = recursive(left); // continue splitting both sides
            Line[] secondHalf = recursive(right);

            //Line[] visibleLines = merge(firstHalf, secondHalf); // First implementation
            Line[] visibleLines = alternativeMerge(firstHalf, secondHalf); // Second implementation
            return visibleLines;
        }
        return list;
    }

    /**
     * Converts an ArrayList<Line> to Line[]
     * @param visibleLines The ArrayList of lines.
     * @return A Line[] of the ArrayList.
     */
    public static Line[] convertToLineArr(ArrayList<Line> visibleLines) {
        Line[] answer = new Line[visibleLines.size()];
        int index = 0;
        for(int i = 0; i < visibleLines.size(); i++){
            answer[index++] = visibleLines.get(i);
        }
        return answer;
    }

    /**
     * Merge the two arrays. Merges by considering the position of the
     * @param left The array containing all the smaller slopes. Sorted.
     * @param right The array containing all the larger slopes. Sorted.
     * @return visibleLines array
     */
    public static Line[] merge(Line[] left, Line[] right){
        int leftArrIndex = 0;
        int rightArrIndex = 0;
        int visibleLinesArrIndex = 0;
        ArrayList<Line> visibleLines = new ArrayList<Line>();

        if(left.length > 1){
            visibleLines.add(left[leftArrIndex++]); // add the first value of the left array to our visible lines
        } else {
            left[leftArrIndex].findIntersection(right[rightArrIndex]);
        }

        while(leftArrIndex < left.length){ // loop through left array
            Line recentVisibleLine = visibleLines.get(visibleLinesArrIndex++); // get the last visible line
            Line currentLeft = left[leftArrIndex]; // get the line on the left
            Line currentRight = right[rightArrIndex]; // get the line on the right

            double leftIntersectionX = recentVisibleLine.findIntersection(currentLeft); // get the intersection between our left line and our visible line
            double rightIntersectionX = recentVisibleLine.findIntersection(currentRight); // get the intersection between our right line and our visible line

            if(rightIntersectionX < leftIntersectionX && currentRight.slope > currentLeft.slope) { // compare the intersection points
                for(Line l : right){
                    visibleLines.add(l); // add the right side and throw away the rest
                }
                return convertToLineArr(visibleLines);
            } else if (leftIntersectionX < rightIntersectionX && currentRight.slope > currentLeft.slope) {
                visibleLines.add(left[leftArrIndex++]);
            }
        }

        for(Line l : right){
            visibleLines.add(l); // add the right side and throw away the rest
        }
        return convertToLineArr(visibleLines);
    }

    /**
     * Recursive function for the code. It breaks the array of lines
     * into halves at each step of the recursion. Then, it merges the pieces
     * into the array of visible lines. If there are any parallel lines,
     * the lower one is removed.
     *
     * It checks to see if THIS line intersects the SECOND TO LAST one before the LAST one.
     * The LAST one is not visible as it is overshadowed by THIS one.
     *
     * We keep checking intersections in a while loop, because THIS line might also be overshadowing
     * the lines we have previously added to the list and assumed to be visible.
     * So keep rechecking intersections until THIS line no longer overshadows anything, or there
     * are no lines left to check.
     *
     * @param list The list of all the lines.
     * @return An array of visible lines.
     */
    public static Line[] alternativeMerge(Line[] lines){
        ArrayList<Line> allLines = new ArrayList<Line>(); // add lines to arraylist of lines
        for(int i = 0; i < lines.length; i++){
            allLines.add(lines[i]);
        }

        ArrayList<Line> visible = new ArrayList<Line>(); // initiate an empty array of visible lines

        visible.add(allLines.remove(0)); // add two lines with least slope: the first one is always visible
        visible.add(allLines.remove(0)); // the second may be overshadowed later

        int visibleCount = 2;

        for (Line thisLine: allLines) { // looks at all the lines in the recursion step
            Line lastVisible = visible.get(visibleCount - 1);
            Line secondToLastVisible = visible.get(visibleCount - 2);

            double visibleLineIntersection = lastVisible.findIntersection(secondToLastVisible);
            double thisIntersection = thisLine.findIntersection(secondToLastVisible);

            while (thisIntersection < visibleLineIntersection && visibleCount > 1) { // check intersections of this line and all previous lines
                visible.remove(visibleCount - 1);
                visibleCount--; // decrement termination case

                if (visibleCount > 1) {
                    lastVisible = visible.get(visibleCount - 1); // move lastVisible to the line before
                    secondToLastVisible = visible.get(visibleCount - 2); // move second to lastVisible to the line before

                    visibleLineIntersection = lastVisible.findIntersection(secondToLastVisible); // update intersection points
                    thisIntersection = thisLine.findIntersection(secondToLastVisible);
                }
            }
            visible.add(thisLine);
            visibleCount++;
        }

        Line[] visibleArray = new Line[visible.size()];
        for (int i = 0; i < visibleArray.length; i++) {
            visibleArray[i] = visible.get(i);
        }
        return visibleArray;
    }

    /**
     * Merge function to handle multiple arrays as an input.
     * This shifts the problem to a merge function that uses one
     * array as an input.
     * @return An array of visible lines.
     */
    public static Line[] alternativeMerge(Line[] left, Line[] right) {
        //create a new array of lines
        Line[] visibleLines = new Line[left.length + right.length];
        //add the first one and then the second one
        for(int i = 0; i < left.length; i++){
            visibleLines[i] = left[i];
        }
        for(int j = 0; j < right.length; j++){
            visibleLines[left.length+j] = right[j];
        }
        return alternativeMerge(visibleLines);
    }


    public static void printLines(){
        for(int i = 0; i < lines.length; i++){
            System.out.println(lines[i]);
        }
    }


    /**
     * Main method with tests.
     */
    public static void main(String[] args) {
        //visibleLines(new double[]{-2, -1, -0.5, -0.25}, new double[]{1, -4, 5, 2});
        //visibleLines(new double[]{0.25, 0.5, 1, 2}, new double[]{-3, 2, 3, -3});
        //visibleLines(new double[]{0.25, 0.5, 1, 2, -2, -1, -0.5, -0.25},
        //       new double[]{-3, 2, 3, -3, 1, -4, 5, 2});
        //visibleLines(new double[]{-2, -1, 1, 2}, new double[]{-4, -2, -2, -4});
        //visibleLines(new double[]{-2, -1, 1, 2}, new double[]{-4, -2, -2, 6});
        //visibleLines(new double[]{0, -1, 1}, new double[]{5, -5, -5});
        visibleLines(new double[]{-4, -6, 0, 4, 1, -1, -9, -7, -8, 3, 2, -5, -10, -3, -2},
                new double[]{-1189.0, -523.0, -3335.0, -6634.0,-4068.0, -2679.0, -41, -305, -153, -5753, -4878, -826,
                0, -1611, -2103});
    }
}


/**
 * Line helper class to collect all the line data into one class
 */
class Line implements Comparable<Line> {
    double slope;
    double intercept;
    int index;

    /**
     * A constructor for the line class.
     * @param slope Slope for the line.
     * @param intercept Intercept of the line.
     * @param index Original index of the line from input arrays.
     */
    Line(double slope, double intercept, int index) {
        this.slope = slope;
        this.intercept = intercept;
        this.index = index;
    }

    /**
     * Gets the slope of the line.
     * @return A double that is the slope.
     */
    public double getSlope() {
        return slope;
    }

    /**
     * Gets the y-intercept of the line.
     * @return A double that is the y-intercept.
     */
    public double getIntercept() {
        return intercept;
    }

    /**
     * Gets the original index of the line from the input array.
     * @return An int that is the original index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the y value of a line at a given x.
     * @param x The x-value you want to put in the line function.
     * @return The y-value of the line.
     */
    public double evaluateAtX(double x) {
        return slope * x + intercept;
    }

    /**
     * Gets the intersection between two lines.
     * @param other The tuple for the other line that intersects this line.
     * @return The x-value where the lines intersect.
     */
    public double findIntersection(Line other) {
        return (other.intercept - intercept) / (slope - other.slope);
    }

    /**
     * Checks the equality of two lines.
     * @param other The tuple for the other line that intersects this line.
     * @return A boolean of if the lines equal each other.
     */
    public boolean equals(Line other){
        return this.slope == other.slope && this.intercept == other.intercept;
    }

    /**
     * Compares two lines. It will compare their slopes if the slopes are different.
     * If the slopes are the same though, then it will compare the y-intercepts.
     * @param other The tuple for the other line that intersects this line.
     * @return A number greater than 1 if this line is larger. O if this line is equal. -1 if this line is smaller.
     */
    @Override
    public int compareTo(Line other){
        if(this.slope == other.slope){
            return Double.compare(this.intercept, other.intercept);
        } else {
            return Double.compare(this.slope, other.slope);
        }
    }

    /**
     * Turns the line into a string to print.
     */
    public String toString() {
        return "y = " + slope + "x + " + intercept;
    }
}

