/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: Abstract Root on which all other models are based on
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class AbstractRootModel implements IPropertySource, Serializable {

    /** Empty property Descriptor */
    private static final IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];

    private static final long serialVersionUID = 1;

    /** Delegate used to implement propertychange support */
    private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        pcsDelegate = new PropertyChangeSupport(this);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (pcsDelegate.hasListeners(property)) {
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener pcl) {
        if (pcl == null) {
            throw new IllegalArgumentException();
        }
        pcsDelegate.addPropertyChangeListener(pcl);
    }

    public Object getEditableValue() {
        return this;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        return EMPTY_ARRAY;
    }

    public Object getPropertyValue(Object id) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
     */
    public boolean isPropertySet(Object id) {
        return true;
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener pcl) {
        if (pcl != null) {
            pcsDelegate.removePropertyChangeListener(pcl);
        }
    }

    public void resetPropertyValue(Object id) {
        // DO NOTHING!!!
    }

    public void setPropertyValue(Object id, Object value) {
        // DO NOTHING!!!
    }
}
