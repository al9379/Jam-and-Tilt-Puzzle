package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Jam {
    public static void main(String[] args) {

        int rows;
        int cols;
        int numCars;

        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        } else {
            try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {

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

                // Print initial state
                System.out.println(args[0]);
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (j == cols - 1) {
                            System.out.print(board[i][j]);
                        } else {
                            System.out.print(board[i][j] + " ");
                        }
                    }
                    System.out.println();
                }

                // Start solving puzzle
                JamConfig config = new JamConfig(board, cars, startRows, startCols, endRows, endCols, cols - 1);
                Solver b = new Solver();
                int x = 0;

                Collection<Configuration> end = new ArrayList<>(b.solve(config));
                int size = end.size();
                boolean endResult = false;


                System.out.println("Total configs: " + b.getConfigurations());
                System.out.println("Unique configs: " + b.getUniqueConfigurations());

                // Print if no solution
                for (Configuration result: end) {
                    if (x == size - 1) {
                        if (result.isSolution()) {
                            endResult = true;
                        } else {
                            System.out.println("No solution");
                        }

                    }
                    x++;
                }

                int step = 0;

                // Print path
                if (endResult) {
                    for (Configuration boards: end) {
                        System.out.println("Step " + step + ":");
                        System.out.println(boards);
                        System.out.println();
                        step++;
                    }
                }

            } catch (IOException e) {
                System.out.println("Invalid File");
            }
        }
    }
}