/**
 * 
 */
package persister.distributed;

import persister.Keystroke;
import persister.NotConnectedException;
import persister.PlannerUIChangeListener;
import persister.UIEventPropagator;

/**
 * @author dhillonh
 * 
 */
public class UIEventPropagatorImplenter implements UIEventPropagator {

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
