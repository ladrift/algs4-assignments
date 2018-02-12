import edu.princeton.cs.algs4.Digraph;

class $ {
    static void checkArgs(Object... args) {
        for (Object o :
                args) {
            if (o == null) {
                throw new IllegalArgumentException("invalid null arguments");
            }
        }
    }

    static Digraph copy(Digraph G) {
        Digraph copied = new Digraph(G.V());
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                copied.addEdge(v, w);
            }
        }
        return copied;
    }
}
