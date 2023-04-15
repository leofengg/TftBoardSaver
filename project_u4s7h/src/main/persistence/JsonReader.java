package persistence;

import model.BoardCollection;
import model.TftBoard;
import model.Units;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

//Represents a reader that reads BoardCollection from JSON stored data
public class JsonReader {

    private String source;

    /*
     * EFFECTS: constructs reader to read from file
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /*
     * EFFECTS: reads file as string and returns it
     */
    private String readFile(String source) throws IOException {

        StringBuilder content = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> content.append(s));
        }
        return content.toString();
    }

    /*
     * EFFECTS: creates new boardCollection object from json and returns it
     */
    private BoardCollection parseBoardCollection(JSONObject jsonObject) {
        BoardCollection collection = new BoardCollection();

        parseBoards(collection, jsonObject);

        return collection;
    }

    /*
     * MODIFIES: collection
     * EFFECTS: gets boards from json object and adds to collection
     */
    private void parseBoards(BoardCollection collection, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("collection");
        for (Object o : jsonArray) {
            JSONObject board = (JSONObject) o;
            addBoards(collection, board);
        }
    }

    /*
     * MODIFIES: collection
     * EFFECTS: creates tftBoard and adds to collection
     */
    private void addBoards(BoardCollection collection, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        TftBoard tftBoard = new TftBoard(name);
        JSONArray jsonArray = jsonObject.getJSONArray("board");
        for (int i = 0; i <= 2; i++) {
            JSONArray arrayTwo = jsonArray.getJSONArray(i);
            int j = 0;
            for (Object o : arrayTwo) {
                j++;
                if (!JSONObject.NULL.equals(o)) {
                    Units unit = createBoard(o);
                    tftBoard.addUnit(unit, i + 1, j);
                }
            }
        }
        collection.addToCollection(tftBoard);
    }

    /*
     * MODIFIES: board collection
     * EFFECTS: parses units and returns unit object
     */
    private Units createBoard(Object object) {

        JSONObject unit = (JSONObject) object;
        String name = unit.getString("name");
        String trait = unit.getString("trait");

        Units newUnit = new Units(name, trait);
        return newUnit;

    }

    /*
     *  EFFECTS: reads file as string, returns as BoardCollection object
     *  throws IO exception if there is an error reading from file
     */
    public BoardCollection read() throws IOException {
        String data = readFile(source);
        JSONObject jsonObject = new JSONObject(data);
        return parseBoardCollection(jsonObject);
    }


}
