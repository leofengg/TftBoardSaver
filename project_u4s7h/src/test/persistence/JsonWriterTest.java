package persistence;

import model.BoardCollection;
import model.TftBoard;
import model.Units;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest{

    @Test
    void testInvalidFile() {
        try {
            BoardCollection collection = new BoardCollection();
            JsonWriter writer = new JsonWriter("./data/wrong\0place");
            writer.open();
            fail("IO Exception");
        } catch (IOException e){
            //pass
        }
    }

    @Test
    void testEmptyCollection() {
        try {
            BoardCollection collection = new BoardCollection();
            JsonWriter writer = new JsonWriter("./data/testEmptyCollection.json");
            writer.open();
            writer.write(collection);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyCollection.json");
            collection = reader.read();
            assertTrue(collection.isEmpty());
        } catch (IOException e) {
            fail("Exception shouldn't have been thrown");
        }
    }

    @Test
    void testNonEmptyCollection() {
        try {
            BoardCollection collection = new BoardCollection();
            JsonWriter writer = new JsonWriter("./data/testNonEmptyCollection.json");
            collection.addToCollection(new TftBoard("TEST1"));
            collection.addToCollection(new TftBoard("TEST2"));

            writer.open();
            writer.write(collection);
            writer.close();
            JsonReader reader = new JsonReader("./data/testNonEmptyCollection.json");
            collection = reader.read();
            assertFalse(collection.isEmpty());
            assertEquals("TEST1 TEST2 ", collection.toString());
        } catch (IOException e) {
            fail("Exception shouldn't have been thrown");
        }
    }

    @Test
    void testEmptyTftBoard() {
        try {
            BoardCollection collection = new BoardCollection();
            TftBoard board = new TftBoard("TEST BOARD");
            collection.addToCollection(board);
            JsonWriter writer = new JsonWriter("./data/testEmptyTftBoard.json");
            writer.open();
            writer.write(collection);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyTftBoard.json");
            collection = reader.read();
            assertTrue(collection.isInCollection("TEST BOARD"));
            assertEquals("_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n",
                    collection.getBoardString("TEST BOARD"));
        } catch (IOException e) {
            fail("Exception shouldn't have been caught");
        }
    }

    @Test
    void testNonEmptyTftBoard() {
        try {

            BoardCollection collection = new BoardCollection();
            TftBoard testBoard = new TftBoard("TEST BOARD");
            Units unit = new Units("kaisa", "lagoon");
            Units unitTwo = new Units("graves", "cannoneer");
            testBoard.addUnit(unit, 2, 1);
            testBoard.addUnit(unitTwo, 3, 7);
            collection.addToCollection(testBoard);

            JsonWriter writer = new JsonWriter("./data/testNonEmptyTftBoard.json");
            writer.open();
            writer.write(collection);
            writer.close();

            JsonReader reader = new JsonReader("./data/testNonEmptyTftBoard.json");
            collection = reader.read();
            assertEquals("_ _ _ _ _ _ _ \nkaisa _ _ _ _ _ _ \n_ _ _ _ _ _ graves \n",
                    collection.getBoardString("TEST BOARD"));
            checkUnit("kaisa", "lagoon", collection.getBoard("TEST BOARD").getUnit(2,1));
            checkUnit("graves", "cannoneer", collection.getBoard("TEST BOARD").getUnit(3,7));



        } catch (IOException e) {
            fail("Exception shouldn't have been caught");
        }
    }

    @Test
    void testMultipleNonEmptyTftBoard() {
        try {
            BoardCollection collection = new BoardCollection();

            TftBoard testBoard = new TftBoard("TEST BOARD");
            TftBoard testBoardTwo = new TftBoard("TEST BOARD2");
            Units unit = new Units("kaisa", "lagoon");
            Units unitTwo = new Units("graves", "cannoneer");
            testBoard.addUnit(unit, 2, 1);
            testBoard.addUnit(unitTwo, 3, 7);
            testBoardTwo.addUnit(unit, 1,1);

            collection.addToCollection(testBoard);
            collection.addToCollection(testBoardTwo);

            JsonWriter writer = new JsonWriter("./data/testMultipleNonEmptyTftBoard.json");
            writer.open();
            writer.write(collection);
            writer.close();

            JsonReader reader = new JsonReader("./data/testMultipleNonEmptyTftBoard.json");
            collection = reader.read();
            assertEquals("_ _ _ _ _ _ _ \nkaisa _ _ _ _ _ _ \n_ _ _ _ _ _ graves \n",
                    collection.getBoardString("TEST BOARD"));
            checkUnit("kaisa", "lagoon", collection.getBoard("TEST BOARD").getUnit(2,1));
            checkUnit("graves", "cannoneer", collection.getBoard("TEST BOARD").getUnit(3,7));
            assertEquals("kaisa _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n",
                    collection.getBoardString("TEST BOARD2"));
            checkUnit("kaisa", "lagoon", collection.getBoard("TEST BOARD2").getUnit(1,1));

        } catch (IOException e) {
            fail("Exception shouldn't have been caught");
        }
    }
}
