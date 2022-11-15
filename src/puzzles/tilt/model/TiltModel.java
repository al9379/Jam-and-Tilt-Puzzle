package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;

import java.util.LinkedList;
import java.util.List;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
