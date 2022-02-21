import java.util.*;

public class Uppermost {
    public static Line[] lines;

    public static int[] visibleLines(double[] slopes, double[] intercepts){
        if(slopes.length != intercepts.length){
            throw new IllegalArgumentException("slopes and intercepts must be the same length");
        }
        lines = new Line[slopes.length];
        for(int i = 0; i < slopes.length; i++){
            lines[i] = new Line(slopes[i], intercepts[i], i);
        }
        Arrays.sort(lines);
        Line[] answer = recurssive(lines);
        int[] indexes = new int[answer.length];

        for(int i = 0; i < answer.length; i++){
            indexes[i] = answer[i].index;
            System.out.println(indexes[i]);
        }

        return indexes;
    }

    public static void print(Line[] lines){
        for(int i = 0; i < lines.length; i++){
            System.out.println(lines[i]);
        }
    }

    public static void print(String word, Line[] lines){
        for(int i = 0; i < lines.length; i++){
            System.out.println(word + " " + lines[i]);
        }
    }

    /**
     * RECURSIVE FUNCTION
     * @param list
     * @return array of visible lines
     */

    public static Line[] recurssive(Line[] list){
        if(list.length <= 2){
            if(list.length <= 1){
                return list;
            } else {
                if(list[0].slope == list[1].slope){
                    Line[] answer = new Line[1];
                    if(list[0].intercept > list[1].intercept){
                        //remove list[1]
                        answer[0] = list[0];
                    } else {
                        //remove list[0]
                        answer[0] = list[1];
                    }
                    return answer;
                }
            }
        }

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

            Line[] firstHalf = recurssive(left);
            //print(firstHalf);
            //take the second half of the list
            Line[] secondHalf = recurssive(right);
            //print(secondHalf);
            Line[] merged = merge(firstHalf, secondHalf);
            return merged;
        }

        return list;
    }

    /**
     * MERGE TWO ARRAYS
     * RECURSIVE FUNCTION HELPER
     * @param left
     * @param right
     * @return merged array
     */

    public static Line[] merge(Line[] left, Line[] right){
        int firstPos = 0;
        int secondPos = 0;
        int mergedPos = 0;
        double min=0;
        Line[] merged = new Line[left.length + right.length];

        merged[mergedPos++] = left[firstPos++];
        merged[merged.length - 1] = right[right.length - 1];

        double minmax = merged[0].findIntersection(merged[merged.length - 1]);

        while(firstPos < left.length){
            Line previous = merged[mergedPos-1];
            Line currentLeft = left[firstPos];
            Line currentRight = right[secondPos];

            double leftIntersection = previous.findIntersection(currentLeft);
            double rightIntersection = previous.findIntersection(currentRight);


            double currValueAtX = currentLeft.evaluateAtX(leftIntersection);
            double rightValueAtX = currentRight.evaluateAtX(rightIntersection);


            if(leftIntersection < rightIntersection && leftIntersection < minmax){
                merged[mergedPos++] = left[firstPos++];
                min = leftIntersection;
            } else {
                min = rightIntersection;
                firstPos=left.length+1;
            }
        }


        //find the earliest intersection
        int minIndex=0;
        for(int i = secondPos; i < right.length; i++){
            Line previous = merged[mergedPos-1];
            Line current = right[i];

            if(current.findIntersection(previous) < min){
                minIndex = i;
                min = current.findIntersection(previous);
            }

        }

        secondPos = minIndex;
        while(secondPos < right.length-1){
            merged[mergedPos++] = right[secondPos++];
        }

        //resize the array
        int count = 0;
        for(int i = 0; i < merged.length; i++){
            if(merged[i] == null){
                count++;
            }
        }
        Line[] answer = new Line[merged.length - count];
        int index = 0;
        for(int i = 0; i < merged.length; i++){
            if(merged[i] != null){
                answer[index++] = merged[i];
            }
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
        visibleLines(new double[]{0, -1, 1}, new double[]{5, -5, -5});
//        visibleLines(new double[]{-4, -6, 0, 4, 1, -1, -9, -7, -8, 3, 2, -5, -10, -3, -2},
//                new double[]{-1189.0, -523.0, -3335.0, -6634.0,-4068.0, -2679.0, -41, -305, -153, -5753, -4878, -826,
//                0, -1611, -2103});

    }
}


/**
 * HELPER FUNCTIONS AND CLASSES
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

