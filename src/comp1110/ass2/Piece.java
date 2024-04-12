package comp1110.ass2;

//This class was written by Alex

import javafx.scene.Group;

/**
 * These are the common methods of the following classes
 * City
 * Road
 * Settlement
 * Knight
 */
public abstract class Piece {

    abstract public int getPoints();
    abstract public Group getModel();
    abstract public String getType();
    abstract public int getIndex();
    abstract public boolean getBuilt();
    abstract public boolean getUsed();
    abstract public int getSubRoad();
    abstract public void build();
    /**
     * removes the correct resources from the given resource state
     * @param resourceState the resource state to be removed from
     */
    abstract public void removeResources(int[] resourceState);
    abstract public String encode();
    public static Piece decode(String arg){
        switch (arg.charAt(0)){
            case ('R') -> {
                return new Road(CatanDice.stringToPos(arg), true);
            }
            case ('S') -> {
                return new Settlement(CatanDice.stringToPos(arg), true);
            }
            case ('C') -> {
                return new City(CatanDice.stringToPos(arg), true);
            }
            case ('K') -> {
                return new Knight(Character.getNumericValue(arg.charAt(1)), true, true);
            }
            case ('J') -> {
                return new Knight(Character.getNumericValue(arg.charAt(1)), true, false);
            }
            default -> {
                return null;
            }
        }
    }
    abstract public void use();
}
