package com.source.dbshrink.daoimpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.source.dbshrink.dao.DbShrinkDao;
import com.source.dbshrink.query.DbShrinkQuery;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DbShrinkDaoImpl implements DbShrinkDao {

	private final DbShrinkQuery dbShrinkQuery;

	@Override
	public List<Map<String, Object>> getDatabase(DataSource dataSource) throws Exception {

		return new JdbcTemplate(dataSource).queryForList(dbShrinkQuery.getDbList());
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getSysTypeName(DataSource dataSource, String dbName, String type) throws Exception {

		return new JdbcTemplate(dataSource).queryForObject(dbShrinkQuery.getSysTypeName(dbName), new Object[] { type },
				String.class);
	}

	@Override
	public boolean shrinkDB(DataSource dataSource, String name, String dbName) throws Exception {
		try {
			new JdbcTemplate(dataSource).update(dbShrinkQuery.shrinkDb(name, dbName));
			return true;
		} catch (Exception e) {
			if (e.getCause() instanceof SQLException) {
				throw new Exception("Error to Shrink Database :" + name + "===>" + e.getCause().getMessage());
			}
			throw e;
		}
	}
}
