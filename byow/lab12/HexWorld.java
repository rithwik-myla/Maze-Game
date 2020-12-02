package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.SimpleTimeZone;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int SIZE = 3;
    private static final int SEED = 18;
    private static final Random RANDOM = new Random(SEED);

    private static class Hex {
        private int _x;
        private int _y;
        private TETile _tile;

        public Hex(int x, int y, TETile tile) {
            _x = x;
            _y = y;
            _tile = tile;
        }

        public void addHex(TETile[][] tiles) {
            int x = _x;
            int y = _y;

            for (int s = 0; s < SIZE; s += 1) {
                int rowLength = SIZE + 2 * s;
                addRow(x, y, rowLength, tiles);
                x = x - 1;
                y = y - 1;
            }

            x += 1;

            for (int s = SIZE - 1; s >= 0; s -= 1) {
                int rowLength = SIZE + 2 * s;
                addRow(x, y, rowLength, tiles);
                x = x + 1;
                y = y - 1;
            }

        }

        private void addRow(int x, int y, int rowLength, TETile[][] tiles) {
            for (int i = 0; i < rowLength; i += 1) {
                tiles[x + i][y] = _tile;
            }
        }
    }

    private static void initWorld(TETile[][] tiles) {
        for (int i = 0; i < tiles.length; i += 1) {
            for (int j = 0; j < tiles[0].length; j += 1) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    private static TETile randomTile() {
        int randNumber = RANDOM.nextInt(6);

        switch(randNumber) {
            case 0:
                return Tileset.FLOWER;
            case 1:
                return Tileset.MOUNTAIN;
            case 2:
                return Tileset.SAND;
            case 3:
                return Tileset.WATER;
            case 4:
                return Tileset.GRASS;
            case 5:
                return Tileset.TREE;
            default:
                return null;
        }
    }

    private static void addCollumnOfHex(int x, int y, int numHexes, TETile[][] world){
        for (int i = 0; i < numHexes; i += 1) {
            TETile tile = randomTile();
            Hex h = new Hex(x, y, tile);
            h.addHex(world);
            y = y - 2 * SIZE;
        }
    }

    private static void tesselateWorld() {
        //Column of 3 hexagons
        //Then 4 then 5 then 4 then 3
        int width = 12 * SIZE;
        int height = 10 * SIZE;

        TETile[][] world = new TETile[width][height];
        initWorld(world);
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        int x = SIZE - 1;
        int y = (height - 1)  - 2 * SIZE;
        addCollumnOfHex(x, y, 3, world);

        x += 2 * SIZE - 1;
        y += SIZE;
        addCollumnOfHex(x, y, 4, world);

        x += 2 * SIZE - 1;
        y += SIZE;
        addCollumnOfHex(x, y, 5, world);

        x += 2 * SIZE - 1;
        y -= SIZE;
        addCollumnOfHex(x, y, 4, world);

        x += 2 * SIZE - 1;
        y -= SIZE;
        addCollumnOfHex(x, y, 3, world);

        ter.renderFrame(world);
    }

    public static void main(String[] args) {
        tesselateWorld();
    }
}
