import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private static final int INFINITY = Integer.MAX_VALUE;

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        $.checkArgs(G);

        this.G = $.copy(G);

    }

    // findSAPLength of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        BreadthFirstDirectedPaths pv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths pw = new BreadthFirstDirectedPaths(G, w);

        return findSAPLength(pv, pw);
    }

    // findSAPLength of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);

        BreadthFirstDirectedPaths pv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths pw = new BreadthFirstDirectedPaths(G, w);

        return findSAPLength(pv, pw);
    }

    private int findSAPLength(BreadthFirstDirectedPaths pv, BreadthFirstDirectedPaths pw) {
        int minLength = INFINITY;
        for (int i = 0; i < G.V(); i++) {
            if (pv.distTo(i) == INFINITY || pw.distTo(i) == INFINITY) {
                continue;
            }

            int distance = pv.distTo(i) + pw.distTo(i);
            if (distance < minLength) {
                minLength = distance;
            }
        }

        if (minLength == INFINITY) {
            return -1;
        }
        return minLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        BreadthFirstDirectedPaths pv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths pw = new BreadthFirstDirectedPaths(G, w);

        return findAncestor(pv, pw);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);

        BreadthFirstDirectedPaths pv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths pw = new BreadthFirstDirectedPaths(G, w);

        return findAncestor(pv, pw);
    }

    private int findAncestor(BreadthFirstDirectedPaths pv, BreadthFirstDirectedPaths pw) {
        int ancestor = -1;
        int minLength = INFINITY;
        for (int i = 0; i < G.V(); i++) {
            if (pv.distTo(i) == INFINITY || pw.distTo(i) == INFINITY) {
                continue;
            }

            int distance = pv.distTo(i) + pw.distTo(i);
            if (distance < minLength) {
                minLength = distance;
                ancestor = i;
            }
        }

        return ancestor;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = G.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = G.V();
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
