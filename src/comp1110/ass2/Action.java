package comp1110.ass2;

import java.util.List;
import java.util.Objects;

import static comp1110.ass2.Action.ActionType.BUILD;
import static comp1110.ass2.Action.ActionType.TRADE;

//This class was written by Alex

/**
 * One of the tree action types: build, trade, swap. Can be used to apply actions to a gamestate.
 */
public class Action{
    public enum ActionType{
        BUILD,
        TRADE,
        SWAP
    }

    public ActionType type;

    //Null if trading gold
    public String target;
    public DiceFace given;
    public DiceFace received;

    public Action(ActionType type, String target, DiceFace given, DiceFace received) {
        this.type = type;
        this.target = target;
        this.given = given;
        this.received = received;
    }

    /**
     * Decodes and action string into an instance of the action class
     *
     * @param action an action string
     * @return the appropriate new instance of the action class
     */
    public static Action decode(String action){
        if(CatanDice.isActionWellFormed(action)) {
            String[] array = action.split(" ");
            switch (array[0]) {
                case ("build") -> {
                    return new Action(BUILD, array[1], null, null);
                }
                case ("trade") -> {
                    return new Action(Action.ActionType.TRADE, null,
                            null, DiceFace.fromInt(Integer.parseInt(array[1])));
                }
                case ("swap") -> {
                    return new Action(Action.ActionType.SWAP, "J" + (Integer.parseInt(array[2])+1),
                            DiceFace.fromInt(Integer.parseInt(array[1])), DiceFace.fromInt(Integer.parseInt(array[2])));
                }
            }
        }
        System.out.println("Action decoder: not a valid action string");
        return null;
    }

    /**
     * Given a board state, resource state and an action it updates the parameters according to that action
     *
     * @param boardState    the current state of the board
     * @param resourceState the current resource state
     * @param action        the action to b applied
     */
    public static void applyAction(List<Piece> boardState, int[] resourceState, Action action){
        try{
            switch (action.type){
                case BUILD -> {
                    Objects.requireNonNull(CatanDice.getPiece(boardState, action.target)).build();
                    Objects.requireNonNull(CatanDice.getPiece(boardState, action.target)).removeResources(resourceState);
                }
                case TRADE -> {
                    resourceState[5] -= 2;
                    resourceState[action.received.diceNum] += 1;
                } case SWAP -> {
                    Objects.requireNonNull(CatanDice.getPiece(boardState, action.target)).use();
                    resourceState[action.given.diceNum] -= 1;
                    resourceState[action.received.diceNum] += 1;
                }
            }
        } catch (NullPointerException ignored) {}

    }
    @Override
    public String toString(){
        if(type.equals(BUILD))
            return "build " + target;
        else if(type.equals(TRADE))
            return "trade " + received;
        else
            return"swap " + given + " " + received;

    }
}
