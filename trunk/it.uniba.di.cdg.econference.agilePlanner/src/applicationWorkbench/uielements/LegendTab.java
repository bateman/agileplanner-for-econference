package applicationWorkbench.uielements;

import java.util.Enumeration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import persister.data.Legend;
import persister.data.Project;
import persister.data.impl.ProjectDataObject;
import sun.security.action.GetLongAction;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class LegendTab implements ModifyListener{

	private Text aqua;
	private Text blue;
	private Text green;
	private Text grey;
	private Text khaki;
	private Text peach;
	private Text pink;
	private Text red;
	private Text white;
	private Text yellow;
	private ProjectModel projectModel;
	private TabItem tab;
	
	public TabItem create(final TabFolder parent, ProjectModel pm){
		setTab(new TabItem(parent, SWT.NONE));
		setProjectModel(pm);
		getTab().setText("Legend");
		
		Composite legendTabComposite = new Composite(parent, SWT.NONE);
		GridLayout layoutItem0 = new GridLayout(1, false);
		legendTabComposite.setLayout(layoutItem0);
	
		Group grouplegendColor = new Group(legendTabComposite, SWT.NONE);
		grouplegendColor.setText("Change Legend Codes");
		grouplegendColor.setLayout(new GridLayout(1, true));
	
		GridData gridDatalegendColor = new GridData();
		gridDatalegendColor.grabExcessHorizontalSpace = true;
		gridDatalegendColor.horizontalAlignment = GridData.FILL;
		gridDatalegendColor.verticalAlignment = GridData.FILL;
		grouplegendColor.setLayoutData(gridDatalegendColor);
	
		
		red = new Text(grouplegendColor, SWT.NONE);
		red.setText(getProjectModel().getProjectDataObject().getLegend().getRed());
		
		red.setBackground(new Color(null, 255, 99, 71));
		red.setLayoutData(new GridData(140, 15));
		red.addModifyListener(this);
		
		green = new Text(grouplegendColor, SWT.NONE);
		green.setText(getProjectModel().getProjectDataObject().getLegend().getGreen());
		green.setBackground(new Color(null, 143, 188, 143));
		green.setLayoutData(new GridData(140, 15));
		green.addModifyListener(this);
		
		blue = new Text(grouplegendColor, SWT.NONE);
		blue.setText(getProjectModel().getProjectDataObject().getLegend().getBlue());
		blue.setBackground(new Color(null, 173, 216, 230));
		blue.setLayoutData(new GridData(140, 15));
		blue.addModifyListener(this);
		
		
		white = new Text(grouplegendColor, SWT.NONE);
		white.setText(getProjectModel().getProjectDataObject().getLegend().getWhite());
		white.setBackground(new Color(null, 255, 255, 255));
		white.setLayoutData(new GridData(140, 15));
		white.addModifyListener(this);
		
		yellow = new Text(grouplegendColor, SWT.NONE);
		yellow.setText(getProjectModel().getProjectDataObject().getLegend().getYellow());
		yellow.setBackground(new Color(null, 255, 246, 143));
		yellow.setLayoutData(new GridData(140, 15));
		yellow.addModifyListener(this);
	
		pink = new Text(grouplegendColor, SWT.NONE);
		pink.setText(getProjectModel().getProjectDataObject().getLegend().getPink());
		pink.setBackground(new Color(null, 255, 192, 203));
		pink.setLayoutData(new GridData(140, 15));
		pink.addModifyListener(this);
	
		grey = new Text(grouplegendColor, SWT.NONE);
		grey.setText(getProjectModel().getProjectDataObject().getLegend().getGrey());
		grey.setBackground(new Color(null, 190, 190, 190));
		grey.setLayoutData(new GridData(140, 15));
		grey.addModifyListener(this);
	
		khaki = new Text(grouplegendColor, SWT.NONE);
		khaki.setText(getProjectModel().getProjectDataObject().getLegend().getKhaki());
		khaki.setBackground(new Color(null, 189, 183, 107));
		khaki.setLayoutData(new GridData(140, 15));
		khaki.addModifyListener(this);
	
		peach = new Text(grouplegendColor, SWT.NONE);
		peach.setText(getProjectModel().getProjectDataObject().getLegend().getPeach());
		peach.setBackground(new Color(null, 255, 218, 185));
		peach.setLayoutData(new GridData(140, 15));
		peach.addModifyListener(this);
	
		aqua = new Text(grouplegendColor, SWT.NONE);
		aqua.setText(getProjectModel().getProjectDataObject().getLegend().getAqua());
		aqua.setBackground(new Color(null, 102, 205, 170));
		aqua.setLayoutData(new GridData(140, 15));
		aqua.addModifyListener(this);
	
		getTab().setControl(legendTabComposite);
		return getTab();
	}

	public void modifyText(ModifyEvent event) {
		// Remove all the listeners, so we don't enter any infinite loops

		blue.removeModifyListener(this);
		yellow.removeModifyListener(this);
		red.removeModifyListener(this);
		green.removeModifyListener(this);
		white.removeModifyListener(this);
		peach.removeModifyListener(this);
		khaki.removeModifyListener(this);
		aqua.removeModifyListener(this);
		grey.removeModifyListener(this);
		pink.removeModifyListener(this);

		// Get the widget whose text was modified
		Text text = (Text) event.widget;

		Legend legend = this.getProjectModel().getProjectDataObject().getLegend();
		Enumeration<String> colors = StoryCardModel.COLORS.keys();
		while(colors.hasMoreElements()){
			String color = colors.nextElement();
			try{
				String dialogText = ((Text)LegendTab.class.getMethod("get" + util.Support.capitalize(color), (Class [])null).invoke(this, (Object [])null)).getText();
				String legendText = (String)Legend.class.getMethod("get" + util.Support.capitalize(color), (Class [])null).invoke(legend, (Object [])null);
				if(!dialogText.equals(legendText)){
					Legend.class.getMethod(
						"set" + util.Support.capitalize(color), String.class
					).invoke(
						legend,
						dialogText
					);
				}
			}catch (Exception e) {
				util.Logger.singleton().error(e);
			}
		}
		blue.addModifyListener(this);
		yellow.addModifyListener(this);
		red.addModifyListener(this);
		green.addModifyListener(this);
		white.addModifyListener(this);
		peach.addModifyListener(this);
		khaki.addModifyListener(this);
		aqua.addModifyListener(this);
		grey.addModifyListener(this);
		pink.addModifyListener(this);

	}
	public Text getAqua() {
		return aqua;
	}

	public Text getBlue() {
		return blue;
	}

	public Text getGreen() {
		return green;
	}

	public Text getGrey() {
		return grey;
	}

	public Text getKhaki() {
		return khaki;
	}

	public Text getPeach() {
		return peach;
	}

	public Text getPink() {
		return pink;
	}

	public Text getRed() {
		return red;
	}

	public Text getWhite() {
		return white;
	}

	public Text getYellow() {
		return yellow;
	}

	public void setAqua(Text aqua) {
		this.aqua = aqua;
	}

	public void setBlue(Text blue) {
		this.blue = blue;
	}

	public void setGreen(Text green) {
		this.green = green;
	}

	public void setGrey(Text grey) {
		this.grey = grey;
	}

	public void setKhaki(Text khaki) {
		this.khaki = khaki;
	}

	public void setPeach(Text peach) {
		this.peach = peach;
	}

	public void setPink(Text pink) {
		this.pink = pink;
	}

	public void setRed(Text red) {
		this.red = red;
	}

	public void setWhite(Text white) {
		this.white = white;
	}

	public void setYellow(Text yellow) {
		this.yellow = yellow;
	}

	public ProjectModel getProjectModel() {
		return projectModel;
	}

	public void setProjectModel(ProjectModel projectModel) {
		this.projectModel = projectModel;
	}

	public TabItem getTab() {
		return tab;
	}

	public void setTab(TabItem tab) {
		this.tab = tab;
	}
	
	
}
