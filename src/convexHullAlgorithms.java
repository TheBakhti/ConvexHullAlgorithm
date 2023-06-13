import java.util.*;

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class convexHullAlgorithms {
    public static List<Point> giftWrapping(List<Point> points) {
        if (points.size() < 3)
            return null;

        List<Point> convexHull = new ArrayList<>();

        // Find the leftmost point
        int leftmost = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).x < points.get(leftmost).x)
                leftmost = i;
        }

        int current = leftmost;
        int next;
        do {
            convexHull.add(points.get(current));
            next = (current + 1) % points.size();

            for (int i = 0; i < points.size(); i++) {
                if (orientation(points.get(current), points.get(i), points.get(next)) == -1)
                    next = i;
            }

            current = next;
        } while (current != leftmost);

        return convexHull;
    }

    public static List<Point> grahamScan(List<Point> points) {
        if (points.size() < 3)
            return null;

        List<Point> convexHull = new ArrayList<>();
        Stack<Point> stack = new Stack<>();

        // Find the bottommost point
        int bottommost = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).y < points.get(bottommost).y)
                bottommost = i;
            else if (points.get(i).y == points.get(bottommost).y && points.get(i).x < points.get(bottommost).x)
                bottommost = i;
        }

        // Sort the points based on their polar angle with the bottommost point
        int finalBottommost = bottommost;
        Collections.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                double angle1 = Math.atan2(p1.y - points.get(finalBottommost).y, p1.x - points.get(finalBottommost).x);
                double angle2 = Math.atan2(p2.y - points.get(finalBottommost).y, p2.x - points.get(finalBottommost).x);
                if (angle1 < angle2)
                    return -1;
                if (angle1 > angle2)
                    return 1;
                return 0;
            }
        });

        stack.push(points.get(0));
        stack.push(points.get(1));

        for (int i = 2; i < points.size(); i++) {
            while (stack.size() >= 2 &&
                    orientation(stack.get(stack.size() - 2), stack.get(stack.size() - 1), points.get(i)) != -1) {
                stack.pop();
            }
            stack.push(points.get(i));
        }

        while (!stack.isEmpty()) {
            convexHull.add(stack.pop());
        }

        return convexHull;
    }

    public static List<Point> jarvisMarch(List<Point> points) {
        if (points.size() < 3)
            return null;

        List<Point> convexHull = new ArrayList<>();

        int leftmost = 0;
        int n = points.size();

        for (int i = 1; i < n; i++) {
            if (points.get(i).x < points.get(leftmost).x)
                leftmost = i;
        }

        int p = leftmost;
        int q;

        do {
            convexHull.add(points.get(p));
            q = (p + 1) % n;

            for (int i = 0; i < n; i++) {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2)
                    q = i;
            }

            p = q;
        } while (p != leftmost);

        return convexHull;
    }

    public static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0)
            return 0;  // collinear
        return (val > 0) ? 1 : -1;  // clockwise or counterclockwise
    }

    public static void main(String[] args) {
        // Test the algorithms
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 3));
        points.add(new Point(2, 2));
        points.add(new Point(1, 1));
        points.add(new Point(2, 1));
        points.add(new Point(3, 0));
        points.add(new Point(0, 0));
        points.add(new Point(3, 3));

        List<Point> giftWrappingConvexHull = giftWrapping(points);
        List<Point> grahamScanConvexHull = grahamScan(points);
        List<Point> jarvisMarchConvexHull = jarvisMarch(points);

        System.out.println("Gift Wrapping Convex Hull:");
        for (Point p : giftWrappingConvexHull) {
            System.out.println("(" + p.x + ", " + p.y + ")");
        }

        System.out.println("\nGraham Scan Convex Hull:");
        for (Point p : grahamScanConvexHull) {
            System.out.println("(" + p.x + ", " + p.y + ")");
        }

        System.out.println("\nJarvis March Convex Hull:");
        for (Point p : jarvisMarchConvexHull) {
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }
}
