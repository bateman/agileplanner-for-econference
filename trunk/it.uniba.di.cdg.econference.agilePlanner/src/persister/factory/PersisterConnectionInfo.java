//package persister.factory;
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.TransformerFactoryConfigurationError;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import org.eclipse.core.runtime.FileLocator;
//import org.eclipse.core.runtime.Platform;
//import org.w3c.dom.DOMException;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import applicationWorkbench.Activator;
//
//import fitintegration.PluginInformation;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//public class PersisterConnectionInfo {
//	private static PersisterConnectionInfo pConnectionInfo;
//	private static String strConfigFile ;
//	private static String pluginID;
//	
//	private static Document buildXMLDocument() {
//		Document document = null;
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			document = builder.newDocument();
//
//			Element root = document.createElement("Config");
//			document.appendChild(root);
//
//			root.appendChild(makeElementWithValue(document, "persisterType", pConnectionInfo.getPersisterType(), "LOCAL"));
//			root.appendChild(makeElementWithValue(document, "mouseMessageOut", pConnectionInfo.isMouseMessageOut(), "true"));
//			root.appendChild(makeElementWithValue(document, "initialLocal", pConnectionInfo.getInitialLocal()));
//			root.appendChild(makeElementWithValue(document, "initialDis", pConnectionInfo.getInitialDis()));
//			root.appendChild(makeElementWithValue(document, "projectName", pConnectionInfo.getProjectName(), "ProjectFile"));
//			root.appendChild(makeElementWithValue(document, "estimate_bestCase", pConnectionInfo.isEstimate_bestCase()));
//			root.appendChild(makeElementWithValue(document, "estimate_worstCase", pConnectionInfo.isEstimate_worstCase()));
//			root.appendChild(makeElementWithValue(document, "estimate_actual", pConnectionInfo.isEstimate_actual(), "true"));
//			root.appendChild(makeElementWithValue(document, "estimat_remaining", pConnectionInfo.isEstimate_remaining(), "true"));
//			root.appendChild(makeElementWithValue(document, "description", pConnectionInfo.isDescription()));
//			root.appendChild(makeElementWithValue(document, "rotating_mode", pConnectionInfo.isRotating_Mode()));
//			root.appendChild(makeElementWithValue(document, "nonRotating_mode", pConnectionInfo.isNonRotating_Mode(), "true"));
//			root.appendChild(makeElementWithValue(document, "state", pConnectionInfo.getState()));
//			root.appendChild(makeElementWithValue(document, "beginTime", pConnectionInfo.getBeginTime()));
//			root.appendChild(makeElementWithValue(document, "endTime", pConnectionInfo.getEndTime()));
//			root.appendChild(makeElementWithValue(document, "projectLocationLocalMode", pConnectionInfo.getProjectLocationLocalMode(), "C:\\workspace\\AgilePlanner_v2.4\\ProjectDirectory"));
//			root.appendChild(makeElementWithValue(document, "projectdistributedMode", pConnectionInfo.getProjectLocationDistributedMode()));
//			root.appendChild(makeElementWithValue(document, "url", pConnectionInfo.getUrl()));
//			root.appendChild(makeElementWithValue(document, "port", pConnectionInfo.getPort()));
//			root.appendChild(makeElementWithValue(document, "user", pConnectionInfo.getUser()));
//			root.appendChild(makeElementWithValue(document, "pass", pConnectionInfo.getPass()));
//			
//		} catch (Exception e) {
//			util.Logger.singleton().error(e);
//		}
//		
//		return document;
//	}
//	
//	private static Element makeElementWithValue(Document document, String element, Object value){
//		Element e = document.createElement(element);
//		e.setAttribute("value", String.valueOf(value));
//		return e;
//	}
//
//	private static Element makeElementWithValue(Document document, String element, Object value, Object default_value){
//		Element e = document.createElement(element);
//		if(pConnectionInfo.getPersisterType() == null){
//			e.setAttribute("value", String.valueOf(default_value));
//		}else{
//			e.setAttribute("value", String.valueOf(value));
//		}
//		return e;
//	}
//
//	private String password;
//
//	private String user;
//
//	public String getPass() {
//
//		return password;
//	}
//
//	public String getUser() {
//
//		return user;
//	}
//
//	public static PersisterConnectionInfo getPersisterConnectionInfo() {
//		
//		
//		if (pConnectionInfo == null) {
//			pConnectionInfo = new PersisterConnectionInfo();
//
//				try {
//					strConfigFile = FileLocator.toFileURL(
//							Platform.getBundle(PluginInformation.getPLUGIN_ID()).getEntry(
//									"config.xml")).getFile().toString();
//					util.Logger.singleton().debug(strConfigFile);
//				} catch (IOException e1) {
//					
//					util.Logger.singleton().error(e1);
//				}
//			DocumentBuilderFactory factory = DocumentBuilderFactory
//					.newInstance();
//			DocumentBuilder loader;
//			try {
//				loader = factory.newDocumentBuilder();
//				Document document;
//				document = loader.parse(strConfigFile);
//				NodeList persisterTypeElements;
//
//				updateConnectionForString(document, "persisterType","PersisterType", "Default UserName");
//				updateConnectionForBoolean(document, "mouseMessageOut", "MouseMessageOut", "false");
//				updateConnectionForString(document, "projectName", "ProjectName", "Untitled Project");
//				
//				persisterTypeElements = document
//						.getElementsByTagName("initialLocal");
//				for (int i = 0; i < persisterTypeElements.getLength(); i++) {
//					Node node = persisterTypeElements.item(i);
//					NamedNodeMap attributes = node.getAttributes();
//					String initialLocal = attributes.getNamedItem("value")
//							.getNodeValue().toString();
//					if (initialLocal.equals(""))
//						initialLocal = "EBE";
//					if (initialLocal.length() > 3)
//						initialLocal = initialLocal.substring(0, 3);
//					pConnectionInfo.setInitialLocal(initialLocal);
//				}
//				persisterTypeElements = document
//						.getElementsByTagName("initialDis");
//				for (int i = 0; i < persisterTypeElements.getLength(); i++) {
//					Node node = persisterTypeElements.item(i);
//					NamedNodeMap attributes = node.getAttributes();
//					String initialDis = attributes.getNamedItem("value")
//							.getNodeValue().toString();
//					if (initialDis.equals(""))
//						initialDis = "EBE";
//					if (initialDis.length() > 3)
//						initialDis = initialDis.substring(0, 3);
//					pConnectionInfo.setInitialDis(initialDis);
//				}
//				updateConnectionForBool(document, "estimate_bestCase",					"Estimate_bestCase");
//				updateConnectionForBool(document, "estimate_worstCase",					"Estimate_worstCase");
//				updateConnectionForBool(document, "estimate_actual",					"Estimate_actual");
//				updateConnectionForBool(document, "estimate_remaining",					"Estimate_remaining");
//				updateConnectionForBool(document, "description",						"Description");
//				updateConnectionForBool(document, "rotating_mode",						"Rotating_Mode");
//				updateConnectionForBool(document, "nonRotating_mode",					"NonRotating_Mode");
//				updateConnectionForString(document, "state",							"State");
//				updateConnectionForString(document, "beginTime",						"BeginTime");
//				updateConnectionForString(document, "endTime",							"EndTime");
//				updateConnectionForString(document, "projectLocationLocalMode",			"ProjectLocationLocalMode",			"C:\\");
//				updateConnectionForString(document, "projectLocationDistributedMode",	"ProjectLocationDistributedMode",	"ProjectFile");
//				updateConnectionForString(document, "url",								"Url",								"localhost");
//				updateConnectionForString(document, "port",								"Port",								"5000");
//				updateConnectionForString(document, "user",								"User",								"maurer@cpsc.ucalgary.ca");
//				updateConnectionForString(document, "pass",								"Pass",								"p@ssw0rd");
//				
//				return pConnectionInfo;
//			} catch (SAXException e) {
//				util.Logger.singleton().error(e);
//			} catch (IOException e) {
//				util.Logger.singleton().error(e);
//			} catch (ParserConfigurationException e) {
//				util.Logger.singleton().error(e);
//			}
//		}
//		return pConnectionInfo;
//
//	}
//	
//	private static void updateConnectionForBoolean(Document document, String elementTagName,String property, String defaultValue){
//		NodeList persisterTypeElements = document.getElementsByTagName(elementTagName);
//		for (int i = 0; i < persisterTypeElements.getLength(); i++) {
//			Node node = persisterTypeElements.item(i);
//			NamedNodeMap attributes = node.getAttributes();
//			try {
//				Method method = PersisterConnectionInfo.class.getMethod("set"+property, Boolean.class);
//				method.invoke(pConnectionInfo, Boolean.valueOf(
//						attributes.getNamedItem("value").getNodeValue().toString().equals("") 
//						? defaultValue
//						: attributes.getNamedItem("value").getNodeValue()
//					)
//				);
//			} catch (SecurityException e) {
//				util.Logger.singleton().error(e);
//			} catch (DOMException e) {
//				util.Logger.singleton().error(e);
//			} catch (NoSuchMethodException e) {
//				util.Logger.singleton().error(e);
//			} catch (IllegalAccessException e){
//				util.Logger.singleton().error(e);
//			} catch (IllegalArgumentException e){
//				util.Logger.singleton().error(e);
//			} catch (InvocationTargetException e){
//				util.Logger.singleton().error(e);
//			}
//		}
//	}
//	
//	private static void updateConnectionForBool(Document document, String elementTagName,String property){
//		NodeList persisterTypeElements = document.getElementsByTagName(elementTagName);
//		for (int i = 0; i < persisterTypeElements.getLength(); i++) {
//			Node node = persisterTypeElements.item(i);
//			NamedNodeMap attributes = node.getAttributes();
//			try {
//				Method method = PersisterConnectionInfo.class.getMethod("set"+property, Boolean.class);
//				method.invoke(pConnectionInfo, Boolean.valueOf(
//						attributes.getNamedItem("value").getNodeValue().toString().equals("true") 
//						? true
//						: false
//					)
//				);
//			} catch (SecurityException e) {
//				util.Logger.singleton().error(e);
//			} catch (DOMException e) {
//				util.Logger.singleton().error(e);
//			} catch (NoSuchMethodException e) {
//				util.Logger.singleton().error(e);
//			} catch (IllegalAccessException e){
//				util.Logger.singleton().error(e);
//			} catch (IllegalArgumentException e){
//				util.Logger.singleton().error(e);
//			} catch (InvocationTargetException e){
//				util.Logger.singleton().error(e);
//			}
//		}
//	}
//	
//	private static void updateConnectionForString(Document document, String elementTagName,String property, String defaultValue){
//		NodeList persisterTypeElements = document.getElementsByTagName(elementTagName);
//		for (int i = 0; i < persisterTypeElements.getLength(); i++) {
//			Node node = persisterTypeElements.item(i);
//			NamedNodeMap attributes = node.getAttributes();
//			try {
//				Method method = PersisterConnectionInfo.class.getMethod("set"+property, String.class);
//				method.invoke(pConnectionInfo, 
//					attributes.getNamedItem("value").getNodeValue().toString().equals("") 
//					? defaultValue
//					: attributes.getNamedItem("value").getNodeValue()
//				);
//			} catch (SecurityException e) {
//				util.Logger.singleton().error(e);
//			} catch (DOMException e) {
//				util.Logger.singleton().error(e);
//			} catch (NoSuchMethodException e) {
//				util.Logger.singleton().error(e);
//			} catch (IllegalAccessException e){
//				util.Logger.singleton().error(e);
//			} catch (IllegalArgumentException e){
//				util.Logger.singleton().error(e);
//			} catch (InvocationTargetException e){
//				util.Logger.singleton().error(e);
//			}
//		}
//	}
//	
//	private static void updateConnectionForString(Document document, String elementTagName,String property){
//		NodeList persisterTypeElements = document.getElementsByTagName(elementTagName);
//		for (int i = 0; i < persisterTypeElements.getLength(); i++) {
//			Node node = persisterTypeElements.item(i);
//			NamedNodeMap attributes = node.getAttributes();
//			try {
//				Method method = PersisterConnectionInfo.class.getMethod("set"+property, String.class);
//				method.invoke(pConnectionInfo, attributes.getNamedItem("value").getNodeValue());
//			} catch (SecurityException e) {
//				util.Logger.singleton().error(e);
//			} catch (DOMException e) {
//				util.Logger.singleton().error(e);
//			} catch (NoSuchMethodException e) {
//				util.Logger.singleton().error(e);
//			} catch (IllegalAccessException e){
//				util.Logger.singleton().error(e);
//			} catch (IllegalArgumentException e){
//				util.Logger.singleton().error(e);
//			} catch (InvocationTargetException e){
//				util.Logger.singleton().error(e);
//			}
//		}
//	}
//	
//	private String beginTime;
//	private String endTime;
//
//	private boolean estimate_actual;
//	private boolean estimate_bestCase;
//	private boolean estimate_remaining;
//	private boolean estimate_worstCase;
//
//	private String initialDis;
//	private String initialLocal;
//
//	private boolean mouseMessageOut;
//
//	private String persisterType;
//
//	private int port = 0;
//
//	private String projectLocationDistributedMode;
//	private String projectLocationLocalMode;
//	private String projectName;
//	private String state;
//	private String url;
//
//	private boolean description;
//	private boolean rotating_mode;
//	private boolean nonRotating_mode;
//	
//
//	/**
//	 * Get and set the persister Connection Infomation
//	 */
//
//	public String getBeginTime() {
//		return beginTime;
//	}
//
//	public String getEndTime() {
//		return endTime;
//	}
//
//	public String getInitialDis() {
//		return initialDis;
//	}
//
//	public String getInitialLocal() {
//		return initialLocal;
//	}
//
//	/**
//	 * Get and set the persister type and mouse message out
//	 */
//	public String getPersisterType() {
//		return persisterType;
//	}
//
//	public int getPort() {
//		return port;
//	}
//
//	public String getProjectLocationDistributedMode() {
//		return projectLocationDistributedMode;
//	}
//
//	public String getProjectLocationLocalMode() {
//		return projectLocationLocalMode;
//	}
//
//	public String getProjectName() {
//		return projectName;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public String getUrl() {
//		return url;
//	}
//
//	public boolean isEstimate_actual() {
//		return estimate_actual;
//	}
//
//	public boolean isEstimate_bestCase() {
//		return estimate_bestCase;
//	}
//
//	public boolean isEstimate_remaining() {
//		return estimate_remaining;
//	}
//
//	public boolean isEstimate_worstCase() {
//		return estimate_worstCase;
//	}
//
//	public boolean isMouseMessageOut() {
//		return mouseMessageOut;
//	}
//	
//	public boolean isDescription() {
//		return description;
//	}
//	
//	public boolean isRotating_Mode(){
//		return rotating_mode;
//	}
//	
//	public boolean isNonRotating_Mode(){
//		return nonRotating_mode;
//	}
//	
//	public void setRotating_Mode(boolean mode){
//		this.rotating_mode=mode;
//	}
//	
//	public boolean setNonRotating_Mode(boolean nonRotating_mode){
//		return this.nonRotating_mode=nonRotating_mode;
//	}
//	public void setRotating_Mode(Boolean mode){
//		this.rotating_mode=mode;
//	}
//	
//	public boolean setNonRotating_Mode(Boolean nonRotating_mode){
//		return this.nonRotating_mode=nonRotating_mode;
//	}
//
//
//	public void setBeginTime(String beginTime) {
//		this.beginTime = beginTime;
//	}
//
//	public void setEndTime(String endTime) {
//		this.endTime = endTime;
//	}
//
//	public void setEstimate_actual(boolean estimate_actual) {
//		this.estimate_actual = estimate_actual;
//	}
//
//	public void setEstimate_bestCase(boolean estimate_bestCase) {
//		this.estimate_bestCase = estimate_bestCase;
//	}
//
//	public void setEstimate_remaining(boolean estimate_remaining) {
//		this.estimate_remaining = estimate_remaining;
//	}
//
//	public void setEstimate_worstCase(boolean estimate_worstCase) {
//		this.estimate_worstCase = estimate_worstCase;
//	}
//	
//	public void setDescription(boolean description) {
//		this.description = description;
//	}
//	
//	public void setEstimate_actual(Boolean estimate_actual) {
//		this.estimate_actual = estimate_actual;
//	}
//
//	public void setEstimate_bestCase(Boolean estimate_bestCase) {
//		this.estimate_bestCase = estimate_bestCase;
//	}
//
//	public void setEstimate_remaining(Boolean estimate_remaining) {
//		this.estimate_remaining = estimate_remaining;
//	}
//
//	public void setEstimate_worstCase(Boolean estimate_worstCase) {
//		this.estimate_worstCase = estimate_worstCase;
//	}
//	
//	public void setDescription(Boolean description) {
//		this.description = description;
//	}
//
//	public void setInitialDis(String initialDis) {
//		this.initialDis = initialDis;
//	}
//
//	public void setInitialLocal(String initial) {
//		this.initialLocal = initial;
//	}
//
//	public void setMouseMessageOut(boolean mouseMessageOut) {
//		this.mouseMessageOut = mouseMessageOut;
//	}
//	public void setMouseMessageOut(Boolean mouseMessageOut) {
//		this.mouseMessageOut = mouseMessageOut;
//	}
//	/**
//	 * Get and set PersisterConnectionInfoBasic object
//	 */
//	public void setPersisterConnectionInfo() {
//		pConnectionInfo = PersisterConnectionInfo.getPersisterConnectionInfo();
//		File file = new File(strConfigFile);
//		file.delete();
//
//		try {
//			file.createNewFile();
//		} catch (IOException e) {
//			util.Logger.singleton().error(e);
//		}
//
//		StreamResult result = new StreamResult(file);
//
//		Document doc = buildXMLDocument();
//		DOMSource src = new DOMSource(doc);
//		Transformer xformer = null;
//		try {
//			xformer = TransformerFactory.newInstance().newTransformer();
//		} catch (TransformerConfigurationException e) {
//			util.Logger.singleton().error(e);
//		} catch (TransformerFactoryConfigurationError e) {
//			util.Logger.singleton().error(e.getException());
//		}
//		try {
//			xformer.transform(src, result);
//		} catch (TransformerException e) {
//			util.Logger.singleton().error(e);
//		}
//
//	}
//
//	public void setPersisterType(String persisterType) {
//		this.persisterType = persisterType;
//	}
//
//	public void setPort(int port) {
//		this.port = port;
//	}
//	public void setPort(String port) {
//		this.port = Integer.parseInt(port);
//	}
//	public void setProjectLocationDistributedMode(
//			String projectLocationDistributedMode) {
//		this.projectLocationDistributedMode = projectLocationDistributedMode;
//	}
//
//	public void setProjectLocationLocalMode(String projectLocationLocalMode) {
//		this.projectLocationLocalMode = projectLocationLocalMode;
//	}
//
//	public void setProjectName(String ProjectName) {
//		this.projectName = ProjectName;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	public void setPass(String pass) {
//		this.password = pass;
//		
//	}
//
//	public void setUser(String user) {
//		this.user = user;
//		
//	}
//
//	public static String getPluginID(){
//		return Activator.PLUGIN_ID;
//	}
//	
//	public static void setPluginID(String pluginID){
//		if (PersisterConnectionInfo.pluginID == null)
//			PersisterConnectionInfo.pluginID = pluginID;
//	}
//	
//}
