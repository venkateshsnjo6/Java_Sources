package com.source.svnsourcedownloader.daoimpl;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.source.svnsourcedownloader.dao.SourceDownloadDao;
import com.source.svnsourcedownloader.query.SourceDownloadQuery;

@Component
@ConditionalOnProperty(prefix = "basedon", value = "data", havingValue = "D", matchIfMissing = false)
public class SourceDownloadDaoImpl implements SourceDownloadDao {

	@Autowired
	private SourceDownloadQuery sourceDownloadQuery;

	@Autowired
	private DataSource dbconn;

	@Override
	public Map<String, Object> getSourceDownload() throws Exception {
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dbconn);
			return jdbcTemplate.queryForMap(sourceDownloadQuery.getSourceDownloadDetail());
		} catch (EmptyResultDataAccessException e) {
			throw new Exception("Source Downloaded Details not found!...");
		} catch (Exception e) {
			throw e;
		}
	}

}
