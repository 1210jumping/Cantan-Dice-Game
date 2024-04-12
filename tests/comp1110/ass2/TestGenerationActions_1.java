package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static comp1110.ass2.CatanDice.generateActions;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGenerationActions_1 {
    @Test
    public void testGeneric(){
        String[] TestData = new String[7];
        TestData[0] = "build S7";
        TestData[1] = "build S9";
        TestData[2] = "build S11";
        TestData[3] = "build C7";
        TestData[4] = "build C12";
        TestData[5] = "build C20";
        TestData[6] = "build C30";
        List<Action> expected = new ArrayList<>();
        expected.add(new Action(Action.ActionType.BUILD, "S7", null, null));
        expected.add(new Action(Action.ActionType.BUILD, "S9", null, null));
        expected.add(new Action(Action.ActionType.BUILD, "S11", null, null));
        expected.add(new Action(Action.ActionType.BUILD, "C7", null, null));
        expected.add(new Action(Action.ActionType.BUILD, "C12", null, null));
        expected.add(new Action(Action.ActionType.BUILD, "C20", null, null));
        expected.add(new Action(Action.ActionType.BUILD, "C30", null, null));
        assertEquals(expected.get(0).toString(), generateActions(TestData).get(0).toString());
        assertEquals(expected.get(1).toString(), generateActions(TestData).get(1).toString());
        assertEquals(expected.get(2).toString(), generateActions(TestData).get(2).toString());
        assertEquals(expected.get(3).toString(), generateActions(TestData).get(3).toString());
        assertEquals(expected.get(4).toString(), generateActions(TestData).get(4).toString());
        assertEquals(expected.get(5).toString(), generateActions(TestData).get(5).toString());
        assertEquals(expected.get(6).toString(), generateActions(TestData).get(6).toString());
    }

    @Test
    public void testBadlyFormed(){
        String[] TestData = new String[3];
        TestData[0] = "build S6";
        TestData[1] = "build C8";
        TestData[2] = "safgwe S3";
        assertEquals(0,generateActions(TestData).size());
    }
}
