import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private HashMap<Double, List<Point>> slopeToEndPoints = new HashMap<>();

    /**
     * finds all line segments containing 4 or more points
     */
    public FastCollinearPoints(Point[] points) {
        validateNotNull(points);
        for (Point p : points) {
            validateNotNull(p);
        }

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDup(sortedPoints);

        for (int i = 0; i < sortedPoints.length; i++) {
            Arrays.sort(sortedPoints, i, sortedPoints.length);
            Point origin = sortedPoints[i];
            Arrays.sort(sortedPoints, i + 1, sortedPoints.length, origin.slopeOrder());

            int count = 0;
            double prevSlope = Double.NaN;
            Point prevPoint = origin;
            for (int j = i + 1; j < sortedPoints.length; j++) {
                Point point = sortedPoints[j];
                double slope = origin.slopeTo(point);

                if (Double.compare(slope, prevSlope) == 0) {
                    count += 1;
                } else {
                    if (count >= 3) {
                        addIfNotExisted(origin, prevPoint, prevSlope);
                    }

                    prevSlope = slope;
                    count = 1;
                }
                prevPoint = point;
            }

            if (count >= 3) {
                addIfNotExisted(origin, prevPoint, prevSlope);
            }
        }
    }

    private void addIfNotExisted(Point start, Point end, double slope) {
        List<Point> existedPoints = slopeToEndPoints.get(slope);
        if (existedPoints == null) {
            existedPoints = new ArrayList<>();
            existedPoints.add(end);
            slopeToEndPoints.put(slope, existedPoints);

            lineSegments.add(new LineSegment(start, end));
        } else {
            if (!existedPoints.contains(end)) {
                existedPoints.add(end);

                lineSegments.add(new LineSegment(start, end));
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

    private void validateNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
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
        return lineSegments.toArray(new LineSegment[]{});
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
