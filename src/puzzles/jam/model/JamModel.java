package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;

    public JamModel() {

    }

    public boolean loadBoard(String fileName) {

        int rows;
        int cols;
        int numCars;

        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {

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

            alertObservers("Loaded: " + fileName);
            return true;
        } catch (IOException e) {
            alertObservers("Failed to load: " + fileName);
            return false;
        }
    }

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
