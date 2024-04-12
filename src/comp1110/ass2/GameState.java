package comp1110.ass2;

//This class was written by Alex, Shu and Pin-Shen

import comp1110.ass2.gui.Game;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A complete game state representation
 * Holds all information needed about a game
 */

public class GameState {
    public int numberOfPlayers;
    public List<List<Piece>> boardStates;
    public int currentPlayer;
    public int[] resourceState;
    public int passedTurns;
    public List<int[]> scores;
    public List<Group> boards;

    public GameState(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        scores = new ArrayList<>();
        boardStates = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++){
            boardStates.add(CatanDice.generatePieces(""));
            scores.add(new int[15]);
        }
        passedTurns = 0;
        currentPlayer = 0;
        resourceState = new int[6];
        boards = new ArrayList<>();
    }

    /**
     * Cycles to the next players turn and updates how many turns have passed
     */
    public void nextTurn(){
        if (currentPlayer == numberOfPlayers - 1) {
            currentPlayer = 0;
            passedTurns++;
        } else currentPlayer++;
    }

    public void createBoard(int player){
        Group currentBoard = new Group();

        // Adding all the pieces to the board
        for (var piece : Game.gameState.boardStates.get(player)) {
            currentBoard.getChildren().add(piece.getModel());
        }

        boards.add(currentBoard);
    }
}
