package persister.data;

import persister.AbstractRoot;

public interface EditorElement extends AbstractRoot{

	public static final int DEFAULT_LOCATION_X = 0;
    public static final int DEFAULT_LOCATION_Y = 0;
    
    public void setWidth(int width);
    public void setLocationX(int locationX);
    public void setLocationY(int locationY);
    public void setHeight(int height);
    public int getHeight();
    public int getLocationX();
    public int getLocationY();
    public int getWidth();
    public void setRotationAngle(float angle); 
    public float getRotationAngle();
    public EditorElement clone();
    
}
