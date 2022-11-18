package puzzles.jam.model;


import puzzles.common.solver.Configuration;
import puzzles.jam.solver.Jam;

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
    private final char[][] board;

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

    @Override
    public boolean isSolution() {
        for (char[] chars : board) {
            if (chars[goal] == 'X') {
                return true;
            }
        }
        return false;
    }

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

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (j == board[0].length - 1) {
                    end += board[i][j];
                } else {
                    end += board[i][j] + " ";
                }
            }
            if (i != board.length - 1) {
                end += "\n";
            }
        }
        return end;
    }
}
