package persister.data.impl;

public class EditorElementDataObject {

	protected long id;
    protected String name;
    protected int width;
    protected int height;
    protected int locationX;
    protected int locationY;
    protected float rotationAngle;

    public EditorElementDataObject clone(){
    	EditorElementDataObject clone = new EditorElementDataObject();
    	clone.setId(getId());
        clone.setHeight(getHeight());
        clone.setWidth(getWidth());
        clone.setLocationX(getLocationX());
        clone.setLocationY(getLocationY());
        clone.setName(getName());
        return clone;
    }
    
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getLocationX() {
		return locationX;
	}
	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}
	public int getLocationY() {
		return locationY;
	}
	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
	public float getRotationAngle() {
		return rotationAngle;
	}
	public void setRotationAngle(float rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

}
