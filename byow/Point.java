package byow;
import java.util.Objects;

/*
@Source
This is copied verbatim from the earlier project within the PointSetClass. It's from
the KDTree code provided to us. MAKE SURE TO DOUBLE CHECK THIS
 */
public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Returns the great-circle (haversine) distance between geographic coordinates
     * (LATV, LONV) and (LATW, LONW).
     *
     * @source Kevin Lowe & Antares Chen, and https://www.movable-type.co.uk/scripts/latlong.html
     **/
    private static double distance(int p1X, int p2X, int p1Y, int p2Y) {
        double xDiff = p1X - p2X;
        double xDist = Math.pow(xDiff, 2.0);
        double yDiff = p1Y - p2Y;
        double yDist = Math.pow(yDiff, 2.0);
        double answer = Math.sqrt(xDist + yDist);
        return answer;
//        double phi1 = Math.toRadians(latV);
//        double phi2 = Math.toRadians(latW);
//        double dphi = Math.toRadians(latW - latV);
//        double dlambda = Math.toRadians(lonW - lonV);
//
//        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
//        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return 3963 * c;
    }

    /**
     * Returns the haversine distance squared between two points, assuming
     * x represents the longitude and y represents the latitude.
     */
    public static double distance(Point p1, Point p2) {
        return distance(p1.getX(), p2.getX(), p1.getY(), p2.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0
                && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point x: %.10f, y: %.10f", x, y);
    }
}
