import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;

public class Board {
    private final int n;
    private int[][] blocks;

    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j)
     */
    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }

    /**
     * board dimension n
     */
    public int dimension() {
        return n;
    }

    /**
     * number of blocks out of place
     */
    public int hamming() {
        int distance = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int value = blocks[row][col];
                if (value != 0 && value != getCorrectValue(row, col)) {
                    distance += 1;
                }
            }
        }

        return distance;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        int distance = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int value = blocks[row][col];
                if (value == 0) {
                    continue;
                }

                int correctRow = getCorrectRow(value);
                int correctCol = getCorrectColumn(value);
                distance += Math.abs(row - correctRow) + Math.abs(col - correctCol);
            }
        }

        return distance;
    }

    /**
     * is this board the goal board?
     */
    public boolean isGoal() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int value = blocks[row][col];
                if (value != 0 && value != getCorrectValue(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     */
    public Board twin() {
        int[] indices = new int[4];
        int curr = 0;

        outer:
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int value = blocks[row][col];
                if (value != 0) {
                    indices[curr++] = row;
                    indices[curr++] = col;
                    if (curr == 4) break outer;
                }
            }
        }

        Board twin = new Board(blocks);
        twin.exchange(indices[0], indices[1], indices[2], indices[3]);

        return twin;
    }

    /**
     * does this board equal y?
     */
    public boolean equals(Object y) {
        if (y == null || Board.class != y.getClass()) {
            return false;
        }

        if(this == y) {
            return true;
        }

        Board o = (Board) y;
        if (n != o.n) {
            return false;
        }

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (blocks[row][col] != o.blocks[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * all neighboring boards
     */
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<>();

        int blankRow = 0;
        int blankCol = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (blocks[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                    break;
                }
            }
        }

        assert blocks[blankRow][blankCol] == 0;

        int row;
        int col;

        row = blankRow - 1;
        col = blankCol;
        if (isValidBlock(row, col)) {
            boards.add(getNeighborBySwap(blankRow, blankCol, row, col));
        }

        row = blankRow + 1;
        col = blankCol;
        if (isValidBlock(row, col)) {
            boards.add(getNeighborBySwap(blankRow, blankCol, row, col));
        }

        row = blankRow;
        col = blankCol - 1;
        if (isValidBlock(row, col)) {
            boards.add(getNeighborBySwap(blankRow, blankCol, row, col));
        }

        row = blankRow;
        col = blankCol + 1;
        if (isValidBlock(row, col)) {
            boards.add(getNeighborBySwap(blankRow, blankCol, row, col));
        }

        return boards;
    }

    /**
     * string representation of this board (in the output format specified below)
     */
    public String toString() {
        // Use the reference implementation from Promgramming Assignment 4 Checklist
        // http://coursera.cs.princeton.edu/algs4/checklists/8puzzle.html
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * unit tests (not graded)
     */
    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = StdIn.readInt();
        Board b = new Board(blocks);
        for (int i = 1; i < 9; i++) {
            System.out.println(b.getCorrectRow(i) + " " + b.getCorrectColumn(i));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(b.getCorrectValue(i, j));
            }
        }

        System.out.println("hamming: " + b.hamming());
        System.out.println("manhattan: " + b.manhattan());

        int[][] goalBlocks = new int[][]{
                new int[]{1, 2, 3},
                new int[]{4, 5, 6},
                new int[]{7, 8, 0},
        };

        Board goal = new Board(goalBlocks);

        System.out.println(goal.isGoal());
        System.out.println(b.isGoal());

        System.out.println("b = " + b);
        System.out.println("b.twin() = " + b.twin());

        Board otherBoard = new Board(blocks);
        System.out.println("otherBoard.equals(b) = " + otherBoard.equals(b));

        System.out.println("b.neighbors() = " + b.neighbors());
    }

    private int getCorrectValue(int row, int col) {
        return 1 + row * n + col;
    }

    private int getCorrectColumn(int num) {
        int remain = Math.floorMod(num, n);
        if (remain == 0) {
            return n - 1;
        } else {
            return num % n - 1;
        }
    }

    private int getCorrectRow(int num) {
        int remain = Math.floorMod(num, n);
        if (remain == 0) {
            return num / n - 1;
        } else {
            return num / n;
        }
    }

    private void exchange(int lhsRow, int lhsCol, int rhsRow, int rhsCol) {
        int tmp = blocks[lhsRow][lhsCol];
        blocks[lhsRow][lhsCol] = blocks[rhsRow][rhsCol];
        blocks[rhsRow][rhsCol] = tmp;
    }

    private Board getNeighborBySwap(int blankRow, int blankCol, int row, int col) {
        Board neighbor = new Board(blocks);
        neighbor.exchange(blankRow, blankCol, row, col);
        return neighbor;
    }

    private boolean isValidBlock(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }
}