package byow.Core;

import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Key;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Engine {
    private TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 75;
    public static final int HEIGHT = 50;
    protected WorldGenerator world;
    protected String seedString;
    protected String inputPassed;
    protected int seedIndex = 0;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public TETile[][] interactWithKeyboard() {
        KeyboardInputSource keyboardSource = new KeyboardInputSource();
        String inputString = "";
        char nextKey = keyboardSource.getNextKey();
        if (nextKey == 'N' || nextKey == 'n') {
            inputString = KeyBoard.renderSeedInteractive();
//            KeyBoard.renderSeed();
//            while (nextKey != 'S' && nextKey != 's') {
//                inputString += (nextKey);
//                nextKey = keyboardSource.getNextKey();
//            }
//            inputString += (nextKey);
            KeyBoard.renderRules();
        } else if (nextKey == 'L' || nextKey == 'l') {
            try {
                File myObj = new File("Strings.txt");
                Scanner myReader = new Scanner(myObj);
                String data = myReader.nextLine();
                myReader.close();
                return interactWithInputString(data);
            } catch (FileNotFoundException e) {
                System.out.println("File not found int engine load keybaord");
            } catch (NoSuchElementException f) {
                KeyBoard.renderFailedLoad();
            }
        } else if (nextKey == 'Q' || nextKey == 'q') {
            System.exit(0);
        }
        return interactWithInputString(inputString);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        /*
        @Source GeeksforGreeks post about reading and writing into .txt file
        in java
        https://www.geeksforgeeks.org/different-ways-reading-text-file-java/


        Initialize renderer and parse the input string for the seed number
        Create a worldGenerator using the seed and then render the gameBoard
         */
        long seed = 0;
        int index = 0;

        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {

//
            try {
                File myObj = new File("./Strings.txt");
                Scanner myReader = new Scanner(myObj);
                String data = myReader.nextLine();
                myReader.close();
                data += input.substring(1);

                return interactWithInputString(data);
            } catch (FileNotFoundException e) {
                System.out.println("The file was not found Engine.java line 103");
                e.printStackTrace();
            }

        } else if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {

            seedString = input;
            while (input.charAt(index) != 's' && input.charAt(index) != 'S') {
                index++;
            }
            String numberString = input.substring(1, index);
            // This is the seed number in string format

            if (numberString.length() > 0) {
                seed = Long.parseLong(numberString);
            }
            world = new WorldGenerator(seed);
            TETile[][] finalWorldFrame = world.returnGameBoard();

            if (input.length() > index) {
                String inputString = input.substring(index + 1);
                seedIndex = index + 1;
                inputPassed = inputString;
            }

            KeyBoard keyboardInner = new KeyBoard(world.avatar, world.end, finalWorldFrame,
                    world.walls, seedString, inputPassed,
                    seedIndex);
            return finalWorldFrame;
        }

        return null;
    }
}
