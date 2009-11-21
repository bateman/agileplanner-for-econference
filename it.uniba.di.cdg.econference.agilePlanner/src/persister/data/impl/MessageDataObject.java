package persister.data.impl;

import java.io.Serializable;
import java.util.HashMap;

import persister.Message;



public class MessageDataObject implements Message, Serializable {

    private static final long serialVersionUID = -5406709808111888256L;

    private long senderId;

    private int messageType, etype;

    private Object message;

    private HashMap<String, String> data = new HashMap<String,String>();

    public MessageDataObject(long senderid, int mtype, Object message, int etype) {
        setSender(senderid);
        setMessageType(mtype);
        setMessage(message);
        setEtype(etype);
    }

    public MessageDataObject(long senderid, int mtype, Object message, HashMap<String, String> data) {
    	setSender(senderid);
        setMessageType(mtype);
        setMessage(message);
        setData(data);
    }

    public MessageDataObject(long senderid, int mtype, Object message) {
    	setSender(senderid);
        setMessageType(mtype);
        setMessage(message);
    }

    
    public MessageDataObject(int mtype, Object message) {
    	setMessageType(mtype);
    	setMessage(message);
    }

    public MessageDataObject(long senderid, int mtype) {
    	setMessageType(mtype);
    	setSender(senderid);
    }

    public MessageDataObject(int mtype) {
    	setMessageType(mtype);
    	setMessage(null);
    }

    public Object getMessage() {
        return this.message;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public long getSender() {
        return this.senderId;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public void setMessageType(int mtpye) {
        this.messageType = mtpye;
    }

    public void setSender(long senderid) {
        this.senderId = senderid;
    }

    public String toString() {
        String message = "###################################\n" + "#             Message             #\n"
                + "###################################\n\n" + "ID:\t" + getSender() + "\n" + "Type:\t" + getMessageType() + "\n\n";
        return message;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public int getEtype() {
        return etype;
    }

    public void setEtype(int etype) {
        this.etype = etype;
    }
    
    public void addData(String key, String value){
    	this.data.put(key, value);
    }

}
