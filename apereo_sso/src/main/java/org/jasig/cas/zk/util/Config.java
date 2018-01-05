package org.jasig.cas.zk.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;


public class Config implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private static PropertiesConfiguration config = null;

	static{
		init();
	}

	public static void init(){
		try{
			config = new PropertiesConfiguration("dbconfig.properties");
		} catch (Exception e){}
	}

	public static Configuration subset(String prefix){
		return config.subset(prefix);
	}

	public static boolean isEmpty(){
		return config.isEmpty();
	}

	public static boolean containsKey(String key){
		return config.containsKey(key);
	}

	public static void addProperty(String key, Object value){
		config.addProperty(key, value);
	}

	public static void setProperty(String key, Object value){
		config.setProperty(key, value);
	}

	public static void clearProperty(String key){
		config.clearProperty(key);
	}

	public static Object getProperty(String key){
		return config.getProperties(key);
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys(String key){
		return config.getKeys(key);
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys(){
		return config.getKeys();
	}

	public static Properties getProperties(String key){
		return config.getProperties(key);
	}

	public static boolean getBoolean(String key){
		return config.getBoolean(key);
	}

	public static boolean getBoolean(String key, boolean defaultValue){
		return config.getBoolean(key, defaultValue);
	}

	public static boolean getBoolean(String key, Boolean defaultValue){
		return config.getBoolean(key, defaultValue);
	}

	public static byte getByte(String key){
		return config.getByte(key);
	}

	public static byte getByte(String key, byte defaultValue){
		return config.getByte(key, defaultValue);
	}

	public static Byte getByte(String key, Byte defaultValue){
		return config.getByte(key, defaultValue);
	}

	public static double getDouble(String key){
		return config.getDouble(key);
	}

	public static double getDouble(String key, double defaultValue){
		return config.getDouble(key, defaultValue);
	}

	public static Double getDouble(String key, Double defaultValue){
		return config.getDouble(key, defaultValue);
	}

	public static float getFloat(String key){
		return config.getFloat(key);
	}

	public static float getFloat(String key, float defaultValue){
		return config.getFloat(key, defaultValue);
	}

	public static Float getFloat(String key, Float defaultValue){
		return config.getFloat(key, defaultValue);
	}

	public static int getInt(String key){
		return config.getInt(key);
	}

	public static int getInt(String key, int defaultValue){
		return config.getInt(key, defaultValue);
	}

	public static Integer getInteger(String key, Integer defaultValue){
		return config.getInteger(key, defaultValue);
	}

	public static long getLong(String key){
		return config.getLong(key);
	}

	public static long getLong(String key, long defaultValue){
		return config.getLong(key, defaultValue);
	}

	public static Long getLong(String key, Long defaultValue){
		return config.getLong(key, defaultValue);
	}

	public static short getShort(String key){
		return config.getShort(key);
	}

	public static short getShort(String key, short defaultValue){
		return config.getShort(key, defaultValue);
	}

	public static Short getShort(String key, Short defaultValue){
		return config.getShort(key, defaultValue);
	}

	public static String getString(String key){
		return config.getString(key) == null ? "" : config.getString(key);
	}

	public static String getString(String key, String defaultValue){
		return config.getString(key, defaultValue);
	}

	public static String[] getStringArray(String key){
		return config.getStringArray(key);
	}
	
	public static List<Object> getList(String key){
		return config.getList(key);
	}
	
	public static Set<String> getStringSet(String key){
		String[] values = getStringArray(key);
		Set<String> setStr = new HashSet<String>();
		for(String str : values){
			setStr.add(str);
		}
		return setStr;
	}
}
