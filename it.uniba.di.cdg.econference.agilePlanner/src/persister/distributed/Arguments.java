/**
 * 
 */
package persister.distributed;

import java.util.HashMap;

/**
 * @author dhillonh
 * 
 */
public class Arguments {

    private HashMap<String, String> values = null;

    private String currentKey;

    public Arguments(String[] args) {
        this.values = new HashMap<String, String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                doKey(args[i]);
            }
            else {
                doValue(args[i]);
            }
        }

        // ensure that the tail arg gets added
        doKey(null);
    }

    private void doValue(String value) {
        this.values.put(this.currentKey, value);
        currentKey = null;
    }

    private void doKey(String key) {

        if (currentKey != null) {
            this.values.put(currentKey, "");
        }
        if (key != null)
            this.currentKey = key.substring(1);

    }

    public String getValue(String key) {
        return this.values.get(key);
    }
}
