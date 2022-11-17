package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.*;

public class StringConfiguration implements Configuration {

    private final String current;
    private static String end;

    public StringConfiguration(String start, String end){
        this.current = start;
        StringConfiguration.end = end;
    }

    public StringConfiguration(String current){
        this.current = current;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        char back;
        char forward;
        Collection<Configuration> nbr = new ArrayList<>();

        for (int i = 0; i < current.length(); i++) {
            char currChar = current.charAt(i);
            if (current.charAt(i) == 65) {
                back = 'Z';
                forward = 'B';
            }
            else if (current.charAt(i) == 90) {
                back = 'Y';
                forward = 'A';
            }
            else{
                back = (char)(currChar-1);
                forward = (char)(currChar+1);
            }
            nbr.add(new StringConfiguration(current.substring(0,i)+ forward +current.substring(i+1)));
            nbr.add(new StringConfiguration(current.substring(0,i)+ back +current.substring(i+1)));
        }
        return nbr;
    }

    @Override
    public boolean isSolution() {
        return current.equals(end);
    }

    @Override
    public boolean equals(Object other){
        boolean result = false;
        if(other instanceof StringConfiguration){
            result = this.current.equals(((StringConfiguration) other).current);
        }
        return result;
    }

    @Override
    public int hashCode(){
        return this.current.hashCode();
    }

    @Override
    public String toString(){
        return current;
    }

}
