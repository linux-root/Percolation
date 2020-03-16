import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double CONFIDENCE_95 = 1.96;
  private final double mean;
  private final double stddev;
  private final double confidenceLo;
  private final double confidenceHi;

  /**
   *  perform independent trials on an n-by-n grid.
   * @param n size of n-by-n grid
   * @param trials number of times running experiment
   */
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    double[] samples = new double[trials];
    for (int i = 0; i < trials; i++) {
      final Percolation percolation = new Percolation(n);
      while (!percolation.percolates()) {
        percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
      }
      double percolationThreshold = (double) percolation.numberOfOpenSites() / (n * n);
      samples[i] = percolationThreshold;
    }

    this.mean = StdStats.mean(samples);
    this.stddev = StdStats.stddev(samples);
    this.confidenceLo = this.mean - CONFIDENCE_95 * this.stddev / Math.sqrt(trials);
    this.confidenceHi = this.mean + CONFIDENCE_95 * this.stddev / Math.sqrt(trials);
  }

  // sample mean of percolation threshold
  public double mean() {
    return this.mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return this.stddev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return this.confidenceLo;
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return this.confidenceHi;
  }

  /** test client.
   *
   */
  public static void main(String[] args) {
    // sunday
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    PercolationStats p = new PercolationStats(n, trials);
    System.out.println(p.mean);
    System.out.println(p.stddev);
    System.out.println(p.confidenceLo);
  }
}
