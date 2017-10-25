import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.lb = null;
            this.rt = null;
        }
    }

    private Node root;
    private int size;

    /**
     * construct an empty set of points
     */
    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        checkNotNull(p);
        if (root == null) {
            root = new Node(p, new RectHV(p.x(), 0, p.x(), 1));
            size += 1;
        } else {
            root = insertByX(p, root);
        }
    }

    private void checkNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Cannot pass null");
        }
    }

    private Node insertByX(Point2D p, Node root) {
        if (p.equals(root.p)) {
            return root;
        }

        if (p.x() < root.p.x()) {
            if (root.lb == null) {
                root.lb = new Node(p, new RectHV(0, p.y(), root.p.x(), p.y()));
                size += 1;
            } else {
                root.lb = insertByY(p, root.lb);
            }
        } else {
            if (root.rt == null) {
                root.rt = new Node(p, new RectHV(root.p.x(), p.y(), 1, p.y()));
                size += 1;
            } else {
                root.rt = insertByY(p, root.rt);
            }
        }

        return root;
    }

    private Node insertByY(Point2D p, Node root) {
        if (p.equals(root.p)) {
            return root;
        }

        if (p.y() < root.p.y()) {
            if (root.lb == null) {
                root.lb = new Node(p, new RectHV(p.x(), 0, p.x(), root.p.y()));
                size += 1;
            } else {
                root.lb = insertByX(p, root.lb);
            }
        } else {
            if (root.rt == null) {
                root.rt = new Node(p, new RectHV(p.x(), root.p.y(), p.x(), 1));
                size += 1;
            } else {
                root.rt = insertByX(p, root.rt);
            }
        }

        return root;
    }

    /**
     * does the set contain point p?
     */
    public boolean contains(Point2D p) {
        checkNotNull(p);
        return containsByX(p, root);
    }

    private boolean containsByX(Point2D p, Node root) {
        if (root == null) {
            return false;
        }

        if (root.p.equals(p)) {
            return true;
        }

        if (p.x() < root.p.x()) {
            return containsByY(p, root.lb);
        } else {
            return containsByY(p, root.rt);
        }
    }

    private boolean containsByY(Point2D p, Node root) {
        if (root == null) {
            return false;
        }

        if (root.p.equals(p)) {
            return true;
        }

        if (p.y() < root.p.y()) {
            return containsByX(p, root.lb);
        } else {
            return containsByX(p, root.rt);
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        drawX(root);
    }

    private void drawX(Node root) {
        if (root == null) {
            return;
        }

        drawLineX(root.rect);
        drawPoint(root.p);
        drawY(root.lb);
        drawY(root.rt);
    }

    private void drawLineX(RectHV rect) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();

        rect.draw();
    }

    private void drawY(Node root) {
        if (root == null) {
            return;
        }

        drawLineY(root.rect);
        drawPoint(root.p);
        drawX(root.lb);
        drawX(root.rt);
    }

    private void drawLineY(RectHV rect) {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();

        rect.draw();
    }

    private void drawPoint(Point2D p) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        p.draw();
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        checkNotNull(rect);
        List<Point2D> points = new ArrayList<>();
        rangeByX(rect, points, root);
        return points;
    }

    private void rangeByX(RectHV rect, List<Point2D> points, Node root) {
        if (root == null) {
            return;
        }

        if (rect.contains(root.p)) {
            points.add(root.p);
        }

        if (rect.xmax() < root.p.x()) {
            rangeByY(rect, points, root.lb);
            return;
        }

        if (rect.xmin() > root.p.x()) {
            rangeByY(rect, points, root.rt);
            return;
        }

        rangeByY(rect, points, root.lb);
        rangeByY(rect, points, root.rt);
    }

    private void rangeByY(RectHV rect, List<Point2D> points, Node root) {
        if (root == null) {
            return;
        }

        if (rect.contains(root.p)) {
            points.add(root.p);
        }

        if (rect.ymax() < root.p.y()) {
            rangeByX(rect, points, root.lb);
            return;
        }

        if (rect.ymin() > root.p.y()) {
            rangeByX(rect, points, root.rt);
            return;
        }

        rangeByX(rect, points, root.lb);
        rangeByX(rect, points, root.rt);
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        checkNotNull(p);

        if (root == null) {
            return null;
        }

        Result res = nearestByX(p, root);

        return res.p;
    }

    private static class Result {
        Point2D p;
        double distSquared;

        Result(Point2D p, double distSquared) {
            this.p = p;
            this.distSquared = distSquared;
        }
    }

    private Result nearestByX(Point2D p, Node root) {
        Result nearest = new Result(root.p, root.p.distanceSquaredTo(p));

        if (root.lb == null && root.rt == null) {
            return nearest;
        }

        if (root.lb == null) {
            Result rtResult = nearestByY(p, root.rt);
            if (rtResult.distSquared < nearest.distSquared) {
                return rtResult;
            } else {
                return nearest;
            }
        }

        if (root.rt == null) {
            Result lbResult = nearestByY(p, root.lb);
            if (lbResult.distSquared < nearest.distSquared) {
                return lbResult;
            } else {
                return nearest;
            }
        }

        // Search the closer side first
        if (p.x() < root.p.x()) {
            Result lbResult = nearestByY(p, root.lb);
            nearest = nearestResult(lbResult, nearest);
            if (nearest.distSquared < root.rect.distanceSquaredTo(p)) {
                // pruning other side
                return nearest;
            }

            Result rtResult = nearestByY(p, root.rt);
            nearest = nearestResult(rtResult, nearest);
            return nearest;
        } else {
            Result rtResult = nearestByY(p, root.rt);
            nearest = nearestResult(rtResult, nearest);
            if (nearest.distSquared < root.rect.distanceSquaredTo(p)) {
                // pruning other side
                return nearest;
            }

            Result lbResult = nearestByY(p, root.lb);
            nearest = nearestResult(lbResult, nearest);
            return nearest;
        }
    }

    private Result nearestResult(Result r1, Result r2) {
        return r1.distSquared < r2.distSquared ? r1 : r2;
    }

    private Result nearestByY(Point2D p, Node root) {
        Result nearest = new Result(root.p, root.p.distanceSquaredTo(p));

        if (root.lb == null && root.rt == null) {
            return nearest;
        }

        if (root.lb == null) {
            Result rtResult = nearestByX(p, root.rt);
            if (rtResult.distSquared < nearest.distSquared) {
                return rtResult;
            } else {
                return nearest;
            }
        }

        if (root.rt == null) {
            Result lbResult = nearestByX(p, root.lb);
            if (lbResult.distSquared < nearest.distSquared) {
                return lbResult;
            } else {
                return nearest;
            }
        }

        // Search the closer side first
        if (p.y() < root.p.y()) {
            Result lbResult = nearestByX(p, root.lb);
            nearest = nearestResult(lbResult, nearest);
            if (nearest.distSquared < root.rect.distanceSquaredTo(p)) {
                // pruning other side
                return nearest;
            }

            Result rtResult = nearestByX(p, root.rt);
            nearest = nearestResult(rtResult, nearest);
            return nearest;
        } else {
            Result rtResult = nearestByX(p, root.rt);
            nearest = nearestResult(rtResult, nearest);
            if (nearest.distSquared < root.rect.distanceSquaredTo(p)) {
                // pruning other side
                return nearest;
            }

            Result lbResult = nearestByX(p, root.lb);
            nearest = nearestResult(lbResult, nearest);
            return nearest;
        }
    }

    /**
     * unit testing of the methods (optional)
     */
    public static void main(String[] args) {
        /*
        KdTree tree = new KdTree();

        Point2D p1 = new Point2D(0.2, 0.2);
        Point2D p2 = new Point2D(0.6, 0.4);

        tree.insert(p1);
//        tree.insert(p2);

        System.out.println("tree.contains(p1) = " + tree.contains(p1));
        System.out.println("tree.contains(p2) = " + tree.contains(p2));
        */

        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(0.271484, 0.195313);
        Point2D p2 = new Point2D(0.744141, 0.136719);
        Point2D p3 = new Point2D(0.781250, 0.398438);
        Point2D p4 = new Point2D(0.535156, 0.593750);

        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);
        tree.insert(p4);

//        tree.insert(null);
    }
}