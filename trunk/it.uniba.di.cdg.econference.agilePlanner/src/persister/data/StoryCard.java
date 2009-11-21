package persister.data;

import persister.xml.converter.ConversionDefinition;

public interface StoryCard extends IndexCard {

    public static final int DEFAULT_WIDTH = 250;
    public static final int DEFAULT_HEIGHT = 125;    
    public static final int FRONT_SIDE = 0;
    public static final int RICH_TEST_SIDE = 1;
    public static final int FIT_TEST_SIDE = 2;
    public static final int PROTOTYPE_SIDE = 3;
    public static final int HANDWRITING_SIDE =4;
    public static final String DEFAULT_FITID="TestStorage.DefaultTest";
    public static final String COLOR ="";        
    
    public String getAcceptanceTestText();
    public String getAcceptanceTestUrl();      
    public String getCardOwner();
    public String getFitId();
    public String getColor();
    public int getCurrentSideUp();    
    public byte[] getHandwritingImage();
    public String [] getPrototype();    
    public void setAcceptanceTestText(String text);
    public void setAcceptanceTestUrl(String url);    
    public void setCardOwner(String cardOwner);
    public void setFitId(String fitId);
    public  void  setColor(String color);
    public boolean getRallyID();
    public void setRallyID(boolean rallyID);
    public void setCurrentSideUp(int currentSideUp);
    public void setHandwritingImage(byte[] image);   
    public void setPrototype(String[] newPrototype);

	// DOES NOT UPDATE PARENT RELATION!
    public void update(StoryCard storycard);	

	//public String getProtoypeText();
}
