package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.io.IOException;
import java.util.Scanner;

public class TiltPTUI implements Observer<TiltModel, String> {
    private final TiltModel model;
    private boolean gameOn;
    private Scanner in;
    private String lastFile;


    /**
     * Default constructor for TiltPTUI
     * @param fileName: name of the file
     * @throws IOException
     */
    public TiltPTUI(String fileName) throws IOException {
        lastFile = fileName;
        model = new TiltModel(fileName);
        model.addObserver(this);
        in = new Scanner( System.in );
        gameStart(fileName);

        System.out.println("""
                h(int)              -- hint next move
                l(oad) filename     -- load new puzzle file
                t(ilt) {N|S|E|W}    -- tilt the board in the given direction
                q(uit)              -- quit the game
                r(eset)             -- reset the current game""");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else{
            TiltPTUI ptui = new TiltPTUI(args[0]);
            ptui.run();
        }
    }

    /**
     * Loads file given
     * @param command
     * @return if it was able to be read
     * @throws IOException
     */
    public boolean loadFromFile(String command) throws IOException {
        boolean result = model.loadBoard(command);
        if(result){
            lastFile = command;
            return true;
        }
        return false;
    }

    public void run() throws IOException {
        while (true) {
            if (!gameLoop()) {  // Quits if gameLoop() is done
                in.close();
                break;
            }
        }
    }

    /**
     * Starts the game by reading file
     * @param fileName name of the file
     * @throws IOException
     */
    public void gameStart(String fileName) throws IOException {
        gameOn = true;
        loadFromFile(fileName);
    }

    /**
     * Continuously loops throughout the game as long as it is playing
     * @return boolean to show if its ready
     * @throws IOException
     */
    public boolean gameLoop() throws IOException {
        boolean ready = false;

        while(!ready){
            String command = in.next();
            switch(command) {
                case "r":
                case "R":
                case "reset":
                    System.out.println("Resetting...");
                    gameStart(lastFile);
                    break;
                case "q":
                case "Q":
                case "quit":
                    System.out.println("Exiting");
                    gameOn = false;
                    in = new Scanner(System.in);
                    return false;
                case "l":
                case "L":
                case "load":
                    command = in.next();
                    if (loadFromFile(command)) {
                        gameOn = true;
                    }
                    break;
                case "t":
                case "T":
                case "tilt":
                    if (gameOn) {
                        char direction = in.next().charAt(0);
                        model.move(direction);
                    }
                    break;
                case "h":
                case "H":
                case "hint":
                    if (gameOn) {
                        model.useHint();
                        ready = true;
                        break;
                    }
                    System.out.println("Already solved");
                    gameOn = false;
                    break;
                default:
                    System.out.println("""
                            h(int)              -- hint next move
                            l(oad) filename     -- load new puzzle file
                            t(ilt) {N|S|E|W}    -- tilt the board in the given direction
                            q(uit)              -- quit the game
                            r(eset)             -- reset the current game""");
            }
        }
        return true;
    }

    //displays the board
    public void displayBoard(){
        model.printBoard();
    }

    /**
     * Whenever an action occurs, this is called on
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel model, String message) {
        if (message.contains("Loaded: ")) {
            System.out.println(message);
            displayBoard();
            if (model.gameOver()) {
                System.out.println("You Win");
                System.out.println("Start a new game or quit");
                gameOn = false;
                return;
            }
            return;
        } else if (message.contains("Failed to load: ")) {
            System.out.println(message);
            if (gameOn) {
                displayBoard();
            }
            return;
        }
        if (model.gameOver()) {
            System.out.println(message);
            displayBoard();
            System.out.println("You Win");
            System.out.println("Start a new game or quit");
            gameOn = false;
            return;
        }
        System.out.println(message);
        displayBoard();
    }
}
