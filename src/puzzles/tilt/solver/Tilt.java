package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tilt {
    public static void main(String[] args) throws IOException{
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else{
            //reads the given file
            String file = args[0];
            String line;
            int row = 0;
            BufferedReader in = new BufferedReader(new FileReader(file));
            int dim = Integer.parseInt(in.readLine());
            char[][] board = new char[dim][dim];

            while((line = in.readLine()) != null){
                String[] field = line.split(" ");
                for(int col = 0; col < field.length; col++){
                    board[row][col] = field[col].charAt(0);
                }
                row++;
            }
            in.close();

            //uses TiltConfig model to solve
            Configuration data = new TiltConfig(board,dim);
            Solver x = new Solver();

            String state = "";
            int step = 0;
            boolean invalid = true;

            for (Configuration result : x.solve(data)) {
                state += ("Step " + step + ":\n" + result + "\n");
                step++;
                if(result.isSolution()) {
                    invalid = false;
                }
            }

            System.out.println("Total configs: " + (x.getConfigurations()+1));
            System.out.println("Unique configs: " + (x.getUniqueConfigurations()+1));

            if(invalid)
                System.out.println("No Solution");
            else
                System.out.println(state);
        }
    }
}
