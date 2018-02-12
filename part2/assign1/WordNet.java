import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final Digraph G;

    private final ArrayList<String> synsets = new ArrayList<>();
    private final HashMap<String, ArrayList<Integer>> nounToIds = new HashMap<>();

    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFilename, String hypernymsFilename) {
        $.checkArgs(synsetsFilename, hypernymsFilename);

        readSynsets(synsetsFilename);

        G = new Digraph(synsets.size());

        int rootCount = readHypernyms(hypernymsFilename);

        if (rootCount != 1) {
            throw new IllegalArgumentException(String.format("invalid G structure with %d root(s).", rootCount));
        }

        DirectedCycle cycleQuery = new DirectedCycle(G);
        if (cycleQuery.hasCycle()) {
            throw new IllegalArgumentException("invalid G structure with cycle(s).");
        }

        sap = new SAP(G);
    }

    private int readHypernyms(String hypernymsFilename) {
        In hypernymsIn = new In(hypernymsFilename);
        int rootCount = G.V();
        while (hypernymsIn.hasNextLine()) {
            String[] line = hypernymsIn.readLine().split(",");

            int id = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int hypernymsId = Integer.parseInt(line[i]);
                if (G.outdegree(id) == 0) {
                    rootCount--;
                }
                G.addEdge(id, hypernymsId);
            }
        }
        return rootCount;
    }

    /**
     * Read synsets file and store synsets to {@code this.synsets}
     * and build up a map {@code nounToIds} associating noun to synset ID.
     *
     * @param synsetsFilename a String of synsets file name
     */
    private void readSynsets(String synsetsFilename) {
        In synsetsIn = new In(synsetsFilename);

        while (synsetsIn.hasNextLine()) {
            String[] line = synsetsIn.readLine().split(",");

            String synset = line[1];
            synsets.add(synset);

            int id = Integer.parseInt(line[0]);
            String[] nouns = synset.split(" ");
            for (String noun : nouns) {
                if (nounToIds.containsKey(noun)) {
                    nounToIds.get(noun).add(id);
                } else {
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(id);
                    nounToIds.put(noun, ids);
                }
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    // is the word a WordNet noun?
    // Performance requirement: in time logarithmic (or better) in the number of nouns.
    public boolean isNoun(String word) {
        $.checkArgs(word);

        return nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        $.checkArgs(nounA, nounB);

        return sap.length(nounToIds.get(nounA), nounToIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        $.checkArgs(nounA, nounB);

        int ancestor = sap.ancestor(nounToIds.get(nounA), nounToIds.get(nounB));
        return synsets.get(ancestor);
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");
        int count = 0;
        for (String noun : wordnet.nouns()) {
            count++;
        }
        System.out.println(String.format("count = %d, should be 119188 for synsets.txt", count));
        System.out.println("wordnet.isNoun(\"worm\") = " + wordnet.isNoun("worm"));

        System.out.println("wordnet.sap(\"worm\", \"bird\") = " + wordnet.sap("worm", "bird"));
        System.out.println("wordnet.distance(\"worm\", \"bird\") = " + wordnet.distance("worm", "bird"));
    }
}