package semanticChecker.formDataStorage.valueData;

import semanticChecker.formDataStorage.valueData.values.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LGGX on 22-Feb-17.
 */
public class ValueData {

    private Map<String, Value> states;

    public ValueData() {
        this.states = new HashMap<>();
    }

    public Map<String, Value> getMap() { return this.states; }

    public void addValue(String key, Value value) {
        states.put(key, value);
    }

    public Value getValue(String key) {
        Value state = states.get(key);
        return state;
    }
}