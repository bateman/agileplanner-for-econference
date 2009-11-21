package persister.data.impl;

import persister.data.IndexCard;

public final class IndexCardDataObject implements IndexCard {

    private static final long serialVersionUID = -9089874433939816245L;
    
    protected long parent;
    protected String description;
    protected String status;
    protected float actualEffort;
    protected float bestCaseEstimate;
    protected float mostLikelyEstimate;
    protected float worstCaseEstimate;
    protected boolean idRecievedFromRally;
    private EditorElementDataObject editorElement = new EditorElementDataObject();
    
    public IndexCardDataObject() {
        super();
    }

    public String getDescription() {
        return this.description;
    }

    public long getParent() {
        return this.parent;

    }

    public String getStatus() {
        return this.status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(long id) {
        this.parent = id;
    }

    public boolean isCompleted() {
        return false;
    }

    public boolean isStarted() {
        return false;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IndexCardDataObject clone() {
        IndexCardDataObject clone = new IndexCardDataObject();
        clone.setParent(getParent());
        clone.setStatus(getStatus());
        clone.setDescription(getDescription());
        clone.setEditorElement(getEditorElement());
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IndexCardDataObject) {
            IndexCardDataObject iteration = (IndexCardDataObject) o;
            return (getId() == iteration.getId());
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        return (int) getId();
    }

    public float getActualEffort() {
        return this.actualEffort;

    }

    public float getBestCaseEstimate() {
        return this.bestCaseEstimate;

    }

    public float getMostlikelyEstimate() {
        return this.mostLikelyEstimate;

    }

    public float getWorstCaseEstimate() {
        return this.worstCaseEstimate;
    }

    public void setActualEffort(float actual) {
        this.actualEffort = actual;

    }

    public void setBestCaseEstimate(float bestcase) {
        this.bestCaseEstimate = bestcase;

    }

    public void setMostlikelyEstimate(float mostlikely) {
        this.mostLikelyEstimate = mostlikely;

    }

    public void setWorstCaseEstimate(float worstcase) {
        this.worstCaseEstimate = worstcase;

    }

	public boolean isIdRecievedFromRally() {
		return idRecievedFromRally;
	}

	public void setIdRecievedFromRally(boolean idRecievedFromRally) {
		this.idRecievedFromRally = idRecievedFromRally;
	}

	
	public float getRemainingEffort() {		
		 return (mostLikelyEstimate - actualEffort);
	}

	
	public float getMostLikelyEstimate() {
		return mostLikelyEstimate;
	}

	public void setMostLikelyEstimate(float mostLikelyEstimate) {
		this.mostLikelyEstimate = mostLikelyEstimate;
	}

	public int getHeight() {
		return editorElement.getHeight();
	}

	public long getId() {
		return editorElement.getId();
	}

	public int getLocationX() {
		return editorElement.getLocationX();
	}

	public int getLocationY() {
		return editorElement.getLocationY();
	}

	public String getName() {
		return editorElement.getName();
	}

	public float getRotationAngle() {
		return editorElement.getRotationAngle();
	}

	public int getWidth() {
		return editorElement.getWidth();
	}

	public void setHeight(int height) {
		editorElement.setHeight(height);
	}

	public void setId(long id) {
		editorElement.setId(id);
	}

	public void setLocationX(int locationX) {
		editorElement.setLocationX(locationX);
	}

	public void setLocationY(int locationY) {
		editorElement.setLocationY(locationY);
	}

	public void setName(String name) {
		editorElement.setName(name);
	}

	public void setRotationAngle(float rotationAngle) {
		editorElement.setRotationAngle(rotationAngle);
	}

	public void setWidth(int width) {
		editorElement.setWidth(width);
	}

	public String toString() {
		return editorElement.toString();
	}

	protected EditorElementDataObject getEditorElement() {
		return editorElement;
	}

	protected void setEditorElement(EditorElementDataObject editorElement) {
		this.editorElement = editorElement;
	}

}