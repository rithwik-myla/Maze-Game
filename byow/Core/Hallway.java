package byow.Core;
import java.util.*;

import byow.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hallway {
    /*
    Boolean array keeping track of which pair of
    points are possible to access:
    True means that we CANNOT go through it
    False means we CAN
     */
//    private boolean[][] notAccessible;
//    Rectangle lastRectangle;
//    int seedNum;
//    Random random;
//    private List<Rectangle> visitedRectangles;


    private TETile[][] gameBoard;
    /*
    Keeps track of rectangles and which ones we're done with.
     */
    private List<Rectangle> allRectangles;
    private List<Rectangle> notVisitedRectangles;

    /*
    Keeps track of wall and floor positions.
    True = the position is not open
    False = the position is open
     */
    private boolean[][] filled;

    /*
    Hashmap to map closings to rectangles for the AStar method
    List of closings that have not been closed by a hallway
    WeirdPointSet to find the nearest rectangle to build a hallway to
     */
    private HashMap<Point, Rectangle> closingToRectangleMap;
    private ArrayList<Point> notVisitedClosings;

    private WeirdPointSet nearestFinder;
    private KDTree nearestFinder2;

    protected ArrayList<Point> walled;

    private Random random;
    protected Point avatar;
    protected Point end;


/*
Hallway constructor that initializes all instance variables and runs the makeHallway
 method to create all hallways
 */
    public Hallway(ArrayList<Rectangle> rectangles, long seed, TETile[][] board,
                   boolean[][] notAccessible, ArrayList<Point> walls) {
        //visitedRectangles = new ArrayList<>();
        random = new Random(seed);
        closingToRectangleMap = new HashMap<>();
        notVisitedClosings = new ArrayList<>();
        gameBoard = board;
        notVisitedRectangles = rectangles;
        allRectangles = rectangles;
        filled = notAccessible;
        walled = walls;
        for (Rectangle current : rectangles) {
            Point closing = new Point(current.getClosingX(), current.getClosingY());
            closingToRectangleMap.put(closing, current);
            notVisitedClosings.add(closing);
        }

        Rectangle first = rectangles.get(0);
        Rectangle last = first;
        int num = allRectangles.size() - 1;


        for (int i = 0; i < num; i++) {
            first = makeHallway(first);
        }

        int lastHallways = RandomUtils.uniform(random, 10, 20);
        for (int i = 0; i < lastHallways; i++) {
            List<Point> path = createLastHallways();
            createFloor(path);
            createWalls(path);
        }

        Point thing;
        Point thing2;
        if (surrounded(last.getClosingX(), last.getClosingY())) {
            thing = new Point(last.getClosingX(), last.getClosingY());
            gameBoard[last.getClosingX()][last.getClosingY()] = Tileset.PACMAN;
        } else {
            thing = new Point(last.getClosingX(), last.getClosingY());
            gameBoard[last.getClosingX()][last.getClosingY()] = Tileset.PACMAN;
        }

        if (surrounded(first.getOpeningX(), first.getOpeningY())) {
            thing2 = new Point(first.getOpeningX(), first.getOpeningY());
            gameBoard[first.getOpeningX()][first.getOpeningY()] = Tileset.CHERRY;
        } else {
            thing2 = new Point(first.getOpeningX(), first.getOpeningY());
            thing2 = new Point(first.getOpeningX(), first.getOpeningY());
            gameBoard[first.getOpeningX()][first.getOpeningY()] = Tileset.CHERRY;
        }

        ArrayList<Point> path = new ArrayList<>();
        path.add(thing);
        path.add(thing2);
        createWalls(path);

        avatar = thing;
        end = thing2;
    }


    /*
    This method takes a rectangle and makes a hallway to the rectangle closest to it
     */
    private Rectangle makeHallway(Rectangle first) {
        Rectangle nearest = nearestRectangle(first);
        List<Point> path = aStar(first, nearest);
        createFloor(path);
        createWalls(path);
        return nearest;
    }

    /*
    This function finds the nearest rectangle to the given one using a WeirdPointSet
     */
    private Rectangle nearestRectangle(Rectangle start) {
        Point startOpening = new Point(start.getOpeningX(), start.getOpeningY());
        Point startClosing = new Point(start.getClosingX(), start.getClosingY());
        notVisitedClosings.remove(startClosing);
//        nearestFinder = new WeirdPointSet(notVisitedClosings);
        nearestFinder2 = new KDTree(notVisitedClosings);
        Point endClosing = nearestFinder2.nearest(startOpening.getX(), startOpening.getY());
        Rectangle nearest = closingToRectangleMap.get(endClosing);
        return nearest;
    }


    /*
    AStar algorithm that takes in a starting and ending rectangle and
    finds the shortest possible path from
    the start rectangle's opening to the end rectangle's closing
    Returns a list of points that will be the floor of the hallway
     */
    private List<Point> aStar(Rectangle start, Rectangle endRect) {
        ArrayHeapMinPQ<Point, Double> distTracker = new ArrayHeapMinPQ<>();
        HashMap<Point, Point> previousPoint = new HashMap<>();
        HashMap<Point, Integer> distToStart = new HashMap<>();

        Point startPoint = new Point(start.getOpeningX(), start.getOpeningY());
        Point endPoint = new Point(endRect.getClosingX(), endRect.getClosingY());

        distTracker.add(startPoint, estimatedDistance(startPoint, endPoint));
        previousPoint.put(startPoint, null);
        distToStart.put(startPoint, 0);

        Point current = startPoint;
        while (distTracker.size() != 0 && Point.distance(current, endPoint) > 1.0) {
            current = distTracker.removeSmallest();
            List<Point> neighbors = new ArrayList<>();
            Point right = new Point(current.getX() + 1, current.getY());
            Point left = new Point(current.getX() - 1, current.getY());
            Point up = new Point(current.getX(), current.getY() + 1);
            Point down = new Point(current.getX(), current.getY() - 1);
            neighbors.add(right);
            neighbors.add(left);
            neighbors.add(up);
            neighbors.add(down);

            for (Point neighbor : neighbors) {
                if (checkIfCanGo(neighbor, distToStart)) {
                    previousPoint.put(neighbor, current);
                    distToStart.put(neighbor, distToStart.get(current) + 1);
                    distTracker.add(neighbor, distToStart.get(neighbor)
                            + estimatedDistance(neighbor, endPoint));
                }
            }
        }

        LinkedList<Point> answer = new LinkedList<>();
        if (Point.distance(current, endPoint) == 1.0) {
            Point place = current;
            while (place != null) {
                answer.addFirst(place);
                place = previousPoint.get(place);
            }
        } else {
            walled.remove(start.generateRandomOpening(gameBoard, filled));
            answer = (LinkedList<Point>) aStar(start, endRect);
        }
        return answer;
    }

    /*
    Heuristic function for the AStar algorithm above
     */
    private double estimatedDistance(Point begin, Point endPnt) {
        return Point.distance(begin, endPnt) * 1.6;
    }

    /*
    This function checks if @param checker is a valid point to visit
    It checks if the point has been visited already and if it is in scope or the board
    Places a buffer of 1 around the board for the wall to be placed
     */
    private boolean checkIfCanGo(Point checker, HashMap<Point, Integer> visited) {
        if (checker.getX() < 1 || checker.getY() < 1) {
            return false;
        }
        if (checker.getX() >= filled.length - 2 || checker.getY() >= filled[0].length - 2) {
            return false;
        }
        if (!visited.containsKey(checker) && !filled[checker.getX()][checker.getY()]) {
            return true;
        }
        return false;
    }



    /*
    This function takes the list from the AStar
     and places floor tiles along the path for the hallway
     */
    private void createFloor(List<Point> points) {
        for (Point newFloor : points) {
            int x = newFloor.getX();
            int y = newFloor.getY();
            gameBoard[x][y] = Tileset.FLOOR;
            if (walled.contains(newFloor)) {
                walled.remove(newFloor);
            }
            filled[x][y] = true;
        }
    }

    /*
    This function takes the floor of the hallway and places a wall around the entire hallway
     */
    private void createWalls(List<Point> points) {
        for (Point newFloor : points) {
            int x = newFloor.getX();
            int y = newFloor.getY();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Point wall = new Point(x + i, y + j);
                    if (checkInBounds(wall) && !filled[x + i][y + j]) {
                        filled[x + i][y + j] = true;
                        gameBoard[x + i][y + j] = Tileset.WALL;
                        walled.add(wall);
                    }
                }
            }
        }
    }


    private ArrayList<Point> createLastHallways() {
        ArrayList<Point> path = new ArrayList<>();

        int num = RandomUtils.uniform(random, 0, walled.size());
        Point wall = walled.get(num);


        if (!checkInBounds(wall) || surrounded(wall.getX(), wall.getY()) || checkIsCorner(wall)) {
            path = createLastHallways();
        } else {
            String whichWay = checkWhichWay(wall);
            path = makeFinalHallwayHelper(wall, whichWay);
        }

        return path;

    }

    private boolean containsSomethingOnDirection(Point wall, String direction) {
        if (direction.equals("right")) {
            int startX = wall.getX();
            for (int i = startX + 1; i < gameBoard.length - 2; i++) {
                if (filled[i][wall.getY()]) {
                    return true;
                }
            }
            return false;
        } else if (direction.equals("left")) {
            int startX = wall.getX();
            for (int i = startX - 1; i > 0; i--) {
                if (filled[i][wall.getY()]) {
                    return true;
                }
            }
            return false;
        } else if (direction.equals("up")) {
            int startY = wall.getY();
            for (int i = startY + 1; i < gameBoard[0].length - 2; i++) {
                if (filled[wall.getX()][i]) {
                    return true;
                }
            }
            return false;
        } else {
            int startY = wall.getY();
            for (int i = startY - 1; i > 0; i--) {
                if (filled[wall.getX()][i]) {
                    return true;
                }
            }
            return false;
        }
    }

    private ArrayList<Point> makeFinalHallwayHelper(Point wall, String direction) {
        ArrayList<Point> answer = new ArrayList<>();
        if (!containsSomethingOnDirection(wall, direction)) {
            return createLastHallways();
        }
        Point current = wall;
        walled.remove(wall);
        gameBoard[current.getX()][current.getY()] = Tileset.FLOOR;
        if (direction.equals("right")) {
            current = new Point(current.getX() + 1, current.getY());
            while (!isFilled(current)) {
                answer.add(current);
                current = new Point(current.getX() + 1, current.getY());
            }
        } else if (direction.equals("left")) {
            current = new Point(current.getX() - 1, current.getY());
            while (!isFilled(current)) {
                answer.add(current);
                current = new Point(current.getX() - 1, current.getY());
            }
        } else if (direction.equals("up")) {
            current = new Point(current.getX(), current.getY() + 1);
            while (!isFilled(current)) {
                answer.add(current);
                current = new Point(current.getX(), current.getY() + 1);
            }
        } else {
            current = new Point(current.getX(), current.getY() - 1);
            while (!isFilled(current)) {
                answer.add(current);
                current = new Point(current.getX(), current.getY() - 1);
            }
        }
        answer.add(current);
        gameBoard[current.getX()][current.getY()] = Tileset.FLOOR;
        return answer;
    }

    private String checkWhichWay(Point wall) {
        Point up = new Point(wall.getX(), wall.getY() + 1);
        Point down = new Point(wall.getX(), wall.getY() - 1);
        Point left = new Point(wall.getX() - 1, wall.getY());
        Point right = new Point(wall.getX() + 1, wall.getY());
        if (!isFilled(right)) {
            return "right";
        } else if (!isFilled(left)) {
            return "left";
        } else if (!isFilled(up)) {
            return "up";
        } else {
            return "down";
        }
    }


    private boolean isFilled(Point checker) {
        return filled[checker.getX()][checker.getY()];
    }

    private boolean surrounded(int x, int y) {
        if (filled[x + 1][y] && filled[x][y + 1] && filled[x - 1][y] && filled[x][y - 1]) {
            return true;
        }
        return false;
    }

    private boolean checkInBounds(Point wall) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (wall.getX() + i >= gameBoard.length || wall.getX() + i < 0) {
                    return false;
                }
                if (wall.getY() + j >= gameBoard[0].length || wall.getY() + j < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIsCorner(Point wall) {
        Point up = new Point(wall.getX(), wall.getY() + 1);
        Point down = new Point(wall.getX(), wall.getY() - 1);
        Point left = new Point(wall.getX() - 1, wall.getY());
        Point right = new Point(wall.getX() + 1, wall.getY());
        if ((gameBoard[up.getX()][up.getY()].equals(Tileset.WALL)
                || gameBoard[down.getX()][down.getY()].equals(Tileset.WALL))
                && (gameBoard[left.getX()][left.getY()].equals(Tileset.WALL)
                || gameBoard[right.getX()][right.getY()].equals(Tileset.WALL))) {
            return true;
        }
        return false;
    }
}
