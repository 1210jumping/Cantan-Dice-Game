package comp1110.ass2;

//This class was written by Alex

import comp1110.ass2.gui.Game;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.HashMap;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.LIGHTGREY;

/**
 * extends abstract class "Piece"
 */
public class Settlement extends Piece {
    public int points;
    public int subRoad;
    public boolean built;
    public Group model;
    public String type;


    public Settlement(int pos, boolean b){
        this.type = "S";
        this.points = pos;
        this.built = b;
        this.model = new Group();

        double x = 0;
        double y = 0;

        switch (this.points){
            case (3) -> {
                x = 280;
                y = 210;
                this.subRoad = -1;
            }
            case (4) -> {
                x = 280;
                y = 430;
                this.subRoad = 2;
            }
            case (5) -> {
                x = 280;
                y = 620;
                this.subRoad = 5;
            }
            case (7) -> {
                x = 470;
                y = 520;
                this.subRoad = 7;
            }
            case (9) -> {
                x = 470;
                y = 330;
                this.subRoad = 9;
            }
            case (11) -> {
                x = 470;
                y = 120;
                this.subRoad = 11;
            }
        }

        this.model.setLayoutX(x);
        this.model.setLayoutY(y);

        Text t = new Text(String.valueOf(this.points));
        t.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        Polygon rectangle = new Polygon();
        rectangle.getPoints().addAll(
                0.0,0.0,
                14.0,14.0,
                10.0,14.0,
                10.0,34.0,
                -10.0,34.0,
                -10.0,14.0,
                -14.0,14.0
        );

        model.setOnMouseClicked(actionEvent -> {
            String action = "build " + Game.gameState.boardStates.get(Game.gameState.currentPlayer).get(getIndex() + 16);
            if (CatanDice.canDoAction(action,
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
        });

        if(this.built){rectangle.setFill(Color.web("0x4a4a4a"));}
        else{rectangle.setFill(LIGHTGREY);}

        rectangle.setStroke(BLACK);
        rectangle.setStrokeWidth(1);

        t.setLayoutX(-6);
        t.setLayoutY(25);

        this.model.getChildren().addAll(rectangle,t);
    }
    public void build(){
        this.built = true;
        try {
            Game.anythingBuilt = true;
        } catch (ExceptionInInitializerError | NoClassDefFoundError ignore) {
        }

        //Change the rectangle colour
        Polygon rectangle = new Polygon();
        rectangle.getPoints().addAll(
                0.0, 0.0,
                14.0, 14.0,
                10.0, 14.0,
                10.0, 34.0,
                -10.0, 34.0,
                -10.0, 14.0,
                -14.0,14.0
        );
        rectangle.setFill(Color.web("0x4a4a4a"));
        rectangle.setStroke(BLACK);
        rectangle.setStrokeWidth(1);

        this.model.getChildren().clear();
        this.model.getChildren().add(rectangle);
    }
    /**
     * removes the correct resources from the given resource state
     * @param resourceState the resource state to be removed from
     */
    public void removeResources(int[] resourceState) {
        resourceState[1]--;
        resourceState[2]--;
        resourceState[3]--;
        resourceState[4]--;
    }

    public String encode(){
        return "S" + this.points;
    }
    public void use(){}

    public boolean getUsed() {
        return false;
    }

    public static HashMap<Integer, Integer> settlements(){
        HashMap<Integer, Integer> settlements = new HashMap<>();
        settlements.put(0, 3);
        settlements.put(1, 4);
        settlements.put(2, 5);
        settlements.put(3, 7);
        settlements.put(4, 9);
        settlements.put(5, 11);
        return settlements;
    }
    public int getIndex(){
        return switch (this.points) {
            case (3) -> 0;
            case (4) -> 1;
            case (5) -> 2;
            case (7) -> 3;
            case (9) -> 4;
            case (11) -> 5;
            default -> -1;
        };
    }
    public String getType(){return this.type;}
    public Group getModel(){return this.model;}
    public int getPoints(){return this.points;}
    public boolean getBuilt(){return  this.built;}
    public int getSubRoad() {return subRoad;}

    @Override
    public String toString() {
        return "" + type + points;
    }
}
