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

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.LIGHTGREY;

public class Road extends Piece{
    public String type;
    //public int points;
    public boolean built;
    public Group model;
    public int index;
    public int subRoad;

    public Road(int ind, boolean built){
        this.type = "R";
        this.model = new Group();
        this.index = ind;
        this.built = built;

        double x = 0;
        double y = 0;
        double r = 0;

        switch (this.index) {
            case (-1) -> {
                x = 250;
                y = 170;
                r = 240;
                this.subRoad = -1;
            }
            case (0) -> {
                x = 250;
                y = 270;
                r = -60;
                this.subRoad = -1;
            }
            case (1) -> {
                x = 160;
                y = 340;
                r = 180;
                this.subRoad = 0;
            }
            case (2) -> {
                x = 250;
                y = 385;
                r = 240;
                this.subRoad = 0;
            }
            case (3) -> {
                x = 250;
                y = 500;
                r = -60;
                this.subRoad = 2;
            }
            case (4) -> {
                x = 160;
                y = 545;
                r = 180;
                this.subRoad = 3;
            }
            case (5) -> {
                x = 250;
                y = 590;
                r = 240;
                this.subRoad = 3;
            }
            case (6) -> {
                x = 350;
                y = 650;
                r = 180;
                this.subRoad = 5;
            }
            case (7) -> {
                x = 435;
                y = 595;
                r = -60;
                this.subRoad = 6;
            }
            case (8) -> {
                x = 440;
                y = 500;
                r = 240;
                this.subRoad = 7;
            }
            case (9) -> {
                x = 440;
                y = 385;
                r = -60;
                this.subRoad = 8;
            }
            case (10) -> {
                x = 440;
                y = 270;
                r = 240;
                this.subRoad = 9;
            }
            case (11) -> {
                x = 440;
                y = 180;
                r = -60;
                this.subRoad = 10;
            }
            case (12) -> {
                x = 525;
                y = 545;
                r = 180;
                this.subRoad = 7;
            }
            case (13) -> {
                x = 610;
                y = 500;
                r = -60;
                this.subRoad = 12;
            }
            case (14) -> {
                x = 610;
                y = 385;
                r = 240;
                this.subRoad = 13;
            }
            case (15) -> {
                x = 610;
                y = 270;
                r = -60;
                this.subRoad = 14;
            }

        }

        this.model.setLayoutX(x);
        this.model.setLayoutY(y);
        this.model.setRotate(r);

        Text t = new Text("1");
        t.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        Polygon rectangle = new Polygon();
        rectangle.getPoints().addAll(
                30.0, 7.0,
                -30.0, 7.0,
                -30.0, -7.0,
                30.0, -7.0
        );

        if(this.built){rectangle.setFill(Color.web("0x4a4a4a"));}
        else{rectangle.setFill(LIGHTGREY);}

        model.setOnMouseClicked(actionEvent -> {
            String action = "build " + Game.gameState.boardStates.get(Game.gameState.currentPlayer).get(getIndex());
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

        rectangle.setStroke(BLACK);
        rectangle.setStrokeWidth(1);

        t.setLayoutX(-2);
        t.setLayoutY(6);
        t.setRotate(90);

        if(this.index == -1){
            rectangle.setFill(Color.web("0x4a4a4a"));
            t.setText("Start");
            t.setRotate(180);
            t.setLayoutX(-17);
            t.setLayoutY(6);
        }

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
                30.0, 7.0,
                -30.0, 7.0,
                -30.0, -7.0,
                30.0, -7.0
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
    public void removeResources(int[] resourceState){
        resourceState[3]--;
        resourceState[4]--;
    }
    public boolean getUsed() {
        return false;
    }
    public String encode(){return "R" + this.index;}
    public void use(){}
    public int getIndex(){return this.index;}
    public String getType(){return this.type;}
    public Group getModel(){return this.model;}
    public int getPoints(){return 1;}
    public boolean getBuilt(){return this.built;}
    public int getSubRoad() {return subRoad;}

    @Override
    public String toString() {
        return "" + type + index;
    }
}
