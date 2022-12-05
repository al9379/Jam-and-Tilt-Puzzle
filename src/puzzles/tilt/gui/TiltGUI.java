package puzzles.tilt.gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private TiltModel model;
    private Label label;
    GridPane gridPane = new GridPane();
    private String filename = "";
    private boolean gameOn = true;
    private String previousFile;
    private boolean newGame = false;


    // for demonstration purposes
    private final Image greenDisk =
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "green.png")));
    private final Image blueDisk =
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "blue.png")));
    private final Image hole =
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "hole.png")));
    private final Image block =
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "block.png")));
    private final Image white =
            new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "white.png")));

    /**
     * Initializes the model
     * @throws IOException
     */
    public void init() throws IOException {
        filename = getParameters().getRaw().get(0);
        this.model = new TiltModel(filename);
        model.addObserver(this);
    }

    /**
     * Sets up the board
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        label = new Label("Tilt GUI");
        BorderPane borderPane = new BorderPane();
        BorderPane inside = new BorderPane();

        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(label);
        borderPane.setTop(hBox1);
        hBox1.setAlignment(Pos.CENTER);

        inside.setCenter(makeGridPane());

        Button up = new Button("^");
        up.setAlignment(Pos.CENTER);
        up.setPrefSize(775,30);
        inside.setTop(up);

        Button down = new Button("v");
        down.setAlignment(Pos.CENTER);
        down.setPrefSize(775,30);
        inside.setBottom(down);

        Button right = new Button(">");
        right.setAlignment(Pos.CENTER);
        right.setPrefSize(30,675);
        inside.setRight(right);

        Button left = new Button("<");
        left.setAlignment(Pos.CENTER);
        left.setPrefSize(30,675);
        inside.setLeft(left);

        up.setOnAction((event) -> {
            model.move('n');
            update(model, "Tilted North");
        });
        down.setOnAction((event) -> {
            model.move('s');
            update(model, "Tilted South");
        });
        right.setOnAction((event) -> {
            model.move('e');
            update(model, "Tilted East");
        });
        left.setOnAction((event) -> {
            model.move('w');
            update(model, "Tilted West");
        });

        loadFile(filename);

        HBox hBox = new HBox();
        Button first = new Button("Load");
        Button second = new Button("Reset");
        Button third = new Button("Hint");
        hBox.getChildren().addAll(first, second, third);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setBottom(hBox);

        //If the player chooses load
        first.setOnAction((event) -> {
            boolean read = false;
            while(!read) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Resource File");
                    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/tilt"));
                    File name = fileChooser.showOpenDialog(stage);
                    newGame = true;
                    loadFile(String.valueOf(name));
                    read = true;
                } catch (Exception e) {
                    label.setText("Failed to load: Try again");
                }
            }
        });
        //if the player resets the file
        second.setOnAction((event) -> {
            resetFile();
        });
        //if the player chooses hint
        third.setOnAction((event) -> {
            hint();
        });

        borderPane.setCenter(inside);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void hint() {
        if (gameOn) {
            model.useHint();
        }
    }

    public void resetFile() {
        loadFile(previousFile);
    }

    //loads the file from the given string
    public void loadFile(String filename) {
        try {
            gameOn = true;
            if (model.loadBoard(filename)) {
                previousFile = filename;
                if(newGame){
                    makeGridPane();
                }
            }
        } catch (Exception e) {
            update(model, "Failed to load: " + filename);
        }
    }

    //makes the board as a gridpane of buttons
    private GridPane makeGridPane(){
        //Uses a GridPane due to the structured layout of the board
        for (int row=0; row<model.getDim(); ++row) {
            for (int col=0; col<model.getDim(); ++col) {
                Button button = new Button();
                button.setMaxSize(10,10);
                if(model.getBoard()[row][col] == 'G'){
                    button.setGraphic(new ImageView(greenDisk));
                }
                else if(model.getBoard()[row][col] == 'B'){
                    button.setGraphic(new ImageView(blueDisk));
                }
                else if(model.getBoard()[row][col] == '*'){
                    button.setGraphic(new ImageView(block));
                }
                else if(model.getBoard()[row][col] == 'O'){
                    button.setGraphic(new ImageView(hole));
                }
                else{
                    button.setGraphic(new ImageView(white));
                }
                button.setStyle("-fx-border-color: grey;");
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    @Override
    public void update(TiltModel tiltModel, String message) {

        label.setText(message);

        //Changes all the buttons to the current model
        if (gameOn) {
            for (Node b : gridPane.getChildren()) {
                if (b instanceof Button) {
                    if(model.getBoard()[GridPane.getRowIndex(b)][GridPane.getColumnIndex(b)] == 'G'){
                        ((Button) b).setGraphic(new ImageView(greenDisk));
                    }
                    else if(model.getBoard()[GridPane.getRowIndex(b)][GridPane.getColumnIndex(b)] == 'B'){
                        ((Button) b).setGraphic(new ImageView(blueDisk));
                    }
                    else if(model.getBoard()[GridPane.getRowIndex(b)][GridPane.getColumnIndex(b)] == '*'){
                        ((Button) b).setGraphic(new ImageView(block));
                    }
                    else if(model.getBoard()[GridPane.getRowIndex(b)][GridPane.getColumnIndex(b)] == 'O'){
                        ((Button) b).setGraphic(new ImageView(hole));
                    }
                    else{
                        ((Button) b).setGraphic(new ImageView(white));
                    }
                    b.setStyle("-fx-border-color: grey;");
                }
            }
        }

        if(message.equals("Blue falls in")){
            label.setText("Not allowed");
        }

        if (model.gameOver()) {
            label.setText("You Win! Start New Game or Quit");
            gameOn = false;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
