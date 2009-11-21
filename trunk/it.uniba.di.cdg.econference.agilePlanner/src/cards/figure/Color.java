package cards.figure;

public class Color {
	public static org.eclipse.swt.graphics.Color byName(String color){
		if(color.equalsIgnoreCase("white"))
        	return(new org.eclipse.swt.graphics.Color(null,255, 255, 255));
        else if(color.equalsIgnoreCase("red"))
            return(new org.eclipse.swt.graphics.Color(null,255, 99, 71));
        else if(color.equalsIgnoreCase("blue"))
            return(new org.eclipse.swt.graphics.Color(null,173, 216, 230));
        else if(color.equalsIgnoreCase("green"))
            return(new org.eclipse.swt.graphics.Color(null,143,188,143));
        else if(color.equalsIgnoreCase("yellow"))
            return(new org.eclipse.swt.graphics.Color(null,255,246 , 143));
        else if(color.equalsIgnoreCase("peach"))
            return(new org.eclipse.swt.graphics.Color(null,255, 218, 185));
        else if(color.equalsIgnoreCase("grey"))
            return(new org.eclipse.swt.graphics.Color(null,190 ,190, 190));
        else if(color.equalsIgnoreCase("aqua"))
            return(new org.eclipse.swt.graphics.Color(null,102, 205 ,170));
        else if(color.equalsIgnoreCase("khaki"))
            return(new org.eclipse.swt.graphics.Color(null,189 ,183 ,107));
		else
            return(new org.eclipse.swt.graphics.Color(null,255,192, 203));
    }
}
