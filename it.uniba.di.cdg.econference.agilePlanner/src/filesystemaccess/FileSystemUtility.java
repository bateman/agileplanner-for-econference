
package filesystemaccess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import persister.data.Iteration;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.impl.BacklogDataObject;
import cards.model.ProjectModel;

public class FileSystemUtility {

	private static FileSystemUtility utility;

	private boolean usingFileSystem = true;
	public File savedFile;

	Project project;

	//    private boolean isNewFile;

	private String[] extensions = { "*.xml", "*.XML" };


	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static synchronized FileSystemUtility getFileSystemUtility() {
		if (utility == null) {
			utility = new FileSystemUtility();
		}

		return utility;
	}

	/**
	 * Getters & Setters
	 * **********************************************************************************
	 */
	/** ************************************************************************************************ */
	public void useFileSystem(boolean useFileSystem) {
		this.usingFileSystem = useFileSystem;
	}

	public boolean getUseFileSystem() {
		return this.usingFileSystem;
	}

	//    public void setnewFile(boolean newFile) {
	//        this.isNewFile = newFile;
	//    }
	//
	//    public boolean getnewFile() {
	//        return this.isNewFile;
	//    }



	/** SAVE******************************************************************************************** */
	/** ************************************************************************************************ */
	public void saveFile(ProjectModel projectModel, File file) {

		this.project = projectModel.getProjectDataObject();


		saveAs(file);

	}

	public boolean saveFileAs(ProjectModel projectModel) {

		String path = null;
		String fileName;

		this.project = projectModel.getProjectDataObject();

		FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE);      
		dialog.setFilterPath("/");
		dialog.setFilterExtensions(this.extensions);

