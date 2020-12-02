package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Engine engine = new Engine();
            TETile[][] board = engine.interactWithInputString(args[0]);
            TERenderer ter = new TERenderer();
            ter.initialize(board.length, board[0].length);
            ter.renderFrame(board);
            KeyBoard keyBoard = new KeyBoard(engine.world.avatar, engine.world.end, board,
                    engine.world.walls, ter, engine.seedString, engine.inputPassed,
                    engine.seedIndex);
        } else {
            Engine engine = new Engine();
            StdDraw.setXscale(0.0, 75.0);
            StdDraw.setYscale(0.0, 75.0);
            StdDraw.picture(37.5, 37.5, "byow/Project_3_Start_Screen.png", 75, 75);

            TETile[][] board = engine.interactWithKeyboard();
            TERenderer ter = new TERenderer();
            ter.initialize(board.length, board[0].length);
            ter.renderFrame(board);
            KeyBoard keyBoard = new KeyBoard(engine.world.avatar, engine.world.end, board,
                    engine.world.walls, ter, engine.seedString, engine.inputPassed,
                    engine.seedIndex);
            mainKeyboard();
        }
    }

    public static void startScreen() {
        StdDraw.setXscale(0.0, 75.0);
        StdDraw.setYscale(0.0, 75.0);
        StdDraw.picture(37.5, 37.5, "byow/Project_3_Start_Screen.png", 75, 75);
    }

    public static void mainKeyboard() {
        Engine engine = new Engine();
        StdDraw.clear();
        StdDraw.setCanvasSize(500,500);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 75.0);
        StdDraw.setYscale(0.0, 75.0);
        StdDraw.picture(37.5, 37.5, "byow/Project_3_Start_Screen.png", 75, 75);
        StdDraw.show();
        TETile[][] board = engine.interactWithKeyboard();
        TERenderer ter = new TERenderer();
        ter.initialize(board.length, board[0].length);
        ter.renderFrame(board);
        KeyBoard keyBoard = new KeyBoard(engine.world.avatar, engine.world.end, board,
                engine.world.walls, ter, engine.seedString, engine.inputPassed,
                engine.seedIndex);
        mainKeyboard();
    }
}
