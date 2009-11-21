/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the class for all the constant values used in MasePlanner
 *								Class is broken into sections for both the Windows OS and the Mac OS and for OS independent values								  
 ***************************************************************************************************/
package cards;

public class CardConstants {
    /** ** ** ** ** ** ** ** ** VALUES FOR ALL OS'S ** ** ** ** ** ** ** ** * */
    /** ************************************************************************ */
    /** ************************************************************************ */

    /** Branding Application Name for Dialog Boxes * */
    /** ************************************************************************ */
    public static final String APPLICATIONNAME = "AgilePlanner";

    /** Constants to define what label is being edited* */
    /** ************************************************************************ */
    public static final int STORYCARDNAMELABEL = 0;

    public static final int STORYCARDDESCRIPTIONLABEL = 1;

    public static final int STORYCARDBESTCASELABEL = 2;

    public static final int STORYCARDWORSTCASELABEL = 3;

    public static final int STORYCARDMOSTLIKELYLABEL = 4;

    public static final int STORYCARDACTUALLABEL = 5;

    public static final int STORYCARDREMAININGLABEL = 6;
    
    public static final int STORYCARDOWNERLABEL = 9;

    public static final int ITERATIONCARDNAMELABEL = 0;

    public static final int ITERATIONCARDDESCRIPTIONLABEL = 1;

    public static final int ITERATIONCARDENDDATELABEL = 2;

    public static final int ITERATIONCARDAVAILABLEEFFORT = 3;
    
    public static final int ITERATIONCARDSTARTDATELABEL = 5;

    /** Iteration Figure Line values (Y location) * */
    /** ************************************************************************ */
    public static final int ITERATIONFIGURENAMELINE = 20;

    public static final int ITERATIONFIGUREDESCRIPTIONLINE = 35;

    public static final int ITERATIONFIGUREAVAILABLEEFFORTLINE = 50;

    public static final int ITERATIONFIGUREBESTCASELINE = 65;

    public static final int ITERATIONFIGUREWORSTCASELINE = 80;

    /** Backlog Figure Line Values (Y location)* */
    /** ************************************************************************ */
    public static final int BACKLOGFIGURENAMELINE = 20;

    /** StoryCard Figure line Values (Y location) * */
    /** ************************************************************************ */
    public static final int STORYCARDFIGURENAMELINE = 20;

    public static final int STORYCARDFIGUREDESCRIPTIONLINE = 15;

    public static final int STORYCARDFIGUREBESTCASELINE = 15;

    public static final int STORYCARDFIGUREWORSTCASELINE = 15;

    public static final int STORYCARDFIGURELINEHEIGHT = 13;

    /** Icon File Locations * */
    /** ************************************************************************ */
    public static final String ArrangeIterationsByEndDateIcon = "Icons/icon_arrange_iter_story.gif";
    
    public static final String CleanUpPlanningObjectsIcon = "Icons/icon_arrange_story.gif";

    public static final String ColapseStoryCardIcon = "Icons/icon_minus.gif";
    
    public static final String ConfigureIcon = "Icons/edit_icon.gif";

    public static final java.util.Hashtable<String, String> ColorCardsIconColors = new java.util.Hashtable<String, String>() {
    	{
    		put("gray","Icons/grey.gif");
    	}
    };
    public static final String PaintBucket = "Icons/ChangeBG.gif";
    
    public static final String getColorCardsIconLocation(String color){
    	if(ColorCardsIconColors.containsKey(color)){
    		return ColorCardsIconColors.get(color);	
    	}else{
    		return "Icons/"+color+".gif";
    	}
    }
    public static final void addColorCardIconConfig(String color, String filePath){
    	ColorCardsIconColors.put(color, filePath);
    }
    
    public static final String NewDrawingArea="Icons/icon_new_drawing_area.gif";
    
    public static final String ExpandStoryCardIcon = "Icons/icon_plus.gif";
    
    public static final String FontIcon="Icons/font.gif";

    public static final String OpenPlanningDataIcon = "Icons/icon_agileplanner_open.gif";

    public static final String NewPlanningDataIcon = "Icons/icon_agileplanner_new.gif";

    public static final String NewStoryCardIcon = "Icons/icon_new_story.gif";

    public static final String NewIterationIcon = "Icons/icon_iteration_status.gif";
    
    public static final String NewTextAreaIcon = "Icons/icon_Textarea.gif";

    public static final String NewBacklogIcon = "Icons/icon_product_backlog.gif";

    public static final String IndexCardIcon = "Icons/sample.gif";
    
    public static final String HELPICON = "Icons/help.gif";

