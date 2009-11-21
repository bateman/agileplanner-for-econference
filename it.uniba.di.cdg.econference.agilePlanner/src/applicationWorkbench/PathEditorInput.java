package applicationWorkbench;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

public class PathEditorInput implements IPathEditorInput {

    private IPath fPath;

    public PathEditorInput(IPath path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        this.fPath = path;
    }

    /** ***************************************************** */

    public IPath getPath() {
        return fPath;
    }

    public boolean exists() {
        return fPath.toFile().exists();
    }

    public ImageDescriptor getImageDescriptor() {
        return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(fPath.toString());
    }

    public String getName() {
        return fPath.toString();
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return fPath.makeRelative().toString();
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    /** ***************************************************** */

    public int hashCode() {
        return fPath.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PathEditorInput))
            return false;
        PathEditorInput other = (PathEditorInput) obj;
        return fPath.equals(other.fPath);
    }
}
