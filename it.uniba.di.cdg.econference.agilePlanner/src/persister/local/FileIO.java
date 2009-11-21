package persister.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static util.File.*;

import javax.xml.transform.TransformerFactoryConfigurationError;

import com.thoughtworks.xstream.XStream;

import persister.data.Project;
import persister.util.FileSystemIDGenerator;
import persister.xml.converter.Converter;

public class FileIO {
	private File file;
	private File projectDirectory;
	private String localProjectDirPath;
	private String currentProjectName;
	private FileSystemIDGenerator generator;
    
	public boolean saveAs(String path, Project project){
		try {
            FileOutputStream out = new FileOutputStream(path);
            out.flush();
            Converter.toXML(project, out);
            out.close();
            return true;
        }catch (IOException e) {
            util.Logger.singleton().error(e);
            return false;
        }
	}
	
	public boolean writeFile(String fileName, byte[] fileContent,int recognizeID) {
        try {
            if(recognizeID == 0){
	           	FileOutputStream e = new FileOutputStream(getProjectDirectory().getPath() + File.separatorChar + fileName);
	        	e.write(fileContent);
	        	e.flush();
	        	e.close();
	            return true;
            } else {
				File uploadedFileForCard = new File(getProjectDirectory().getPath() + File.separatorChar + this.getCurrentProjectName()+File.separatorChar+"Uploaded_Files"+File.separatorChar+String.valueOf(recognizeID));
				if (!uploadedFileForCard.exists()) {
					uploadedFileForCard.mkdirs();
				}
	           	FileOutputStream e = new FileOutputStream(uploadedFileForCard.getPath()+File.separatorChar+fileName);
	           	e.write(fileContent);
	           	e.flush();
	           	e.close();
	           	return true;
            }
        }catch (IOException e) {
           
            util.Logger.singleton().error(e);
            return false;
        }
    }

    public byte[] readFile(String filename,int recognizeID) {
    	 try {
			if(recognizeID == 0){
				FileInputStream readFile = new FileInputStream(getProjectDirectory().getPath() + File.separatorChar + filename);
				byte[] originalSpace = new byte[readFile.available()];
				readFile.read(originalSpace);
				return originalSpace;
			}else{
				FileInputStream readFile = new FileInputStream(getProjectDirectory().getPath()+ File.separatorChar + this.getCurrentProjectName()+File.separatorChar+"Uploaded_Files"+File.separatorChar+String.valueOf(recognizeID)+File.separatorChar+filename);
				byte[] originalSpace = new byte[readFile.available()];
				readFile.read(originalSpace);
				return originalSpace;
			} 
        }catch (IOException e) {
        	util.Logger.singleton().error(e);
        }
        return null;
    }
	
	public synchronized File getFile(){
		return file;
	}
	
	public File getProjectDirectory(){
		return projectDirectory;
	}
	
	public void setProjectDirectory(File file){
		projectDirectory = file;
	}
	
	public synchronized void setFile(File file){
		this.file = file;
	}
	
	public void setLocalProjectDirPath(String str){
		localProjectDirPath = str;
	}
	
	public String getLocalProjectDirPath(){
		return localProjectDirPath;
	}

	public String getCurrentProjectName() {
		return currentProjectName;
	}

	public void setCurrentProjectName(String currentProjectName) {
		this.currentProjectName = currentProjectName;
	}

	public FileSystemIDGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(FileSystemIDGenerator generator) {
		this.generator = generator;
	}
}
