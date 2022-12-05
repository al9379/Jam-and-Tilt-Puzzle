package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.*;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;
    private int dim;

    public static String LOADED = "loaded";

    public static String LOAD_FAILED = "loadFailed";

    public static String HINT_PREFIX = "Hint:";

    /**
     * Default constructor for TiltModel
     * @param filename
     * @throws IOException
     */
    public TiltModel(String filename) throws IOException {
        loadBoard(filename);
    }

    /**
     * Gets the 2-d array of the current configuration
     * @return the 2-d array
     */
    public char[][] getBoard(){
        return currentConfig.getBoard();
    }

    /**
     * @param filename
     * @return boolean to show if it can load the board
     * @throws IOException
     */
    public boolean loadBoard(String filename) throws IOException {
        return loadBoard(new File(filename));
    }

    /**
     * Loads the board from the file given
     * @param fileName
     * @return boolean to show if it was able to be loaded
     */
    public boolean loadBoard(File fileName){
        boolean valid = true;
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            dim = Integer.parseInt(in.readLine());
            char[][] board = new char[dim][dim];
            while((line = in.readLine()) != null){
                String[] field = line.split(" ");
                for(int col = 0; col < field.length; col++){
                    board[row][col] = field[col].charAt(0);
                }
                row++;
            }
            currentConfig = new TiltConfig(board,dim);
            alertObservers(LOADED + fileName);
        }
        catch(Exception e){ //catches failure to load
            alertObservers(LOAD_FAILED);
            valid = false;
        }
        return valid;
    }

    public boolean gameOver(){
        return currentConfig.isSolution();
    }

    public void printBoard(){
        System.out.println(currentConfig);
    }

    public int getDim(){
        return dim;
    }

    /**
     * Finds the next best option by calling on our solver
     */
    public void useHint() {
        Collection<Configuration> path = new ArrayList<>(new Solver().solve(currentConfig));
        int i = 0;
        for (Configuration other: path) {
            if (i == 1) {
                if (other instanceof TiltConfig) {
                    currentConfig = (TiltConfig) other;
                    alertObservers(HINT_PREFIX);
                }
                break;
            }
            i++;
        }
    }

    /**
     * Moves the board into whichever direction it needs to go
     * @param direction
     */
    public void move(char direction){
        TiltConfig lastConfig = currentConfig;

        if(direction == 'n' || direction == 'N'){
            currentConfig = currentConfig.moveNorth();
            if(currentConfig.getBoard()[0][0] != 'e') {
                alertObservers("Tilted North");
            }
            else {
                System.out.println("Blue will fall in");
                currentConfig = lastConfig;
            }
        }
        if(direction == 's' || direction == 'S'){
            currentConfig = currentConfig.moveSouth();
            if(currentConfig.getBoard()[0][0] != 'e') {
                alertObservers("Tiled South");
            }
            else {
                System.out.println("Blue falls in");
                currentConfig = lastConfig;
            }
        }
        if(direction == 'w' || direction == 'W'){
            currentConfig = currentConfig.moveWest();
            if(currentConfig.getBoard()[0][0] != 'e') {
                alertObservers("Tilted West");
            }
            else {
                System.out.println("Blue falls in");
                currentConfig = lastConfig;
            }
        }
        if(direction == 'e' || direction == 'E'){
            currentConfig = currentConfig.moveEast();
            if(currentConfig.getBoard()[0][0] != 'e') {
                alertObservers("Tilted East");
            }
            else {
                System.out.println("Blue falls in");
                currentConfig = lastConfig;
            }
        }
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
