import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points;

    /**
     * construct an empty set of points
     */
    public PointSET() {
        points = new TreeSet<>();
    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * number of points in the set
     */
    public int size() {
        return points.size();
    }

    /**
     * add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        points.add(p);
    }

    /**
     * does the set contain point p?
     */
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        // Copied from the answer of the question in FAQ:
        // "How should I set the size and color of the points and rectangles when drawing?"
        // http://coursera.cs.princeton.edu/algs4/checklists/kdtree.html
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D p : points) {
            p.draw();
        }
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> ranges = new ArrayList<>();

        for (Point2D p : points) {
            if (rect.contains(p)) {
                ranges.add(p);
            }
        }

        return ranges;
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        double minDist = Double.MAX_VALUE;
        Point2D nearestPoint = null;

        for (Point2D otherPoint : points) {
            double dist = otherPoint.distanceTo(p);

            if (dist < minDist) {
                minDist = dist;
                nearestPoint = otherPoint;
            }
        }

        return nearestPoint;
    }

    /**
     * unit testing of the methods (optional)
     */
    public static void main(String[] args) {

        PointSET set = new PointSET();
        Point2D p1 = new Point2D(0.271484, 0.195313);
        Point2D p2 = new Point2D(0.744141, 0.136719);
        Point2D p3 = new Point2D(0.781250, 0.398438);
        Point2D p4 = new Point2D(0.535156, 0.593750);

        set.insert(p1);
        set.insert(p2);
        set.insert(p3);
        set.insert(p4);

        System.out.println("set.contains(p1) = " + set.contains(p1));
        System.out.println("set.contains(p2) = " + set.contains(p2));

        System.out.println("set.range(new RectHV(0.3, 0.2, 0.8, 0.8)) = " + set.range(new RectHV(0.3, 0.2, 0.8, 0.8)));
    }
}