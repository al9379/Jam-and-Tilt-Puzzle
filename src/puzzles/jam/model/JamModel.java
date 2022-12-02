package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.*;

public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;

    /** selection or movement mode */
    private boolean selecting = true;

    /** stores initial selection */
    private int[] selected;

    private int rows;
    private int cols;


    public JamModel() {
    }

    public char[][] getBoard() {
        return currentConfig.getBoard();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean loadBoard(String filename) {
        return loadBoard(new File(filename));
    }

    /**
     * Updates the current configuration with input from specified file
     * @param fileName Name of file
     * @return If the load was successful
     */
    public boolean loadBoard(File fileName) {

        int numCars;

        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {

            // Read first two lines
            String[] fields = in.readLine().split("\\s+");
            rows = Integer.parseInt(fields[0]);
            cols = Integer.parseInt(fields[1]);
            numCars = Integer.parseInt(in.readLine());

            char[] cars = new char[numCars];
            int[] startRows = new int[numCars];
            int[] startCols = new int[numCars];
            int[] endRows = new int[numCars];
            int[] endCols = new int[numCars];
            char[][] board = new char[rows][cols];

            // Fill the board with .
            for (char[] row: board) {
                Arrays.fill(row, '.');
            }

            // For each car in file
            for (int i = 0; i < numCars; i++) {

                // Store information of cars in each index
                fields = in.readLine().split("\\s+");
                cars[i] = fields[0].charAt(0);
                startRows[i] = Integer.parseInt(fields[1]);
                startCols[i] = Integer.parseInt(fields[2]);
                endRows[i] = Integer.parseInt(fields[3]);
                endCols[i] = Integer.parseInt(fields[4]);

                // Fill the board with cars
                if (startRows[i] == endRows[i]) {
                    for (int j = 0; j <= endCols[i] - startCols[i]; j++) {
                        board[startRows[i]][startCols[i] + j] = cars[i];
                    }
                } else if (startCols[i] == endCols[i]) {
                    for (int j = 0; j <= endRows[i] - startRows[i]; j++) {
                        board[startRows[i] + j][startCols[i]] = cars[i];
                    }
                }
            }

            currentConfig = new JamConfig(board, cars, startRows, startCols, endRows, endCols, cols - 1);

            // Update board with successful/unsuccessful load message
            if (fileName.toString().length() > 50) {
                alertObservers("Loaded: " + fileName.toString().substring(61));
            } else {
                alertObservers("Loaded: " + fileName);
            }
            return true;
        } catch (Exception e) {

            alertObservers("Failed to load: Try again");
            return false;
        }
    }


    /**
     * Finds the next step and updates the board
     */
    public void useHint() {

        // Solve the current configuration and update config with the next index in the arraylist
        Collection<Configuration> path = new ArrayList<>(new Solver().solve(currentConfig));
        int i = 0;
        for (Configuration configs: path) {
            if (i == 1) {
                if (configs instanceof JamConfig) {
                    currentConfig = (JamConfig) configs;
                    alertObservers("Next step");
                }
                break;
            }
            i++;
        }
    }


    /**
     * Attempts to select or move to specified row and column
     * @param r row
     * @param c column
     */
    public void selectSquare(int r, int c) {

        // If in initial selection mode
        if (selecting) {

            // If there is something at r, c, change selection mode and alertObservers()
            if (currentConfig.getBoard()[r][c] == '.') {
                alertObservers("No car at (" + r + ", " + c + ")");
            } else {
                selecting = false;
                selected = new int[]{r, c};
                alertObservers("Selected (" + selected[0] + ", " + selected[1] + ")");
            }
        } else {

            // Try to move to specified square
            if (currentConfig.trySelect(selected[0], selected[1], r, c)) {
                alertObservers("Moved (" + selected[0] + ", " + selected[1] + ") to (" + r + ", " + c + ")");
                selecting = true;
            } else {
                alertObservers("Cannot move (" + selected[0] + ", " + selected[1] + ") to (" + r + ", " + c + ")");
            }
        }

    }

    /**
     * @return True if the game is over
     */
    public boolean gameOver() {
        return currentConfig.isSolution();
    }

    /**
     * Print the board
     */
    public void printBoard() {
        System.out.println(currentConfig);
    }
    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
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
