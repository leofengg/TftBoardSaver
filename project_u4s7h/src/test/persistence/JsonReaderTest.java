package persistence;

import model.BoardCollection;
import model.TftBoard;
import model.Units;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReadWrongFile() {
        JsonReader reader = new JsonReader("./data/alsdkfjasd.json");
        try {
            BoardCollection collection = reader.read();
            fail("Expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    void testReadEmptyCollection() {
        JsonReader reader = new JsonReader("./data/testReadEmptyCollection.json");
        try {
            BoardCollection collection = reader.read();
            assertTrue(collection.isEmpty());
        } catch (IOException e) {
            System.out.println("Exception shouldn't happen1");
        }

    }

    @Test
    void testReadNonEmptyCollection() {
        JsonReader reader = new JsonReader("./data/testReadNonEmptyCollection.json");
        try {
            BoardCollection collection = reader.read();
            assertFalse(collection.isEmpty());
            assertEquals("TEST BOARD ", collection.toString());
        } catch (IOException e) {
            System.out.println("Exception shouldn't happen2");
        }
    }

    @Test
    void testReadEmptyBoards() {
        JsonReader reader = new JsonReader("./data/testReadEmptyBoards.json");

        try {
            BoardCollection collection = reader.read();

            assertEquals("_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n",
                    collection.getBoardString("TEST BOARD"));
        } catch (IOException e) {
            System.out.println("Exception shouldn't happen3");
        }
    }

    @Test
    void testReadNonEmptyBoards() {
        JsonReader reader = new JsonReader("./data/testReadNonEmptyBoards.json");
        try {
            BoardCollection collection = reader.read();
            assertEquals("_ _ _ _ _ _ _ \nkaisa _ _ _ _ _ _ \n_ _ _ _ _ _ graves \n",
                    collection.getBoardString("TEST BOARD"));
            checkUnit("kaisa", "lagoon", collection.getBoard("TEST BOARD").getUnit(2,1));
            checkUnit("graves", "cannoneer", collection.getBoard("TEST BOARD").getUnit(3,7));

            assertEquals("_ _ _ _ _ _ _ \nkaisa _ _ _ _ _ _ \n_ _ _ _ _ _ _ \n",
                    collection.getBoardString("TEST BOARD2"));
            checkUnit("kaisa", "lagoon", collection.getBoard("TEST BOARD").getUnit(2,1));
        } catch (IOException e) {
            System.out.println("Exception shouldn't happen4");
        }
    }
}
