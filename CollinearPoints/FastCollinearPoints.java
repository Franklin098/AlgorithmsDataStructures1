/* *****************************************************************************
 *  Name:              Franklin Velasquez
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {

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

        List<Point> visited = new ArrayList<>();
        for (int i = 0; i < points.length - 1; i++) {
            Point p = points[i];
            ArrayList<Point> otherPoints = new ArrayList<>();
            for (int k = i + 1; k < points.length; k++) {
                Point q = points[k];
                if (!visited.contains(q)) {
                    otherPoints.add(q);
                }
            }
            if (otherPoints.size() <= 2) {
                continue;
            }
            // sort other points by comparing their slopes with respect to p
            otherPoints.sort(p.slopeOrder());
            int startIndex = 0;
            int endIndex = 1;

            while (startIndex < otherPoints.size() && endIndex < otherPoints.size()) {
                double previousSlope = p.slopeTo(otherPoints.get(startIndex));
                Point currentPoint = otherPoints.get(endIndex);
                double currentSlope = p.slopeTo(currentPoint);
                if (previousSlope != currentSlope) {
                    testAndAddSegment(p, startIndex, endIndex, otherPoints, visited);
                    startIndex = endIndex;
                    endIndex++;
                }
                else {
                    endIndex++;
                }
            }
            // when reach the end of the array, we should also check if last segment is valid
            testAndAddSegment(p, startIndex, endIndex, otherPoints, visited);
        }
    }

    private void testAndAddSegment(Point p, int startIndex, int endIndex,
                                   ArrayList<Point> otherPoints,
                                   List<Point> visited) {
        int previousLength = (endIndex - startIndex + 1) - 1;
        if (previousLength >= 3) {
            // valid segment
            List<Point> collinearPoints = otherPoints.subList(startIndex, endIndex);
            collinearPoints.add(p);
            // sort by "y" coordinates, in order to get only first and last points of the segment
            // stream().sorted() returns a new stream, then a new list
            collinearPoints = collinearPoints.stream().sorted().collect(Collectors.toList());
            visited.addAll(collinearPoints);
            this.segments.add(
                    new LineSegment(collinearPoints.get(0),
                                    collinearPoints.get(previousLength))
            );
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
