package model;

import persistence.Writable;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.ArrayList;
//Represents collection of tft boards

public class BoardCollection implements Writable {

    //the collection of tft boards
    private ArrayList<TftBoard> collection;
    private String name;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final String JSON_STORE = "./data/boardCollection.json";

    /*
     * EFFECTS: Creates an array named collection
     */

    public BoardCollection() {

        this.name = "MY COLLECTION";
        this.collection = new ArrayList<>();
    }

    public String getCollectionName() {
        return this.name;
    }

    /*
     * REQUIRES: a tft board to add to collection
     * MODIFIES: this
     * EFFECTS: adds a tft board to the list of boards
     */

    public void addToCollection(TftBoard tftboard) {

        collection.add(tftboard);
        EventLog.getInstance().logEvent(new Event("Added " + tftboard.getName() + " to collection."));
    }

    /*
     * REQUIRES: a non-empty collection of boards and a board that's in the collection
     * MODIFIES: this
     * EFFECTS: removes board from collection
     */
    public boolean removeFromCollection(String boardName) {
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getName().equals(boardName)) {
                collection.remove(i);
                EventLog.getInstance().logEvent(new Event("Removed " + boardName + " from collection."));
                return true;
            }
        }
        return false;
    }

    /*
     * EFFECTS: returns whether a specific board is in the collection
     */
    public boolean isInCollection(String boardName) {
        for (TftBoard board : collection) {
            if (board.getName().equals(boardName)) {
                return true;
            }
        }
        return false;
    }

    /*
     * EFFECTS: returns if collection is empty or not
     */
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /*
     * EFFECTS: displays the collection of tft board names
     */
    @Override
    public String toString() {

        String collectionToString = "";

        for (TftBoard board : collection) {
            collectionToString =  collectionToString + board.getName() + " ";
        }
        return collectionToString;
    }

    /*
     * REQUIRES: a board that's in the collection of boards
     * EFFECTS: returns string representation of a board
     */

    public String getBoardString(String boardName) {

        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getName().equals(boardName)) {
                return collection.get(i).toString();
            }
        }
        return "The board you are trying to view does not exist.\n";
    }

    /*
     * REQUIRES: valid tft board name
     * EFFECTS: returns board object
     */
    public TftBoard getBoard(String boardName) {
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getName().equals(boardName)) {
                return collection.get(i);
            }
        }
        return null;
    }

    /*
     * EFFECTS: returns json representation of boardcollection
     */
    @Override
    public JSONObject toJson() {
        EventLog.getInstance().logEvent(new Event("Saved collection."));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("collection", boardsToJson());
        return jsonObject;
    }

    /*
     * EFFECTS: returns and changes boards in collection to json array
     */
    private JSONArray boardsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (TftBoard board : collection) {
            jsonArray.put(board.toJson());
        }
        return jsonArray;
    }

}
