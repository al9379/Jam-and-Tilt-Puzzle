package puzzles.strings;
import java.util.*;
import puzzles.common.solver.*;
import puzzles.common.solver.Solver;

/**
 * Main class for the strings puzzle.
 *
 * @author YOUR NAME HERE
 */
public class Strings {
    /**
     * Run an instance of the strings puzzle.
     *
     * @param args [0]: the starting string;
     *             [1]: the finish string.
     */
    public static void main(String[] args) {
        int config = 0;
        int uConfig = 0;
        String state = "";
        boolean invalid = true;
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            System.out.println("Start: " + args[0] + ", End: " + args[1]);
            Solver x = new Solver();
            Configuration data = new StringConfiguration(args[0],args[1]);
            Configuration end = new StringConfiguration(args[1]);
            int step = 0;
            for (Configuration result : x.solve(data)) {
                state += "Step " + step + ": " + result + "\n";
                step++;
                if(result.equals(end))
                    invalid = false;
            }
            config += x.getConfigurations();
            uConfig += x.getUniqueConfigurations();
            System.out.println("Total configs: " + config);
            System.out.println("Unique configs: " + uConfig);
            if(invalid)
                System.out.println("No Solution");
            else
                System.out.println(state);
        }
    }
}
