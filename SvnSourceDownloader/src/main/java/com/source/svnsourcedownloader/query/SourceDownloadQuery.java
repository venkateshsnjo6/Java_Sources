package com.source.svnsourcedownloader.query;

import org.springframework.stereotype.Component;

@Component
public class SourceDownloadQuery {

	public String getSourceDownloadDetail() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				"select isnull(Platform,'') as Platform,isnull(FromURL,'') as FromURL,isnull(ToURL,'') as ToURL,isnull(Username,'') as Username,isnull(Password,'') as Password, \n");
		builder.append("isnull(BackUpTime,0) as BackUpTime  from SourceDownload where isnull(PLATFORM,'')='SVN' \n");
		return builder.toString();
	}

}
