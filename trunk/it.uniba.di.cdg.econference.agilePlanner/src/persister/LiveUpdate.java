package persister;

public interface LiveUpdate {
	public void setId(long id);
	public void setField(int field);
	public void setText(String text);
	public long getId();
	public int getField();
	public String getText();
}
