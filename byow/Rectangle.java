package byow;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Rectangle {
    /*
    Width and height of the rectangle
     */
    private int width;
    private int height;

    /*
    These are the instance variables for the locations
    of the opening and closing locations of the rectangle.
    Opening is where the hallway BEGINS out of the rectangle.
    Closing is where the hallways ENTERS the rectangle.
     */
    private int openingX;
    private int openingY;
    private int closingX;
    private int closingY;

    /*
    This is the information for the x and y coordinates for the
    bottom left corner of the rectangle.
     */
    private int leftCornerX;
    private int leftCornerY;


    /*
    Whether or not the the opening and closing tiles have been visited.
    TRUE = It has been visited
    FALSE = It has not been visited
     */
    private boolean openingBool;
    private boolean closingBool;

    /*
    For random number generation
     */
    private Random random;

    /*
    Given a x,y coordinate and the width and height,
     the rectangle object stores all of the variables
    useful for world generation in one object
     */
    public Rectangle(int leftX, int leftY, int width, int height, long seed) {
        leftCornerX = leftX;
        leftCornerY = leftY;
        this.width = width;
        this.height = height;
        random = new Random(seed);

//        generateRandomOpening();
//        generateRandomClosing();

        openingBool = false;
        closingBool = false;

        // Need a method to randomly generate the opening and closing tile
    }

    /*
    Generates a random spot on the rectangle as the opening
     */
    public Point generateRandomOpening(TETile[][] board) {
        /*
         If the random number is 0 make the opening on
          the left wall (choose a random y thats not a corner)
         If the random number is 1 make the opening on
          the bottom wall (choose a random x thats not a corner)
         If the random number is 2 make the opening on the right wall
         If the random number is 3 make the opening on the top wall
         */
        int side = RandomUtils.uniform(random, 4);
        if (side == 0) {
            openingX = leftCornerX;
            openingY = RandomUtils.uniform(random, leftCornerY + 1, leftCornerY + height - 1);
        } else if (side == 1) {
            openingX = RandomUtils.uniform(random, leftCornerX + 1, leftCornerX + width - 1);
            openingY = leftCornerY;
        } else if (side == 2) {
            openingX = leftCornerX + width;
            openingY = RandomUtils.uniform(random, leftCornerY + 1, leftCornerY + height - 1);
        } else {
            openingX = RandomUtils.uniform(random, leftCornerX + 1, leftCornerX + width - 1);
            openingY = leftCornerY + height;
        }

        board[openingX][openingY] = Tileset.FLOOR;
        return new Point(openingX, openingY);
    }

    public Point generateRandomOpening(TETile[][] board, boolean[][] visited) {
        int side = RandomUtils.uniform(random, 4);
        if (side == 0) {
            openingX = leftCornerX;
            openingY = RandomUtils.uniform(random, leftCornerY + 1, leftCornerY + height - 1);
        } else if (side == 1) {
            openingX = RandomUtils.uniform(random, leftCornerX + 1, leftCornerX + width - 1);
            openingY = leftCornerY;
        } else if (side == 2) {
            openingX = leftCornerX + width;
            openingY = RandomUtils.uniform(random, leftCornerY + 1, leftCornerY + height - 1);
        } else {
            openingX = RandomUtils.uniform(random, leftCornerX + 1, leftCornerX + width - 1);
            openingY = leftCornerY + height;
        }

        if (surrounded(visited, openingX, openingY)) {
            generateRandomOpening(board, visited);
        } else {
            board[openingX][openingY] = Tileset.FLOOR;
            return new Point(openingX, openingY);
        }
        return null;
    }

    private boolean surrounded(boolean[][] visited, int x, int y) {
        if (visited[x + 1][y] && visited[x][y + 1] && visited[x - 1][y] && visited[x][y - 1]) {
            return true;
        }
        return false;
    }

    /*
    Generates a random spot on the rectangle as the opening
    */
    public Point generateRandomClosing(TETile[][] board) {
        /*
         If the random number is 0 make the closing on the
          left wall (choose a random y thats not a corner)
         If the random number is 1 make the closing on the
          bottom wall (choose a random x thats not a corner)
         If the random number is 2 make the closing on the right wall
         If the random number is 3 make the closing on the top wall
         */
//        System.out.println("Just got into generateClosing");
        int side = RandomUtils.uniform(random, 4);
        if (side == 0) {
            closingX = leftCornerX;
            closingY = RandomUtils.uniform(random, leftCornerY + 1, leftCornerY + height - 1);
        } else if (side == 1) {
            closingX = RandomUtils.uniform(random, leftCornerX + 1, leftCornerX + width - 1);
            closingY = leftCornerY;
        } else if (side == 2) {
            closingX = leftCornerX + width;
            closingY = RandomUtils.uniform(random, leftCornerY + 1, leftCornerY + height - 1);
        } else {
            closingX = RandomUtils.uniform(random, leftCornerX + 1, leftCornerX + width - 1);
            closingY = leftCornerY + height;
        }

//        System.out.println("After 1st set of whiles");
        Point opening = new Point(openingX, openingY);
        Point closing = new Point(closingX, closingY);
        if (Math.sqrt(Point.distance(opening, closing)) <= 1) {
            generateRandomClosing(board);
        } else {
            board[closingX][closingY] = Tileset.FLOOR;
            return new Point(closingX, closingY);
        }
//        System.out.println("After 2nd set of whiles");
        return null;
    }

    /*
    Creates the border but makes careful mention to leave the opening and closing spots blank.
     */
    public void borderCreation() {

    }

    /*
    This method will be used to start the hallway out of the rectangle.
     */
    public void openingMarked() {
        openingBool = true;
    }

    private void closingMarked() {
        closingBool = true;
    }

    /*
    Get methods to give access to World Generation
     */
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOpeningX() {
        return openingX;
    }

    public int getOpeningY() {
        return openingY;
    }

    public int getClosingX() {
        return closingX;
    }

    public int getClosingY() {
        return closingY;
    }

    public int getLeftCornerX() {
        return leftCornerX;
    }

    public int getLeftCornerY() {
        return leftCornerY;
    }

    public boolean getOpened() {
        return openingBool;
    }

    public boolean getClosed() {
        return closingBool;
    }


}
