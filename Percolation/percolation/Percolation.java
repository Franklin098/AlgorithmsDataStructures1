/* *****************************************************************************
 *  Name:              Franklin Velasquez
 *  Coursera User ID:  123456
 *  Last modified:     January 04, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid;
    private int n;
    private int total;
    private WeightedQuickUnionUF unionFind;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n<=0) throw new IllegalArgumentException();
        this.total = n*n;
        this.n = n;
        this.grid = new boolean[total];
        for(int i = 0 ; i <this.grid.length; i++){
            // initially all sites blocked
            grid[i] = false;
        }

        // init QuickFind data type
        this.unionFind = new WeightedQuickUnionUF(total + 2  ); // 2 virtual nodes for check percolation
    }

    // opens the site (row,col) if it is not open already
    public void open(int row, int col){
        int position_mapped = getPositionMapped(row,col);
        // if it is already open, do nothing
        if(this.grid[position_mapped]) return;
        // if it is closed, open it, and connect its open neighbors
        this.grid[position_mapped] = true;
        // check for up, bottom, right and left neighbors
        this.connectTo(position_mapped, row-1,col); // up
        this.connectTo(position_mapped,row+1,col); // bottom
        this.connectTo(position_mapped,row,col-1); // left
        this.connectTo(position_mapped,row,col+1); // right
        // if node is in the top or bottom row, now that is open, connect to virtual nodes
        int topVirtualNode = total;
        int bottomVirtualNode = total+1;
        if(row == 1)  this.unionFind.union(position_mapped, topVirtualNode);
        if(row == this.n)  this.unionFind.union(position_mapped,bottomVirtualNode);
    }

    private void connectTo(int node_position_mapped, int row_neighbor, int col_neighbor){
        if(row_neighbor <= 0 || col_neighbor <= 0 || row_neighbor > n || col_neighbor > n) return ; // position does not exist, the node is at a border
        int neighbor_position_mapped = getPositionMapped(row_neighbor,col_neighbor);
        // if neighbor is also open
        if(this.grid[neighbor_position_mapped]){
            this.unionFind.union(node_position_mapped, neighbor_position_mapped);
        }
    }

    private int getPositionMapped(int row, int col){
        if(row <= 0 || col <= 0 || row > n || col > n) throw new IllegalArgumentException();
        int position_mapped = (row-1)*n + (col-1);
        return position_mapped;
    }

    // is the site (row,col) open?
    public boolean isOpen(int row, int col){
        if(row <= 0 || col <= 0 || row > n || col > n) throw new IllegalArgumentException();
        int position_mapped = (row-1)*n + (col-1);
        return this.grid[position_mapped];
    }

    // is the site (row,col) full?
    public boolean isFull(int row, int col){
        if(row <= 0 || col <= 0 || row > n || col > n) throw new IllegalArgumentException();
        int position_mapped = (row-1)*n + (col-1);
        int topVirtualNode = total;
        if(this.grid[position_mapped]){
            int set1 = unionFind.find(topVirtualNode);
            int set2 = unionFind.find(position_mapped);
            return set1 == set2;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        int openSitesCount = 0;
        for(boolean isOpen : this.grid){
            if(isOpen) openSitesCount++;
        }
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates(){
        int topVirtualNode = total;
        int bottomVirtualNode = total+1;
        int set1 = unionFind.find(topVirtualNode);
        int set2 = unionFind.find(bottomVirtualNode);
        return set1 == set2;
    }


    //Remove
    private void printGrind(){
        for (int i = 0; i < this.grid.length; i++) {
            boolean isOpen = this.grid[i];
            String itemChar = isOpen ? "O" : "X";
            if(i % this.n == 0){
                System.out.print("\n " + itemChar);
            }else{
                System.out.print(" | " + itemChar);
            }
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {

        Percolation percolation= new Percolation(5);
        System.out.println("Number of Sets in QuickFind DT: " + percolation.unionFind.count());
        percolation.open(1,3);
        percolation.open(2,3);
        percolation.open(2,2);
        percolation.open(3,2);
        percolation.open(4,2);
        percolation.open(4,3);
        percolation.open(4,4);
        percolation.open(4,5);

        System.out.println("============== Init ============");
        percolation.printGrind();
        System.out.println("Are Open: " + percolation.numberOfOpenSites());
        System.out.println("is (4,5) full ? " + percolation.isFull(4,5));
        System.out.println("percolates ? " + percolation.percolates());
        System.out.println("\n============================== \n");

        percolation.open(5,5);
        System.out.println("============== Init ============");
        percolation.printGrind();
        System.out.println("Are Open: " + percolation.numberOfOpenSites());
        System.out.println("percolates ? " + percolation.percolates());
        System.out.println("\n============================== \n");

    }

    private static void test1(){
        Percolation percolation= new Percolation(5);
        System.out.println("Number of Sets in QuickFind DT: " + percolation.unionFind.count());
        percolation.open(1,1);
        percolation.open(5,5);
        percolation.open(3,5);
        percolation.open(5,1);

        System.out.println("============== Init ============");
        percolation.printGrind();
        System.out.println("Are Open: " + percolation.numberOfOpenSites());
        System.out.println("Number of Sets in QuickFind DT: " + percolation.unionFind.count());
        System.out.println("is (1,1) full ? " + percolation.isFull(1,1));
        System.out.println("is (1,2) full ? " + percolation.isFull(1,2));
        System.out.println("is (2,2) full ? " + percolation.isFull(2,2));
        System.out.println("is (2,1) full ? " + percolation.isFull(2,1));
        System.out.println("percolates ? " + percolation.percolates());
        System.out.println("\n============================== \n");


        percolation.open(2,1);
        System.out.println("============== Init ============");
        percolation.printGrind();
        System.out.println("Are Open: " + percolation.numberOfOpenSites());
        System.out.println("Number of Sets in QuickFind DT: " + percolation.unionFind.count());
        System.out.println("is (2,1) full ? " + percolation.isFull(2,1));
        System.out.println("percolates ? " + percolation.percolates());
        System.out.println("\n============================== \n");

        percolation.open(3,1);
        percolation.open(4,1);
        System.out.println("============== Init ============");
        percolation.printGrind();
        System.out.println("Are Open: " + percolation.numberOfOpenSites());
        System.out.println("Number of Sets in QuickFind DT: " + percolation.unionFind.count());
        System.out.println("is (4,1) full ? " + percolation.isFull(4,1));
        System.out.println("percolates ? " + percolation.percolates());
        System.out.println("\n============================== \n");
    }

}
