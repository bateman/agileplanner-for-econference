package util;
import java.lang.Comparable;

public class Comparer {
	
	private Comparable comparable;
	
	private Comparer(Comparable o){
		comparable = o;
	}
	
	public static Comparer compare(Comparable o1){
		return new Comparer(o1);
	}
	
	public int to(Object o2){
		return comparable.compareTo(o2);
	}
	
}
