package com.source.dbshrink.service;

import java.util.List;
import java.util.Map;

public interface DbShrinkService {

	List<Map<String, Object>> getDatabase() throws Exception;

	boolean shrink(String dbList, String type);

}
