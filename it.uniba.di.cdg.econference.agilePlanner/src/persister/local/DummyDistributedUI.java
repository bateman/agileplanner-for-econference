package persister.local;

import persister.Keystroke;
import persister.NotConnectedException;
import persister.PlannerUIChangeListener;
import persister.UIEventPropagator;

/**
 * @author FM Class does nothing as it is only used when connected to the
 *         LocalPersister (i.e. we do not need to propagatte MouseEvents etc
 */
public class DummyDistributedUI implements UIEventPropagator {

    public void addPlannerUIChangeListener(PlannerUIChangeListener listener) {
    }

    public void bringToFront(long id) throws NotConnectedException {
    }

    public void deleteRemoteMouse(long id) throws NotConnectedException {
    }

    public void moveMouse(String name, int x, int y) throws NotConnectedException {
    }

    public void removePlannerUIChangeListener(PlannerUIChangeListener listener) {
    }

    public void sendKeystrokeOut(Keystroke ch) throws NotConnectedException {
    }

}
