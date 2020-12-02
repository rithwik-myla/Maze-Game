package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.Stopwatch;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String answer = "";
        while (n > 0) {
            int index = RandomUtils.uniform(rand, 0, 26);
            Character insertion = CHARACTERS[index];
            answer += insertion;
            n -= 1;
        }
        return answer;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        StdDraw.clear(StdDraw.LIGHT_GRAY);

        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.text(20, 20, s);

        StdDraw.show();

        //TODO: If game is not over, display relevant game information at the top of the screen

    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < letters.length(); i++) {
            double start = stopwatch.elapsedTime();
            while (stopwatch.elapsedTime() <= start + 1) {
                drawFrame(String.valueOf(letters.charAt(i)));
            }
            double start2 = stopwatch.elapsedTime();
            while (stopwatch.elapsedTime() <= start2 + 0.5) {
                StdDraw.clear(StdDraw.LIGHT_GRAY);
            }
        }
        drawFrame("Now Type in your String:");
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String current = "";

        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                Character next = StdDraw.nextKeyTyped();
                current += next;
                drawFrame(current);
                n--;
            }
        }

        return current;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        String playerAnswer;
        String challenge;
        round = 1;
        do {
            drawFrame("Round: " + round);

            challenge = generateRandomString(round);
            flashSequence(challenge);

            playerAnswer = solicitNCharsInput(round);

            round++;
        }
        while (playerAnswer.equals(challenge));

        drawFrame("Game Over! You made it to round: " + (round-1));

        //TODO: Establish Engine loop
    }

}
