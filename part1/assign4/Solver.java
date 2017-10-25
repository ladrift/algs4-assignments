import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {
    private class SearchNode {
        Board board;
        SearchNode prev;
        int step = 0;
        int hamming = -1;
        int manhattan = -1;

        SearchNode(Board board) {
            this.board = board;
            this.prev = null;
            this.step = 0;

        }

        public SearchNode(Board board, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            this.step = prev.step + 1;
        }

        private int manhattanPriority() {
            if (manhattan != -1) return manhattan;

            manhattan = board.manhattan() + step;
            return manhattan;
        }

        private int hammingPriority() {
            if (hamming != -1) return hamming;

            hamming = board.hamming() + step;
            return hamming;
        }

        boolean isGoal() {
            return board.isGoal();
        }
    }

    private boolean isSolvable;

    private LinkedList<Board> sequences = new LinkedList<>();

    /**
     * find a solution to the initial board (using the A* algorithm)
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<SearchNode> nodes = new MinPQ<>(Comparator.comparingInt(SearchNode::manhattanPriority));
        nodes.insert(new SearchNode(initial));

        MinPQ<SearchNode> twinNodes = new MinPQ<>(Comparator.comparingInt(SearchNode::manhattanPriority));
        Board twinInitial = initial.twin();
        twinNodes.insert(new SearchNode(twinInitial));

        while (nodes.size() > 0 && twinNodes.size() > 0) {
            SearchNode twinNode = twinNodes.delMin();
            if (twinNode.isGoal()) {
                isSolvable = false;
                return;
            } else {
                for (Board neighbor : twinNode.board.neighbors()) {
                    if (twinNode.prev != null && neighbor.equals(twinNode.prev.board)) continue;
                    twinNodes.insert(new SearchNode(neighbor, twinNode));
                }
            }

            SearchNode node = nodes.delMin();
            if (node.board.isGoal()) {
                isSolvable = true;
                SearchNode curr = node;
                while (curr != null) {
                    sequences.addFirst(curr.board);
                    curr = curr.prev;
                }
                return;
            } else {
                for (Board neighbor : node.board.neighbors()) {
                    if (node.prev != null && neighbor.equals(node.prev.board)) continue;
                    nodes.insert(new SearchNode(neighbor, node));
                }
            }
        }
    }

    /**
     * is the initial board solvable?
     */
    public boolean isSolvable() {
        return isSolvable;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        if (!isSolvable) {
            return -1;
        } else {
            return sequences.size() - 1;
        }
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        } else {
            return sequences;
        }
    }

    /**
     * solve a slider puzzle (given below)
     */
    public static void main(String[] args) {
        // Copied from Programming Assignment Specification
        // http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}