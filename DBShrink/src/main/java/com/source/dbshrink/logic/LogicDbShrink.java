package com.source.dbshrink.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.source.dbshrink.components.CheckList;
import com.source.dbshrink.components.Item;
import com.source.dbshrink.service.DbShrinkService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogicDbShrink {

	private final DbShrinkService dbShrinkService;

	public boolean loadDatabase(CheckList checkList) throws Exception {
		try {
			List<Map<String, Object>> lstDbList = dbShrinkService.getDatabase();

			if (lstDbList.isEmpty()) {
				throw new Exception("Database Details not Found...!");
			}

			List<Item> newData = new ArrayList<>();

			for (Map<String, Object> map : lstDbList) {
				newData.add(new Item(map.get("name"), map.get("dbid")));
			}

			checkList.setModelItems(newData);

			return true;
		} catch (Exception e) {
			throw e;
		}

	}

	public boolean shrink(String dbList, String type) {
		return dbShrinkService.shrink(dbList, type);
	}

}
