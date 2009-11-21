package cards.celleditorlocator.listener;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;

import persister.IndexCardLiveUpdate;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.StoryCardModel;

public class StoryCardLiveTextEditorListener implements ICellEditorListener {

    private StoryCardModel storyCard;

    private final CellEditor cell;

    private int cellType;

    private int updateCounter = 0;

    public StoryCardLiveTextEditorListener(StoryCardModel model, CellEditor cellEditor, int type) {
        this.storyCard = model;
        this.cell = cellEditor;
        this.cellType = type;
       
    }

    public void applyEditorValue() {
        // DO Nothing here!
    }

    public void cancelEditor() {
        // Do Nothing here!
    }

    public void editorValueChanged(boolean oldValidState, boolean newValidState) {
        /***********************************************************************
         * This counter makes it so that only every 5th update is sent. this is
         * done for 2 reasons: 1) we didn't need to have every key strok sent as
         * it updates the entire field. 2) every key strok triggers this method
         * a min of 3 times.
         **********************************************************************/
        if (newValidState) {
            // if(updateCounter ==3){
            liveTextUpdate();
            // updateCounter = 0;
            // }
            // updateCounter++;
        }
    }

    /***************************************************************************
     * Only does live editing for the name and the description because of the
     * imposed delay it does not make sense to listen to the estimate values.
     **************************************************************************/
    private void liveTextUpdate() {
        
        IndexCardLiveUpdate data = new IndexCardLiveUpdate();

        switch (cellType) {
        case CardConstants.STORYCARDNAMELABEL:// NameLabel:
            data.setId(storyCard.getStoryCard().getId());
            data.setField(IndexCardLiveUpdate.FIELD_TITLE);
            data.setText((String) cell.getValue());
            PersisterFactory.getPersister().sendLiveTextUpdate(data);
            break;
        case CardConstants.STORYCARDDESCRIPTIONLABEL:// DescritionLabel:
           
            data.setId(storyCard.getStoryCard().getId());
            data.setField(IndexCardLiveUpdate.FIELD_DESCRIPTION);
            data.setText((String) cell.getValue());
            
            PersisterFactory.getPersister().sendLiveTextUpdate(data);
            break;
            
        case CardConstants.STORYCARDACCEPTTESTLABEL:
        	data.setId(storyCard.getStoryCard().getId());
        	data.setField(IndexCardLiveUpdate.FIELD_TESTTEXT);
        	data.setText((String) cell.getValue());
        	PersisterFactory.getPersister().sendLiveTextUpdate(data);
        	break;
        }
       
    }
}
