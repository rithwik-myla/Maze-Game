package byow.TileEngine;

import byow.Point;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private int RADIUS = 6;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        getMouse(world);
        StdDraw.show();
    }

    // TODO change the method calls in Keyboard and Main
    public void renderFrameRange(TETile[][] world, Point avatar, Point goal, int open, int tilesVisited) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        int xLeftBound = Math.max(0, avatar.getX() - RADIUS);
        int xRightBound = Math.min(numXTiles, avatar.getX() + RADIUS);
        int yDownBound = Math.max(0, avatar.getY() - RADIUS);
        int yUpBound = Math.min(numYTiles, avatar.getY() + RADIUS);

        StdDraw.clear(new Color(0, 0, 0));
        for (int x = xLeftBound; x < xRightBound; x += 1) {
            for (int y = yDownBound; y < yUpBound; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }

        world[goal.getX()][goal.getY()].draw(goal.getX() + xOffset, goal.getY() + yOffset);

        getMouse(world);
        StdDraw.text(35, 49, "Number of Tiles Visited: " + tilesVisited);
        StdDraw.text(70, 49, "Opens Left: " + open);
        StdDraw.show();
    }

    public void renderGameOver(int tilesVisited, int optimalPath) {
        int difficulty = (int) Math.round(((double) optimalPath / 250.0) * 10);
        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(37.5, 40, "Game Over! You are a winner!");
        StdDraw.text(37.5, 35, "This level has " + difficulty + " difficulty");
        StdDraw.text(37.5, 30, "You visited " + tilesVisited + " number of tiles");
        StdDraw.text(37.5, 25, "The optimal path would take " + optimalPath + " tiles");
        StdDraw.text(37.5, 20, "Press N to play next level");
        StdDraw.text(37.5, 15, "Press S to play again");
        StdDraw.text(37.5, 10, "Press Z to exit");
        StdDraw.show();

//        StdDraw.pause(5000);
    }

    public void renderGameOverLoser(int tilesVisited, int optimalPath, int bestFromHere) {
        int difficulty = (int) Math.round(((double) optimalPath / 250.0) * 10);
        StdDraw.clear(new Color(0, 0, 0));

        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(37.5, 42.5, "Game Over! You are a loser!");
        StdDraw.text(37.5, 37.5, "This level has " + difficulty + " difficulty");
        StdDraw.text(37.5, 32.5, "You visited " + tilesVisited + " tiles");
        StdDraw.text(37.5, 27.5, "The best path from your avatar would take "
                + bestFromHere + " more tiles");
        StdDraw.text(37.5, 22.5, "The optimal path would take " + optimalPath + " tiles");
        StdDraw.text(37.5, 17.5, "Press N to play next level");
        StdDraw.text(37.5, 12.5, "Press S to play again");
        StdDraw.text(37.5, 7.5, "Press Z to exit");
        StdDraw.show();
    }




    public void getMouse(TETile[][] gameBoard) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int tileX = (int) Math.floor(x);
        int tileY = (int) Math.floor(y);
        if (tileX < gameBoard.length && tileX >= 0 && tileY < gameBoard[0].length && tileY >= 0) {
            TETile tile = gameBoard[tileX][tileY];
            StdDraw.setPenColor(StdDraw.WHITE);
            if (tile.equals(Tileset.PACMAN)) {
                StdDraw.text(3, 49, "Avatar");
            }
            if (tile.equals(Tileset.FLOOR)) {
                StdDraw.text(3, 49, "Floor");
            }
            if (tile.equals(Tileset.WALL)) {
                StdDraw.text(3, 49, "Wall");
            }
            if (tile.equals(Tileset.CHERRY)) {
                StdDraw.text(3, 49, "Destination");
            }
            if (tile.equals(Tileset.NOTHING)) {
                StdDraw.text(3, 49, "Nothing");
            }
        } else {
            StdDraw.text(3, 49, "Nothing");
        }
    }
}
