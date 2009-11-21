package util;

public class Support {
	public static String capitalize(String str){
		String result = "";
		str = str.toLowerCase();
		if(str.length() > 0){
			result += str.charAt(0);
			result = result.toUpperCase();
		}
		for(int i=1;i<str.length();i++){
			result += str.charAt(i);
		}
		return result;
	}
}
