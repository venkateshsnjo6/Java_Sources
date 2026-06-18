package com.source.dbshrink.serviceimpl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.source.dbshrink.common.Enums.MessageType;
import com.source.dbshrink.common.Enums.ShrinkType;
import com.source.dbshrink.common.Utils;
import com.source.dbshrink.config.ApplicationConfig;
import com.source.dbshrink.dao.DbShrinkDao;
import com.source.dbshrink.service.DbShrinkService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DbShrinkServiceImpl implements DbShrinkService {

	private final DbShrinkDao dbShrinkDao;

	private final ApplicationConfig applicationConfig;

	@Override
	public List<Map<String, Object>> getDatabase() throws Exception {
		DataSource dataSource = applicationConfig.getDataSource(applicationConfig.getServerName(),
				applicationConfig.getPortNo(), applicationConfig.getPassWord(), applicationConfig.getUserName(),
				"master");
		return dbShrinkDao.getDatabase(dataSource);
	}

	@Override
	public boolean shrink(String dbList, String type) {

		try {
			String db[] = dbList.split(",");
			boolean shrink = false;
			for (String dbName : db) {
				DataSource dataSource = applicationConfig.getDataSource(applicationConfig.getServerName(),
						applicationConfig.getPortNo(), applicationConfig.getPassWord(), applicationConfig.getUserName(),
						dbName);
				String name = dbShrinkDao.getSysTypeName(dataSource, dbName, type);
				shrink = dbShrinkDao.shrinkDB(dataSource, name, dbName);
				String msgType = type.equalsIgnoreCase(ShrinkType.LDF.getType()) ? " Log" : " Main";
				Utils.writeLog(MessageType.Info, dbName + msgType + " Database Shrinked Successfully", getClass());

			}

			return shrink;
		} catch (Exception e) {
			Utils.writeLog(MessageType.Error, e.getMessage(), getClass());
			return false;
		}

	}

}
