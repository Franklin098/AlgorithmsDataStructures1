/* *****************************************************************************
 *  Name:              Franklin Velasquez
 * June, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> pointsSet;

    public PointSET() {
        this.pointsSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return this.pointsSet.isEmpty();
    }

    public void insert(Point2D p) {
        checkNotNull(p);
        this.pointsSet.add(p);
    }

    private void checkNotNull(Object p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    public boolean contains(Point2D p) {
        checkNotNull(p);
        return this.pointsSet.contains(p);
    }

    public void draw() {
        for (Point2D point : this.pointsSet) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNotNull(rect);
        List<Point2D> insideRectangle = new ArrayList<>();
        for (Point2D point : this.pointsSet) {
            if (rect.contains(point)) {
                insideRectangle.add(point);
            }
        }
        return insideRectangle;
    }

    public Point2D nearest(Point2D p) {
        checkNotNull(p);
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : this.pointsSet) {
            double distance = p.distanceTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }
        return nearest;
    }

    public int size() {
        return this.pointsSet.size();
    }

    public static void main(String[] args) {

    }
}
