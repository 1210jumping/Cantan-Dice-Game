package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//This class was written by Alex and Pin-Shen

/**
 * The runnable java fx game
 */

public class Game extends Application {

    public static final Group root = new Group();
    public static Group diceTime = new Group();
    public static Text diceTimeText;
    public static Text scoreText;
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    public static Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    public static GameState gameState;
    public static Button tradeButton = new Button("Trade");
    public static List<Dice> diceList = new ArrayList<>();
    private final Group controls = new Group();
    public static Button diceRollButton;
    public static Button stopButton = new Button("Finish rolling");
    public static Button completeRound = new Button("Finish turn");
    //To see if 2 points need to be subtracted at the end of the turn
    public static boolean anythingBuilt = false;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Catan Dice");

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void makeControls() {
        Rectangle background = new Rectangle(1200, 700);
        background.setFill(Color.web("0x66371a"));

        Label boardLabel = new Label("Enter number of players");
        boardLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        boardLabel.setTextFill(Color.WHITE);
        boardLabel.setLayoutX(450);
        boardLabel.setLayoutY(225);

        Text pressEnter = new Text("Press enter to start");
        pressEnter.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        pressEnter.setFill(Color.WHITE);
        pressEnter.setLayoutX(475);
        pressEnter.setLayoutY(450);

        TextField boardTextField = new TextField();
        boardTextField.setPrefWidth(100);
        boardTextField.setPrefHeight(50);
        boardTextField.setLayoutX(550);
        boardTextField.setLayoutY(315);

        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                try{
                    startGame(Integer.parseInt(boardTextField.getText()));
                }catch (NumberFormatException exception){
                    Text t = new Text("Please enter a valid number of players");
                    t.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
                    t.setLayoutX(600-t.getWrappingWidth());
                    t.setLayoutY(200);
                    controls.getChildren().add(t);
                }
            }
        });


        controls.getChildren().addAll(background, boardLabel, boardTextField, pressEnter);
    }

    void startGame(int numPlayers){
        root.getChildren().clear();
        gameState = new GameState(numPlayers);
        for(int i = 0; i<numPlayers; i++)
            gameState.createBoard(i);

        // Adding the background image
        Image board = new Image("file:assets/StylisedBoard.png");
        ImageView view = new ImageView(board);

        // Adding all the dice
        for (int i = 0; i<6; i++)
            Game.diceList.add(new Dice(DiceFace.GOLD, i, false));
        for (var dice : Game.diceList)
            root.getChildren().add(dice.getModel());

        // show Dice Remain times
        Text diceTimeTitle = new Text("Rolls remaining: ");
        diceTimeTitle.setLayoutX(820);
        diceTimeTitle.setLayoutY(45);
        diceTimeTitle.setFill(Color.WHITE);
        diceTimeTitle.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        diceTime.getChildren().add(diceTimeTitle);

        diceTimeText = new Text("3");
        diceTimeText.setLayoutX(1020);
        diceTimeText.setLayoutY(45);
        diceTimeText.setFill(Color.WHITE);
        diceTimeText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        diceTime.getChildren().add(diceTimeText);

        //add a Roll Dice button
        diceRollButton = new Button("Roll");
        diceRollButton.setLayoutX(770);
        diceRollButton.setLayoutY(25);
        diceRollButton.setOnAction(actionEvent -> {
            stopButton.setDisable(false);
            int remain_time = Integer.parseInt(diceTimeText.getText()) - 1;
            diceTimeText.setText(String.valueOf(remain_time));

            // At the first, player should roll all the dices
            if(remain_time == 2){
                Arrays.fill(Game.gameState.resourceState, 0); //Just a temporary fix
                CatanDice.rollDice(6, Game.gameState.resourceState);
            }


            // In roll #2,3, player can choose which Dice to re-roll
            else {
                int diceToRoll = 0;
                for (int i = 0; i < 6; i++) {
                    if (diceList.get(i).select) {
                        // if Dice is selected, renew the property of resource randomly
                        gameState.resourceState[Game.diceList.get(i).resource.diceNum]--;
                        diceToRoll++;
                    }
                }
                CatanDice.rollDice(diceToRoll, gameState.resourceState);
            }
            for (var dice : diceList)
                dice.select = false;
            CatanDice.updateDice();

            // if it comes to the final chance to re-roll, players cannot re-roll again next time
            if (remain_time == 0) {
                diceRollButton.setDisable(true);
                tradeButton.setDisable(Game.gameState.resourceState[5] < 2);
                diceRollButton.setDisable(true);
                completeRound.setDisable(false);
                stopButton.setDisable(true);
                for (var piece : gameState.boards.get(gameState.currentPlayer).getChildren())
                    piece.setDisable(false);
            }
        });

        // complete the statement of rolling dices
        stopButton.setLayoutX(1050);
        stopButton.setLayoutY(25);
        stopButton.setDisable(diceTimeText.getText().equals("3"));
        stopButton.setOnAction(actionEvent -> {
            tradeButton.setDisable(Game.gameState.resourceState[5] < 2);
            diceRollButton.setDisable(true);
            completeRound.setDisable(false);
            stopButton.setDisable(true);
            for (var piece : gameState.boards.get(gameState.currentPlayer).getChildren())
                piece.setDisable(false);
        });


        // Layout the trade button
        tradeButton.setLayoutX(1050);
        tradeButton.setLayoutY(175);
        tradeButton.setDisable(true);
        tradeButton.setOnAction(actionEvent -> {
            tradeButton.setDisable(true);
            completeRound.setDisable(true);
            //Add the menu background
            Rectangle rectangle = new Rectangle(500, 200);
            rectangle.setLayoutX(350);
            rectangle.setLayoutY(250);
            rectangle.setFill(Color.web("0x66371a"));
            rectangle.setOpacity(0.95);
            root.getChildren().add(rectangle);

            //Add the instructional text
            Text text = new Text("Select what to swap for:");
            text.setLayoutX(400);
            text.setLayoutY(300);
            text.setFont(Font.font(null, FontWeight.BOLD, 35));
            text.toFront();
            root.getChildren().add(text);

            //Generate buttons to select which resource
            ImageView[] selections = new ImageView[5];
            String actionStr = "trade ";
            selections[0] = new ImageView("file:assets/OreFace.png");
            selections[1] = new ImageView("file:assets/GrainFace.png");
            selections[2] = new ImageView("file:assets/WoolFace.png");
            selections[3] = new ImageView("file:assets/LumberFace.png");
            selections[4] = new ImageView("file:assets/BrickFace.png");
            for(int i = 0; i<selections.length; i++) {
                selections[i].setScaleX(0.75);
                selections[i].setScaleY(0.75);
                selections[i].setLayoutX(i*90 + 370);
                selections[i].setLayoutY(320);

                int finalI = i;
                selections[i].setOnMouseClicked(actionEvent1 -> {
                    Action.applyAction(gameState.boardStates.get(gameState.currentPlayer),
                            gameState.resourceState, Action.decode(actionStr+ finalI));
                    CatanDice.updateDice();
                    for (var v : selections)
                        root.getChildren().remove(v);
                    root.getChildren().remove(rectangle);
                    root.getChildren().remove(text);
                    tradeButton.setDisable(gameState.resourceState[5] < 2);
                    completeRound.setDisable(false);
                });

                root.getChildren().add(selections[i]);
            }
        });

        // add Score text
        scoreText = new Text(gameState.scores.get(gameState.currentPlayer)[gameState.passedTurns] + "    " +
                Arrays.stream(gameState.scores.get(gameState.currentPlayer)).sum());
        scoreText.setLayoutX(800);
        scoreText.setLayoutY(600);
        scoreText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 100));

        // add Round Text
        Text roundText = new Text("Round: " + (gameState.passedTurns+1));
        roundText.setLayoutX(275);
        roundText.setLayoutY(345);
        roundText.setFill(Color.WHITE);
        roundText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));

        // add the current player text
        Text currentPlayerText = new Text("Current player: " + (gameState.currentPlayer+1));
        currentPlayerText.setLayoutX(30);
        currentPlayerText.setLayoutY(25);
        currentPlayerText.setFill(Color.BLACK);
        currentPlayerText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

        //add a Complete Round button
        completeRound.setLayoutX(300);
        completeRound.setLayoutY(360);
        completeRound.setDisable(true);
        completeRound.setOnAction(actionEvent -> {
            if (!anythingBuilt)
                gameState.scores.get(gameState.currentPlayer)[gameState.passedTurns] -= 2;
            anythingBuilt = false;

            stopButton.setDisable(false);
            tradeButton.setDisable(true);
            completeRound.setDisable(true);

            for (var piece : gameState.boards.get(gameState.currentPlayer).getChildren())
                piece.setDisable(true);

            if (gameState.passedTurns < 14) progressTurn(roundText, currentPlayerText);
            else if (gameState.currentPlayer != (gameState.numberOfPlayers - 1))
                progressTurn(roundText, currentPlayerText);

            else {
                root.getChildren().clear();
                makeEndScreen();
            }
        });

        root.getChildren().addAll(gameState.boards.get(0),diceRollButton,diceTime,stopButton, tradeButton,completeRound,roundText,
                scoreText,currentPlayerText,view,new Road(-1,true).getModel());
        view.toBack();
    }

    /**
     * Displays the scores in a table after the final round is complete
     */
    public void makeEndScreen(){
        String[][] scores = new String[gameState.numberOfPlayers][16];
        for (int i = 0; i < gameState.numberOfPlayers; i++){
            scores[i][0] = "Player " + (i+1);
            for (int j = 1; j < 15; j ++)
                scores[i][j] = String.valueOf(gameState.scores.get(i)[j]);
            scores[i][15] = String.valueOf(Arrays.stream(gameState.scores.get(i)).sum());
        }


        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(scores));
        TableView<String[]> table = new TableView<>();

        for (int i = 0; i < scores[0].length; i++) {
            TableColumn<String[], String> tc = new TableColumn<>();
            if (i == 0) tc.setText("Player");
            else if(i<15) tc.setText("Round " + i);
            else tc.setText("Total");

            final int colNo = i;
            tc.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[colNo])));
            tc.setPrefWidth((double) 1200 / (double) 17);
            table.getColumns().add(tc);
        }
        table.setItems(data);
        root.getChildren().add(table);
    }

    public void progressTurn(Text roundText, Text currentPlayerText) {
        root.getChildren().remove(gameState.boards.get(gameState.currentPlayer));
        gameState.nextTurn();
        //Change to the next board
        root.getChildren().add(gameState.boards.get(gameState.currentPlayer));
        gameState.boards.get(gameState.currentPlayer).toFront();

        //Move to the next player
        scoreText.setText(gameState.scores.get(gameState.currentPlayer)[gameState.passedTurns] + "    " +
                Arrays.stream(gameState.scores.get(gameState.currentPlayer)).sum());
        roundText.setText("Round: " + (gameState.passedTurns + 1));
        currentPlayerText.setText("Current player: " + (gameState.currentPlayer + 1));

        //Reset the dice
        Arrays.fill(gameState.resourceState, 0);
        for (var dice : diceList)
            dice.select = false;
        CatanDice.updateDice();
        diceRollButton.setDisable(false);
        diceTimeText.setText("3");
    }
}