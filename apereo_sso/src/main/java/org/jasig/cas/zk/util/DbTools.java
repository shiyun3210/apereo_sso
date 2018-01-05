package org.jasig.cas.zk.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供便捷对数据库的处理实现
 * 
 */
public class DbTools {

	static Logger logger = LoggerFactory.getLogger(DbTools.class);

	private static boolean debug = true;

	/**
	 * 
	 * @throws java.lang.SQLException
	 */
	public static boolean checkExsit(String tableName, String colName, String value) throws SQLException{
		String sql = "select count(1) from " + tableName + " where " + colName + " = ?";
		return checkExsit(sql, makeParams(value));
	}

	public static boolean checkExsit(String tableName, String colName, long value) throws SQLException{
		String sql = "select count(1) from " + tableName + " where " + colName + " = ?";
		return checkExsit(sql, makeParams(value));
	}

	public static boolean checkExsit(String tableName, String colName, int value) throws SQLException{
		String sql = "select count(1) from " + tableName + " where " + colName + " = ?";
		return checkExsit(sql, makeParams(value));
	}

	public static boolean checkExsit(String sql, List<Object> params) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return checkExsit(sql, params, conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	/**
	 * 查询是否存在
	 * 
	 * @param sql
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static boolean checkExsit(String sql, List<Object> params, Connection conn) throws SQLException{
		if (debug)
			logger.info("check exist:" + sql);
		Validate.notNull(params);
		boolean ret = false;
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next())
			ret = true;

		resultSet.close();
		preparedStatement.close();

		return ret;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws java.lang.SQLException
	 */
	public static HashMap<String, Object> getHashValue(String sql) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getHashValue(sql, Collections.emptyList(), conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws java.lang.SQLException
	 */
	public static HashMap<String, Object> getHashValue(String sql, Connection conn) throws SQLException{

		return getHashValue(sql, Collections.emptyList(), conn);
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws java.lang.SQLException
	 */
	public static HashMap<String, Object> getHashValue(String sql, List<Object> params) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getHashValue(sql, params, conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static HashMap<String, Object> getHashValue(String sql, List<Object> params, Connection conn)
			throws SQLException{
		if (debug)
			logger.info("query:" + sql);
		Validate.notNull(params);
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}
		ResultSet resultSet = preparedStatement.executeQuery();
		HashMap<String, Object> hp = new HashMap<String, Object>();
		if (resultSet.next()){
			int cols = resultSet.getMetaData().getColumnCount();
			for (int i = 0; i < cols; i++){
				hp.put(resultSet.getMetaData().getColumnName(i + 1), resultSet.getString(i + 1));
			}
		}

		resultSet.close();
		preparedStatement.close();

		return hp;
	}

	public static List<Object> makeParams(Object... params){

		List<Object> paramList = new ArrayList<Object>();
		for (Object param : params){
			paramList.add(param);
		}

		return paramList;
	}

	public static <T> T load(Class<T> clazz, String sql) throws SQLException, InstantiationException,
			IllegalAccessException, InvocationTargetException{

		Connection conn = ConnectionManager.getConnection();
		try{
			return load(clazz, sql, Collections.emptyList(), conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static <T> T load(Class<T> clazz, String sql, Connection conn) throws SQLException,
			InstantiationException, IllegalAccessException, InvocationTargetException{

		return load(clazz, sql, Collections.emptyList(), conn);
	}

	public static <T> T load(Class<T> clazz, String sql, List<Object> params) throws SQLException,
			InstantiationException, IllegalAccessException, InvocationTargetException{

		Connection conn = ConnectionManager.getConnection();
		try{
			return load(clazz, sql, params, conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static <T> T load(Class<T> clazz, String sql, List<Object> params, Connection conn) throws SQLException,
			InstantiationException, IllegalAccessException, InvocationTargetException{

		Validate.notNull(params);

		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}
		ResultSet resultSet = preparedStatement.executeQuery();
		T result = clazz.newInstance();
		if (resultSet.next()){
			int cols = resultSet.getMetaData().getColumnCount();
			for (int i = 0; i < cols; i++){
				if(resultSet.getObject(i + 1)!= null){
					BeanUtils.setProperty(result, resultSet.getMetaData().getColumnName(i + 1),
							resultSet.getObject(i + 1));
				}
			}
		}

		resultSet.close();
		preparedStatement.close();

		return result;
	}

	/**
	 * 
	 * @param sql
	 * @return List<HashMap<String, Object>>
	 * @throws SQLException
	 */
	public static List<HashMap<String, Object>> getResultSetList(String sql) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getResultSetList(sql, Collections.emptyList(), conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	/**
	 * 
	 * @param sql
	 * @return List<HashMap<String, Object>>
	 * @throws SQLException
	 */
	public static List<HashMap<String, Object>> getResultSetList(String sql, List<Object> params)
			throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getResultSetList(sql, params, conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	/**
	 * 
	 * @param sql
	 * @return List<HashMap<String, Object>>
	 * @throws SQLException
	 */
	public static List<HashMap<String, Object>> getResultSetList(String sql, Connection conn) throws SQLException{

		return getResultSetList(sql, Collections.emptyList(), conn);
	}

	public static List<HashMap<String, Object>> getResultSetList(String sql, List<Object> params, Connection conn)
			throws SQLException{
		if (debug)
			logger.info("getResultSetList .. query:" + sql);

		Validate.notNull(params);

		List<HashMap<String, Object>> values = new ArrayList<HashMap<String, Object>>();

		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}
		ResultSet resultSet = preparedStatement.executeQuery();
		ResultSetMetaData data = resultSet.getMetaData();
		int cols = data.getColumnCount();
		while (resultSet != null && resultSet.next()){
			HashMap<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= cols; i++){
				rowData.put(data.getColumnLabel(i), resultSet.getObject(i));
			}
			values.add(rowData);
		}
		resultSet.close();
		preparedStatement.close();

		return values;
	}

	public static <T> List<T> loadList(Class<T> clazz, String sql) throws SQLException, InstantiationException,
			IllegalAccessException, InvocationTargetException{

		Connection conn = ConnectionManager.getConnection();
		try{
			return loadList(clazz, sql, Collections.emptyList(), conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static <T> List<T> loadList(Class<T> clazz, String sql, Connection conn) throws SQLException,
			InstantiationException, IllegalAccessException, InvocationTargetException{

		return loadList(clazz, sql, Collections.emptyList(), conn);
	}

	public static <T> List<T> loadList(Class<T> clazz, String sql, List<Object> params) throws SQLException,
			InstantiationException, IllegalAccessException, InvocationTargetException{

		Connection conn = ConnectionManager.getConnection();
		try{
			return loadList(clazz, sql, params, conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static <T> List<T> loadList(Class<T> clazz, String sql, List<Object> params, Connection conn)
			throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException{
		if (debug){
			logger.info("query:" + sql);
			logger.info("params" + params);
		}
		Validate.notNull(params);

		List<T> values = new ArrayList<T>();
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}
		ResultSet resultSet = preparedStatement.executeQuery();
		ResultSetMetaData data = resultSet.getMetaData();
		int cols = data.getColumnCount();
		while (resultSet != null && resultSet.next()){
			T object = clazz.newInstance();
			for (int i = 1; i <= cols; i++){
				if(resultSet.getObject(i)!=null){
					BeanUtils.setProperty(object, data.getColumnLabel(i), resultSet.getObject(i));
				}
			}
			values.add(object);
		}
		resultSet.close();
		preparedStatement.close();

		return values;
	}

	public static String getStringValue(String sql) throws SQLException{

		Connection conn = ConnectionManager.getConnection();

		try{
			return getStringValue(sql, Collections.emptyList(), conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static String getStringValue(String sql, List<Object> params) throws SQLException{

		Connection conn = ConnectionManager.getConnection();

		try{
			return getStringValue(sql, params, conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static String getStringValue(String sql, Connection conn) throws SQLException{

		return getStringValue(sql, Collections.emptyList(), conn);
	}

	public static String getStringValue(String sql, List<Object> params, Connection conn) throws SQLException{

		return getObjectValue(sql, params, conn).toString();
	}

	public static int getIntValue(String sql) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getIntValue(sql, Collections.emptyList(), conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static int getIntValue(String sql, List<Object> params) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getIntValue(sql, params, conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static int getIntValue(String sql, Connection conn) throws SQLException{

		return getIntValue(sql, Collections.emptyList(), conn);
	}

	public static int getIntValue(String sql, List<Object> params, Connection conn) throws SQLException{

		Object obj = getObjectValue(sql, params, conn);
		if (obj instanceof Integer){
			return Integer.parseInt( obj.toString());
		}
		return Integer.parseInt(obj.toString());
	}

	private static Object getObjectValue(String sql, List<Object> params, Connection conn) throws SQLException{
		if (debug)
			logger.info("query:" + sql);

		Validate.notNull(params);

		PreparedStatement preparedStatement = conn.prepareStatement(sql);

		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}

		ResultSet resultSet = preparedStatement.executeQuery();

		Object result = "";
		if (resultSet.next())
			result = resultSet.getObject(1);

		resultSet.close();
		preparedStatement.close();

		return result;
	}

	public static List<String> getList(String sql) throws SQLException{

		Connection conn = ConnectionManager.getConnection();
		try{
			return getList(sql, Collections.emptyList(), conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}

	}

	public static List<String> getList(String sql, Connection conn) throws SQLException{

		return getList(sql, Collections.emptyList(), conn);

	}

	public static List<String> getList(String sql, List<Object> params, Connection conn) throws SQLException{

		Validate.notNull(params);

		PreparedStatement preparedStatement = conn.prepareStatement(sql);

		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}

		ResultSet resultSet = preparedStatement.executeQuery();
		ResultSetMetaData data = resultSet.getMetaData();
		int cols = data.getColumnCount();
		List<String> values = new ArrayList<String>();
		while (resultSet != null && resultSet.next()){
			for (int i = 1; i <= cols; i++){
				values.add(resultSet.getObject(i).toString());
			}
		}
		resultSet.close();
		preparedStatement.close();

		return values;

	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws java.lang.SQLException
	 */
	public static List<Object> getValues(String sql) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getValues(sql, Collections.emptyList(), conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws java.lang.SQLException
	 */
	public static List<Object> getValues(String sql, List<Object> params) throws SQLException{
		Connection conn = ConnectionManager.getConnection();
		try{
			return getValues(sql, params, conn);
		} catch (SQLException e){
			throw e;
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws java.lang.SQLException
	 */
	public static List<Object> getValues(String sql, Connection conn) throws SQLException{

		return getValues(sql, Collections.emptyList(), conn);
	}

	public static List<Object> getValues(String sql, List<Object> params, Connection conn) throws SQLException{
		if (debug)
			logger.info("query:" + sql);
		Validate.notNull(params);

		PreparedStatement preparedStatement = conn.prepareStatement(sql);

		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}

		ResultSet resultSet = preparedStatement.executeQuery();
		ResultSetMetaData data = resultSet.getMetaData();
		int cols = data.getColumnCount();
		List<Object> values = new ArrayList<Object>();
		while (resultSet != null && resultSet.next()){
			for (int i = 1; i <= cols; i++){
				values.add(resultSet.getObject(i));
			}
		}
		resultSet.close();
		preparedStatement.close();

		return values;
	}

	public static int executeUpdate(String sql) throws SQLException, InstantiationException,
			IllegalAccessException, InvocationTargetException{

		Connection conn = ConnectionManager.getConnection(ConnectionManager.WRITE_DB);
		try{
			return executeUpdate(sql, Collections.emptyList(), conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static int executeUpdate(String sql, List<Object> params) throws SQLException, InstantiationException,
			IllegalAccessException, InvocationTargetException{

		Connection conn = ConnectionManager.getConnection(ConnectionManager.WRITE_DB);
		try{
			return executeUpdate(sql, params, conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static int executeUpdate(String sql, Connection conn) throws SQLException, InstantiationException,
			IllegalAccessException, InvocationTargetException{

		return executeUpdate(sql, Collections.emptyList(), conn);
	}

	public static int executeUpdate(String sql, List<Object> params, Connection conn) throws SQLException,
			InstantiationException, IllegalAccessException, InvocationTargetException{

		Validate.notNull(params);

		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}
		int result = preparedStatement.executeUpdate();
		preparedStatement.close();

		return result;
	}

	public static Set<String> getStringSet(String sql, List<Object> params) throws SQLException{

		Connection conn = ConnectionManager.getConnection();
		try{
			return getStringSet(sql, params, conn);
		} finally{
			ConnectionManager.returnConnection(conn);
		}
	}

	public static Set<String> getStringSet(String sql, List<Object> params, Connection conn) throws SQLException{
		if (debug)
			logger.info("query:" + sql);

		Validate.notNull(params);

		Set<String> values = new HashSet<String>();

		PreparedStatement preparedStatement = conn.prepareStatement(sql);

		for (int i = 0; i < params.size(); i++){
			preparedStatement.setObject(i + 1, params.get(i));
		}

		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next())
			values.add(resultSet.getString(1));

		resultSet.close();
		preparedStatement.close();
		return values;
	}
	
	/*
	 * 转义敏感符号（解决SQL注入）
	 */
	public static String escape(String str){
		return str.replaceAll("\\'", "\\\\'").replaceAll("\\;", "\\\\;").replaceAll("\\|", "\\\\|")
				.replaceAll("\\<", "\\\\<").replaceAll("\\>", "\\\\>").replaceAll("\\_", "\\\\_")
				.replaceAll("\\/", "\\\\/");
	}
	/**
	 * 转化为带''的字符串
	 * @param str
	 * @return
	 * pxw
	 */
	public static String makeStringValue(String str) {
		return "'" + escape(str) + "'";
	}
	
	/**
	 * 把数组转化为可以用于in操作的String
	 * @return
	 */
	public static String makearrayToStringForIn(int[] ids) {
		
		if (ids == null || ids.length == 0) {
			throw new IllegalArgumentException("ids参数为null或者长度为0");
		}
		
		String idsStr = "";
	    for (int id : ids) {
	    	idsStr += "," + id;
	    }
	    return idsStr.substring(1, idsStr.length());
	}
}
