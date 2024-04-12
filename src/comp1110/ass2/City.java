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
public class City extends Piece{
    public int points;
    public int subRoad;
    public boolean built;
    public Group model;
    public String type;


    public City(int pos, boolean b){
        this.type = "C";
        this.model = new Group();
        this.points = pos;
        this.built = b;

        double x = 0;
        double y = 0;

        switch (this.points) {
            case (7) -> {
                x = 75;
                y = 350;
                this.subRoad = 1;
            }
            case (12) -> {
                x = 75;
                y = 545;
                this.subRoad = 4;
            }
            case (20) -> {
                x = 555;
                y = 555;
                this.subRoad = 13;
            }
            case (30) -> {
                x = 555;
                y = 350;
                this.subRoad = 15;
            }

        }

        this.model.setLayoutX(x);
        this.model.setLayoutY(y);

        Text t = new Text(String.valueOf(this.points));
        t.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        Polygon rectangle = new Polygon();
        rectangle.getPoints().addAll(
                0.0, 0.0,
                0.0,  -20.0,
                20.0, -20.0,
                30.0, -35.0,
                40.0, -20.0,
                40.0, -0.0
        );

        model.setOnMouseClicked(actionEvent -> {
            String action = "build "+ Game.gameState.boardStates.get(Game.gameState.currentPlayer).get(getIndex()+22);
            if(CatanDice.canDoAction(action,
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

        t.setLayoutX(10);
        t.setLayoutY(-8);

        this.model.getChildren().addAll(rectangle, t);
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
                0.0, -20.0,
                20.0, -20.0,
                30.0, -35.0,
                40.0, -20.0,
                40.0, -0.0
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
        resourceState[0]-=3;
        resourceState[1]-=2;
    }

    public String encode(){
        return "C" + this.points;
    }
    public void use(){}
    public boolean getUsed() {
        return false;
    }

    public static HashMap<Integer, Integer> cities(){
        HashMap<Integer, Integer> cities = new HashMap<>();
        cities.put(0,7);
        cities.put(1,12);
        cities.put(2,20);
        cities.put(3,30);
        return cities;
    }
    public int getIndex(){
        return switch (this.points) {
            case (7) -> 0;
            case (12) -> 1;
            case (20) -> 2;
            case (30) -> 3;
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
