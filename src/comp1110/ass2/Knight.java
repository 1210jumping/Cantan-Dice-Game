package comp1110.ass2;

//This class was written by Alex

import comp1110.ass2.gui.Game;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Arrays;

import static javafx.scene.paint.Color.*;

/**
 * extends abstract class "Piece"
 *Knight can change the dice to what it represents.
 */
public class Knight extends Piece{
    public DiceFace resource;
    // Highest index road needed to build
    public int subRoad;
    public boolean built;
    public boolean used;
    public int points;
    public String type;
    public Group model;
    private final Circle circle;

    public Knight(int p, boolean b, boolean u){
        this.points = p;
        this.model = new Group();
        this.built = b;
        this.used = u;
        circle = new Circle();

        double x = 0;
        double y = 0;

        switch(this.points){
            case (1) -> {
                x = 175;
                y = 190;
                this.resource = DiceFace.ORE;
                this.subRoad = -1;
            }
            case (2) -> {
                x = 170;
                y = 390;
                this.resource = DiceFace.GRAIN;
                this.subRoad = -1;
            }
            case (3) -> {
                x = 345;
                y = 495;
                this.resource = DiceFace.WOOL;
                this.subRoad = -1;
            }
            case (4) -> {
                x = 525;
                y = 390;
                this.resource = DiceFace.LUMBER;
                this.subRoad = -1;
            }
            case (5) -> {
                x = 530;
                y = 180;
                this.resource = DiceFace.BRICK;
                this.subRoad = -1;
            }
            case (6) -> {
                x = 350;
                y = 75;
                this.resource = DiceFace.GOLD;
                this.subRoad = -1;
            }
        }
        this.model.setLayoutX(x);
        this.model.setLayoutY(y);

        Text t = new Text(String.valueOf(this.points));
        t.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        circle.setRadius(15.0);

        if(this.used){
            circle.setFill(web("0x9e1803"));
            this.type = "K";
        }
        else if(this.built){
            circle.setFill(web("0x278004"));
            this.type = "J";
        }
        else{
            circle.setFill(LIGHTGREY);
            this.type = "J";
        }
        model.setOnMouseClicked(actionEvent -> {
            Action action = new Action(Action.ActionType.BUILD, this.toString(),null, null);
            //String action = "build " + Game.gameState.boardStates.get(Game.gameState.currentPlayer).get(getIndex() + 26);
            if(!getBuilt()) {
                if (CatanDice.canDoAction(action.toString(),
                        CatanDice.boardStateToString(Game.gameState.boardStates.get(Game.gameState.currentPlayer)),
                        Game.gameState.resourceState) && Game.diceRollButton.isDisable()) {
                    build();
                    //Update the score
                    Game.gameState.scores.get(Game.gameState.currentPlayer)[Game.gameState.passedTurns] += this.getPoints();
                    Game.scoreText.setText(
                            Game.gameState.scores.get(Game.gameState.currentPlayer)[Game.gameState.passedTurns] + "    " +
                                    Arrays.stream(Game.gameState.scores.get(Game.gameState.currentPlayer)).sum());
                    //Update resources
                    removeResources(Game.gameState.resourceState);
                    CatanDice.updateDice();
                }
            }
            //This will be called when the knight is built and player wants to use it
            else if (!used){
                // If they are using the 6th knight, they can choose what to trade for
                if (this.getPoints()==6){
                    //Ask the player which dice they want to swap out
                    Text alert = new Text("Please select which dice to swap out");
                    alert.setLayoutY(350);
                    alert.setFont(Font.font(null, FontWeight.BOLD, 70));
                    alert.toFront();
                    Game.root.getChildren().add(alert);

                    //Generate buttons to select which dice
                    Button[] selections = new Button[Arrays.stream(Game.gameState.resourceState).sum()];
                    for(int i = 0; i<selections.length; i++){
                        selections[i] = new Button();
                        selections[i].setLayoutX(i*80+700);
                        selections[i].setLayoutY(140);
                        selections[i].setScaleX(3);

                        selections[i].setOnAction(actionEvent1 -> {
                            //Add the menu background
                            Rectangle rectangle = new Rectangle(500, 200);
                            rectangle.setLayoutX(350);
                            rectangle.setLayoutY(250);
                            rectangle.setFill(Color.web("0x66371a"));
                            rectangle.setOpacity(0.95);
                            Game.root.getChildren().add(rectangle);

                            //Add the instructional text
                            Text text = new Text("Select what to swap for:");
                            text.setLayoutX(400);
                            text.setLayoutY(300);
                            text.setFont(Font.font(null, FontWeight.BOLD, 35));
                            text.toFront();
                            Game.root.getChildren().add(text);

                            //Generate buttons to select which resource
                            ImageView[] selectionsOut = new ImageView[5];
                            String actionStr = "trade ";
                            selectionsOut[0] = new ImageView("file:assets/OreFace.png");
                            selectionsOut[1] = new ImageView("file:assets/GrainFace.png");
                            selectionsOut[2] = new ImageView("file:assets/WoolFace.png");
                            selectionsOut[3] = new ImageView("file:assets/LumberFace.png");
                            selectionsOut[4] = new ImageView("file:assets/BrickFace.png");
                            for(int j = 0; j<selectionsOut.length; j++) {
                                selectionsOut[j].setScaleX(0.75);
                                selectionsOut[j].setScaleY(0.75);
                                selectionsOut[j].setLayoutX(j*90 + 370);
                                selectionsOut[j].setLayoutY(320);

                                int finalJ = j;
                                selectionsOut[j].setOnMouseClicked(actionEvent2 -> {
                                    Action.applyAction(Game.gameState.boardStates.get(Game.gameState.currentPlayer),
                                            Game.gameState.resourceState, Action.decode(actionStr+ finalJ));
                                    CatanDice.updateDice();
                                    for (var v : selectionsOut)
                                        Game.root.getChildren().remove(v);
                                    Game.root.getChildren().remove(rectangle);
                                    use();
                                });

                                Game.root.getChildren().add(selectionsOut[j]);
                            }
                            for (var v : selections)
                                Game.root.getChildren().remove(v);
                            Game.root.getChildren().remove(alert);
                        });
                        Game.root.getChildren().add(selections[i]);
                    }
                }

                // For all the normal knights
                else{
                    //Ask the player which dice they want to swap out
                    Text alert = new Text("Please select which dice to swap");
                    alert.setLayoutY(350);
                    alert.setFont(Font.font(null, FontWeight.BOLD, 70));
                    alert.toFront();
                    Game.root.getChildren().add(alert);

                    //Generate buttons to select which dice
                    Button[] selections = new Button[Arrays.stream(Game.gameState.resourceState).sum()];
                    for(int i = 0; i<selections.length; i++){
                        selections[i] = new Button();
                        selections[i].setLayoutX(i*80+700);
                        selections[i].setLayoutY(140);
                        selections[i].setScaleX(3);

                        int finalI = i;
                        selections[i].setOnAction(actionEvent1 -> {
                            String action1 = ("swap " +
                                    CatanDice.resStateToDiceFaces(Game.gameState.resourceState).get(finalI).diceNum
                                    + " " + resource.diceNum);

                            if(CatanDice.canDoAction(action1,
                                    CatanDice.boardStateToString(Game.gameState.boardStates.get(Game.gameState.currentPlayer)),
                                    Game.gameState.resourceState)){
                                //Update the resource state
                                Action.applyAction(Game.gameState.boardStates.get(Game.gameState.currentPlayer),
                                        Game.gameState.resourceState, Action.decode(action1));
                                CatanDice.updateDice();
                            }

                            for (var v : selections)
                                Game.root.getChildren().remove(v);
                            Game.root.getChildren().remove(alert);
                            use();
                        });

                        Game.root.getChildren().add(selections[i]);
                    }
                }
            }
        });

        circle.setStroke(BLACK);
        circle.setStrokeWidth(1);

        t.setLayoutX(-5);
        t.setLayoutY(5);

        this.model.getChildren().addAll(circle, t);
    }

    public void build(){
        this.built = true;
        circle.setFill(Color.web("0x278004"));
        try {
            Game.anythingBuilt = true;
        } catch (ExceptionInInitializerError | NoClassDefFoundError ignore) {
        }
    }

    /**
     * removes the correct resources from the given resource state
     * @param resourceState the resource state to be removed from
     */
    public void removeResources(int[] resourceState) {
        resourceState[0]--;
        resourceState[1]--;
        resourceState[2]--;
    }

    public void use(){
        this.used = true;
        this.type = "K";
        circle.setFill(Color.web("0x9e1803"));
    }
    public boolean getUsed() {
        return used;
    }
    public String encode(){
        if(this.used){return "K" + this.points;}
        else{return "J" + this.points;}
    }
    public int getIndex(){return points - 1;}
    public String getType(){return type;}
    public Group getModel(){return model;}
    public int getPoints(){return points;}
    public boolean getBuilt(){return built;}
    public int getSubRoad() {return subRoad;}

    @Override
    public String toString() {
        return "" + type + points;
    }
}