package com.source.dbshrink.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public interface DbShrinkDao {

	List<Map<String, Object>> getDatabase(DataSource dataSource) throws Exception;

	String getSysTypeName(DataSource dataSource, String dbName, String type) throws Exception;

	boolean shrinkDB(DataSource dataSource, String name, String dbName) throws Exception;

}
