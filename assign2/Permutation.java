import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        if (k <= 0) {
            return;
        }

        RandomizedQueue<String> rq = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }

        Iterator<String> iterator = rq.iterator();
        for (int i = 0; i < k && iterator.hasNext(); i++) {
            String item = iterator.next();
            StdOut.println(item);
        }
    }
}
