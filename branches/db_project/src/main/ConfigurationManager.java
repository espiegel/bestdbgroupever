package main;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

public class ConfigurationManager {
	public static String DB_SCHEMA   = "DbMysql02";
	public static String DB_USER = "DbMysql02";
	public static String DB_PASS = "DbMysql02";
	public static String DB_HOSTNAME = "localhost";
	public static int DB_PORT = 3305;
	
	public final static String CONFIG_FILE_PATH = "configuration.properties";
	
	private static boolean isFieldToLoad(Field field) {
		if (Modifier.isFinal(field.getModifiers())) //We do not want to load final variables
			return false;
		
		return true;
	}
	
	public static void init() {
		java.util.Properties props = new Properties();
		try {
			props.load(new FileInputStream(CONFIG_FILE_PATH));
		} catch (Exception e) {
			System.err.println("Error loading log file, will use default (obselete) configuration.");
			e.printStackTrace();
		}
		
		System.out.println(props.entrySet());
		
		final Class<ConfigurationManager> thisStaticClass = ConfigurationManager.class;
		for (Field field : thisStaticClass.getDeclaredFields()) {
			
			
			if (isFieldToLoad(field)) {
				final String propname = field.getName();
				String value = props.getProperty(propname);
				
				if (value==null) {
					try {
						final Object defvalue = field.get(thisStaticClass); 
						System.out.println("Warning: property " + propname +
								" was not found in config file. Will use default value " +
								defvalue +".");
					} catch (Exception e) {
						System.err.println("Error while trying to load property " + propname +".");
					}
				} else {
					try {
						if (field.getType()==String.class) {
							field.set(thisStaticClass, value);
						} else {
							field.setInt(thisStaticClass, Integer.parseInt(value));
						}
					} catch (Exception e) {
						System.err.println("Error setting property " + propname +
								"according to config file. Make sure in the config file there's the " +
								"appropriate type (int for int, etc.).");
						e.printStackTrace();
					}
				}
			}
		}
		
		System.out.println(DB_HOSTNAME);
		System.out.println(DB_USER);
		System.out.println(DB_PORT);
	}
}
