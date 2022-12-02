package puzzles.jam.gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.jam.model.JamModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.File;

public class JamGUI extends Application  implements Observer<JamModel, String>  {
    private final static int ICON_SIZE = 75;

    private String previousFile;

    Label label;             // Text box at the top
    GridPane gridPane;       // Grid in center
    FlowPane flowPane;       // Buttons at bottom


    private boolean gameOn = true;  // Stores state of the game
    private JamModel model;    // Instance of the model

    private String filename = "";

    public void init() {
        this.model = new JamModel();
        model.addObserver(this);
        filename = getParameters().getRaw().get(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();

        // Create label at top
        label = new Label();
        borderPane.setTop(label);
        BorderPane.setAlignment(label, Pos.CENTER);

        // Make a grey grid
        gridPane = new GridPane();
        gridPane.setBackground(Background.fill(Color.GRAY));
        gridPane.setMinHeight(ICON_SIZE * (model.getRows() + 1));
        gridPane.setMinWidth(ICON_SIZE * (model.getCols() + 1));
        gridPane.setGridLinesVisible(true);

        loadFile(filename);

        // Make a grid of buttons
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {

                Button button = new Button();
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                button.setMinSize(ICON_SIZE, ICON_SIZE);

                button.setBackground(Background.fill(Color.LIGHTGRAY));

                int finalJ = j;
                int finalI = i;
                button.setOnAction(event -> select(finalI, finalJ));

                gridPane.add(button, j, i);
            }
        }

        // Organize the grid
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        borderPane.setCenter(gridPane);

        // Set bottom buttons
        flowPane = new FlowPane();
        Button load = new Button("Load");
        Button reset = new Button("Reset");
        Button hint = new Button("Hint");

        // Set button actions
        load.setOnAction(event -> newBoard(stage));
        reset.setOnAction(event -> resetFile());
        hint.setOnAction(event -> hint());

        // Add buttons to flowpane at bottom
        flowPane.getChildren().add(load);
        flowPane.getChildren().add(reset);
        flowPane.getChildren().add(hint);
        flowPane.setAlignment(Pos.CENTER);
        borderPane.setBottom(flowPane);

        // Create scene
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("Jam Puzzle");
        stage.setHeight(ICON_SIZE * (model.getRows() + 1) + 20);
        stage.setWidth(ICON_SIZE * model.getCols() + 22);
        stage.setResizable(false);
        stage.show();
        update(model, "Loaded: " + filename);
    }

    public void newBoard(Stage stage) {
        BorderPane borderPane = new BorderPane();

        // Create label at top
        label = new Label();
        borderPane.setTop(label);
        label.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(label, Pos.CENTER);

        // Make a grey grid
        gridPane = new GridPane();
        gridPane.setBackground(Background.fill(Color.GRAY));
        gridPane.setGridLinesVisible(true);

        loadFile(stage);

        // Make a grid of buttons
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {

                Button button = new Button();
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                button.setMinSize(ICON_SIZE, ICON_SIZE);

                button.setBackground(Background.fill(Color.LIGHTGRAY));

                int finalJ = j;
                int finalI = i;
                button.setOnAction(event -> select(finalI, finalJ));

                gridPane.add(button, j, i);
            }
        }

        // Organize the grid
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        borderPane.setCenter(gridPane);

        flowPane.setAlignment(Pos.CENTER);
        borderPane.setBottom(flowPane);
        BorderPane.setAlignment(flowPane, Pos.CENTER);

        // Create scene
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("Jam Puzzle");
        stage.setHeight(ICON_SIZE * (model.getRows() + 1) + 20);
        stage.setWidth(ICON_SIZE * model.getCols() + 22);
        stage.setResizable(false);
        stage.show();
        update(model, "Loaded: " + previousFile.substring(61));
    }

    /**
     * Button to use a hint
     */
    public void hint() {
        if (gameOn) {
            model.useHint();
        }
    }

    /**
     * Button to reset file
     */
    public void resetFile() {
        loadFile(previousFile);
    }

    /**
     * Loads file from a filename
     * @param filename name of file
     */
    public void loadFile(String filename) {

        // Tries to load a board from filename
        try {
            gameOn = true;
            if (model.loadBoard(filename)) {
                previousFile = filename;
            }
        } catch (Exception e) {
            update(model, "Failed to load: " + filename);
        }

    }

    /**
     * Loads file from a file chooser
     * @param stage current stage
     */
    public void loadFile(Stage stage) {

        // Uses fileChooser to select a file and send it to the model
        String name = "";
        try {
            gameOn = true;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load a game board.");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/jam"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selected = fileChooser.showOpenDialog(stage);
            name = selected.toString();

            if (model.loadBoard(selected)) {
                previousFile = selected.toString();
            }
        } catch (Exception e) {
            update(model, "Failed to load: " + name.substring(61));
        }

    }

    /**
     * Select button
     * @param x value
     * @param y value
     */
    public void select(int x, int y) {
        if (gameOn) {
            model.selectSquare(x, y);
        }
    }

    @Override
    public void update(JamModel jamModel, String message) {

        label.setText(message);

        // If the game is active
        if (gameOn) {

            // Update every button in the board with new colors
            for (Node b : gridPane.getChildren()) {

                if (b instanceof Button) {
                    Color color = findColor(model.getBoard()[GridPane.getRowIndex(b)][GridPane.getColumnIndex(b)]);
                    ((Button) b).setBackground(Background.fill(color));
                    ((Button) b).setText(String.valueOf(model.getBoard()[GridPane.getRowIndex(b)][GridPane.getColumnIndex(b)]));
                }
            }
        }

        if (model.gameOver()) {
            label.setText("You Win! Start New Game or Quit");
            gameOn = false;
        }
    }

    /**
     * Finds the color associated with a char
     * @param letter char
     * @return Color
     */
    public Color findColor(char letter) {
        return switch (letter) {
            case 'A' -> Color.LIMEGREEN;
            case 'B' -> Color.ORANGE;
            case 'C' -> Color.BLUE;
            case 'D' -> Color.MAGENTA;
            case 'E' -> Color.web("5E4EE8");
            case 'F' -> Color.DARKGREEN;
            case 'G' -> Color.DARKGRAY;
            case 'H' -> Color.web("837C7C");
            case 'I' -> Color.LIGHTYELLOW;
            case 'J' -> Color.BROWN;
            case 'K' -> Color.DARKOLIVEGREEN;
            case 'L' -> Color.WHITE;
            case 'O' -> Color.YELLOW;
            case 'P' -> Color.PURPLE;
            case 'Q' -> Color.LIGHTBLUE;
            case 'R' -> Color.FORESTGREEN;
            case 'S' -> Color.BLACK;
            case 'X' -> Color.RED;
            default -> Color.LIGHTGRAY;
        };
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
