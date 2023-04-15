package persistence;

import org.json.JSONObject;
//interface that allows writing ot json object

public interface Writable {
    //EFFECTS: returns json object
    JSONObject toJson();
}
