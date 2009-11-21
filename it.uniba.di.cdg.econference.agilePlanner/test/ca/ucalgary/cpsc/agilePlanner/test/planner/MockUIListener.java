package ca.ucalgary.cpsc.agilePlanner.test.planner;

import persister.IndexCardLiveUpdate;
import persister.Event;
import persister.PlannerUIChangeListener;
import persister.data.impl.DisconnectDataObject;

public class MockUIListener implements PlannerUIChangeListener {

    public boolean didMouseMoveGetFired = false;

    public boolean didLiveTextGetFired = false;

    public void broughtToFront(long id) {
    }

    public void movedMouse(Event mm) {
        this.didMouseMoveGetFired = true;
    }

    public void disconnectMouse(DisconnectDataObject data) {
    }

    public void liveTextUpdate(IndexCardLiveUpdate data) {
        this.didLiveTextGetFired = true;
    }

}
