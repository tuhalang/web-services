package myetapp.engine;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class StateLookup {
	private static StateLookup instance = null;
	private static Hashtable<String, String> titles = new Hashtable();
	private static String propertiesName = "StateLookup";
	private ResourceBundle rb;

	private StateLookup() {
		prepareLabels();
	}

	public static StateLookup getInstance(String name) {
		if (name.equals(propertiesName)) {
			if (instance == null)
				instance = new StateLookup();
		} else {
			propertiesName = name;
			instance = new StateLookup();
		}
		return instance;
	}

	public static StateLookup getInstance() {
		if (instance == null)
			instance = new StateLookup();
		return instance;
	}

	public static StateLookup reload() {
		instance = new StateLookup();
		return instance;
	}

	private void prepareLabels() {
		this.rb = ResourceBundle.getBundle(propertiesName);
		for (Enumeration keys = this.rb.getKeys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			String value = this.rb.getString(key);
			titles.put(key, value);
		}
	}

	public static String getTitle(String name) {
		StateLookup labels = getInstance("labels");
		return ((titles.get(name) != null) ? (String) titles.get(name) : "");
	}

	public Hashtable getTitles() {
		return titles;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getTitle("password"));
		System.out.println(getTitle("username"));
	}
}