package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Arrays;

/**
 * Main class for the water buckets puzzle.
 *
 * @author YOUR NAME HERE
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        String state = "";
        boolean invalid = true;
        int config = 0;
        int uConfig = 0;
        int step = 0;
        if (args.length < 2) {
            System.out.println(("Usage: java Water amount bucket1 bucket2 ..."));
        } else {
            int end = Integer.parseInt(args[0]);
            int[] buckets = new int[args.length-1];
            for(int i = 1; i < args.length; i++){
                buckets[i-1] = Integer.parseInt(args[i]);
            }
            System.out.println("Amount: " + args[0] + ", Buckets: " + Arrays.toString(buckets));
            Configuration data = new WaterConfiguration(end,buckets);
            Solver x = new Solver();
            for (Configuration result : x.solve(data)) {
                state += ("Step " + step + ": " + result + "\n");
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
