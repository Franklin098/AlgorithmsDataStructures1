/* *****************************************************************************
 *  Name:              Franklin Velasquez
 *  Coursera User ID:  123456
 *  Last modified:     January 06, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    private int trials;
    private int n;
    private double[] openSitesFractions;

    // perform independent trials on a n-by-n grid
    public PercolationStats(int n, int trials){
        if(n<= 0 || trials <= 0) throw new IllegalArgumentException("n and trials must be greater than cero.");
        this.n = n;
        this.trials = trials;
        this.openSitesFractions = new double[trials];
        this.runTrials();
    }

    // run experiments
    private void runTrials(){
        // computational experiment t
        int totalSites = n*n;
        for (int t = 0; t < openSitesFractions.length; t++) {
            Percolation percolation = new Percolation(this.n);
            while (!percolation.percolates()){
                int rowSelected = StdRandom.uniform(this.n) + 1;
                int columnSelected = StdRandom.uniform(this.n) + 1;
                percolation.open(rowSelected,columnSelected);
            }
            this.openSitesFractions[t] = percolation.numberOfOpenSites()/(double)totalSites;
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(openSitesFractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(openSitesFractions);
    }

    // low endpoint of 95% confidence interval
    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        double s = this.stddev();
        double x = this.mean();
        return x - ((1.96*s)/Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        double s = this.stddev();
        double x = this.mean();
        return x + ((1.96*s)/Math.sqrt(trials));
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        System.out.println("n = " + n + " | T = " + trials);
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats percolationStats = new PercolationStats(n,trials);
        double elapsedTime = stopwatch.elapsedTime();
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + "," + percolationStats.confidenceHi()+"]");
        System.out.println("elapsed time (seconds)  = " + elapsedTime);
    }
}
