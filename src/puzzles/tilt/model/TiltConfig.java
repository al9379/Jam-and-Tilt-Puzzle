package puzzles.tilt.model;

// TODO: implement your TiltConfig for the common solver

import puzzles.common.solver.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class TiltConfig implements Configuration {
    private final char[][] current;
    private static int dim;

    /**
     * Default constructor for TiltConfig
     * @param initial the initial 2d array given
     * @param size the dimensions of the board
     */
    public TiltConfig(char[][] initial, int size){
        dim = size;
        current = new char[size][size];
        //copies the array onto another one
        for(int i = 0; i < size; i++){
            System.arraycopy(initial[i], 0, current[i], 0, size);
        }
    }

    public TiltConfig(char[][] initial){
        current = new char[dim][dim];
        for(int i = 0; i < dim; i++){
            System.arraycopy(initial[i], 0, current[i], 0, dim);
        }
    }

    /**
     * Checks to see if there is any G blocks left
     * @return if the puzzle is solved
     */
    @Override
    public boolean isSolution(){
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                if(current[i][j] == 'G')
                    return false;
            }
        }
        return true;
    }

    /**
     * @return the possible neighbors of the current board
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        HashSet<Configuration> set = new LinkedHashSet<>();
        boolean uValid = true, lValid = true, rValid = true, dValid = true;

        char[][] left = new char[dim][dim];
        char[][] right = new char[dim][dim];
        char[][] up = new char[dim][dim];
        char[][] down = new char[dim][dim];

        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                left[i][j] = current[i][j];
                right[i][j] = current[i][j];
                up[i][j] = current[i][j];
                down[i][j] = current[i][j];
            }
        }

        //starts from the top left and goes to the bottom right
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                if(current[i][j] == 'B' || current[i][j] == 'G'){
                    if(i != 0){
                        for(int pos = i-1; pos >= 0; pos--){
                            if(up[pos][j] == 'G' || up[pos][j] == 'B')
                                break;
                            else if(up[pos][j] == '*')
                                break;
                            else if(up[pos][j] == '.'){
                                up[pos + 1][j] = '.';
                                up[pos][j] = current[i][j];
                            }
                            else if(up[pos][j] == 'O'){
                                if(current[i][j] == 'G'){
                                    up[pos+1][j] = '.';
                                }
                                else
                                    uValid = false;
                                break;
                            }
                        }
                    }
                    if(j != 0){
                        for(int pos = j-1; pos >= 0; pos--){
                            if(left[i][pos] == 'G' || left[i][pos] == 'B')
                                break;
                            else if(left[i][pos] == '*')
                                break;
                            else if(left[i][pos] == '.'){
                                left[i][pos + 1] = '.';
                                left[i][pos] = current[i][j];
                            }
                            else if(left[i][pos] == 'O'){
                                if(current[i][j] == 'G'){
                                    left[i][pos + 1] = '.';
                                }
                                else
                                    lValid = false;
                                break;
                            }
                        }
                    }
                }
            }
        }

        //starts from the bottom right and moves tot he top left
        for(int i = dim-1; i >= 0; i--){
            for(int j = dim-1; j >= 0; j--){
                if(current[i][j] == 'B' || current[i][j] == 'G'){
                    if(i != dim-1){
                        for(int pos = i+1; pos < dim; pos++){
                            if(down[pos][j] == 'G' || down[pos][j] == 'B')
                                break;
                            else if(down[pos][j] == '*')
                                break;
                            else if(down[pos][j] == '.'){
                                down[pos - 1][j] = '.';
                                down[pos][j] = current[i][j];
                            }
                            else if(down[pos][j] == 'O'){
                                if(current[i][j] == 'G'){
                                    down[pos-1][j] = '.';
                                }
                                else
                                    dValid = false;
                                break;
                            }

                        }
                    }
                    //right
                    if(j != dim-1){
                        for(int pos = j+1; pos < dim; pos++){
                            if(right[i][pos] == 'G' || right[i][pos] == 'B')
                                break;
                            else if(right[i][pos] == '*')
                                break;
                            else if(right[i][pos] == '.'){
                                right[i][pos - 1] = '.';
                                right[i][pos] = current[i][j];
                            }
                            else if(right[i][pos] == 'O'){
                                if(current[i][j] == 'G'){
                                    right[i][pos - 1] = '.';
                                }
                                else
                                    rValid = false;
                                break;
                            }

                        }
                    }
                }
            }
        }

        //adds to the set if it was valid
        if(uValid)
            set.add(new TiltConfig(up));
        if(rValid)
            set.add(new TiltConfig(right));
        if(dValid)
            set.add(new TiltConfig(down));
        if(lValid)
            set.add(new TiltConfig(left));

        return set;
    }

    /**
     *
     * @return tiltconfig of the board moving north
     */
    public TiltConfig moveNorth(){
        char[][] up = new char[dim][dim];
        char[][] empty = new char[dim][dim];
        empty[0][0] = 'e';
        boolean isValid = true;

        for(int i = 0; i < dim; i++){
            System.arraycopy(current[i], 0, up[i], 0, dim);
        }

        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                if(current[i][j] == 'B' || current[i][j] == 'G'){
                    if(i != 0){
                        for(int pos = i-1; pos >= 0; pos--){
                            if(up[pos][j] == 'G' || up[pos][j] == 'B')
                                break;
                            else if(up[pos][j] == '*')
                                break;
                            else if(up[pos][j] == '.'){
                                up[pos + 1][j] = '.';
                                up[pos][j] = current[i][j];
                            }
                            else if(up[pos][j] == 'O'){
                                if(current[i][j] == 'G'){
                                    up[pos+1][j] = '.';
                                }
                                else {
                                    isValid = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(isValid){
            return new TiltConfig(up);
        }
        else
            return new TiltConfig(empty);
    }

    /**
     *
     * @return tiltconfig of the baord moving west
     */
    public TiltConfig moveWest(){
        boolean isValid = true;
        char[][] left = new char[dim][dim];
        char[][] empty = new char[dim][dim];
        empty[0][0] = 'e';

        for(int i = 0; i < dim; i++){
            System.arraycopy(current[i], 0, left[i], 0, dim);
        }

        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                if(current[i][j] == 'B' || current[i][j] == 'G'){
                    if(j != 0){
                        for(int pos = j-1; pos >= 0; pos--){
                            if(left[i][pos] == 'G' || left[i][pos] == 'B')
                                break;
                            else if(left[i][pos] == '*')
                                break;
                            else if(left[i][pos] == '.'){
                                left[i][pos + 1] = '.';
                                left[i][pos] = current[i][j];
                            }
                            else if(left[i][pos] == 'O'){
                                if(current[i][j] == 'G'){
                                    left[i][pos + 1] = '.';
                                }
                                else{
                                    isValid = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(isValid){
            return new TiltConfig(left);
        }
        else
            return new TiltConfig(empty);    }

    /**
     *
     * @return tiltconfig of the board moving south
     */
    public TiltConfig moveSouth(){
        boolean isValid = true;
        char[][] down = new char[dim][dim];
        char[][] empty = new char[dim][dim];
        empty[0][0] = 'e';

        for(int i = 0; i < dim; i++){
            System.arraycopy(current[i], 0, down[i], 0, dim);
        }

        for(int i = dim-1; i >= 0; i--){
            for(int j = dim-1; j >= 0; j--){
                if(current[i][j] == 'B' || current[i][j] == 'G'){
                    if(i != dim-1){
                        for(int pos = i+1; pos < dim; pos++){
                            if(down[pos][j] == 'G' || down[pos][j] == 'B')
                                break;
                            else if(down[pos][j] == '*')
                                break;
                            else if(down[pos][j] == '.'){
                                down[pos - 1][j] = '.';
                                down[pos][j] = current[i][j];
                            }
                            else if(down[pos][j] == 'O'){
                                if(current[i][j] == 'G'){
                                    down[pos-1][j] = '.';
                                }
                                else{
                                    isValid = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(isValid){
            return new TiltConfig(down);
        }
        else
            return new TiltConfig(empty);
    }

    /**
     *
     * @return tiltconfig of the board moving east
     */
    public TiltConfig moveEast(){
        boolean isValid = true;
        char[][] right = new char[dim][dim];
        char[][] empty = new char[dim][dim];
        empty[0][0] = 'e';

        for(int i = 0; i < dim; i++){
            System.arraycopy(current[i], 0, right[i], 0, dim);
        }

        for(int i = dim-1; i >= 0; i--){
            for(int j = dim-1; j >= 0; j--){
                if(current[i][j] == 'B' || current[i][j] == 'G'){
                    if(j != dim-1){
                        for(int pos = j+1; pos < dim; pos++){
                            if(right[i][pos] == 'G' || right[i][pos] == 'B')
                                break;
                            else if(right[i][pos] == '*')
                                break;
                            else if(right[i][pos] == '.'){
                                right[i][pos - 1] = '.';
                                right[i][pos] = current[i][j];
                            }
                            else if(right[i][pos] == 'O'){
                                if(current[i][j] == 'G'){
                                    right[i][pos - 1] = '.';
                                }
                                else{
                                    isValid = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(isValid){
            return new TiltConfig(right);
        }
        else
            return new TiltConfig(empty);
    }

    public char[][] getBoard() {
        return current;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = true;
        if(other instanceof TiltConfig otherConfig){
            for(int i = 0; i < dim; i++){
                for(int j = 0; j < dim; j++){
                    if (this.current[i][j] != otherConfig.current[i][j]) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.current);
    }

    @Override
    public String toString() {
        String msg = "";
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                msg += String.valueOf(this.getBoard()[i][j]) + " ";
            }
            msg += "\n";
        }
        return msg;
    }
}
