// TODO: Add file header
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private WeightedQuickUnionUF uf;
    private int openSiteCount;

    private char[] siteStates;
    private final char BLOCKED = 0b0000;
    private final char OPEN = 0b0001;
    private final char FULL = 0b0010;
    private final char BOTTOM = 0b0100;
    private boolean isPercolated;

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException("n should be greater than zero.");
        }
        this.n = n;
        uf = new WeightedQuickUnionUF(n * n);
        siteStates = new char[n * n];
    }

    public void open(int row, int col) {    // open site (row, col) if it is not open already
        validate(row, col);

        if (getSiteState(row, col) != BLOCKED) {
            return;
        }

        char centerState = OPEN;
        if (row == 1 && row == n) {
            centerState = OPEN | FULL | BOTTOM;
        } else if (row == n) {
            centerState = OPEN | BOTTOM;
        } else if (row == 1) {
            centerState = OPEN | FULL;
        }
        setSiteState(row, col, centerState);
        openSiteCount += 1;
        // Left
        char leftState = unionWhenOpen(row, col - 1, row, col);
        // Right
        char rightState = unionWhenOpen(row, col + 1, row, col);
        // Up
        char upState = unionWhenOpen(row - 1, col, row, col);
        // Down
        char downState = unionWhenOpen(row + 1, col, row, col);

        char state = (char) (centerState | leftState | rightState | upState | downState);
        setSiteState(uf.find(getID(row, col)), state);
        if ((state & FULL) != 0 && (state & BOTTOM) != 0 && !isPercolated) {
            isPercolated = true;
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        validate(row, col);

        return (getSiteState(row, col) & OPEN) != 0;
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        validate(row, col);

        return isOpen(row, col) && (getSiteState(uf.find(getID(row, col))) & FULL) != 0;
    }

    public int numberOfOpenSites() {      // number of open sites
        return openSiteCount;
    }

    public boolean percolates() {             // does the system percolate?
        return isPercolated;
    }

    public static void main(String[] args) {  // test client (optional)
//        int N = StdIn.readInt();
//        Percolation p = new Percolation(N * N);
//        while (!StdIn.isEmpty()) {
//            int row = StdIn.readInt();
//            int col = StdIn.readInt();
//            p.open(row, col);
//            StdOut.printf("(%2d, %2d) numberOfOpenSites: %2d, isOpen: %b, isFull: %b, percolates: %b\n", row, col, p.numberOfOpenSites(), p.isOpen(row, col), p.isFull(row, col), p.percolates());
//        }
        Percolation p = new Percolation(20);
        p.open(7, 11);
        System.out.println(p.isOpen(7, 11));
        System.out.println(p.isOpen(7, 10));
    }

    private char unionWhenOpen(int r, int c, int row, int col) {
        if (isValidIndices(r, c, n) && getSiteState(r, c) != BLOCKED) {
            char origState = getSiteState(uf.find(getID(r, c)));
            uf.union(getID(r, c), getID(row, col));
            return origState;
        }

        return BLOCKED;
    }

    private boolean isValidIndices(int row, int col, int n) {
        return !(row <= 0 || row > n || col <= 0 || col > n);
    }

    private int getID(int row, int col) {
        int x = row - 1;
        int y = col - 1;
        return n * x + y;
    }

    private void validate(int row, int col) {
        if (!isValidIndices(row, col, n)) {
            throw new IllegalArgumentException("row and col should be between one and n.");
        }
    }

    private char getSiteState(int row, int col) {
        return siteStates[getID(row, col)];
    }

    private char getSiteState(int id) {
        return siteStates[id];
    }

    private void setSiteState(int row, int col, char state) {
        siteStates[getID(row, col)] = state;
    }

    private void setSiteState(int id, char state) {
        siteStates[id] = state;
    }
}
