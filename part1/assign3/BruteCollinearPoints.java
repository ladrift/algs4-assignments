import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> lineSegments = new ArrayList<>();

    /**
     * finds all line segments containing 4 points
     */
    public BruteCollinearPoints(Point[] points) {
        validateNotNull(points);
        for (Point p : points) {
            validateNotNull(p);
        }

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDup(sortedPoints);
        for (int i = 0; i < sortedPoints.length; i++) {
            for (int j = i + 1; j < sortedPoints.length; j++) {
                for (int k = j + 1; k < sortedPoints.length; k++) {
                    for (int l = k + 1; l < sortedPoints.length; l++) {
                        Point p1 = sortedPoints[i];
                        Point p2 = sortedPoints[j];
                        Point p3 = sortedPoints[k];
                        Point p4 = sortedPoints[l];
                        // [p1, p2, p3, p4] is in ascending order.

                        if (isCollinear(p1, p2, p3, p4)) {
                            lineSegments.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }
    }

    private void checkDup(Point[] sortedPoints) {
        for (int i = 0; i < sortedPoints.length - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    private boolean isCollinear(Point p1, Point p2, Point p3, Point p4) {
        double s1 = p1.slopeTo(p2);
        double s2 = p2.slopeTo(p3);
        double s3 = p3.slopeTo(p4);

        return s1 == s2 && s2 == s3;
    }

    /**
     * the number of line segments
     */
    public int numberOfSegments() {
        return lineSegments.size();
    }

    /**
     * the line segments
     */
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    private void validateNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
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
