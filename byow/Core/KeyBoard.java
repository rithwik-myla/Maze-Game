package byow.Core;

import byow.ArrayHeapMinPQ;
import byow.Point;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class KeyBoard {
    Point avatar;
    Point end;
    TETile[][] gameBoard;
    ArrayList<Point> walled;
    String totalInput = "";
    TERenderer ter1;
    String seed;
    int numTilesVisited;
    int seedIndex;
    int open = 3;
    Point begin;


    public KeyBoard(Point avatar, Point end, TETile[][] board, ArrayList<Point> walls,
                    TERenderer ter, String seed2, String inputString, int seedIndex1) {
        this.avatar = avatar;
        this.end = end;
        gameBoard = board;
        walled = walls;
        ter1 = ter;
        seed = seed2;
        seedIndex = seedIndex1;
        numTilesVisited = 0;
        begin = new Point(avatar.getX(), avatar.getY());
        if (inputString.length() > 0) {
            getNextKey(inputString, seedIndex);
        }
        getNextKey();

    }

    //Inner KeyBoard Constructor
    public KeyBoard(Point avatar, Point end, TETile[][] board, ArrayList<Point> walls,
                     String seed2, String inputString, int seedIndex1) {
        this.avatar = avatar;
        this.end = end;
        gameBoard = board;
        walled = walls;
        seed = seed2;
        seedIndex = seedIndex1;
        numTilesVisited = 0;
        begin = new Point(avatar.getX(), avatar.getY());
        if (inputString.length() > 0) {
            getNextKey(inputString, seedIndex);
        }
    }

    public void getNextKey(String input, int seedIndex) {
        int index = 0;
        int size = input.length();
        String inputSoFar = "";
        while (index < size) {
            char c = input.charAt(index);
            c = Character.toUpperCase(c);
            inputSoFar += c;

            if (c == ':' && (index + 1 < size)
                    && Character.toUpperCase(input.charAt(index + 1)) == 'Q') {
                numTilesVisited--;
                totalInput = inputSoFar.substring(0, inputSoFar.length() - 1);
                try {
                    FileWriter myWriter = new FileWriter("./Strings.txt");
                    String cutSeed = seed.substring(0, index + seedIndex);
                    myWriter.write(cutSeed);
                    myWriter.close();
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("An error occurred. Line 53 Keyboard.java");
                    e.printStackTrace();
                }
            }
            if (c == 'W') {
                Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                Point checker = new Point(avatar.getX(), avatar.getY() + 1);
                if (!walled.contains(checker)) {
                    numTilesVisited += 1;
                    avatar = checker;
                    gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                    gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                }

            }
            if (c == 'A') {
                Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                Point checker = new Point(avatar.getX() - 1, avatar.getY());
                if (!walled.contains(checker)) {
                    numTilesVisited += 1;
                    avatar = checker;
                    gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                    gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                }
            }
            if (c == 'S') {
                Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                Point checker = new Point(avatar.getX(), avatar.getY() - 1);
                if (!walled.contains(checker)) {
                    numTilesVisited += 1;
                    avatar = checker;
                    gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                    gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                }
            }
            if (c == 'D') {
                Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                Point checker = new Point(avatar.getX() + 1, avatar.getY());
                if (!walled.contains(checker)) {
                    numTilesVisited += 1;
                    avatar = checker;
                    gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                    gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                }
            }
            if (c == 'O') {
                open--;
            }
            index += 1;
        }
    }

    public void getNextKey() {
        while (possibleNextInput()) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                totalInput += c;

                if (c == 'P') {
                    renderBestPath();
                    ter1.renderFrame(gameBoard);
                    StdDraw.pause(5000);
                    int tilesOptimal = aStarBestPath(begin, end).size();
                    int tilesFromHere = aStarBestPath(avatar, end).size();
                    ter1.renderGameOverLoser(numTilesVisited, tilesOptimal, tilesFromHere);
                    startOver();
                    break;
                }

                if (c == 'W') {
                    Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                    Point checker = new Point(avatar.getX(), avatar.getY() + 1);
                    if (!walled.contains(checker)) {
                        numTilesVisited += 1;
                        avatar = checker;
                        gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                        gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                    }

                }
                if (c == 'A') {
                    Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                    Point checker = new Point(avatar.getX() - 1, avatar.getY());
                    if (!walled.contains(checker)) {
                        numTilesVisited += 1;
                        avatar = checker;
                        gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                        gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                    }
                }
                if (c == 'S') {
                    Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                    Point checker = new Point(avatar.getX(), avatar.getY() - 1);
                    if (!walled.contains(checker)) {
                        numTilesVisited += 1;
                        avatar = checker;
                        gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                        gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                    }
                }
                if (c == 'D') {
                    Point previousAvatar = new Point(avatar.getX(), avatar.getY());
                    Point checker = new Point(avatar.getX() + 1, avatar.getY());
                    if (!walled.contains(checker)) {
                        numTilesVisited += 1;
                        avatar = checker;
                        gameBoard[previousAvatar.getX()][previousAvatar.getY()] = Tileset.FLOOR;
                        gameBoard[avatar.getX()][avatar.getY()] = Tileset.PACMAN;
                    }
                }
                if (c == 'O' && open > 0) {
                    open -= 1;
                    ter1.renderFrame(gameBoard);
                    StdDraw.pause(6000);
                }
//                ter1.renderFrame(gameBoard);
                ter1.renderFrameRange(gameBoard, avatar, end, open, numTilesVisited);
                if (avatar.equals(end)) {
                    int optimalPath = aStarBestPath(begin, end).size();
                    ter1.renderGameOver(numTilesVisited, optimalPath);
                    startOver();
                    break;
                }
            }
//            ter1.renderFrame(gameBoard);
            ter1.renderFrameRange(gameBoard, avatar, end, open, numTilesVisited);
        }
    }

    public void startOver() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());

                if (c == 'N') {
                    long seedNum;
                    String intSeed = seed.substring(1, seedIndex - 1);
                    seedNum = Long.parseLong(intSeed);
                    seedNum++;
                    intSeed = Long.toString(seedNum);
                    intSeed = 'N' + intSeed + 'S';
                    Main.main(new String[]{intSeed});
                }
                if (c == 'S') {
                    Main.mainKeyboard();
                    break;
                }
                if (c == 'Z') {
                    System.exit(0);
                    break;
                }
            }
        }
    }

    /*
    @Source: For writing into the file, we used a GeeksForGreeks post linked
    below:
    https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
     */
    public boolean possibleNextInput() {
        int length = totalInput.length();
        if (length > 1 && totalInput.charAt(length - 2) == ':'
                && totalInput.charAt(length - 1) == 'Q') {

            totalInput = totalInput.substring(0, totalInput.length() - 2);

            try {
                FileWriter myWriter = new FileWriter("./Strings.txt");
                myWriter.write(seed + totalInput);
                myWriter.close();
                System.exit(0);
            } catch (IOException e) {
                System.out.println("An error occurred. Line 201 Keyboard.java");
                e.printStackTrace();
            }

            return false;
        }
        return true;
    }

    private List<Point> aStarBestPath(Point avatar, Point end) {
        ArrayHeapMinPQ<Point, Double> distTracker = new ArrayHeapMinPQ<>();
        HashMap<Point, Point> previousPoint = new HashMap<>();
        HashMap<Point, Integer> distToStart = new HashMap<>();

        Point startPoint = avatar;
        Point endPoint = end;

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
                if (checkIfCanGoOptimal(neighbor, distToStart)) {
                    previousPoint.put(neighbor, current);
                    distToStart.put(neighbor, distToStart.get(current) + 1);
                    distTracker.add(neighbor, distToStart.get(neighbor)
                            + estimatedDistance(neighbor, endPoint));
                }
            }
        }

        LinkedList<Point> answer = new LinkedList<>();
        Point place = current;
        while (place != null) {
            answer.addFirst(place);
            place = previousPoint.get(place);
        }
        return answer;
    }

    public void renderBestPath() {
        List<Point> path = aStarBestPath(avatar, end);
        path.remove(avatar);
        for (Point newFloor : path) {
            int x = newFloor.getX();
            int y = newFloor.getY();
            gameBoard[x][y] = Tileset.SAND;
        }
    }

    /*
   This checkIfCanGoOptimal checks if there are walls instead of walls and floors
    */
    private boolean checkIfCanGoOptimal(Point checker, HashMap<Point, Integer> visited) {
        if (checker.getX() < 1 || checker.getY() < 1) {
            return false;
        }
        if (checker.getX() >= gameBoard.length - 1 || checker.getY() >= gameBoard[0].length - 1) {
            return false;
        }
        if (!visited.containsKey(checker) && !walled.contains(checker)) {
            return true;
        }
        return false;
    }

    private double estimatedDistance(Point begin, Point endPnt) {
        return Point.distance(begin, endPnt) * 1.6;
    }

    public static void renderFailedLoad(){
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0.0, 75.0);
        StdDraw.setYscale(0.0, 75.0);
        StdDraw.text(37.5, 42, "You have not saved anything to load");
        StdDraw.text(37.5, 33, "Please enter a new seed");
        StdDraw.show();
        StdDraw.pause(3000);
        Main.mainKeyboard();
    }

    public static void renderSeed() {
        StdDraw.setCanvasSize(500,500);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 75.0);
        StdDraw.setYscale(0.0, 75.0);
        StdDraw.picture(37.5, 37.5, "byow/SeedStart.png", 75, 75);
        StdDraw.show();
    }

    private static void renderHelper(String seed) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0.0, 75.0);
        StdDraw.setYscale(0.0, 75.0);
        StdDraw.text(37.5, 42, "Your Seed: " + seed.substring(1));
        StdDraw.text(37.5, 33, "Press S to submit your seed");
        StdDraw.show();
    }

    public static String renderSeedInteractive() {
        String seed = "N";
        char keyTyped = ' ';
        renderHelper(seed);
        while (keyTyped != 'S' && keyTyped != 's') {
            if (StdDraw.hasNextKeyTyped()) {
                keyTyped = StdDraw.nextKeyTyped();
                seed = seed + keyTyped;
                renderHelper(seed);
            }
        }
        return seed;
    }

    public static void renderRules() {
        StdDraw.setCanvasSize(500,500);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 75.0);
        StdDraw.setYscale(0.0, 75.0);
        StdDraw.picture(37.5, 37.5, "byow/Pacman_Rules.png", 75, 75);
        StdDraw.show();
        StdDraw.pause(5000);
    }


}
