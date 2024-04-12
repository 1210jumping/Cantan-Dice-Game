package comp1110.ass2;

//This class was written by Pin-Shen

/**
 * The dice have 6 outcomes, equivalent to the resources
 * Each outcome can reflect to a unique number. Useful for indexing
 */
public enum DiceFace {
    ORE(0),
    GRAIN(1),
    WOOL(2),
    LUMBER(3),
    BRICK(4),
    GOLD(5);

    public final int diceNum;
    DiceFace(int diceNum) {
        this.diceNum = diceNum;
    }

    public static DiceFace fromInt(int i){
        switch (i){
            case (0) -> {
                return ORE;
            }
            case (1) -> {
                return GRAIN;
            }
            case (2) -> {
                return WOOL;
            }
            case (3) -> {
                return LUMBER;
            }
            case (4) -> {
                return BRICK;
            }
            case (5) -> {
                return GOLD;
            }
        }
        return null;
    }
}
