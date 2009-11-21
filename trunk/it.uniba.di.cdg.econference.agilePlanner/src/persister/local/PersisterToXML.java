package persister.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static util.File.*;
import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.ProjectNotFoundException;
import persister.SynchronousPersister;
import persister.data.Backlog;
import persister.data.IndexCard;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.LegendDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.data.impl.TeamMemberDataObject;
import persister.util.FileSystemIDGenerator;
import persister.xml.converter.BacklogConverter;
import persister.xml.converter.Converter;
import persister.xml.converter.IterationConverter;
import persister.xml.converter.LegendConverter;
import persister.xml.converter.OwnerConverter;
import persister.xml.converter.ProjectConverter;
import persister.xml.converter.StoryCardConverter;

import com.thoughtworks.xstream.XStream;


public class PersisterToXML implements SynchronousPersister {

    private long maxid = 1;
    private FileIO io;
    private static Project currProject;
    protected Project project;
    
    private class ProjectXMLFileFilter  implements FilenameFilter {
        public boolean accept(java.io.File f) {
            return f.getName().endsWith(".xml");
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(".xml"));
        }
    }

    /**
     * 
     * @param localProjectDirPath
     *            path to the directory where the project files are stored
     * @param projectName
     * @throws CouldNotLoadProjectException
     * @throws RemoteException 
     * 
     */
    public PersisterToXML(String localProjectDirPath, String projectName) throws ConnectionFailedException, CouldNotLoadProjectException, RemoteException {
    	this.io = new FileIO();
    	this.setGenerator(FileSystemIDGenerator.getInstance());
        setProjectDirectory(new File(localProjectDirPath));
        getProjectDirectory().mkdir();
        assert (getProjectDirectory().isDirectory());
        setFile(new File(localProjectDirPath + File.separatorChar + projectName + ".xml"));
	   this.load(projectName);
       this.setCurrentProjectName(projectName);
       this.setLocalProjectDirPath(localProjectDirPath);
    }

    public synchronized Project createProject(String name) {
        ProjectDataObject project = new ProjectDataObject(name);
        this.getGenerator().init(1);
        project.setId(this.getGenerator().getNextID());
        this.setProject(project);
        
        
        String filePath = getProjectDirectory().getPath();
        setFile(new File(filePath+File.separatorChar + name + ".xml"));
        this.setCurrentProjectName(name);
        try {
			this.getProject().createBacklog(0, 0, 0, 0);
			//cambiare qui i nomi delle storycard x assegnare una semantica ai colori
			this.getProject().createLegend("Feature", "Bug", "Rally", "yellow1", "Spike", "pink1", "gray1", "khaki1", "peach2", "aqua3");
		} catch (ForbiddenOperationException e1) {
			util.Logger.singleton().error(e1);
		}
                
        this.save();

        return (Project) project.clone();
    }

    /***************************************************************************
     * LOAD AND SAVE *
     * @throws RemoteException 
     * 
     **************************************************************************/

    public Project load(String projectName, Timestamp start, Timestamp end) throws CouldNotLoadProjectException, RemoteException {
        return (Project) load(projectName).clone();
    }

    public Project load(String projectName)throws CouldNotLoadProjectException, RemoteException {
		this.setProject(new ProjectDataObject());
		return loadProjectFromLocalFile(projectName);
    }

	private Project loadProjectFromLocalFile(String projectName) throws CouldNotLoadProjectException {
		boolean fileMissing = false;
        this.getProject().setName(projectName);
        this.getGenerator().init(1);
        this.getProject().setId(this.getGenerator().getNextID());
//        this.start = new Timestamp(0);
//        this.end = new Timestamp(Long.MAX_VALUE);
        String filename = projectName + ".xml";
       
		setFile(new File(getProjectDirectory().getPath() + File.separatorChar + filename));
		
        if (!getFile().exists()) {
        	fileMissing = true;
            File dir = new File(getFile().getParent());

            if (!dir.exists()) {
                dir.mkdirs();
            }
            createProject(projectName);
        }

        assert (getFile().isFile());
        assert (getFile().exists());
        try {
			FileInputStream inputFile = new FileInputStream(this.getFile().getPath());
			
			//Fallback if the current project cant be read
			Project p = new ProjectDataObject();
			try{
				p = (Project)Converter.fromXML(inputFile);
			}catch (Exception e) {
				System.out.println(readAll(getFile()));
				copy(getFile(), new File(getFile().getParent() + File.separator+getFile().getName() + ".corrupt"));
				util.Logger.singleton().error(e);
			}
			this.setProject(p);
			this.getProject().setName(this.getFile().getName().substring(0,this.getFile().getName().lastIndexOf('.')));
			
			this.getGenerator().init(getMaxIDofProject(project)+1);
			try {
				inputFile.close();
			} catch (IOException e) {
				util.Logger.singleton().error(e);
			}
		} catch (FileNotFoundException e) {
			util.Logger.singleton().error(e);
		}catch (IOException e) {
			util.Logger.singleton().error(e);
		}

        this.save();
        setCurrProject(getProject());
        return (Project) this.getProject().clone();
	}


    /***************************************************************************
     * CREATE OBJECTS FROM XML FILE HELPER METHODS *
     * 
     **************************************************************************/
    public List<String> getProjectNames() {
        String[] projectNames = getProjectDirectory().list(new ProjectXMLFileFilter());
        List<String> projectNamesList = new ArrayList<String>();
        for (int i = 0; i < projectNames.length; i++) {
            projectNames[i] = projectNames[i].substring(0, projectNames[i].length() - 4);
            projectNamesList.add(projectNames[i]);
        }
        return projectNamesList;
    }

    /***************************************************************************
     * FIND HELPER METHODS *
     **************************************************************************/
    public IndexCard findCard(long id) throws IndexCardNotFoundException {
        return (IndexCard) getProject().findCard(id).clone();
    }

    public IndexCard updateCard(IndexCard indexCard) throws IndexCardNotFoundException {
        IndexCard ic = getProject().updateCard(indexCard);
        this.save();
        return (IndexCard) ic.clone();

    }
    
    public Legend updateLegend (Legend legend){
    	Legend ldo = getProject().updateLegend(legend);
    	this.save();
    	return (Legend) ldo.clone();
    }
    public TeamMember updateOwner (TeamMember teamMember){
    	TeamMember ldo = getProject().updateOwner(teamMember);
    	this.save();
    	return (TeamMember) ldo.clone();
    }
    
    public synchronized boolean save() {
        return this.saveAs(this.getFile().getPath());
    }

    public synchronized boolean saveAs(String path) {
    	return io.saveAs(path, project);
    }

    public boolean writeFile(String fileName, byte[] fileContent,int recognizeID) {
        return io.writeFile(fileName, fileContent, recognizeID);
    }

    public byte[] readFile(String filename,int recognizeID) {
    	 return io.readFile(filename, recognizeID);
    }

    public File getProjectDirectory() {
        return io.getProjectDirectory();
    }

    public String[][] getIterationNames(String projectName) {
        List<Iteration> lst = getProject().getIterationChildren();
        String[][] iterations = new String[lst.size()][2];
        int j = 0;
        for (Iteration i : lst) {
            iterations[j][0] = i.getName();
            iterations[j][1] = Long.toString(i.getId());
            j++;

        }
        return iterations;
    }

    public Project getProject() {
        return (Project) project;
    }

    public synchronized File getFile() {
        return io.getFile();
    }

    public synchronized void setFile(File file) {
        io.setFile(file);
    }

    public Backlog createBacklog(int width, int height, int locationX, int locationY) throws ForbiddenOperationException {
        Backlog backlog = getProject().createBacklog(width, height, locationX, locationY);
        this.save();
        return (Backlog) backlog.clone();
    }

    public Iteration createIteration(String name, String description, int width, int height, int locationX, int locationY, float availableEffort,
            Timestamp startDate, Timestamp endDate, float rotationAngle,boolean rallyID) {
        Iteration iteration = getProject().createIteration(name, description, width, height, locationX, locationY, availableEffort, startDate, endDate, rotationAngle,rallyID);
        this.save();
        return (Iteration) iteration.clone();
    }
    
    public TeamMember createOwner(String name) {
        TeamMember teamMember = getProject().createOwner(name);
        this.save();
        return (TeamMember) teamMember.clone();
    }
    
    public Legend createLegend(String blue, String red, String green, String yellow, String white, String pink, String gray, String khaki, String peach , String aqua ){
    	Legend legend = getProject().createLegend(blue,red,green,yellow,white,pink,gray,khaki,peach,aqua);
    	this.save();
    	return (Legend) legend.clone();
    }

    public StoryCard createStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, String cardOwner, float rotationAngle, boolean rallyID, String fitId)
            throws IndexCardNotFoundException {
        StoryCard storyCard = getProject().createStoryCard(name, description, width, height, locationX, locationY, parentid, bestCaseEstimate,
                mostlikelyEstimate, worstCaseEstimate, actualEffort, status,color, cardOwner, rotationAngle,rallyID,fitId);
        this.save();
        return (StoryCard) storyCard.clone();
    }
    
    public StoryCard createTabletPCStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color,byte[] image,boolean rallyID)
            throws IndexCardNotFoundException {
        StoryCard storyCard = getProject().createTabletPCStoryCard(name, description, width, height, locationX, locationY, parentid, bestCaseEstimate,
                mostlikelyEstimate, worstCaseEstimate, actualEffort, status,color,image,rallyID);
        this.save();
        return (StoryCard) storyCard.clone();
    }
    
    public IndexCard deleteCard(long id) throws IndexCardNotFoundException, ForbiddenOperationException {
        IndexCard deletedCard = getProject().deleteCard(id);
        this.save();
        this.deleteTabletPCCardImage(id);
        return (IndexCard) deletedCard.clone();
    }

    public StoryCard moveStoryCardToNewParent(long id, long oldparentid, long newparentid, int locationX, int locationY,float rotation)
            throws IndexCardNotFoundException {
        StoryCard movedStoryCard = getProject().moveStoryCardToNewParent(id, oldparentid, newparentid, locationX, locationY,rotation);
        this.save();
        return (StoryCard) movedStoryCard.clone();
    }

    public IndexCard undeleteCard(IndexCard indexCard) throws IndexCardNotFoundException, ForbiddenOperationException {
        IndexCard undeletedCard = getProject().undeleteCard(indexCard);
        this.save();
        return (IndexCard) undeletedCard.clone();
    }
   
    private void deleteTabletPCCardImage(long id)
    {
    	new File(getProjectDirectory().getPath()+File.separatorChar
    			+this.getCurrentProjectName()+File.separatorChar+"Handwritings"+File.separatorChar+String.valueOf(id)+".jpg").delete();
    	
    }

	public Project arrangeProject(Project project2) {
		((ProjectDataObject)project2).setGenerator(FileSystemIDGenerator.getInstance());
		this.setProject(project2);
		
		this.save();
		return (Project)getProject().clone();
	}
	
	public void setNewPersister(String projectName) throws RemoteException
	{
		Project temp = this.getProject();
		try {
			this.setProject(this.load(projectName));
		} catch (CouldNotLoadProjectException e) {
			this.setProject(temp);
			return;
		}
		this.setCurrentProjectName(projectName);
		setFile(new File(getProjectDirectory().getPath() + File.separatorChar + getProject().getName()+".xml"));
	}

	
	public Project synchronizeProject(String projName) throws CouldNotLoadProjectException {
		return null;
	}


	public boolean deleteProject(String projectName) throws ProjectNotFoundException {
        File file1 = new File(getLocalProjectDirPath() + File.separatorChar + projectName + ".xml");
        return file1.delete();
	}
	

   public boolean checkAvailable(String projectName){
	   List <String> projectNames = this.getProjectNames();
	   for (String projectname : projectNames ){
		   if(projectName.equalsIgnoreCase(projectname))
			   return true;
	   }
	   return false;
   }
   
   private long getMaxIDofProject(Project project)
   {
	    long temp = 1;
	    if(project.getId()>temp)
	    	temp = project.getId();
	    if(project.getBacklog()!=null&&project.getBacklog().getId()>temp){
	    	temp = project.getBacklog().getId();
	    	for(Iterator i =project.getBacklog().getStoryCardChildren().iterator();i.hasNext();){
	    		IndexCard child = (IndexCard)i.next();
	    		temp = (child.getId()>temp)?child.getId():temp;
	    	}
	    }
	    for(Iterator j = project.getIterationChildren().iterator(); j.hasNext();)
	    {
	    	Iteration child = (Iteration)j.next();
	    	temp = (child.getId()>temp)?child.getId():temp;
	    	for(Iterator k = child.getStoryCardChildren().iterator();k.hasNext();)
	    	{
	    		IndexCard child2 = (IndexCard)k.next();
	    		temp = (child2.getId()>temp)?child2.getId():temp;  		
	    	}  
	    }
	       return temp;
   }

	public TeamMember deleteOwner(TeamMember teamMember) {
		TeamMember deletedOwner = getProject().deleteOwner(teamMember);
	    this.save();
	    
	    return (TeamMember) deletedOwner.clone();
	}
	
	public static Project getCurrProject() {
		if(currProject == null){
			currProject.toString();
		}
		return currProject;
	}
	
	public static void setCurrProject(Project currProject) {
		PersisterToXML.currProject = currProject;
	}

	public FileSystemIDGenerator getGenerator() {
		return io.getGenerator();
	}

	public void setGenerator(FileSystemIDGenerator generator) {
		io.setGenerator(generator);
	}

	public String getCurrentProjectName() {
		return io.getCurrentProjectName();
	}

	public void setCurrentProjectName(String currentProjectName) {
		io.setCurrentProjectName(currentProjectName);
	}

	public String getLocalProjectDirPath() {
		return io.getLocalProjectDirPath();
	}

	public void setLocalProjectDirPath(String localProjectDirPath) {
		io.setLocalProjectDirPath(localProjectDirPath);
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setProjectDirectory(File projectDirectory) {
		io.setProjectDirectory(projectDirectory);
	}
}
