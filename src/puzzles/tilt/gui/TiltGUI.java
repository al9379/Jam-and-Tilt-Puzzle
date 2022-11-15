package puzzles.tilt.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));

    public void init() {
        String filename = getParameters().getRaw().get(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Button button = new Button();
        button.setGraphic(new ImageView(greenDisk));
        Scene scene = new Scene(button);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(TiltModel tiltModel, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
