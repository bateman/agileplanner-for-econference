package persister;

import java.io.Serializable;

public class IndexCardLiveUpdate implements LiveUpdate, Serializable {

    public static final int FIELD_TITLE = 0;
    public static final int FIELD_DESCRIPTION = 1;
	public static final int FIELD_TESTTEXT = 2;

    private String text;
    private int field;
    private long id;

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
