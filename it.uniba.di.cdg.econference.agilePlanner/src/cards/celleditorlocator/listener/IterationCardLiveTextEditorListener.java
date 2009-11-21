package cards.celleditorlocator.listener;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;

import persister.IndexCardLiveUpdate;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.IterationCardModel;

public class IterationCardLiveTextEditorListener implements ICellEditorListener {

    private CellEditor cell;

    private int cellType;

    private IterationCardModel iterationCard;

    public IterationCardLiveTextEditorListener(IterationCardModel model, CellEditor cellEditor, int type) {
        this.iterationCard = model;
        this.cell = cellEditor;
        this.cellType = type;
    }

    public void applyEditorValue() {
    }

    public void cancelEditor() {
    }

    public void editorValueChanged(boolean oldValidState, boolean newValidState) {
        /***********************************************************************
         * This counter makes it so that only every 5th update is sent. this is
         * done for 2 reasons: 1) we didn't need to have every key strok sent as
         * it updates the entier field. 2) every key strok triggers this method
         * a min of 3 times.
         **********************************************************************/
        if (newValidState) {
            liveTextUpdate();
        }
    }

    /***************************************************************************
     * Only does live editing for the name and the description because of the
     * imposed delay it does not make sense to listen to the estimate values.
     **************************************************************************/
    private void liveTextUpdate() {
       
        IndexCardLiveUpdate data = new IndexCardLiveUpdate();

        switch (cellType) {
        case CardConstants.ITERATIONCARDNAMELABEL:// NameLabel:
            data.setId(iterationCard.getIterationDataObject().getId());
            data.setField(IndexCardLiveUpdate.FIELD_TITLE);
            data.setText((String) cell.getValue());
            PersisterFactory.getPersister().sendLiveTextUpdate(data);
            // PersisterFactory.getPersister().updateStoryCard(storyCard.getStoryCard());
            break;
        case CardConstants.ITERATIONCARDDESCRIPTIONLABEL:// DescritionLabel:
            
            data.setId(iterationCard.getIterationDataObject().getId());
            data.setField(IndexCardLiveUpdate.FIELD_DESCRIPTION);
            data.setText((String) cell.getValue());
            
            PersisterFactory.getPersister().sendLiveTextUpdate(data);
            break;
        }
    }

}
