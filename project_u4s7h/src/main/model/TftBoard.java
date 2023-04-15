package model;

import persistence.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

//Represents a TFT Board with a 3 by 7 matrix containing objects from the champions class
public class TftBoard implements Writable {

    final String name;                //Board name
    final Units[][] board;            //the matrix

    /*
     * REQUIRES: a board name
     * EFFECTS: name  of board is set to boardName
     */
    public TftBoard(String name) {
        this.name = name;
        this.board = new Units[3][7];
    }

    /*
     * REQUIRES: unit name and unit size > 0, 3 >= row >= 0 and 7 >= col >= 0
     * MODIFIES: this
     */
    public void addUnit(Units champ, int row, int col) {

        board[row - 1][col - 1] = champ;
        EventLog.getInstance().logEvent(new Event("Added " + champ.getName() + " unit to " + name + " board"));
    }

    /*
     * REQUIRES: unit name and unit size > 0, unit size <= board size
     * MODIFIES: unit amount
     */
    public void removeUnit(int row, int col) {

        EventLog.getInstance().logEvent(new Event("Removed " + board[row - 1][col - 1].getName()
                + " from " + name + " board "));
        board[row - 1][col - 1] = null;

    }

    /*
     * REQUIRES: 3 >= row >= 0 and 7 >= col >= 0
     * EFFECTS: returns true if specific cell is empty
     */
    public boolean cellIsEmpty(int row, int col) {
        return board[row - 1][col - 1] == null;
    }

    /*
     * EFFECTS: returns true if board is empty, false otherwise
     */
    public boolean isEmpty() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    /*
     * EFFECTS: returns tft board as a string
     */
    @Override
    public String toString() {

        String boardToString = "";

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    boardToString = boardToString + "_" + " ";
                } else {
                    boardToString = boardToString + board[i][j].getName() + " ";
                }
            }
            boardToString = boardToString + "\n";
        }
        return boardToString;
    }

    /*
     * EFFECTS: returns json representation of tftboard
     */
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("board", unitsToJson());
        return jsonObject;
    }

    /*
     * EFFECTS: returns and changes every unit into json array
     */
    private JSONArray unitsToJson() {
        JSONArray finalArray = new JSONArray();

        for (int i = 0; i < board.length; i++) {
            JSONArray intermediateArray = new JSONArray();
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    intermediateArray.put(JSONObject.NULL);
                } else {
                    intermediateArray.put(board[i][j].toJson());
                }
            }
            finalArray.put(intermediateArray);
        }
        return finalArray;
    }

    /*
     * EFFECTS: returns unit in specified slot
     */
    public Units getUnit(int row, int col) {
        return board[row - 1][col - 1];
    }
}