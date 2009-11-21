package persister.data.impl;

import java.io.Serializable;

import persister.Keystroke;
import persister.MouseClick;
import persister.Event;



public class EventDataObject implements Event, Keystroke, MouseClick, Serializable {

    private static final long serialVersionUID = 2908054717758878315L;

    private int locationX, locationY, mouseClientID;

    private long id;

    private String name;

    private char c;

    private boolean sendKeyOut;

    private int eventType;

    //public static Keystroke keystroke;

    public EventDataObject() {
        setId(0);
        setName("");
        setLocationX(0);
        setLocationY(0);
    }

    public EventDataObject(long clientid, String name, int locationX, int locationY) {
        setId(clientid);
        setName(name);
        setLocationX(locationX);
        setLocationY(locationY);
    }

    public Object clone() {
        EventDataObject clone = new EventDataObject();
        clone.locationX = getLocationY();
        clone.id = getId();
        clone.name = getName();
        clone.c = getKeystroke();
        clone.sendKeyOut = isSendKeyOut();
        clone.eventType = getEventType();

        return clone;
    }

    public long getId() {
        return this.id;
    }

    public int getLocationX() {
        return this.locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setId(long id) {
        this.id = id;

    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;

    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        String mm = "###################################\n" + "#            Event            #\n" + "###################################\n\n"
                + "ID:\t\t" + getId() + "\n" + "Location:\t( " + getLocationX() + " x " + getLocationY() + " )\n" + "Name:\t\t" + getName() + "\n\n";
        return mm;
    }

    public char getKeystroke() {
        return c;
    }

    public void setKeystroke(char c) {
        this.c = c;
    }

    public boolean isSendKeyOut() {

        return sendKeyOut;
    }

    public void setSendKeyOut(boolean sendKeyOut) {
        this.sendKeyOut = sendKeyOut;

    }

    public int getEventType() {

        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;

    }
	

}
