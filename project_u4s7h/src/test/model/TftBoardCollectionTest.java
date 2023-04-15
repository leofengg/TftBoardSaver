package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class TftBoardCollectionTest {

    private BoardCollection testCollection;
    private BoardCollection collectionTwo;
    private TftBoard testBoard;
    private TftBoard boardTwo;
    private Units unit;
    private Units unitTwo;

    @BeforeEach
    void runBefore() {

        testCollection = new BoardCollection();
        collectionTwo = new BoardCollection();

        testBoard = new TftBoard("TEST BOARD");
        boardTwo = new TftBoard("BOARD TWO");

        unit = new Units("kaisa", "lagoon");
        unitTwo = new Units("graves", "cannoneer");

    }

    @Test
    void testConstructors() {

        assertEquals("TEST BOARD", testBoard.getName());

        assertEquals("", testCollection.toString());
        assertEquals("MY COLLECTION", testCollection.getCollectionName());

        assertEquals("kaisa", unit.getName());
        assertEquals("lagoon", unit.getTrait());
    }

    @Test
    void testAddToCollection() {

        assertTrue(testCollection.isEmpty());
        testCollection.addToCollection(testBoard);

        assertFalse(testCollection.isEmpty());
    }

    @Test
    void testRemoveFromCollection() {

        assertFalse(testCollection.removeFromCollection("fake board"));

        testCollection.addToCollection(testBoard);
        assertFalse(testCollection.isEmpty());
        assertTrue(testCollection.removeFromCollection(testBoard.getName()));
        assertTrue(testCollection.isEmpty());


    }

    @Test
    void testRemoveMultipleFromCollection() {

        testCollection.addToCollection(testBoard);
        testCollection.addToCollection(boardTwo);
        assertTrue(testCollection.removeFromCollection(boardTwo.getName()));
        assertFalse(testCollection.isEmpty());
        assertTrue(testCollection.removeFromCollection(testBoard.getName()));
        assertTrue(testCollection.isEmpty());

        //Test event log
        List<Event> l = new ArrayList<Event>();

        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            l.add(next);
        }
    }

    @Test
    void testIsInCollection() {

        assertFalse(testCollection.isInCollection(testBoard.getName()));
        testCollection.addToCollection(testBoard);
        assertTrue(testCollection.isInCollection(testBoard.getName()));

        testCollection.addToCollection(boardTwo);
        assertTrue(testCollection.isInCollection(boardTwo.getName()));

    }

    @Test
    void testCollectionToString() {

        collectionTwo.addToCollection(testBoard);

        assertEquals("",testCollection.toString());
        assertEquals("TEST BOARD ", collectionTwo.toString());

    }

    @Test
    void testCollectionGetBoard() {

        testCollection.addToCollection(testBoard);

        boardTwo.addUnit(unit, 2, 1);
        testCollection.addToCollection(boardTwo);


        assertEquals("_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n",
                testCollection.getBoardString("TEST BOARD"));
        assertEquals("_ _ _ _ _ _ _ \nkaisa _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n",
                testCollection.getBoardString("BOARD TWO"));
        assertEquals("The board you are trying to view does not exist.\n",
                testCollection.getBoardString("BOARD 3"));
        assertEquals(null, testCollection.getBoard("fake"));
    }
    @Test
    void testAddUnit() {

        assertTrue(testBoard.isEmpty());
        assertTrue(testBoard.cellIsEmpty(2,2));
        testBoard.addUnit(unit, 2, 2);

        assertFalse(testBoard.isEmpty());
        assertFalse(testBoard.cellIsEmpty(2,2));
    }

    @Test
    void testRemoveUnit() {

        testBoard.addUnit(unit, 1, 6);
        testBoard.addUnit(unitTwo, 2,3);
        assertFalse(testBoard.cellIsEmpty(1,6));
        testBoard.removeUnit(1,6);
        assertTrue(testBoard.cellIsEmpty(1,6));
        assertFalse(testBoard.isEmpty());
    }

    @Test
    void testBoardToString() {

        assertEquals("_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n", testBoard.toString());

        boardTwo.addUnit(unit, 2, 1);
        boardTwo.addUnit(unitTwo, 2, 2);
        assertEquals("_ _ _ _ _ _ _ \nkaisa graves _ _ _ _ _ \n_ _ _ _ _ _ _ \n", boardTwo.toString());
    }

    @Test
    void testUnitToString() {

        assertEquals("kaisa has the trait lagoon", unit.toString());
    }

}