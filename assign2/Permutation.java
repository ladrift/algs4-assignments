import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        if (k <= 0) {
            return;
        }

        RandomizedQueue<String> rq = new RandomizedQueue<>();

        int seq = 0;
        while (!StdIn.isEmpty()) {
            seq += 1;
            String s = StdIn.readString();
            if (rq.size() == k) {
                int idx = StdRandom.uniform(seq);
                if (idx < k) {
                    rq.dequeue();
                } else {
                    continue;
                }
            }
            rq.enqueue(s);
        }

        Iterator<String> iterator = rq.iterator();
        for (int i = 0; i < k && iterator.hasNext(); i++) {
            String item = iterator.next();
            StdOut.println(item);
        }
    }
}
