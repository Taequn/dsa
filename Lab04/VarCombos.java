import java.util.*;

public class VarCombos {

    private static final boolean debug = true;

    private int numVars;
    private int missing;

    private int[] table;

    private void makeTable() {
        Random rand = new Random();

        table = new int[ 1 << numVars ];
        for (int i = 0; i < table.length; i++) {
            table[i] = i;
        }

        for (int i = 0; i < table.length-1; i++) {
            int r = rand.nextInt(table.length-i);
            if (r != 0) {
                int tmp = table[i];
                table[i] = table[i+r];
                table[i+r] = tmp;
            }
        }
    }

    public VarCombos(int numVars, int missing) {
        if (numVars < 1 || missing < 0 || numVars > 30)
            throw new IllegalArgumentException();
        
        this.numVars = numVars;
        this.missing = missing;

        makeTable();

    }

    public boolean queryTable(int row, int var) {
        if (row < 0 || row >= table.length-missing || var < 0 || var >= numVars)
            throw new IllegalArgumentException();

        return (table[row] & (1 << var)) != 0;
    }

    public int size() {
        return table.length - missing;
    }

    public int numVars() {
        return numVars;
    }

    public int numMissing() {
        return missing;
    }

    public String toString() {
        if (debug) {
            return missing + ", " + Arrays.toString(table);
        } else {
            return super.toString();
        }
    }

    public static void main(String[] args) {
        int numVars = Integer.parseInt(args[0]);
        int missing = Integer.parseInt(args[1]);
        VarCombos v = new VarCombos(numVars, missing);

        if (args.length > 2)
            System.out.println(v);

        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            int row = scan.nextInt();
            int var = scan.nextInt();
            System.out.println(v.queryTable(row, var));
        }
        scan.close();

    }
}