package com.source.svnsourcedownloader.serviceimpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.source.svnsourcedownloader.dao.SourceDownloadDao;
import com.source.svnsourcedownloader.service.SourceDownloadService;

@Component
@ConditionalOnProperty(prefix = "basedon", value = "data", havingValue = "D", matchIfMissing = false)
public class SourceDownloadServiceImpl implements SourceDownloadService {

	@Autowired
	private SourceDownloadDao sourceDownloadDao;

	@Override
	public Map<String, Object> getSourceDownloadDetails() throws Exception {

		return sourceDownloadDao.getSourceDownload();
	}

}
