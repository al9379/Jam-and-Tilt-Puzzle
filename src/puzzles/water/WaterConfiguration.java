package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.*;

public class WaterConfiguration implements Configuration {

    private static int end = 0;
    private static int[] capacity;
    private final int[] current;

    public WaterConfiguration(int end, int[] buckets){
        capacity = buckets;
        WaterConfiguration.end = end;
        current = new int[buckets.length];
    }

    public WaterConfiguration(int[] current) {
        this.current = new int[capacity.length];
        for(int i = 0; i < current.length; i++) {
            this.current[i] = current[i];
        }
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        HashSet<Configuration> nbr = new LinkedHashSet<>();
        for(int i = 0; i < current.length; i++){
            //Dump the bucket
            int temp = current[i];
            current[i] = 0;
            nbr.add(new WaterConfiguration(current));
            //Fill the bucket
            current[i] = capacity[i];
            nbr.add(new WaterConfiguration(current));

            current[i] = temp; //Set back to original level

            for(int j = 0; j < current.length; j++){
                if(i != j){
                    int temp1 = current[i];
                    int temp2 = current[j];
                    if(current[i]+current[j]>=capacity[j]){
                        current[i] = current[i]-(capacity[j]-current[j]);
                        current[j] = capacity[j];
                    }
                    else{
                        current[j] = current[i] + current[j];
                        current[i] = 0;
                    }
                    nbr.add(new WaterConfiguration(current));
                    current[i] = temp1;
                    current[j] = temp2;
                }
            }
        }
        return nbr;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i : current) {
            result += Objects.hash(i);
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof WaterConfiguration otherConfig)
        {
            for(int i = 0;i<current.length;i++)
            {
                if(current[i]!=otherConfig.current[i])
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "[";
        for(int i = 0; i < current.length; i++)
        {
            if(i == 0)
                result += current[i];
            else
                result += "," + current[i];
        }
        result += "]";
        return result;
    }

    @Override
    public boolean isSolution() {
        for (int j : current) {
            if (j == end) {
                return true;
            }
        }
        return false;
    }
}