		if (dialog.open() != null) {
			path = dialog.getFilterPath();

			fileName = dialog.getFileName();
			String compleFileName =  path+File.separator + fileName;
			getFile(compleFileName);
			return true;
		}
		return false;
	}

	private void getFile(String completeFilename) {
		try {
			File file = new File(completeFilename);
			if (file.exists()) {
				MessageBox messageBox = new MessageBox(new Shell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
				messageBox.setMessage(" Already exists, \n Do you want to replace it?");
				messageBox.setText("Save As");
				if (messageBox.open() != SWT.OK) {
					return;
				}
				else {
					file.delete();
					saveAs(file);                    
				}
			}
			else {
				saveAs(file);                
			}
		}
		catch (Exception e) {
			util.Logger.singleton().error(e);
		}

	}

	public synchronized boolean saveAs(File file) {

		try {
			savedFile=file;
			Transformer xformer;

			xformer = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(file);
			result.setSystemId(result.getSystemId().replaceAll("%20"," "));

			Document doc = buildDOMToFile();
			DOMSource src = new DOMSource(doc);            

			
			xformer.transform(src, result);

			return true;
		}
		catch (TransformerConfigurationException e) {
			util.Logger.singleton().error(e);
			return false;
		}
		catch (TransformerException e) {
			util.Logger.singleton().error(e);
			return false;
		}
		catch (TransformerFactoryConfigurationError e) {
			util.Logger.singleton().error(e.getException());
			return false;
		}

	}


	//modified
	public static void createEmptyFile(){
		try {
			
			File f=new File("EmptyProject.xml");
			if (f.exists()) f.delete();

			FileWriter empty=new FileWriter("EmptyProject.xml");
			empty.write("<Project ID=\"1\" Name=\"project\">"+
					"<Backlog Height=\"0\" Width=\"0\" ID=\"2\" Name=\"Project Backlog\" Parent=\"1\" XLocation=\"0\" YLocation=\"0\"/>"+
					"<Legend aqua=\"aqua3\" blue=\"Feature\" gray=\"gray1\" green=\"Rally\" khaki=\"khaki1\" peach=\"peach2\" pink=\"pink1\" red=\"Bug\" white=\"User Story\" yellow=\"yellow1\"/>"
					+"</Project>");
			empty.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private Document buildDOMToFile() {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			Element root = document.createElement("Project");
			root.setAttribute("ID", String.valueOf(this.project.getId()));
			root.setAttribute("Name", this.project.getName());
			document.appendChild(root);

			this.createIterationElements(document, root);
			this.createBacklogElements(document, root);

		}
		catch (Exception e) {
			util.Logger.singleton().error(e);
		}
		return document;
	}

	/***************************************************************************
	 * WRITE TO XML FILE HELPER METHODS *
	 **************************************************************************/
	private void createBacklogElements(Document document, Element parent) {
		BacklogDataObject backlog = (BacklogDataObject) this.project.getBacklog();

		if (backlog != null) {

			Element element = document.createElement("Backlog");

			element.setAttribute("ID", String.valueOf(backlog.getId()));
			element.setAttribute("Parent", String.valueOf(backlog.getParent()));

			element.setAttribute("Name", backlog.getName());

			element.setAttribute("XLocation", String.valueOf(backlog.getLocationX()));
			element.setAttribute("YLocation", String.valueOf(backlog.getLocationY()));

			element.setAttribute("Width", String.valueOf(backlog.getWidth()));
			element.setAttribute("Height", String.valueOf(backlog.getHeight()));

			parent.appendChild(element);

			if (backlog.getStoryCardChildren().size() != 0) {
				createStoryCardElement(document, element, backlog.getStoryCardChildren());
			}
		}
	}

	private void createIterationElements(Document document, Element parent) {

		List<Iteration> iterations = this.project.getIterationChildren();

		if (iterations != null) {

			for (Iteration iteration : iterations) {
				Element element = document.createElement("Iteration");

				element.setAttribute("ID", String.valueOf(iteration.getId()));
				element.setAttribute("Parent", String.valueOf(iteration.getParent()));

				element.setAttribute("Name", iteration.getName());
				element.setAttribute("Description", iteration.getDescription());

				element.setAttribute("XLocation", String.valueOf(iteration.getLocationX()));
				element.setAttribute("YLocation", String.valueOf(iteration.getLocationY()));

				element.setAttribute("Width", String.valueOf(iteration.getWidth()));
				element.setAttribute("Height", String.valueOf(iteration.getHeight()));

				element.setAttribute("EndDate", iteration.getEndDate().toString());
				element.setAttribute("StartDate", iteration.getStartDate().toString());

				element.setAttribute("AvailableEffort", String.valueOf(iteration.getAvailableEffort()));

				element.setAttribute("Status", String.valueOf(iteration.getStatus()));
				parent.appendChild(element);

				if (iteration.getStoryCardChildren().size() != 0) {
					createStoryCardElement(document, element, iteration.getStoryCardChildren());
				}
			}
		}
	}

	private void createStoryCardElement(Document document, Element parent, List<StoryCard> children) {
		for (StoryCard card : children) {
			Element element = document.createElement("StoryCard");

			element.setAttribute("ID", String.valueOf(card.getId()));
			element.setAttribute("Parent", String.valueOf(card.getParent()));

			element.setAttribute("Name", card.getName());
			element.setAttribute("Description", card.getDescription());

			element.setAttribute("XLocation", String.valueOf(card.getLocationX()));
			element.setAttribute("YLocation", String.valueOf(card.getLocationY()));

			element.setAttribute("Width", String.valueOf(card.getWidth()));
			element.setAttribute("Height", String.valueOf(card.getHeight()));

			element.setAttribute("BestCase", String.valueOf(card.getBestCaseEstimate()));
			element.setAttribute("MostLikely", String.valueOf(card.getMostlikelyEstimate()));
			element.setAttribute("WorstCase", String.valueOf(card.getWorstCaseEstimate()));
			element.setAttribute("Actual", String.valueOf(card.getActualEffort()));

			element.setAttribute("Status", String.valueOf(card.getStatus()));

			element.setAttribute("RotationAngle", String.valueOf(card.getRotationAngle()));


			//add the acceptance test url to the xml file.
			element.setAttribute("TestURL", card.getAcceptanceTestUrl());
			element.setAttribute("CurrentSideUp", "" +card.getCurrentSideUp());
			element.setAttribute("TestText", card.getAcceptanceTestText());


			element.setAttribute("Color", String.valueOf(card.getColor()));
			element.setAttribute("CardOwner", String.valueOf(card.getCardOwner()));

			parent.appendChild(element);
		}
	}
}
