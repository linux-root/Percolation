import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final int edge;
  private final WeightedQuickUnionUF siteList;
  private final int topVirtualNode;
  private final int bottomVirtualNode;
  private int numberOfOpenSite;
  private final boolean[] open;

  /**
   * Constructor creates n-by-n grid, with all sites initially blocked.
   *
   * @param  n grid size
   * @throws IllegalArgumentException unless {@code 0 <= n}
   */
  public Percolation(int n) {
    this.edge = n;
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    this.siteList = new WeightedQuickUnionUF(n * n + 2); // 2 extra sites for virtual node
    this.numberOfOpenSite = 0;
    this.topVirtualNode = n * n;
    this.bottomVirtualNode = n * n + 1;
    this.open = new boolean[n * n];

    // connect extra sites to first row and last row
    for (int i = 0; i < n; i++) {
      this.siteList.union(topVirtualNode, i);
      this.siteList.union(bottomVirtualNode, id(n, i + 1));
    }
  }

  /**
   *  opens the site (row, col) if it is not open already.
   *
   * @param row number in n-by-n grid
   * @param col number in n-by-n grid
   */
  public void open(int row, int col) {
    validate(row, col);
    int id = id(row, col);
    if (isOpen(row, col)) {
      return;
    } else {
      this.open[id] = true;
      this.numberOfOpenSite++;
    }

    final int topNeighborId = id(row - 1, col);

    if (this.isValidIdToUnion(topNeighborId) && !this.siteList.connected(id, topNeighborId)) {
      this.siteList.union(id, topNeighborId);
    }

    final int rightNeighborId = id(row, col + 1);
    if (this.isValidIdToUnion(topNeighborId) && !this.siteList.connected(id, rightNeighborId)) {
      this.siteList.union(id, rightNeighborId);
    }

    final int bottomNeighborId = id(row + 1, col);
    if (this.isValidIdToUnion(bottomNeighborId) && !this.siteList.connected(id, bottomNeighborId)) {
      this.siteList.union(id, bottomNeighborId);
    }

    final int leftNeighborId = id(row, col - 1);
    if (this.isValidIdToUnion(leftNeighborId) && !this.siteList.connected(id, leftNeighborId)) {
      this.siteList.union(id, leftNeighborId);
    }
  }

  private boolean isValidIdToUnion(int id) {
    return this.validId(id) && this.open[id];
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    validate(row, col);
    return open[id(row, col)];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    validate(row, col);
    return this.siteList.connected(id(row, col), topVirtualNode) && isOpen(row, col);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return this.numberOfOpenSite;
  }

  // does the system percolate?
  public boolean percolates() {
    if(this.edge == 1) return isOpen(1,1);
    return this.siteList.connected(this.topVirtualNode, this.bottomVirtualNode);
  }

  private void validate(int row, int col) {
    if (row <= 0 || row > this.edge) {
      throw new IllegalArgumentException();
    }

    if (col <= 0 || col > this.edge) {
      throw new IllegalArgumentException();
    }
  }

  private boolean validId(int id) {
    return id >= 0 && id < this.edge * this.edge;
  }

  private int id(int row, int col) {
    return this.edge * (row - 1) + (col - 1);
  }


  /**
   * test client (optional).
   */
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    Percolation s = new Percolation(n);


    for (int i = 0; i < 44; i++) {
      int randomRow = StdRandom.uniform(1, n+1);
      int randomCol = StdRandom.uniform(1, n+1);
      while (s.isOpen(randomRow, randomCol)){
       randomRow = StdRandom.uniform(1, 4);
       randomCol = StdRandom.uniform(1, 4);
      }
      s.open(randomRow, randomCol);
    }
//    System.out.println("open: " + s.isOpen(1, 1) + ". is Full: " + s.isFull(1, 1));
    System.out.println("Open sites: " + s.numberOfOpenSites() + ". Percolate: " + s.percolates());
  }
}
