package model;

import persistence.Writable;
import org.json.JSONObject;

//Represents a champion having a name, trait, size and level
public class Units implements Writable {

    final String name;        //name of champion
    final String trait;       //trait of champion


    /*
     * REQUIRES: string name and trait have non-zero length, unit size and level > 0
     * EFFECTS: name on Unit name is  set to name, name on unit trait is set to trait;
     *         unit size and level are set to integers > 0
     */
    public Units(String name, String trait) {
        this.name = name;
        this.trait = trait;
    }

    public String getName() {
        return name;
    }

    public String getTrait() {
        return trait;
    }

    /*
     * EFFECTS: returns string representation of champion
     */

    @Override
    public String toString() {
        return name + " has the trait " + trait;
    }

    /*
     * EFFECTS: returns json representation of units
     */
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("trait", trait);

        return jsonObject;
    }
}
