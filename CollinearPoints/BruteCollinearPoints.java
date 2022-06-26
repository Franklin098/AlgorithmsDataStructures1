/* *****************************************************************************
 *  Name:              Franklin Velasquez
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }

        // check for duplicates and null values
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException();
                }
                Point p = points[i];
                Point q = points[j];
                if (p.compareTo(q) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        this.segments = new ArrayList<>();

        // sort using Point.compareTo(): points with lowest "y" coordinate will be first
        // in this way we can always take the first and last points to create the segment that contains all points
        Point[] pointsClone = points.clone();
        Arrays.sort(pointsClone);

        // we have n points, check 4 at a time
        for (int a = 0; a < pointsClone.length - 3; a++) {
            for (int b = a + 1; b < pointsClone.length - 2; b++) {
                for (int c = b + 1; c < pointsClone.length - 1; c++) {
                    for (int d = c + 1; d < pointsClone.length; d++) {
                        Point firstPoint = pointsClone[a];
                        Point secondPoint = pointsClone[b];
                        Point thirdPoint = pointsClone[c];
                        Point fourthPoint = pointsClone[d];
                        double slopeOne = firstPoint.slopeTo(secondPoint);
                        double slopeTwo = secondPoint.slopeTo(thirdPoint);
                        double slopeTree = thirdPoint.slopeTo(fourthPoint);
                        if (slopeOne == slopeTwo && slopeTwo == slopeTree) {
                            // collinear points, valid segment, get the lowest and highest point
                            // because the main array is already sorted, took the first and the last one
                            this.segments.add(new LineSegment(firstPoint, fourthPoint));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.segments.size();
    }


    public LineSegment[] segments() {
        LineSegment[] arr = new LineSegment[this.segments.size()];
        arr = this.segments.toArray(arr);
        return arr;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
