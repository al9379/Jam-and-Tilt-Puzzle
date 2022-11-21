package puzzles.jam.model;


import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class JamConfig implements Configuration {

    private final char[] car;
    private final int[] startRow;
    private final int[] startCol;

    private final int[] endRow;
    private final int[] endCol;
    private final int goal;
    private char[][] board;

    private final boolean[] isVert;
    private final boolean[] isHorizontal;


    public JamConfig(char[][] board, char[] car, int[] startRow, int[] startCol, int[] endRow, int[] endCol, int goal) {
        this.car = car;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.goal = goal;
        this.board = board;
        this.isVert = new boolean[car.length];
        this.isHorizontal = new boolean[car.length];

        for (int i = 0; i < car.length; i++) {
            if (startRow[i] == endRow[i]) {
                isHorizontal[i] = true;
            } else if (startCol[i] == endCol[i]){
                isVert[i] = true;
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    /**
     * Tries to move car to a different position
     * @param oldR Selected row
     * @param oldC Selected col
     * @param r Row to move to
     * @param c Col to move to
     * @return True if move was successful
     */
    public boolean trySelect(int oldR, int oldC, int r, int c) {

        // Index of this cars information
        int arrayIndex = getArrayIndex(oldR, oldC);

        try {

            // If its horizontal
            if (isHorizontal[arrayIndex] && oldR == r) {

                int difference = c - oldC;

                char[][] copy = copyBoard(board);
                int copyStartCol = startCol[arrayIndex];
                int copyEndCol = endCol[arrayIndex];

                // If moving to the right
                if (difference > 0) {

                    // Loop through move distance and return false if there is an obstacle
                    for (int i = 0; i < difference; i++) {
                        if (copy[r][copyEndCol + 1] =='.') {
                            copy[r][copyStartCol] = '.';
                            copyStartCol++;
                            copyEndCol++;
                            copy[r][copyEndCol] = car[arrayIndex];
                        } else {
                            return false;
                        }
                    }

                  // If moving to the left
                } else if (difference < 0) {

                    // Loop through move distance and return false if there is an obstacle
                    for (int i = 0; i < Math.abs(difference); i++) {
                        if (copy[r][copyStartCol - 1] =='.') {
                            copy[r][copyEndCol] = '.';
                            copyStartCol--;
                            copyEndCol--;
                            copy[r][copyStartCol] = car[arrayIndex];
                        } else {
                            return false;
                        }
                    }
                } else {
                    return true;
                }

                // If there were no obstacles, set old values to new values
                board = copy;
                startCol[arrayIndex] = copyStartCol;
                endCol[arrayIndex] = copyEndCol;
                return true;


              // If it is vertical
            } else if (isVert[arrayIndex] && oldC == c) {

                int difference = r - oldR;

                char[][] copy = copyBoard(board);
                int copyStartRow = startRow[arrayIndex];
                int copyEndRow = endRow[arrayIndex];

                // If moving down
                if (difference > 0) {

                    // Loop through move distance and return false if there is an obstacle
                    for (int i = 0; i < difference; i++) {
                        if (copy[copyEndRow + 1][c] =='.') {
                            copy[copyStartRow][c] = '.';
                            copyStartRow++;
                            copyEndRow++;
                            copy[copyEndRow][c] = car[arrayIndex];
                        } else {
                            return false;
                        }
                    }

                  // If moving up
                } else if (difference < 0) {

                    // Loop through move distance and return false if there is an obstacle
                    for (int i = 0; i < Math.abs(difference); i++) {
                        if (copy[copyStartRow - 1][c] =='.') {
                            copy[copyEndRow][c] = '.';
                            copyStartRow--;
                            copyEndRow--;
                            copy[copyStartRow][c] = car[arrayIndex];
                        } else {
                            return false;
                        }
                    }
                } else {
                    return true;
                }

                // If there were no obstacles, set old values to new values
                board = copy;
                startRow[arrayIndex] = copyStartRow;
                endRow[arrayIndex] = copyEndRow;

                return true;

            }

          // If it goes out of bounds, invalid move
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return false;
    }

//    public void remakeBoard() {
//        for (char[] row: board) {
//            Arrays.fill(row, '.');
//        }
//
//        // For each car in file
//        for (int i = 0; i < car.length; i++) {
//
//            // Store information of cars in each index
////            fields = in.readLine().split("\\s+");
////            cars[i] = fields[0].charAt(0);
////            startRows[i] = Integer.parseInt(fields[1]);
////            startCols[i] = Integer.parseInt(fields[2]);
////            endRows[i] = Integer.parseInt(fields[3]);
////            endCols[i] = Integer.parseInt(fields[4]);
//
//            // Fill the board with cars
//            if (startRow[i] == endRow[i]) {
//                for (int j = 0; j <= endCol[i] - startCol[i]; j++) {
//                    board[startRow[i]][startCol[i] + j] = car[i];
//                }
//            } else if (startCol[i] == endCol[i]) {
//                for (int j = 0; j <= endRow[i] - startRow[i]; j++) {
//                    board[startRow[i] + j][startCol[i]] = car[i];
//                }
//            }
//        }
//    }

    /**
     * Find the index of information for this car
     * @param r row
     * @param c col
     * @return index
     */
    public int getArrayIndex(int r, int c) {
        char carVal = board[r][c];
        int index = 0;
        for (char vals: car) {
            if (vals == carVal) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override
    public boolean isSolution() {
        for (char[] chars : board) {
            if (chars[goal] == 'X') {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a copy of the board
     * @param other board to copy
     * @return Copy of board
     */
    private char[][] copyBoard(char[][] other) {
        char[][] newOne = new char[other.length][other[0].length];
        for (int i = 0; i < other.length; i++) {
            System.arraycopy(other[i], 0, newOne[i], 0, other[0].length);
        }
        return newOne;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();

        // For every car on the board
        for (int i = 0; i < car.length; i++) {
            if (isHorizontal[i]) {

                // If there is an empty space to the right
                if (endCol[i] + 1 < board[0].length
                        && board[startRow[i]][endCol[i] + 1] == '.') {

                    // Make local variables
                    char[][] horizAdd = copyBoard(board);
                    int[] rightStartCol = startCol.clone();
                    int[] rightEndCol = endCol.clone();

                    // Move car to the right one and create new neighbor
                    horizAdd[startRow[i]][rightStartCol[i]] = '.';
                    rightStartCol[i]++;
                    rightEndCol[i]++;
                    horizAdd[startRow[i]][rightEndCol[i]] = car[i];
                    neighbors.add(new JamConfig(
                            horizAdd, car, startRow, rightStartCol, endRow, rightEndCol, goal));
                }

                // If there is an empty space to the left
                if (startCol[i] > 0
                        && board[startRow[i]][startCol[i] - 1] == '.') {

                    // Make local variables
                    char[][] horizSubtract = copyBoard(board);
                    int[] leftStartCol = startCol.clone();
                    int[] leftEndCol = endCol.clone();

                    // Move car to the left one and create new neighbor
                    horizSubtract[startRow[i]][leftEndCol[i]] = '.';
                    leftStartCol[i]--;
                    leftEndCol[i]--;
                    horizSubtract[startRow[i]][leftStartCol[i]] = car[i];
                    neighbors.add(new JamConfig(
                            horizSubtract, car, startRow, leftStartCol, endRow, leftEndCol, goal));
                }

            } else if (isVert[i]) {

                // If there is an empty space below
                if (endRow[i] + 1 < board.length
                        && board[endRow[i] + 1][endCol[i]] == '.') {

                    // Make local variables
                    char[][] vertAdd = copyBoard(board);
                    int[] downStartRow = startRow.clone();
                    int[] downEndRow = endRow.clone();

                    // Move car down one and create new neighbor
                    vertAdd[downStartRow[i]][startCol[i]] = '.';
                    downStartRow[i]++;
                    downEndRow[i]++;
                    vertAdd[downEndRow[i]][startCol[i]] = car[i];
                    neighbors.add(new JamConfig(
                            vertAdd, car, downStartRow, startCol, downEndRow, endCol, goal));
                }

                // If there is an empty space above
                if (startRow[i] > 0
                        && board[startRow[i] - 1][endCol[i]] == '.') {

                    // Make local variables
                    char[][] vertAdd = copyBoard(board);
                    int[] upStartRow = startRow.clone();
                    int[] upEndRow = endRow.clone();

                    // Move car up one and create new neighbor
                    vertAdd[upEndRow[i]][startCol[i]] = '.';
                    upStartRow[i]--;
                    upEndRow[i]--;
                    vertAdd[upStartRow[i]][startCol[i]] = car[i];
                    neighbors.add(new JamConfig(
                            vertAdd, car, upStartRow, startCol, upEndRow, endCol, goal));
                }
            }
        }

        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof JamConfig) {
            return Arrays.deepEquals(board, ((JamConfig) other).getBoard());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        String end = "";

        end += "   ";
        for(int c =0; c<board[0].length; c++){
            end += c+" ";
        }
        end += "\n";
        end += "  ";

        for (int i = 0; i < board[0].length; i++) {
            end += "--";
        }

        end += "\n";

        for (int i = 0; i < board.length; i++) {
            end += i + "| ";
            for (int j = 0; j < board[0].length; j++) {
                end += board[i][j] + " ";
            }
            end += "\n";
        }
        return end;
    }
}
