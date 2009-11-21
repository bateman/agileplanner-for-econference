package util;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.FileAppender;

public class Logger {
	
	private org.apache.log4j.Logger	_logger;
	private static final Level		LEVEL			= Level.DEBUG;
	private static final String		LOG_FILE		= "./log/dev";
	private static Hashtable<String, Logger> loggers = new Hashtable<String, Logger>();
	
	private Logger(String logFilePath, Level level){
		_logger = org.apache.log4j.Logger.getRootLogger();
		
		SimpleLayout layout = new SimpleLayout();
		FileAppender appender = null;
		try {
			appender = new FileAppender(layout,logFilePath+".log",false);
		} catch(Exception e) {
		}
		_logger.addAppender(appender);
		_logger.setLevel(LEVEL);
	}
	
	public String getFile(){
		Enumeration<org.apache.log4j.Appender> appenders = (Enumeration<org.apache.log4j.Appender>)_logger.getAllAppenders();
		org.apache.log4j.Appender appender = null;
		while(appenders.hasMoreElements()){
			appender = appenders.nextElement();
		}
		return ((FileAppender)appender).getFile();
	}
	
	public void warn(String message){
		_logger.warn(message);
	}

	public void fatal(String message){
		_logger.fatal(message);
	}

	public void error(String message){
		_logger.error(message);
	}
	
	public void error(Exception e){
		String msg = e.getMessage() + "\r\n";
		for(int i = 0; i<e.getStackTrace().length; i++){
			msg += "\t" + e.getStackTrace()[i].toString() + "\r\n";
		}
		_logger.error(msg);
	}

	public void info(String message){
		_logger.info(message);
	}

	public void debug(String message){
		_logger.debug(message);
	}
	
	public static void clear(){
		loggers.clear();
	}
	
	public static Logger singleton(){
		return singleton(null, null);
	}
	
	public static Logger singleton(Level level, String logFilePath){
		if(level == null)level = LEVEL;
		if(logFilePath == null)logFilePath = LOG_FILE;
		/************************************/
		if(!loggers.containsKey(logFilePath)){
			 loggers.put(logFilePath, new Logger(logFilePath, level));
		}
		Logger logger = loggers.get(logFilePath);
		
		return logger;
	}
//	
//	public static Logger singleton(String logFilePath){
//		return singleton(null, logFilePath);
//	}
//	
//	public static Logger singleton(Level level){
//		return singleton(level, null);
//	}
//	
	public static Logger singleton(File logFile){
		return singleton(null, logFile.getAbsolutePath());
	}
//	
//	public static void clear(){
//		loggers.clear();
//	}
//	
}
