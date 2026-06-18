package com.source.dbshrink.query;

import org.springframework.stereotype.Component;

@Component
public class DbShrinkQuery {

	public String getDbList() {
		StringBuilder builder = new StringBuilder();
		builder.append("select name,dbid from sysdatabases where dbid>4 order by name");
		return builder.toString();
	}

	public String getSysTypeName(String dbName) {
		StringBuilder builder = new StringBuilder();
		builder.append("select name from ").append(dbName).append(".dbo.sysfiles where groupid=?");
		return builder.toString();
	}

	public String shrinkDb(String name, String dbName) {
		StringBuilder builder = new StringBuilder();
		builder.append("ALTER DATABASE ").append(dbName).append(" SET RECOVERY SIMPLE WITH NO_WAIT \n");
		builder.append("DBCC SHRINKFILE (N'").append(name).append("' , 0, TRUNCATEONLY) \n");
		return builder.toString();
	}

}
