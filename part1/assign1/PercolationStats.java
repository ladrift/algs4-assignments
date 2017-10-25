// TODO: Add file header
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trails should be greater than zero.");
        }

        double[] thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }
            thresholds[i] = p.numberOfOpenSites();
        }

        mean = StdStats.mean(thresholds) / (n * n);

        stddev = StdStats.stddev(thresholds) / (n * n);

        double halfWidth = 1.96 * stddev / Math.sqrt(trials);
        confidenceLo = mean - halfWidth;
        confidenceHi = mean + halfWidth;
    }

    public double mean() {                         // sample mean of percolation threshold
        return mean;
    }

    public double stddev() {                       // sample standard deviation of percolation threshold
        return stddev;
    }

    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        return confidenceLo;
    }

    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return confidenceHi;
    }

    public static void main(String[] args) {       // test client (described below)
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, T);
        StdOut.printf("%-23s = %f\n", "mean", ps.mean());
        StdOut.printf("%-23s = %f\n", "stddev", ps.stddev());
        StdOut.printf("%-23s = [%f, %f]\n", "95% confidence interval", ps.confidenceLo(), ps.confidenceHi());
    }
}
