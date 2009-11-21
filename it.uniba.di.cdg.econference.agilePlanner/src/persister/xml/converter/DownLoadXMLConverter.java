package persister.xml.converter;

import java.util.Hashtable;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DownLoadXMLConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
      String[] downloadedFile = (String[])value;
      writer.startNode("DownloadFile");
      writer.addAttribute("path", downloadedFile[0]);
      writer.addAttribute("recognitionID", downloadedFile[1]);
      writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String[] downloadedFile = new String[2];
		downloadedFile[0] = reader.getAttribute("path");
		downloadedFile[1] = reader.getAttribute("recognitionID");
		return downloadedFile;
	}

	public boolean canConvert(Class arg0) {
		return arg0 == String[].class;
	}

}
