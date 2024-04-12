package comp1110.ass2;

//This class was written by Alex and Pin-Shen

import comp1110.ass2.gui.Game;

import java.util.*;

import static java.lang.Character.*;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.of;

/**
 * Class for task methods and other helpful methods with no specific class.
 */

public class CatanDice {

    /**
     * Check if the string encoding of a board state is well-formed.
     * Note that this does not mean checking if the state is valid
     * (represents a state that the player could get to in game play),
     * only that the string representation is syntactically well-formed.
     *
     * @param board_state: The string representation of the board state.
     * @return true iff the string is a well-formed representation of
     * a board state, false otherwise.
     */
    public static boolean isBoardStateWellFormed(String board_state) {
        String[] b = board_state.split(",");
        if(board_state.equals("")){
            return true;
        }
        {
            int i = 0;
            while (i < board_state.length()) {
                if (isLetterOrDigit(board_state.charAt(i)) || (board_state.charAt(i) == ',')) {
                    i++;
                    continue;
                }
                return false;
            }
        }
        for (String s : b) {
            if (!(s.charAt(0) == 'R' || s.charAt(0) == 'S' || s.charAt(0) == 'C'
                    || s.charAt(0) == 'J' || s.charAt(0) == 'K')) {
                return false;
            } else {
                if (s.charAt(0) == 'C') {
                    if (s.length() == 2) {
                        if (!(getNumericValue(s.charAt(1)) == 7)) {
                            return false;
                        }
                    } else {
                        if (s.length() == 3) {
                            if (of(12, 20, 30).noneMatch(i -> parseInt(s.substring(1, 3)) == i)) {
                                return false;
                            }
                        }
                    }
                } else {
                    if (s.charAt(0) == 'S') {
                        if (s.length() == 2) {
                            if (of(3, 4, 5, 7, 9, 11).noneMatch(i -> getNumericValue(s.charAt(1)) == i)) {
                                return false;
                            }
                        }
                    } else if (s.charAt(0) == 'J' || s.charAt(0) == 'K') {
                        if (s.length() != 2) {
                            return false;
                        } else if (of(1, 2, 3, 4, 5, 6).noneMatch(i -> getNumericValue(s.charAt(1)) == i)) {
                            return false;
                        }
                    } else if (s.length() != 2) {
                        if (s.length() == 3) {
                            if (parseInt(s.substring(1, 3)) <= 17) {
                                if (parseInt(s.substring(1, 3)) < 12) {
                                    continue;
                                }
                            }
                            return false;
                        }
                    } else {
                        if (isDigit(s.charAt(1))) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check if the string encoding of a player action is well-formed.
     *
     * @param action: The string representation of the action.
     * @return true iff the string is a well-formed representation of
     *         a board state, false otherwise.
     */
    public static boolean isActionWellFormed(String action) {
        String[] ElementArray = {"R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8"
                , "R9", "R10", "R11", "R12", "R13", "R14", "R15"
                , "S3", "S4", "S5", "S7", "S9", "S11"
                , "C7", "C12", "C20", "C30"
                , "J1", "J2", "J3", "J4", "J5", "J6"};
        try {
            if (action.contains("build")) {
                for (int i = 0; i <= 31; i++) {
                    if (action.equals("build " + ElementArray[i])) {
                        return true;
                    }
                }
            } else if (action.contains("swap")) {
                String Num1 = action.split(" ")[1];
                String Num2 = action.split(" ")[2];
                if (!Num1.equals(Num2) &&
                        parseInt(Num1) <= 5 &&
                        parseInt(Num1) >= 0 &&
                        parseInt(Num2) <= 5 &&
                        parseInt(Num2) >= 0) {

                    if (action.equals("swap " + Num1 + " " + Num2)) {
                        return true;
                    }
                }
            } else if (action.contains("trade")) {
                String Num1 = action.split(" ")[1];
                if (parseInt(Num1) <= 5 && parseInt(Num1) >= 0) {
                    if (action.equals("trade " + Num1)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Roll the specified number of dice and add the result to the
     * resource state.
     *
     * The resource state on input is not necessarily empty. This
     * method should only _add_ the outcome of the dice rolled to
     * the resource state, not remove or clear the resources already
     * represented in it.
     *
     * @param n_dice: The number of dice to roll (>= 0).
     * @param resource_state The available resources that the dice
     *        roll will be added to.
     *
     * This method does not return any value. It should update the given
     * resource_state.
     */
    public static void rollDice(int n_dice, int[] resource_state) {
        if(n_dice<0||n_dice>6||(n_dice+Arrays.stream(resource_state).sum())>6) {
            System.out.println("CatanDice.rollDice(): cant roll that many dice!");
            return;
        }
        Random rand = new Random();
        while (n_dice>0) {
            n_dice--;
            resource_state[rand.nextInt(6)]++;
        }
        //Only need this when running the game not tests
        try{
            updateDice();
        }catch(ExceptionInInitializerError | NoClassDefFoundError ignore){}

    }

    /**
     * Updates the dice which are on the screen to represent the current resource state
     */
    public static void updateDice(){
        List<DiceFace> resources = resStateToDiceFaces(Game.gameState.resourceState);
        for (int i = 0; i<Game.diceList.size(); i++){
            if(i<resources.size())
                Game.diceList.get(i).setResource(resources.get(i));
            else
                Game.diceList.get(i).setResource(null);
            Game.diceList.get(i).select = false;
        }
//        Game.tradeButton.setDisable(Game.gameState.resourceState[5] < 2);
    }

    /**
     * Check if the specified structure can be built next, given the
     * current board state. This method should check that the build
     * meets the constraints described in section "Building Constraints"
     * of the README file.
     *
     * @param structure: The string representation of the structure to
     *        be built.
     * @param board_state: The string representation of the board state.
     * @return true iff the structure is a possible next build, false
     *         otherwise.
     */
    public static boolean checkBuildConstraints(String structure, String board_state) {
        Piece str = null;
        ArrayList<Piece> state = new ArrayList<>(generatePieces(board_state));

        for (var v : state){
            if(Objects.equals(v.toString(), structure)) str = v;
        }

        assert str != null;
        boolean reqRoad;
        if(str.getBuilt())
            return false;

        if(str.getSubRoad()==-1)
            reqRoad = true;
        else
            reqRoad = state.get(str.getSubRoad()).getBuilt();

        if(str.getIndex()==0)
            return true;

        switch (str.getType()){
            case("R") -> {
                return reqRoad;
            }
            case("S") -> {
                if(state.get(str.getIndex()+15).getBuilt()){
                    return reqRoad;
                }
            }
            case("C") -> {
                if(state.get(str.getIndex()+21).getBuilt()){
                    return reqRoad;
                }
            }
            case("J"), ("K") -> {
                if(state.get(str.getIndex()+25).getBuilt()){
                    return reqRoad;
                }
            }
        }
        return false;
    }

    /**
     * Converts from a resource state to a list of the dice faces in that state
     * e.g. [1,0,0,2,0,0] -> [ORE, LUMBER, LUMBER]
     * @param resourceState the resource state
     * @return the corresponding
     */
    public static List<DiceFace> resStateToDiceFaces(int[] resourceState){
        List<DiceFace> result = new ArrayList<>();
        for (int i = 0; i < resourceState.length; i++){
            int j = resourceState[i];
            while (j>0){
                j--;
                result.add(DiceFace.fromInt(i));
            }
        }
        return result;
    }

    /**
     * Gets the position of a piece. Handles th cases for 1 or 2 digit positions
     * @param piece the string representation fo the piece
     * @return its position
     */
    public static int stringToPos(String piece){
        if(piece.length() == 2){
            return getNumericValue(piece.charAt(1));
        }
        return getNumericValue(piece.charAt(1)+piece.charAt(2));
    }

    /**
     * Generates a board state as a list of all the pieces on the board
     * @param board_state the string encoding of the board state
     * @return A list with all the corresponding pieces
     */
    public static List<Piece> generatePieces (String board_state){
        List<Piece> pieces = new ArrayList<>();
        List<String> builtPieces = new ArrayList<>(Arrays.asList(board_state.split(",")));

        for(int i=0; i<=15; i++){
            pieces.add(new Road(i, false));
            if(builtPieces.contains(pieces.get(i).encode())){
                pieces.get(i).build();
            }
        }
        for(int i=0; i <Settlement.settlements().size(); i++){
            pieces.add(new Settlement(Settlement.settlements().get(i), false));
            if(builtPieces.contains(pieces.get(i+15).encode())){
                pieces.get(i+15).build();
            }
        }
        for(int i=0; i <City.cities().size(); i++){
            pieces.add(new City(City.cities().get(i), false));
            if(builtPieces.contains(pieces.get(i+21).encode())){
                pieces.get(i+21).build();
            }
        }
        for(int i=1; i<=6; i++){
            pieces.add(new Knight(i,false,false));
            if(builtPieces.contains(pieces.get(i+25).encode())||
                    builtPieces.contains("K"+pieces.get(i+25).encode().charAt(1))){
                pieces.get(i+25).build();
                if(builtPieces.contains("K"+pieces.get(i+25).encode().charAt(1))){
                    pieces.get(i+25).use();
                }
            }
        }
        return pieces;
    }

    /**
     * Check if the available resources are sufficient to build the
     * specified structure, without considering trades or swaps.
     *
     * @param structure: The string representation of the structure to
     *        be built.
     * @param resource_state: The available resources.
     * @return true iff the structure can be built with the available
     *         resources, false otherwise.
     */
    public static boolean checkResources(String structure, int[] resource_state) {
        if (structure.contains("R")) {
            return resource_state[4] >= 1 && resource_state[3] >= 1;
        } else if (structure.contains("J")) {
            return resource_state[0] >= 1 && resource_state[2] >= 1 && resource_state[1] >= 1;
        } else if (structure.contains("S")) {
            return resource_state[4] >= 1 && resource_state[3] >= 1 && resource_state[1] >= 1 && resource_state[2] >= 1;
        } else if (structure.contains("C")) {
            return resource_state[0] >= 3 && resource_state[1] >= 2;
        }
        return false;
    }

    /**
     * Check if the available resources are sufficient to build the
     * specified structure, considering also trades and/or swaps.
     * This method needs access to the current board state because the
     * board state encodes which Knights are available to perform swaps.
     *
     * @param structure: The string representation of the structure to
     *        be built.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the structure can be built with the available
     *         resources, false otherwise.
     */
    public static boolean checkResourcesWithTradeAndSwap(String structure, String board_state, int[] resource_state) {
        List<Piece> boardState = generatePieces(board_state);
        int[] resourceState = resource_state.clone();
        Piece piece = getPiece(boardState, structure);

        //Piece is not in the list of pieces
        if (piece == null) {
            System.out.println("checkResourcesWithTradeAndSwap: not a valid piece");
            return false;
        }

        assert !piece.getBuilt();
        int[] requiredRes = new int[6];
        piece.removeResources(resourceState);
        //resourceState now only has useless resources that
        //can be used for trades and swaps

        //Make requiredResources a list of all the missing resources for the build
        for (int i = 0; i < 6; i++)
            if (resourceState[i] < 0) requiredRes[i] = Math.abs(resourceState[i]);

        for (int i = 0; i < 6; i++) {
            for (int j = requiredRes[i]; j > 0; j--) {
                //Check for knights first
                if (boardState.get(i + 26).getBuilt() && !boardState.get(i + 26).getUsed()) {
                    //Knight can be used
                    requiredRes[i]--;
                    for (int k = 0; k < 6; k++)
                        if (resourceState[k] > 0) resourceState[k]--;
                    boardState.get(i + 26).use();
                } else if (resourceState[5] > 1) {
                    //Gold can be traded
                    requiredRes[i]--;
                    resourceState[5] -= 2;
                }
            }
        }

        return Arrays.stream(requiredRes).sum() == 0;
    }

    /**
     * Check if a player action (build, trade or swap) is executable in the
     * given board and resource state.
     *
     * @param action: String representation of the action to check.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the action is applicable, false otherwise.
     */
    public static boolean canDoAction(String action, String board_state, int[] resource_state) {
        if(!isActionWellFormed(action) || !isBoardStateWellFormed(board_state) ||
                resource_state==null || resource_state.length==0)
            return false;

        if (action.contains("build ")) {
            String[] b = action.split(" ");
            return checkResources(b[1], resource_state) && checkBuildConstraints(b[1], board_state);
        } else if (action.contains("trade "))
            return resource_state[resource_state.length - 1] > 1;

        else if (action.contains("swap ")) {
            String[] s_1 = action.split(" ");
            String[] s_2 = board_state.split(",");
            return (resource_state[Integer.parseInt(s_1[1])] > 0) &&
                    (Arrays.asList(s_2).contains("J" + (Integer.parseInt(s_1[2]) + 1))
                            || Arrays.asList(s_2).contains("J6"));
        }
        return false;
    }

    /**
     * Check if the specified sequence of player actions is executable
     * from the given board and resource state.
     *
     * @param actions: The sequence of (string representations of) actions.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the action sequence is executable, false otherwise.
     */
    public static boolean canDoSequence(String[] actions, String board_state, int[] resource_state) {
        int[] resourceState = resource_state.clone();
        List<Piece> state = generatePieces(board_state);
        List<Action> actionList = generateActions(actions);

        for (int i = 0; i < actions.length; i++){
            StringBuilder stringBoard = new StringBuilder();
            for (var v : state){
                if(v.getBuilt()){
                    stringBoard.append(v);
                    stringBoard.append(",");
                }
            }
            stringBoard.deleteCharAt(stringBoard.length()-1);
            System.out.println(Arrays.toString(resourceState) + " for action " + i);
            if (!canDoAction(actions[i], stringBoard.toString(), resourceState)) return false;
            try {
                if (Objects.requireNonNull(getPiece(state, actionList.get(i).target)).getUsed() &&
                        Objects.requireNonNull(getPiece(state, "J6")).getBuilt())
                    actionList.get(i).target = "J6";
            } catch (NullPointerException ignored) {
            }

            Action.applyAction(state, resourceState, actionList.get(i));
        }
        return true;
    }

    /**
     * Converts from a board state to the string representation
     * @param state the board state to convert
     * @return the string representation
     */
    public static String boardStateToString(List<Piece> state){
        StringBuilder result = new StringBuilder();
        for (var v : state){
            if(v.getBuilt()) result.append(v.encode()).append(',');
        }
        if (result.length()>0)
            result.deleteCharAt(result.length()-1);

        return result.toString();
    }

    /**
     * Returns a piece from a list given its type and index/point value as a string
     *
     * @param pieces the list to return from
     * @param piece the string representation of the piece you are looking for
     * @return the first occurrence of that piece type and point value/index
     */
    public static Piece getPiece(List<Piece> pieces, String piece){
        for (var v : pieces){
            if (Objects.equals(v.encode(), piece)) return v;
        }
        return null;
    }

    /**
     * Converts from an array of action strings to a list of Action class'
     *
     * @param actions the array of strings to convert
     * @return the list of action instances
     */
    public static List<Action> generateActions(String[] actions){
        List<Action> result = new ArrayList<>();
        for(var action : actions){
            result.add(Action.decode(action));
        }
        return result;
    }

    /**
     * Find the path of roads that need to be built to reach a specified
     * (un-built) structure in the current board state. The roads should
     * be returned as an array of their string representation, in the
     * order in which they have to be built. The array should _not_ include
     * the target structure (even if it is a road). If the target structure
     * is reachable via the already built roads, the method should return
     * an empty array.
     *
     * Note that on the Island One map, there is a unique path to every
     * structure. 
     *
     * @param target_structure: The string representation of the structure
     *        to reach.
     * @param board_state: The string representation of the board state.
     * @return An array of string representations of the roads along the
     *         path.
     */
    public static String[] pathTo(String target_structure,
                                  String board_state) {
        // We should first create a Hashmap(Target[key], Condition[value])
        // Therefore, we can determine what should be built before we build our target
        HashMap<String, String> Target_Condition = new HashMap<>() {
        };

        //R
        Target_Condition.put("R0", "");
        Target_Condition.put("R1", "R0");
        Target_Condition.put("R2", "R0");
        Target_Condition.put("R3", "R2");
        Target_Condition.put("R4", "R3");
        Target_Condition.put("R5", "R3");
        Target_Condition.put("R6", "R5");
        Target_Condition.put("R7", "R6");
        Target_Condition.put("R8", "R7");
        Target_Condition.put("R9", "R8");
        Target_Condition.put("R10", "R9");
        Target_Condition.put("R11", "R10");
        Target_Condition.put("R12", "R7");
        Target_Condition.put("R13", "R12");
        Target_Condition.put("R14", "R13");
        Target_Condition.put("R15", "R14");

        //S
        Target_Condition.put("S3", "");
        Target_Condition.put("S4", "R2");
        Target_Condition.put("S5", "R5");
        Target_Condition.put("S7", "R7");
        Target_Condition.put("S9", "R9");
        Target_Condition.put("S11", "R11");

        //C
        Target_Condition.put("C7", "R1");
        Target_Condition.put("C12", "R4");
        Target_Condition.put("C20", "R13");
        Target_Condition.put("C30", "R15");


        try{
            String Output = Target_Condition.get(target_structure); // initialize the Output
            // if the board_state already contains the road that should be built,
            // it can skip the following below, and return "" directly.
            // Otherwise, it should go through the following below
            if(!board_state.contains(Output)){
                List<String> board_state_ArrayList = Arrays.asList(board_state.split(","));
                List<String> new_board_state_ArrayList = new ArrayList<>();
                while(!Objects.equals(Output, "")){ // we should keep adding what is required as long as we meet values == ""
                    new_board_state_ArrayList.add(Output);
                    Output = Target_Condition.get(Output); // renew the variable which need to search in HashMap next
                }
                new_board_state_ArrayList.removeAll(board_state_ArrayList); // reserve the elements not appearing in the given board_state
                Collections.reverse(new_board_state_ArrayList); // {R5, R3, R2, R0} -> {R0, R2, R3, R5}
                // transfer "new_board_state_ArrayList" to Array
                String[] result = new String[new_board_state_ArrayList.size()];
                result = new_board_state_ArrayList.toArray(result);
                return result;
            }
        } catch (NullPointerException ignored) {
        }
        return new String[]{};
    }

    /**
     * Generate a plan (sequence of player actions) to build the target
     * structure from the given board and resource state. The plan may
     * include trades and swaps, as well as building other structures if
     * needed to reach the target structure or to satisfy the build order
     * constraints.
     *
     * However, the plan must not have redundant actions. This means it
     * must not build any other structure that is not necessary to meet
     * the building constraints for the target structure, and it must not
     * trade or swap for resources if those resources are not needed.
     *
     * If there is no valid build plan for the target structure from the
     * specified state, return null.
     *
     * @param target_structure: The string representation of the structure
     *        to be built.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return An array of string representations of player actions. If
     *         there exists no valid build plan for the target structure,
     *         the method should return null.
     */
    public static String[] buildPlan(String target_structure,
                                     String board_state,
                                     int[] resource_state) {
        return null; // FIXME: Task #14
    }

}
