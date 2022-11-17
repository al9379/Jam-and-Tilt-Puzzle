package puzzles.common.solver;

import java.util.*;

public class Solver{
    List<Configuration> queue = new LinkedList<>();
    HashMap<Configuration, Configuration> predecessors = new HashMap<>();
    List<Configuration> path = new LinkedList<>();
    private int level = 0;
    private int uLevel = 0;

    public Collection<Configuration> solve(Configuration start) {
        queue.add(start);
        predecessors.put(start, null);
        Configuration current = start;

        while (!queue.isEmpty()) {
            current = queue.remove(0);
            if (current.isSolution()) {
                break;
            }
            for (Configuration nbr : current.getNeighbors()) {
                level++;
                if (!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                    uLevel++;
                }
            }
        }
        if(predecessors.containsKey(current)) {
            path.add(0, current);
            Configuration currConfig = predecessors.get(current);
            while (currConfig != start) {
                path.add(0, currConfig);
                currConfig = predecessors.get(currConfig);
            }
            path.add(0, start);
        }
        return path;
    }

    public int getConfigurations(){
        return level;
    }

    public int getUniqueConfigurations(){
        return uLevel;
    }
}
