/* *****************************************************************************
 *  Name:              Franklin Velasquez
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    private class Node {
        private Point2D point;
        private Node left;
        private Node right;

        public Node(Point2D point) {
            this.point = point;
        }
    }

    public void insert(Point2D p) {
        checkNotNull(p);
        this.root = insert(this.root, p, 1);
    }

    private Node insert(Node node, Point2D point, int level) {
        if (node == null) {
            this.size++;
            return new Node(point);
        }
        if (node.point.equals(point)) {
            return node;
        }
        // odd numbers : 1,3,5... etc compare by X-coordinate
        if (level % 2 != 0) {
            int compare = Point2D.X_ORDER.compare(point, node.point);
            if (compare < 0) {
                // to the left
                node.left = insert(node.left, point, ++level);
            }
            else {
                // no the right
                node.right = insert(node.right, point, ++level);
            }
        }
        else {
            // even numbers: 2,4,6 ...etc compare by Y-coordinate
            int compare = Point2D.Y_ORDER.compare(point, node.point);
            if (compare < 0) {
                // to the bottom
                node.left = insert(node.left, point, ++level);
            }
            else {
                // no the top
                node.right = insert(node.right, point, ++level);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        checkNotNull(p);
        Node current = this.root;
        int level = 1;
        while (current != null) {
            if (current.point.equals(p)) {
                return true;
            }
            Comparator<Point2D> comparator = level % 2 != 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int compare = comparator.compare(p, current.point);
            current = compare < 0 ? current.left : current.right;
            level++;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void draw() {
        draw(this.root, 1, new Point2D(0, 0), new Point2D(1, 1));
    }

    private void draw(Node current, int level, Point2D lowPoint, Point2D highPoint) {
        if (current == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        current.point.draw();
        StdDraw.setPenRadius(0.002);
        if (level % 2 != 0) {
            Point2D updatedLow = new Point2D(current.point.x(), lowPoint.y());
            Point2D updatedHigh = new Point2D(current.point.x(), highPoint.y());
            StdDraw.setPenColor(StdDraw.RED);
            updatedLow.drawTo(updatedHigh);
            draw(current.left, level + 1, lowPoint, updatedHigh);
            draw(current.right, level + 1, updatedLow, highPoint);
        }
        else {
            Point2D updatedLow = new Point2D(lowPoint.x(), current.point.y());
            Point2D updatedHigh = new Point2D(highPoint.x(), current.point.y());
            StdDraw.setPenColor(StdDraw.BLUE);
            updatedLow.drawTo(updatedHigh);
            draw(current.left, level + 1, lowPoint, updatedHigh);
            draw(current.right, level + 1, updatedLow, highPoint);
        }

    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNotNull(rect);
        List<Point2D> intersectPoints = new ArrayList<>();
        range(this.root, 1, new Value2D(0, 0), new Value2D(1, 1), rect, intersectPoints);
        return intersectPoints;
    }

    private class Value2D {
        private double x;
        private double y;

        public Value2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }
    }


    private void range(Node current, int level, Value2D lowPoint, Value2D highPoint, RectHV rect,
                       List<Point2D> intersectsPoints) {
        if (current == null) {
            return;
        }

        if (rect.contains(current.point)) {
            intersectsPoints.add(current.point);
        }

        if (level % 2 != 0) {  // odd number, vertical division line
            Value2D updatedLow = new Value2D(current.point.x(), lowPoint.y());
            Value2D updatedHigh = new Value2D(current.point.x(), highPoint.y());

            RectHV leftRec = new RectHV(lowPoint.x(), lowPoint.y(), updatedHigh.x(),
                                        updatedHigh.y());
            if (current.left != null && leftRec.intersects(rect)) {
                range(current.left, level + 1, lowPoint, updatedHigh, rect, intersectsPoints);
            }

            RectHV rightRec = new RectHV(updatedLow.x(), updatedLow.y(), highPoint.x(),
                                         highPoint.y());
            if (current.right != null && rightRec.intersects(rect)) {
                range(current.right, level + 1, updatedLow, highPoint, rect, intersectsPoints);
            }
        }
        else { // even number, horizontal division line
            Value2D updatedLow = new Value2D(lowPoint.x(), current.point.y());
            Value2D updatedHigh = new Value2D(highPoint.x(), current.point.y());
            RectHV bottomRect = new RectHV(lowPoint.x(), lowPoint.y(), updatedHigh.x(),
                                           updatedHigh.y());
            if (current.left != null && bottomRect.intersects(rect)) {
                range(current.left, level + 1, lowPoint, updatedHigh, rect, intersectsPoints);
            }

            RectHV upperRect = new RectHV(updatedLow.x(), updatedLow.y(), highPoint.x(),
                                          highPoint.y());
            if (current.right != null && upperRect.intersects(rect)) {
                range(current.right, level + 1, updatedLow, highPoint, rect, intersectsPoints);
            }
        }
    }


    public Point2D nearest(Point2D p) {
        checkNotNull(p);
        return nearest(this.root, 1, new Point2D(0, 0), new Point2D(1, 1), new Point2D(2, 2),
                       p);
    }


    private Point2D nearest(Node current, int level, Point2D lowPoint, Point2D highPoint,
                            Point2D closest, Point2D p) {
        if (current == null) {
            return closest;
        }
        double currentDistance = current.point.distanceTo(p);
        double minDistance = closest.distanceTo(p);
        if (currentDistance < minDistance) {
            closest = current.point;
            minDistance = currentDistance;
        }

        double xLow = level % 2 != 0 ? current.point.x() : lowPoint.x();
        double yLow = level % 2 != 0 ? lowPoint.y() : current.point.y();
        Point2D updatedLow = new Point2D(xLow, yLow);
        double xHigh = level % 2 != 0 ? current.point.x() : highPoint.x();
        double yHigh = level % 2 != 0 ? highPoint.y() : current.point.y();
        Point2D updatedHigh = new Point2D(xHigh, yHigh);

        RectHV leftRec = new RectHV(lowPoint.x(), lowPoint.y(), updatedHigh.x(),
                                    updatedHigh.y());

        RectHV rightRec = new RectHV(updatedLow.x(), updatedLow.y(), highPoint.x(),
                                     highPoint.y());

        if (current.left != null) {
            Point2D nearLeft = nearest(current.left, level + 1, lowPoint, updatedHigh,
                                       closest, p);
            if (nearLeft.distanceTo(p) < minDistance) {
                closest = nearLeft;
                minDistance = nearLeft.distanceTo(p);
            }
            if (current.right != null && rightRec.distanceTo(p) < minDistance) {
                Point2D nearRight = nearest(current.right, level + 1, updatedLow, highPoint,
                                            closest, p);
                if (nearRight.distanceTo(p) < minDistance) {
                    closest = nearRight;
                    minDistance = nearRight.distanceTo(p);
                }
            }
        }
        else if (current.right != null) {
            Point2D nearRight = nearest(current.right, level + 1, updatedLow, highPoint,
                                        closest, p);
            if (nearRight.distanceTo(p) < minDistance) {
                closest = nearRight;
                minDistance = nearRight.distanceTo(p);
            }
            if (current.left != null && leftRec.distanceTo(p) < minDistance) {
                Point2D nearLeft = nearest(current.left, level + 1, lowPoint, updatedHigh,
                                           closest, p);
                if (nearLeft.distanceTo(p) < minDistance) {
                    closest = nearLeft;
                    minDistance = nearLeft.distanceTo(p);
                }
            }
        }

        return closest;
    }


    private void checkNotNull(Object p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        System.out.println(kdTree.contains(new Point2D(0.704, 0.972)));

        // draw the points
        StdDraw.clear();
        kdTree.draw();


        //draw rect
        // StdDraw.setPenColor(StdDraw.GREEN);
        // RectHV rectHV = new RectHV(0.5, 0, 1, 1);
        // rectHV.draw();
        // Iterable<Point2D> inRange = kdTree.range(rectHV);
        // for (Point2D point : inRange) {
        //     StdDraw.setPenColor(StdDraw.ORANGE);
        //     StdDraw.setPenRadius(0.02);
        //     point.draw();
        // }

        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.GREEN);
        Point2D target = new Point2D(0.704, 0.972);
        target.draw();

        StdDraw.show();

        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.ORANGE);
        Point2D nearest = kdTree.nearest(target);
        nearest.draw();


    }


}
