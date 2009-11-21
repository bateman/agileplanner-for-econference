package persister.data;

import persister.AbstractRoot;

public interface Legend extends AbstractRoot{

	
	public String getBlue();
	public void setBlue(String blue);
	public String getRed();
	public String getColor(String color);
	public void setRed(String red);
	public String getGreen();
	public void setGreen(String green);
	public String getYellow();
	public void setYellow(String yellow);
	public String getWhite();
	public void setWhite(String white);
	public String getPink();
	public void setPink(String pink);
	public String getAqua();
	public void setAqua(String aqua);
	public String getKhaki();
	public void setKhaki(String khaki);
	public String getPeach();
	public void setPeach(String peach);
	public String getGrey();
	public void setGrey(String gray);
	public long getId();
	public String getName();
	public void setId(long id);
	public void setName(String name);
}
