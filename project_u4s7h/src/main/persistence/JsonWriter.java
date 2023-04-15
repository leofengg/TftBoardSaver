package persistence;

import model.BoardCollection;
import org.json.JSONObject;


import java.io.*;
//a class representing a writer to json

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter printWriter;
    private String destination;

    /*
     * EFFECTS: creates JsonWriter to write to destination file
     */
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    /*
     * MODIFIES: this
     * EFFECTS: opens writer, throws exception if destination file can't be opened/found for writing
     */
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destination));
    }

    /*
     * MODIFIES: this
     * EFFECTS: closes writer
     */
    public void close() {
        printWriter.close();
    }

    /*
     * MODIFIES: this
     * EFFECTS: writes file
     */
    public void write(BoardCollection collection) {
        JSONObject jsonObject = collection.toJson();
        saveToFile(jsonObject.toString(TAB));
    }

    /*
     * MODIFIES: this
     * EFFECTS: writes file to string
     */
    public void saveToFile(String json) {
        printWriter.print(json);
    }
}