    public static final String PersisterSelectionIcon = "Icons/icon_detail_link.gif";
    public static final String PENTOOL ="Icons/icon_pen_tool.gif";
    public static final String CompleteStoryAndIterationButtonUnchecked = "Icons/icon_inProgress.gif";
    
    
    public static final String CompleteStoryAndIterationButtonChecked = "Icons/icon_complete.gif";
    public static final String TRIANGLEBUTTON="Icons/triangleFinal.gif";
    public static final String TRIANGLERED="Icons/trianglered.gif";
    public static final String TRIANGLEBLUE="Icons/triangleblue.gif";
    public static final String TRIANGLEGREEN="Icons/trianglegreen.gif";
    public static final String TRIANGLEWHITE="Icons/trianglewhite.gif";
    public static final String TRIANGLEPINK="Icons/trianglepink.gif";
    
    public static final String BAR_COMPLETED = "Icons/bar_completed.gif";
    public static final String BAR_DEFINED = "Icons/bar_defined.gif";
    public static final String BAR_ACCEPTED = "Icons/bar_accepted.gif";
    public static final String BAR_INPROGRESS = "Icons/bar_inProgress.gif";
    public static final String CompleteStoryAndIteratoinButtonDefined = "Icons/icon_defined.gif";
    
    public static final String CompleteStoryAndIteratoinButtonAccepted = "Icons/icon_accepted.gif";

    public static final String SearchIcon = "Icons/icon_find.gif";
    
    public static final String SyncIcon = "Icons/synch.gif";

    public static final String HiddenStoryCardButton = "Icons/sample.gif";

    public static final String RemoteMouseIcon = "Icons/mouse.gif";

    /** Main Dialog Button Return Values * */
    /** ************************************************************************ */
    public static final int CANCEL_ID = 0;

    public static final int CONTINUE_ID = 1;

    /** File I/O Settings * */
    /** ************************************************************************ */
    public static final String fileName = " ";

    public static final String filePath = " ";

    /** Cell Editor Type * */
    /** ************************************************************************ */
    public static final int ActualLabel = 0;

    public static final int BestCaseLabel = 1;

    public static final int DescritionLabel = 2;

    public static final int MostLikelyLabel = 3;

    public static final int NameLabel = 4;

    public static final int RemainingLabel = 5;

    public static final int WorstCaseLabel = 6;
    
    public static final int OwnerLabel = 7;
    
	public static final int STORYCARDURLLABEL = 7;
	public static final int STORYCARDACCEPTTESTLABEL = 8;
	public static final int STORYCARD_FITID_LABEL = 10;

    /** ************************************************************************ */

    /** ** ** ** ** ** ** ** ** VALUES FOR WINDOWS OS ** ** ** ** ** ** ** ** * */
    /** ************************************************************************ */
    /** ************************************************************************ */

    /** Default Iteration Dimentions!* */
    /** ************************************************************************ */
    public static final int WindowsIterationCardWidth = 400;

    public static final int WindowsIterationCardHeight = 250;

    /** Default Backlog Dimentions * */
    /** ************************************************************************ */
    public static final int WindowsBacklogWidth = 400;

    public static final int WindowsBacklogHeight = 400;

    /** Default StoryCard Dimentions * */
    /** ************************************************************************ */
    public static final int WindowsStoryCardWidth = 300;

    public static final int WindowsStoryCardheight = 150;

    public static final int WindowsStoryCardSmallHeight = 42;

    public static final int WindowsStoryCardEstimateWidth = 102;

    public static final int WindowsStoryCardEstimateTextBoxWidth =26;

    public static final int WindowsStoryCardEstimateTextBoxHeight = 13;

    /** Iteration Figure Label Start Points (X value) * */
    /** ************************************************************************ */
    public static final int WindowsIterationFigureNamePoint = 27;

    public static final int WindowsIterationFigureDescriptionPoint = 65;

    public static final int WindowsIterationFigureEndDatePoint = 255;
    
    public static final int WindowsIterationFigureStartDatePoint = 255;

    public static final int WindowsIterationFigureAvailablePoint = 85;

    public static final int WindowsIterationFigureRemainingPoint = 285;

    public static final int WindowsIterationFirstEstimateLineOffset = 51;

    public static final int WindowsIterationEstimateLineIncrementValue = 15;

    public static final int WindowsIterationEstimateLeftSideOffset = 5;

    public static final int WindowsIterationEstimateRightSideOffset = WindowsIterationCardWidth / 2;

    /** Story Card Figure Label Start Points (X Values) * */
    /** ************************************************************************ */
    public static final int WindowsFirstEstimateLineOffset = 21;

    public static final int WindowsEstimateLineIncrementValue = 15;

    public static final int WindowsEstimateLeftSideOffset = 5;

    public static final int WindowsEstimateRightSideOffset = WindowsStoryCardWidth / 2;

    public static final int WindowsEstimateLabelWidth = 30;

    public static final int WindowsEstimateLabelHeight = 42;

    public static final int WindowsEstimateLabelLeftSideOffest = 55;

    public static final int WindowsEstimateLabelRightSideOffest = WindowsStoryCardWidth / 2 + 60;
    
    public static final int WindowsFitIdLablLeftSideOffset=20;

