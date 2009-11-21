package persister.data.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import cards.model.StoryCardModel;

import persister.data.Legend;



public class LegendDataObject implements Legend, Serializable {

	private static final long serialVersionUID = -350041806629713982L;
	private String blue = null;
	private String red = null;
	private String green = null;
	private String yellow = null;
	private String white = null;
	private String pink = null;
	private String aqua = null;
	private String khaki = null;
	private String peach = null;
	private String grey = null;

	public LegendDataObject() {

		setBlue("blue");
		setRed("red");
		setGreen("green");
		setYellow("yellow");
		setWhite("white");
		setPink("pink");
		setAqua("aqua");
		setKhaki("khaki");
		setPeach("peach");
		setGrey("gray");

	}

	public LegendDataObject clone() {
		LegendDataObject clone = new LegendDataObject();

		clone.blue = getBlue();
		clone.red = getRed();
		clone.green = getGreen();
		clone.yellow = getYellow();
		clone.white = getWhite();
		clone.pink = getPink();
		clone.aqua = getAqua();
		clone.khaki = getKhaki();
		clone.peach = getPeach();
		clone.grey = getGrey();

		return clone;
	}

	public LegendDataObject(String aqua, String blue, String gray,
			String green, String khaki, String peach, String pink, String red,
			String white, String yellow) {
		setAqua(aqua);
		setBlue(blue);
		setGrey(gray);
		setGreen(green);
		setKhaki(khaki);
		setPeach(peach);
		setPink(pink);
		setRed(red);
		setWhite(white);
		setYellow(yellow);

	}

	public String getBlue() {
		return blue;
	}

	public void setBlue(String blue) {
		this.blue = blue;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}

	public String getGreen() {
		return green;
	}

	public void setGreen(String green) {
		this.green = green;
	}

	public String getYellow() {
		return yellow;
	}

	public void setYellow(String yellow) {
		this.yellow = yellow;
	}

	public String getWhite() {
		return white;
	}

	public void setWhite(String white) {
		this.white = white;
	}

	public String getPink() {
		return pink;
	}

	public void setPink(String pink) {
		this.pink = pink;
	}

	public String getAqua() {
		return aqua;
	}

	public void setAqua(String aqua) {
		this.aqua = aqua;
	}

	public String getKhaki() {
		return khaki;
	}

	public void setKhaki(String khaki) {
		this.khaki = khaki;
	}

	public String getPeach() {
		return peach;
	}

	public void setPeach(String peach) {
		this.peach = peach;
	}

	public String getGrey() {
		return grey;
	}

	public void setGrey(String gray) {
		this.grey = gray;
	}

	public String getColor(String color){
		try {
			Method method = LegendDataObject.class.getMethod("get"+util.Support.capitalize(color), null);
			return (String)method.invoke(this, null);
		} catch (Exception e) {
			util.Logger.singleton().error(e);
		}
		return null;
	}
	public long getId() {
		return 0;
	}

	public String getName() {
		return null;
	}

	public void setId(long id) {
	}

	public void setName(String name) {
	}
	
	public boolean equals(Object other){
		Legend legend = (Legend) other;
		boolean result = true;
		Enumeration<String> colors = StoryCardModel.COLORS.keys();
		while(colors.hasMoreElements()){
			String color = colors.nextElement();
			result = result && getColor(color).equals(legend.getColor(color));
		}
		return result;
	}
}
