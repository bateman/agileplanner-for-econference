package persister;

import java.util.HashMap;

public interface Message {
    public static final int EXCEPTION = -1;
    public static final int CONNECT = 99;
    public static final int DISCONNECT = 98;
    public static final int AUTH_FAIL = 97;
    
    public static final int GET_PROJECT_NAMES = 1;
    public static final int LOAD = 2;
    public static final int TIMEBOX_LOAD = 3;
    public static final int SAVE = 4;
    public static final int SAVE_AS = 5;
    public static final int UPLOAD = 6;
    public static final int DOWNLOAD = 7;
    public static final int GET_PROJECT_FILES_NAME = 8;
    public static final int CREATE_BACKLOG = 10;
    public static final int DELETE_BACKLOG = 11;
    public static final int DELETE_CARD = 12;
    public static final int UNDELETE_CARD = 13;
    public static final int UPDATE_BACKLOG = 14;
    public static final int CREATE_ITERATION = 20;
    public static final int CREATE_OWNER = 26;
    public static final int DELETE_OWNER=27;
    public static final int DELETE_ITERATION = 21;
    public static final int UNDELETE_ITERATION = 23;
    public static final int UPDATE_ITERATION = 24;
    public static final int CREATE_STORYCARD = 40;
    public static final int DELETE_STORYCARD = 41;
    public static final int MOVE_STORYCARD_TO_NEW_PARENT = 43;
    public static final int UNDELETE_STORYCARD = 46;
    public static final int UPDATE_STORYCARD = 47;
    public static final int UPDATE_CARD = 48;
    public static final int UPDATE_LEGEND=49;
    public static final int UPDATE_OWNER=50;
    public static final int MOUSE_MOVE = 60;
    public static final int BRING_TO_FRONT = 61;
    public static final int DELETE_REMOTE_MOUSE = 62;
    public static final int KEYSTROKE = 63;
    public static final int CREATE_PROJECT = 70;
    public static final int GET_ACCEPTANCE_TEST_REQ = 80;
    public static final int GET_ACCEPTANCE_TEST_RESP = 81;
    public static final int ARRANGE_PROJECT = 82;
    public static final int SYNCH_PROJECT = 83;
	public static final int LOGIN = 84;
	public static final int LOST_CONNECTION = 85;
	public static final int REMOVE_REMOTE_MOUSE = 86;
	public static final int CONNECT_TO_RALLY = 87;
	public static final int VERSION_VERIFICATION = 88;
	
	public long getSender();
    public void setSender(long senderid);
    public int getMessageType();
    public void setMessageType(int mtpye);
    public Object getMessage();
    public void setMessage(Object message);
    public int getEtype();
    public HashMap<String, String> getData();
    public void setData(HashMap<String, String> data);
    public void setEtype(int etype);
    public void addData(String key, String value);

}
