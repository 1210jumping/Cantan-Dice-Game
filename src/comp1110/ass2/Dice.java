package comp1110.ass2;

//This class was written by Alex and Pin-Shen

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.BLACK;

/**
 * One of the dice to display on the board
 * Instantiated 6 times
 */

public class Dice {
    public DiceFace resource;
    public Group model;
    public boolean select;
    public int place;
    public boolean used;

    public Dice(DiceFace resource, int place, boolean select) {
        this.used = false;
        this.select = select;
        this.resource = resource;
        this.model = new Group();
        this.place = place;

        Image face;

        switch (this.resource) {
            case ORE -> face = new Image("file:assets/OreFace.png");
            case WOOL -> face = new Image("file:assets/WoolFace.png");
            case GRAIN -> face = new Image("file:assets/GrainFace.png");
            case LUMBER -> face = new Image("file:assets/LumberFace.png");
            case BRICK -> face = new Image("file:assets/BrickFace.png");
            default -> face = new Image("file:assets/GoldFace.png");
        }
        ImageView view = new ImageView(face);

        view.setFitHeight(face.getHeight() / 1.3);
        view.setFitWidth(face.getWidth() / 1.3);
        view.setLayoutX(place * 80 + 670);
        view.setLayoutY(70);
        model.getChildren().add(view);

        // Select/Cancel Select Dice(s)
        Image xImage = new Image("file:assets/X.png");
        ImageView view1 = new ImageView(xImage);
        model.setOnMouseClicked(actionEvent -> {
            if (!this.select) {
                view1.setFitHeight(xImage.getHeight() / 1.6);
                view1.setFitWidth(xImage.getWidth() / 1.6);
                view1.setLayoutX(this.place * 80 + 685);
                view1.setLayoutY(75);
                model.getChildren().add(view1);
                this.select = true;
            } else {
                model.getChildren().remove(view1);
                this.select = false;
            }
        });
    }

    public Group getModel(){return model;}

    public void setResource(DiceFace resource){

        model.getChildren().clear();
        if (resource==null)
            return;

        this.resource = resource;
        Image face;
        switch (resource){
            case ORE -> face = new Image("file:assets/OreFace.png");
            case WOOL -> face = new Image("file:assets/WoolFace.png");
            case GRAIN -> face = new Image("file:assets/GrainFace.png");
            case LUMBER -> face = new Image("file:assets/LumberFace.png");
            case BRICK -> face = new Image("file:assets/BrickFace.png");
            default -> face = new Image("file:assets/GoldFace.png");
        }
        ImageView view = new ImageView(face);
        view.setFitHeight(face.getHeight()/1.3);
        view.setFitWidth(face.getWidth()/1.3);
        view.setLayoutX(place*80+670);
        view.setLayoutY(70);

        // Select/Cancel Select Dice(s)
        Image xImage = new Image("file:assets/X.png");
        ImageView view1 = new ImageView(xImage);
        model.setOnMouseClicked(actionEvent -> {
            if (!this.select) {
                view1.setFitHeight(xImage.getHeight() / 1.6);
                view1.setFitWidth(xImage.getWidth() / 1.6);
                view1.setLayoutX(this.place * 80 + 685);
                view1.setLayoutY(75);
                model.getChildren().add(view1);
                this.select = true;
            } else {
                model.getChildren().remove(view1);
                this.select = false;
            }
        });

        model.getChildren().add(view);
    }

    public void use(){
        Rectangle active = new Rectangle(100/1.3,90/1.3);
        active.setFill(BLACK);
        active.setOpacity(0.5);
        active.setLayoutX(place*80+670);
        active.setLayoutY(70);

        if (this.used) {
            used = false;
            model.getChildren().remove(model.getChildren().size() - 1);
        } else {
            used = true;
            model.getChildren().add(active);
        }
    }

    @Override
    public String toString() {
        return "Dice at " + place + " with resource " + resource + " " + select;
    }
}
