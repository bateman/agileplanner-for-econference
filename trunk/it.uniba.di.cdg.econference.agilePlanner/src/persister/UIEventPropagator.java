package persister;

public interface UIEventPropagator {

    /***************************************************************************
     * USERINTERACTION *
     **************************************************************************/

    public void moveMouse(String name, int x, int y) throws NotConnectedException;

    public void bringToFront(long id) throws NotConnectedException;

    public void deleteRemoteMouse(long id) throws NotConnectedException;

    /***************************************************************************
     * LISTENER *
     **************************************************************************/

    public void addPlannerUIChangeListener(PlannerUIChangeListener listener);

    public void removePlannerUIChangeListener(PlannerUIChangeListener listener);

}
