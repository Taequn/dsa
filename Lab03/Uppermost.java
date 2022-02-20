public class Uppermost {
    public static Line[] lines;

    public static void visibleLines(double[] slopes, double[] intercepts){
        if(slopes.length != intercepts.length){
            throw new IllegalArgumentException("slopes and intercepts must be the same length");
        }
        lines = new Line[slopes.length];
        for(int i = 0; i < slopes.length; i++){
            lines[i] = new Line(slopes[i], intercepts[i], i);
        }
        sortLines();
        Line[] answer = recurssive(lines);

        //for(int i = 0; i < answer.length; i++){
        //    System.out.println(answer[i]);
        //}
    }

    public static void print(Line[] lines){
        for(int i = 0; i < lines.length; i++){
            System.out.println(lines[i]);
        }
    }

    public static Line[] recurssive(Line[] list){
        System.out.println();
        if(list.length <= 2){
            if(list.length == 1){
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

    public static Line[] merge(Line[] left, Line[] right){
        System.out.println("Left");
        print(left);
        System.out.println("Right");
        print(right);

        //initialize the answer
        int firstPos = 0;
        int secondPos = 0;
        int mergedPos = 0;
        Line[] merged = new Line[left.length + right.length];

        merged[mergedPos++] = left[firstPos++];

        while(firstPos < left.length && secondPos < right.length){
            double prevIntersection = left[firstPos-1].findIntersection(left[secondPos]);
            double rightIntersection = left[firstPos].findIntersection(right[secondPos]);

            double currValueAtX = left[firstPos].evaluateAtX(prevIntersection);
            double rightValueAtX = right[secondPos].evaluateAtX(prevIntersection);

            if(prevIntersection < rightIntersection && currValueAtX > rightValueAtX){
                merged[mergedPos++] = left[firstPos++];
            } else {
                merged[mergedPos++] = right[secondPos++];
                firstPos=left.length+1;
            }
        }

        while(firstPos < left.length){
            merged[mergedPos++] = left[firstPos++];
        }
        while(secondPos < right.length){
            merged[mergedPos++] = right[secondPos++];
        }

        System.out.println("Merged");

        //resize the array
        int count = 0;
        for(int i = 0; i < merged.length; i++){
            if(merged[i] == null){
                count++;
            }
        }
        Line[] answer = new Line[merged.length - count];
        for(int i = 0; i < answer.length; i++){
            answer[i] = merged[i];
        }
        print(answer);
        return answer;
    }



    /**
     * CHANGE INTO A PROPER SORT LATER
     * O(n^2) for now
     */
    public static void sortLines(){
        Line temp;
        for(int i = 0; i < lines.length; i++){
            for(int j = i + 1; j < lines.length; j++){
                if(lines[i].slope > lines[j].slope){
                    temp = lines[i];
                    lines[i] = lines[j];
                    lines[j] = temp;
                }
            }
        }
    }



    public static void printLines(){
        for(int i = 0; i < lines.length; i++){
            System.out.println(lines[i]);
        }
    }

    public static void main(String[] args) {
        visibleLines(new double[]{1, 2, 3}, new double[]{1, 2, 3});
    }

}


class Line {
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
        return (other.intercept - this.intercept) / (this.slope - other.slope);
    }

    public String toString() {
        return "y = " + slope + "x + " + intercept;
    }
}

