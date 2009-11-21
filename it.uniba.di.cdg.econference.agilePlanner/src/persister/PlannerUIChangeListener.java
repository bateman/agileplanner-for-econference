package persister;

import java.util.EventListener;

import persister.data.impl.DisconnectDataObject;



public interface PlannerUIChangeListener extends EventListener {

    public void broughtToFront(long id);
    public void movedMouse(Event mm);
    public void disconnectMouse(DisconnectDataObject data);
    public void liveTextUpdate(IndexCardLiveUpdate data);
}
