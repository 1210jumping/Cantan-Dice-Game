package comp1110.ass2;

import java.util.*;

import gittest.A;
import org.junit.jupiter.api.Test;

import static comp1110.ass2.CatanDice.generateActions;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class TestGenerateActions {
    @Test
    public void testGeneric(){
        String[] action = new String[]{"build R2", "build J1", "trade 4", "swap 1 4"};
        List<Action> answer = new ArrayList<>();
        answer.add(new Action(Action.ActionType.BUILD, "R2", null, null));
        answer.add(new Action(Action.ActionType.BUILD, "J1", null, null));
        answer.add(new Action(Action.ActionType.TRADE, null, null, DiceFace.ORE));
        answer.add(new Action(Action.ActionType.SWAP, null, DiceFace.LUMBER, DiceFace.ORE));
        assertEquals(answer.get(0).toString(), generateActions(action).get(0).toString());
        assertEquals(answer.get(1).toString(), generateActions(action).get(1).toString());
        assertEquals(answer.get(2).toString(), generateActions(action).get(2).toString());
        assertEquals(answer.get(3).toString(), generateActions(action).get(3).toString());
    }
    @Test
    public void testEmpty(){
        String[] action = new String[0];
        assertEquals(0,generateActions(action).size());
    }
    @Test
    public void testBadlyFormed(){
        String[] action = new String[]{"action R2", "build R17", "trade 3 4", "swap 1", "swap ore lumber"};
        assertEquals(0,generateActions(action).size());
    }

    public static void main(String[] args) {
        TestGenerateActions tests = new TestGenerateActions();
        System.out.println("testing...");
        tests.testGeneric();
        tests.testEmpty();
        tests.testBadlyFormed();
        System.out.println("all done!");
    }
}
