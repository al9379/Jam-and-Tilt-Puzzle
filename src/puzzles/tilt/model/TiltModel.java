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

    public TiltModel(String filename) throws IOException {
        loadBoard(filename);
    }

    public char[][] getBoard(){
        return currentConfig.getBoard();
    }

    public boolean loadBoard(String filename) throws IOException {
        return loadBoard(new File(filename));
    }

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
        catch(Exception e){
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

    public void move(char direction){
        TiltConfig lastConfig = currentConfig;

        if(direction == 'n' || direction == 'N'){
            currentConfig = currentConfig.moveNorth();
            if(currentConfig.getBoard()[0][0] != 'e') {
                //printBoard();
                alertObservers("Tilted North");
            }
            else {
                System.out.println("Blue will fall in");
                currentConfig = lastConfig;
                //printBoard();
            }
        }
        if(direction == 's' || direction == 'S'){
            currentConfig = currentConfig.moveSouth();
            if(currentConfig.getBoard()[0][0] != 'e') {
                //printBoard();
                alertObservers("Tiled South");
            }
            else {
                System.out.println("Blue falls in");
                currentConfig = lastConfig;
                //printBoard();
            }
        }
        if(direction == 'w' || direction == 'W'){
            currentConfig = currentConfig.moveWest();
            if(currentConfig.getBoard()[0][0] != 'e') {
                //printBoard();
                alertObservers("Tilted West");
            }
            else {
                System.out.println("Blue falls in");
                currentConfig = lastConfig;
                //printBoard();
            }
        }
        if(direction == 'e' || direction == 'E'){
            currentConfig = currentConfig.moveEast();
            if(currentConfig.getBoard()[0][0] != 'e') {
                //printBoard();
                alertObservers("Tilted East");
            }
            else {
                System.out.println("Blue will fall in");
                currentConfig = lastConfig;
                //printBoard();
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
