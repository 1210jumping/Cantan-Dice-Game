package comp1110.ass2.gui;

import comp1110.ass2.CatanDice;
import comp1110.ass2.Road;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

//This class was written by Alex and COMP1110 assignment designers?

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField boardTextField;


    /**
     * Show the state of a (single player's) board in the window.
     *
     * @param board_state string representation of the board state.
     */
    void displayState(String board_state) {
        Image board = new Image("file:assets/StylisedBoard.png");
        ImageView view = new ImageView(board);
        view.setLayoutX(100);
        view.setLayoutY(50);
        root.getChildren().add(view);
        root.getChildren().add(new Road(-1,true).getModel());

        for(var v : CatanDice.generatePieces(board_state)){
            root.getChildren().add(v.getModel());
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label boardLabel = new Label("Board State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(500);
        Button button = new Button("Show");
        button.setOnAction(e -> displayState(boardTextField.getText()));
        HBox hb = new HBox();
        hb.getChildren().addAll(boardLabel, boardTextField, button);
        hb.setSpacing(10);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Board State Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
