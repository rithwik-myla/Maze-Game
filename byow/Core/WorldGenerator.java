package byow.Core;
import byow.Point;
import byow.Rectangle;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {
    private ArrayList<Rectangle> allRooms;

    /*
    How big the overall board is.
    Width = x-axis
    Height = y-axis
     */
    private TETile[][] gameBoard;
    private int width;
    private int height;

    /*
    boolean 2d array of if each spot is already occupied
     */
    private boolean[][] occupied;

    /*
    For the hallway part. Is the point traversable?
     */
    private boolean[][] notAccessible;

    protected long seed;

    private Random random;

    protected ArrayList<Point> walls;
    protected Point avatar;
    protected Point end;

    /*
    Creates the runtime screen where the user enters
    in the seed number
     */
    public WorldGenerator(long seedNum) {
        /*
        Create the home screen; the image is titled
        Project_3_Start_Screen.png located in the
        byow folder. This is a placeholder as it was
        what was used in the example. We can change
        it later. I wasn't sure on the right dimensions
        to use for the image. We can check in Office Hours
        about this later on.
         */
        // Arbitrary scaling numbers: we can set them for sure later
        //Commented out to test rectangle drawing demo

//        StdDraw.setXscale(0.0, 20.0);
//        StdDraw.setYscale(0.0, 20.0);
//        StdDraw.picture(10, 10, "byow/Project_3_Start_Screen.png", 20, 20);


        /*
        This part of the code should generate the TETile[][] board we will
        use for the creation of the rectangles.
         */

        /*
        This entire set of code is designed to find the seed number.
         */
//        Engine stringReader = new Engine();
//        KeyboardInputSource keyboardSource = new KeyboardInputSource();
//        String inputString = "";
//        char nextKey = 'N';
//        while (nextKey != 'S') {
//            nextKey = keyboardSource.getNextKey();
//            inputString += (nextKey);
//        }
//        String seedString = inputString.substring(1, inputString.length()-1);
//        if (seedString.length() > 0) {
//            seed = Integer.parseInt(seedString);
//        }

        seed = seedNum;

        random = new Random(seed);

        walls = new ArrayList<>();

        /*
        Initializing and drawing a demo world
         */
        int testWidth = 75;
        int testHeight = 50;
        width = testWidth;
        height = testHeight;

//        ter.initialize(testWidth,testHeight);
        gameBoard = new TETile[testWidth][testHeight];
        notAccessible = new boolean[testWidth][testHeight];
        for (int x = 0; x < testWidth; x++) {
            for (int y = 0; y < testHeight; y++) {
                notAccessible[x][y] = false;
            }
        }

        boardInitializer(gameBoard);
        int numRooms = RandomUtils.uniform(random, 5, 12);
        roomGenerator(numRooms, 500, gameBoard);

        Hallway createHallways = new Hallway(allRooms, seed, gameBoard, notAccessible, walls);
        walls = createHallways.walled;
        avatar = createHallways.avatar;
        end = createHallways.end;

//        ter.renderFrame(gameBoard);
    }

    /*
    Return gameBoard function to return the final gameBoard to the engine
     */
    public TETile[][] returnGameBoard() {
        return gameBoard;
    }

    /*
    Given the world this method should keep generating
     rectangle objects and putting the tiles for them into the world
    The method should generate random widths and heights
     and check to see if they are valid rectangles
    Then it should create valid random rectangles and
     put them into the world until maxArea is filled
    Each rectangle has to have min width, height
     3 otherwise it can be a hallway
     */
    public void roomGenerator(int numRooms, int maxArea, TETile[][] board) {
        /*
        I am only going to try to generate the initial valid rectangle and print it
         */
        int area = 0;

        ArrayList<Rectangle> rectangles = new ArrayList<>();


        while (numRooms > 0 && area < maxArea) {
            int rectangleLeftX = RandomUtils.uniform(random, width);
            int rectangleLeftY = RandomUtils.uniform(random, height);
            int rectWidth = RandomUtils.uniform(random, 5, 8);
            int rectHeight = RandomUtils.uniform(random, 5, 8);

            Rectangle room1 = new Rectangle(rectangleLeftX, rectangleLeftY,
                    rectWidth, rectHeight, seed);


            boolean fit = checkCushion(room1);
            if (fit) {
                setRectangleTiles(room1);
                rectangles.add(room1);
                area = area + rectWidth * rectWidth;
                numRooms--;
            }
        }

        allRooms = rectangles;
    }

    /*
    Function to check if the space the rectangle is taking up is a valid space
    If anything in the rectangles area is already occupied return false, otherwise return true
     */
    private boolean checkCushion(Rectangle room) {
        int roomWidth = room.getWidth();
        int roomHeight = room.getHeight();
        int roomLeftX = room.getLeftCornerX();
        int roomLeftY = room.getLeftCornerY();

        //Check if the rectangle is in bounds
        if (roomLeftX + roomWidth > width || roomLeftY + roomHeight > height) {
            return false;
        }

        for (int x = roomLeftX; x <= roomLeftX + roomWidth; x++) {
            for (int y = roomLeftY; y <= roomLeftY + roomHeight; y++) {
                if (occupied[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
    Function to set the tiles for each rectangle created
    This function sets the tiles so that when the world is printed it has the floor and wall tiles
     */
    private void setRectangleTiles(Rectangle room) {
        /*
        Iterate through entire rectangle area and mark the edges
         with a wall and inside with the floor
        Also iterate from 4 before to 4 after to create a cushion around the rectangle
        ***Ignoring opening and closing for now
         */
        int leftX = room.getLeftCornerX();
        int leftY = room.getLeftCornerY();
        int rectWidth = room.getWidth();
        int rectHeight = room.getHeight();

        /*
        Sets cushion and creates the wall and floor tiles.
        I changed it so that it's 3 not 4. We were running
         into some index out of bounds errors otherwise.
         */
        for (int x = leftX - 4; x <= leftX + rectWidth + 4; x++) {
            for (int y = leftY - 4; y <= leftY + rectHeight + 4; y++) {
                if (x < leftX || x > leftX + rectWidth || y < leftY || y > leftY + rectHeight) {
                    occupied[x][y] = true;
                } else if (x == leftX || x == leftX + rectWidth
                        || y == leftY || y == leftY + rectHeight) {
                    Point wall = new Point(x, y);
                    walls.add(wall);
                    gameBoard[x][y] = Tileset.WALL;
                    notAccessible[x][y] = true;
                } else if (x > leftX && x < leftX + rectWidth
                        && y > leftY && y < leftY + rectHeight) {
                    gameBoard[x][y] = Tileset.FLOOR;
                    notAccessible[x][y] = true;
                    // Can look into this; idk if it is correct to have floors inaccessible
                }
                occupied[x][y] = true;
            }
        }

        walls.remove(room.generateRandomOpening(gameBoard));
        walls.remove(room.generateRandomClosing(gameBoard));

    }

    /*
    Initialize the world of tiles with the cushion built in and
     initializing a boolean 2D array that keeps track of if each spot is filled
     */
    private void boardInitializer(TETile[][] board) {
        /*
        Set a minimum length and width for the room just in case
         */
        if (width < 10 || height < 10) {
            throw new IllegalArgumentException();
        }

        /*
        Initializing and filling the occupied array with the initial cushion condition
         */
        occupied = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < 5 || x >= width - 5 || y < 5 || y > height - 5) {
                    occupied[x][y] = true;
                } else {
                    occupied[x][y] = false;
                }
                board[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args) {
//        WorldGenerator test = new WorldGenerator(12);
    }

}
