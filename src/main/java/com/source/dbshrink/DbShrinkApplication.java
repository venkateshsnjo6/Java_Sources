package com.source.dbshrink;

import java.security.Security;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.source.dbshrink.form.FrmDBShrink;
import com.source.dbshrink.form.FrmServerDetails;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = { "com.source.dbshrink.*" })
@EnableScheduling
public class DbShrinkApplication implements ApplicationContextAware {
	public static ApplicationContext applicationContext;

	public static void main(String[] args) {
		try {

			/*** Disabled Algorithm ***/
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

			UIManager.setLookAndFeel(new MetalLookAndFeel());

			new SpringApplicationBuilder(DbShrinkApplication.class).headless(false).run(args);

			FrmServerDetails frmServerDetails = applicationContext.getBean(FrmServerDetails.class);
			frmServerDetails.loadInitials();
			frmServerDetails.setVisible(true);

//			FrmDBShrink frmDBShrink = applicationContext.getBean(FrmDBShrink.class);
//			frmDBShrink.setVisible(true);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(1);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		DbShrinkApplication.applicationContext = applicationContext;

	}

}