    public static final int WindowsStoryCardNameLabelX = 5;

    public static final int WindowsStoryCardRankX = 42;

    /** ************************************************************************ */

    /** ** ** ** ** ** ** ** ** VALUES FOR MAC OS ** ** ** ** ** ** ** ** * */
    /** ************************************************************************ */
    /** ************************************************************************ */

    /** Default Iteration Dimentions!* */
    /** ************************************************************************ */
    public static final int MacIterationCardWidth = 400;

    public static final int MacIterationCardHeight = 250;

    /** Default Backlog Dimentions * */
    /** ************************************************************************ */
    public static final int MacBacklogWidth = 400;

    public static final int MacBacklogHeight = 400;

    /** Default StoryCard Dimentions * */
    /** ************************************************************************ */
    public static final int MacStoryCardWidth = 220;

    public static final int MacStoryCardheight = 138;

    public static final int MacStoryCardSmallHeight = 21;

    public static final int MacStoryCardEstimateTextBoxWidth = 32;

    public static final int MacStoryCardEstimateTextBoxHeight = 16;

    /** Iteration Figure Label Start Points (X value) * */
    /** ************************************************************************ */
    public static final int MacIterationFigureNamePoint = 37;

    public static final int MacIterationFigureDescriptionPoint = 86;

    public static final int MacIterationFigureEndDatePoint = 263;
    
    public static final int MacIterationFigureStartDatePoint = 263;

    public static final int MacIterationFigureAvailablePoint = 108;

    public static final int MacIterationFigureRemainingPoint = 311;

    public static final int MacIterationFirstEstimateLineOffset = 51;

    public static final int MacIterationEstimateLineIncrementValue = 15;

    public static final int MacIterationEstimateLeftSideOffset = 5;

    public static final int MacIterationEstimateRightSideOffset = MacIterationCardWidth / 2 + 5;

    /** Story Card Figure Label Start Points (X Values) * */
    /** ************************************************************************ */
    public static final int MacFirstEstimateLineOffset = 21;

    public static final int MacEstimateLineIncrementValue = 15;

    public static final int MacEstimateLeftSideOffset = 5;

    public static final int MacEstimateRightSideOffset = MacStoryCardWidth / 2 + 5;

    public static final int MacEstimateLabelWidth = 30;

    public static final int MacEstimateLabelHeight = 15;

    public static final int MacEstimateLabelLeftSideOffset = 80;

    public static final int MacEstimateLabelRightSideOffset = MacStoryCardWidth / 2 + 5 + 80;

    public static final int MacStoryCardNameLabelX = 15;

    public static final int MacStoryCardRankX = 50;

    /** ************************************************************************ */

    /** Update Flags to determin what value to update * */
    /** ************************************************************************ */
    public static final int LocationUpdate = 1;

    public static final int SizeUpdate = 2;

    public static final int ParentUpdate = 3;

    public static final int CompleteUpdate = 4;

    public static final int NameUpdate = 5;

    public static final int DescriptionUpdate = 6;

    public static final int BestCaseUpdate = 7;

    public static final int WorstCaseUpdate = 8;

    public static final int MostLikelyUpdate = 9;

    public static final int ActualEffortUpdate = 10;

    public static final int EndDateUpdate = 11;

    public static final int AvailableEffortUpdate = 12;

    public static final int RemoveStoryCard = 13;

    public static final int RemoveIteration = 14;

    public static final int RemoveBacklog = 15;

    public static final int RecoverStoryCard = 16;

    public static final int RecoverIteration = 17;

    public static final int UndeleteStoryCard = 18;

    /** Card Flags to determin what kind of Card * */
    /** ************************************************************************ */
    public static final int StoryCard = 0;

    public static final int BacklogCard = 1;

    public static final int IterationCard = 2;

    public static final int Project = 3;

    /** Special Flag to Indicate that the type of object is a Mouse* */
    public static final int Mouse = 99;

    /** Values for persister * */
    /** ************************************************************************ */
    public static final int LargeNum = 1000000;

    public static final int ApplicationHeightOffset = 50;
    
    /***
     * Acceptance Test Constants
     ***/
    
    public static final int WindowsUrlLabelHeight = 30;
    public static final int WindowsUrlLabelOffset = 35;
    
    public static final int MacUrlLabelHeight = 30;
    public static final int MacUrlLabelOffset = 35;

	public static final int MacUrlLabelTop = 14;

	public static final int WindowsUrlLabelTop = 14;


	
	public static final String ACCEPTANCE_TEST_EDIT_BUTTON_ICON = "Icons/EditButtonSmall.png";
	//to be changed later
	public static final String PROTOTYPE_ADD_BUTTON_ICON = "Icons/AddButtonSmall.gif";

    /***
     * FitClipse perspective ID constant
     ***/

	public static final String FitClipsePerspectiveID = "ca.ucalgary.cpsc.ebe.fitClipse.DevelopmentPerspective";

	
	
}
