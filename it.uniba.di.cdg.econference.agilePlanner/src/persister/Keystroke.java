package persister;

public interface Keystroke extends Event {
    public void setKeystroke(char c);
    public char getKeystroke();
    public boolean isSendKeyOut();
    public void setSendKeyOut(boolean sendKeyOut);
}
