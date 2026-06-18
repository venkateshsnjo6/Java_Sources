package com.source.svnsourcedownloader.start;

import java.security.Security;

import javax.swing.JOptionPane;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { "com.source.svnsourcedownloader.*" })
@EnableScheduling
public class SvnSourceDownloaderApplication implements ApplicationContextAware {
	public static ApplicationContext applicationContext;

	public static void main(String[] args) {

		try {
			// TLSv1, TLSv1.1
			String disabledAlgorithms = Security.getProperty("jdk.tls.disabledAlgorithms");
			String[] algorithms = disabledAlgorithms.split(",");
			for (int i = 0; i < algorithms.length; i++) {
				String string = algorithms[i];
				if ("TLSv1".contains(string.trim()) || "TLSv1.1".contains(string.trim())) {
					algorithms[i] = null;
				}
			}
			String altered = "";
			for (int i = 0; i < algorithms.length; i++) {
				String string = algorithms[i];
				if (null == string) {
					continue;
				}

				altered += string + ",";
			}
			Security.setProperty("jdk.tls.disabledAlgorithms", altered);
			new SpringApplicationBuilder(SvnSourceDownloaderApplication.class).headless(false).run(args);
			DownloadClass downloadClass = SvnSourceDownloaderApplication.applicationContext
					.getBean(DownloadClass.class);
			downloadClass.process();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SvnSourceDownloaderApplication.applicationContext = applicationContext;

	}

}
