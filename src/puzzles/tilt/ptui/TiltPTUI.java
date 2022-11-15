package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

public class TiltPTUI implements Observer<TiltModel, String> {
    private TiltModel model;

    @Override
    public void update(TiltModel model, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
    }
}
