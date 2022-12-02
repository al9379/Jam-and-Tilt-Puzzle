package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamModel;
import java.util.Scanner;

public class JamPTUI implements Observer<JamModel, String> {
    private final JamModel model;
    private Scanner in;
    private boolean gameOn;
    private String lastFile;


    public JamPTUI (String fileName) {
        lastFile = fileName;
        gameOn = true;
        model = new JamModel();
        model.addObserver(this);
        in = new Scanner( System.in );
        gameStart(fileName);

        System.out.println("h(int)           -- hint next move");
        System.out.println("l(oad) filename  -- load new puzzle file");
        System.out.println("s(elect) r c     -- select cell at r, c");
        System.out.println("q(uit)           -- quit the game");
        System.out.println("r(eset)          -- reset the current game");
    }



    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        } else {
            JamPTUI ptui = new JamPTUI(args[0]);
            ptui.run();

        }
    }

    /**
     * Starts the game and loads the file
     * @param fileName File to load
     */
    public void gameStart(String fileName){
        gameOn = true;
        loadFromFile(fileName);
    }

    /**
     * Runs the main game loop
     */
    public void run() {
        while (true) {
            if (!gameLoop()) {  // Quits if gameLoop() is done
                in.close();
                break;
            }
        }
    }

    /**
     * Main game loop
     * @return True if game is still running
     */
    private boolean gameLoop(){
        boolean ready = false;
        while(!ready){

            String command =  in.next();  // Using next allows you to string together load commands

            switch (command) {

                // Get a hint
                case "H", "h" -> {
                    if (gameOn) {
                        model.useHint();
                        ready = true;
                        break;
                    }
                    System.out.println("Already solved");
                    gameOn = false;
                }

                // Load a file
                case "L", "l" -> {
                    command = in.next();
                    if (loadFromFile(command)) {
                        gameOn = true;
                    }
                }

                // Select square
                case "S", "s" -> {
                    if (gameOn) {
                        int row = Integer.parseInt(in.next());
                        int col = Integer.parseInt(in.next());
                        model.selectSquare(row, col);
                        break;
                    }
                    System.out.println("Already solved");
                    gameOn = false;
                    in = new Scanner(System.in);//get rid of any remaining commands from the start menu
                }

                // Quit program
                case "Q", "q", "quit" -> {
                    System.out.println("Exiting...");
                    gameOn = false;
                    return false;
                }

                // Reset file
                case "R", "r", "reset" -> {
                    System.out.println("Resetting...");
                    gameStart(lastFile);
                    gameOn = true;
                }

                // Command list if invalid input
                default -> {
                    System.out.println("h(int)           -- hint next move");
                    System.out.println("l(oad) filename  -- load new puzzle file");
                    System.out.println("s(elect) r c     -- select cell at r, c");
                    System.out.println("q(uit)           -- quit the game");
                    System.out.println("r(eset)          -- reset the current game");
                }
            }
        }

        return true;
    }

    /**
     * Loads a board
     * @param command file name
     * @return True if the file can be loaded
     */
    public boolean loadFromFile(String command){

        // If the file can be loaded, make it the previous file
        boolean result = model.loadBoard(command);
        if (result) {
            lastFile = command;
            return true;
        }
        return false;
    }

    public void displayBoard(){
        model.printBoard();
    }

    @Override
    public void update(JamModel jamModel, String message) {

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