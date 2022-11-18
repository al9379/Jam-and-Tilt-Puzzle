package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JamPTUI implements Observer<JamModel, String> {
    private JamModel model;
    private Scanner in;
    private boolean gameOn;
    private boolean reset = false;
    private String lastFile;

    @Override
    public void update(JamModel jamModel, String message) {

        if (message.contains("Loaded: ")) {
            System.out.println(message);
            displayBoard();
            return;
        } else if (message.contains("Failed to load: ")) {
            System.out.println(message);
            displayBoard();
            return;
        }

//        if (model.gameOver()) {
//            displayBoard();
//            System.out.println("You Win");
//            gameOn = false;
//            return;
//        }
        displayBoard();
        System.out.println(message);


    }

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

    public void gameStart(String fileName){
        loadFromFile(fileName);
    }
    public void run() {
        while (true) {
            if (!gameLoop()) //loads new games or quits
                break;
            //gameLoop(); // gameplay
        }
    }

    private boolean gameLoop(){
        boolean ready = false;
        while(!ready){

            String command =  in.next(); // Using next allows you to string together load commands

            switch (command){
                case "H":
                case "h":
                    ready=true;
                    break;

                // Load a file
                case "L":
                case "l":
                    command = in.next();
                    ready = loadFromFile(command);
                    break;
                case "S":
                case "s":
                    break;

                // Quit program
                case "Q":
                case "q":
                    System.out.println("Exiting...");
                    ready = true;
                    in = new Scanner(System.in);  //get rid of any remaining commands from the start menu
                    return false;

                // Reset file
                case "R":
                case "r":
                    reset = true;
                    System.out.println("Resetting...");
                    gameStart(lastFile);
                    break;
                default:
                    System.out.println("Enter H, L, S, Q or R.");
            }
            gameOn = true;
        }
        in = new Scanner(System.in);//get rid of any remaining commands from the start menu
        return true;
    }

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
}