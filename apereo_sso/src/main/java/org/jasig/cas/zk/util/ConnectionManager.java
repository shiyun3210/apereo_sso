package org.jasig.cas.zk.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 提供对c3p0的数据库连接池的管理操作 Central manager of database connections.
 */
public class ConnectionManager {

	public static final String WRITE_DB = "write";
	public static final String READ_DB = "read";

	private static String currentDatabase = Config.getString("database.read", "read");
	private static String writeDatabase = Config.getString("database.write", "write");

	private static boolean usePool = true;

	private static Map<String, Object> dataSourceMap = new HashMap<String, Object>();

	private static ComboPooledDataSource[] dataSourceArray = new ComboPooledDataSource[50];

	public static int count = 0;

	public static int total = 0;

	static{
		init(currentDatabase);
		init(writeDatabase);
	}

	public static void init(String dbName){
		if (usePool){
			try{
				Configuration config = Config.subset("database." + dbName);
				dataSourceArray[total] = new ComboPooledDataSource();
				dataSourceArray[total].setDriverClass(config.getString("driver"));
				dataSourceArray[total].setJdbcUrl(config.getString("url"));
				dataSourceArray[total].setUser(config.getString("username", ""));
				dataSourceArray[total].setPassword(config.getString("password", ""));
				dataSourceArray[total].setInitialPoolSize(config.getInt("initialPoolSize", 10));
				dataSourceArray[total].setMinPoolSize(config.getInt("minPoolSize", 20));
				dataSourceArray[total].setMaxPoolSize(config.getInt("maxPoolSize", 200));
				dataSourceArray[total].setAcquireIncrement(config.getInt("acquireIncrement", 3));
				dataSourceArray[total].setMaxIdleTime(config.getInt("maxIdleTime", 3600));
				dataSourceArray[total].setMaxStatements(config.getInt("maxStatements", 30));
				dataSourceArray[total].setTestConnectionOnCheckin(config.getBoolean(
						"testConnectionOnCheckin", true));
				dataSourceArray[total].setAutomaticTestTable(config.getString("automaticTestTable",
						"c3p0testtable"));
				dataSourceArray[total].setIdleConnectionTestPeriod(config.getInt(
						"idleConnectionTestPeriod", 60));
				dataSourceArray[total].setTestConnectionOnCheckout(config.getBoolean(
						"testConnectionOnCheckout", true));
				dataSourceArray[total].setPreferredTestQuery(config.getString("preferredTestQuery",
						"select id from c3p0testtable where 1 = 1"));
				dataSourceMap.put(dbName, dataSourceArray[total]);
				total++;
			} catch (Exception e){}
		}
	}

	/**
	 * Returns a database connection from the currently active connection
	 * provider.
	 */
	public static Connection getConnection(){
		return getConnection(currentDatabase);
	}

	public static String getCurrentDatabase(){
		return currentDatabase;
	}

	public static void setCurrentDatabase(String dbName){
		currentDatabase = dbName;
	}

	public static void reConnect(){
		init(currentDatabase);
	}

	public static Connection getConnection(String dbName){
		if (dbName == null || dbName.length() == 0)
			dbName = currentDatabase;
		Connection con = null;
		if (!usePool)
			return createConnection(dbName);
		else{
			try{
				con = getDataSource(dbName).getConnection();
			} catch (Exception e){
				init(dbName);
				try{
					con = getDataSource(dbName).getConnection();
				} catch (Exception ex){}
			}
			return con;
		}
	}

	public static Connection createConnection(){
		return createConnection(currentDatabase);
	}

	/**
	 * 
	 * @return
	 */
	public static Connection createConnection(String dbName){
		Connection con = null;
		Configuration config = Config.subset("database." + dbName);
		try{
			Class.forName(config.getString("driver"));
			con = DriverManager.getConnection(config.getString("url"), config.getString("username", ""),
					config.getString("password", ""));
			count++;
			total++;
		} catch (Exception e){
			e.printStackTrace();
		}
		return con;
	}

	public static void returnConnection(Connection conn){
		try{
			if (!usePool){

				if (conn != null)
					conn.close();
				conn = null;
				count--;
			} else{
				if (conn != null){
					conn.close();
					conn = null;
				}
			}
		} catch (Exception ex){}
	}

	/**
	 * @return the usePool
	 */
	public static boolean isUsePool(){
		return usePool;
	}

	/**
	 * @param usePool
	 *                the usePool to set
	 */
	public static void setUsePool(boolean usePool){
		ConnectionManager.usePool = usePool;
	}

	/**
	 * @return the count
	 */
	public static int getCount(){
		return count;
	}

	/**
	 * @param count
	 *                the count to set
	 */
	public static void setCount(int count){
		ConnectionManager.count = count;
	}

	/**
	 * @return the total
	 */
	public static int getTotal(){
		return total;
	}

	/**
	 * @param total
	 *                the total to set
	 */
	public static void setTotal(int total){
		ConnectionManager.total = total;
	}

	/**
	 * @return the dataSource
	 */
	public static ComboPooledDataSource getDataSource(String dbName){
		if (dbName == null || dbName.length() == 0)
			dbName = currentDatabase;
		return (ComboPooledDataSource) dataSourceMap.get(dbName);
	}

	/**
	 * @param dataSource
	 *                the dataSource to set
	 */
	public static void setDataSource(ComboPooledDataSource dataSource){
		dataSourceMap.put(currentDatabase, dataSource);
	}

	/**
	 * @param dataSource
	 *                the dataSource to set
	 */
	public static void setDataSource(String dbName, ComboPooledDataSource dataSource){
		dataSourceMap.put(dbName, dataSource);
	}

	public static void destory(){
		System.out.println("关闭JDBC连接。。。");
		for (ComboPooledDataSource dataSource : dataSourceArray){
			if (dataSource != null){
				dataSource.close();
			}
		}
		System.out.println("JDBC连接已关闭");
	}
}
