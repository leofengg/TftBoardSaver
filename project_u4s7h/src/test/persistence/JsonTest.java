package persistence;

import model.BoardCollection;
import model.TftBoard;
import model.Units;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkUnit(String name, String trait, Units unit) {
        assertEquals(unit.getName(), name);
        assertEquals(unit.getTrait(), trait);
    }

    protected void checkBoard(String name, TftBoard board) {
        assertEquals(board.getName(), name);
    }

}
