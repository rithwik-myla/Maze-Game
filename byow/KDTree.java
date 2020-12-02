package byow;

import java.util.List;

/*
KDTree class
 */
public class KDTree {
    private List<Point> listPoints;
    private Node root;


    /*
    @Professor Hug
    The video demo for this class helped.
    For instance, having the left and right direction.
     */
    private class Node {
        private int x;
        private int y;
        private boolean xDirection;
        private Node left;
        private Node right;

        private Node(Point value, boolean horizontal) {
            x = value.getX();
            y = value.getY();
            xDirection = horizontal;
        }

        private double distance(Point other) {
            Point currentPoint = new Point(this.x, this.y);
            return Point.distance(currentPoint, other);
        }

        private Point nodeToPoint() {
            return new Point(this.x, this.y);
        }

        /*
        Comparator for a Node.
        Checks first which direction (x or y) to look at,
        and, then, returns greater than 1 if the Node has
        the higher value. Otherwise, returns less than 1.
        Returns 0 if they have the same value.
         */
        private double compareTo(Point point) {
            if (this.xDirection) {
                return this.x - point.getX();
            }
            return this.y - point.getY();
        }
    }


    public KDTree(List<Point> points) {
        listPoints = points;
        root = new Node(listPoints.get(0), true);
        for (int i = 1; i < points.size(); i++) {
            insert(listPoints.get(i));
        }
    }

    private void insert(Point insertion) {
        insertHelper(root, insertion, true);
    }

    private void insertHelper(Node place, Point insertion, boolean xDirection) {
        if (xDirection) {
            if (place.x <= insertion.getX()) {
                if (place.right == null) {
                    if (place.y != insertion.getY()
                            || place.x != insertion.getX()) {
                        place.right = new Node(insertion, false);
                    }
                } else {
                    insertHelper(place.right, insertion, false);
                }
            } else {
                if (place.left == null) {
                    place.left = new Node(insertion, false);
                } else {
                    insertHelper(place.left, insertion, false);
                }
            }
        } else {
            if (place.y <= insertion.getY()) {
                if (place.right == null) {
                    if (place.y != insertion.getY()
                            || place.x != insertion.getX()) {
                        place.right = new Node(insertion, true);
                    }
                } else {
                    insertHelper(place.right, insertion, true);
                }
            } else {
                if (place.left == null) {
                    place.left = new Node(insertion, true);
                } else {
                    insertHelper(place.left, insertion, true);
                }
            }
        }
    }

    /*
    @Professor Hug.
    The slides with the pseudocode for this helped me tremendously
    in the creation of this method.
     */
    public Point nearest(int x, int y) {
        Point goal = new Point(x, y);
        return nearestHelper(root, goal, root).nodeToPoint();
    }

    /*
    @Professor Hug.
    The slides with the pseudocode for this helped me tremendously
    in the creation of this method.
     */
    private Node nearestHelper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }

        if (n.distance(goal) < best.distance(goal)) {
            best = n;
        }

        if (n.compareTo(goal) > 0) {
            Node goodSide = n.left;
            Node badSide = n.right;
            String bSide = "right";
            best = nearestHelper(goodSide, goal, best);

            /*
            if (goodSide != null) {
                System.out.println("Checked and x-coordinate is "
                + goodSide.x + " "  + goodSide.y);
            }
             */


            double currentBest = Point.distance(best.nodeToPoint(), goal);
            if (bestBadSide(n.xDirection, bSide, goal, n, currentBest)) {
                best = nearestHelper(badSide, goal, best);

                /*
                if (badSide != null) {
                    System.out.println("Checked and x-coordinate is "
                    + badSide.x + " "  + badSide.y);
                }
                */

            }
        } else {
            Node goodSide = n.right;
            Node badSide = n.left;
            String bSide = "left";
            best = nearestHelper(goodSide, goal, best);

            /*
            Pruning Print Test
            if (goodSide != null) {
                System.out.println("Checked and x-coordinate is " +
                goodSide.x + " "  + goodSide.y);
            }
             */

            double currentBest = Point.distance(best.nodeToPoint(), goal);
            if (bestBadSide(n.xDirection, bSide, goal, n, currentBest)) {
                best = nearestHelper(badSide, goal, best);

                /*
                This is a printing test to make sure it's pruning
                the right things

                if (badSide != null) {
                    System.out.println("Checked and
                    x-coordinate is " + badSide.x + " "  + badSide.y);
                }
                 */


            }
        }

        return best;
    }

    private boolean bestBadSide(boolean xDirection, String bSide, Point goal,
                                 Node current, double currentBest) {
        if (xDirection) {
            Point bPoint = new Point(current.x, goal.getY());
            double bDistance = Point.distance(bPoint, goal);
            return bDistance < currentBest;
        } else {
            Point bPoint = new Point(goal.getX(), current.y);
            double bDistance = Point.distance(bPoint, goal);
            return bDistance < currentBest;
        }
    }
}
