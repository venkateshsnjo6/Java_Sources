package com.java.javasecurity;

import java.security.Security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavasecurityApplication {

	public static void main(String[] args) {
		try {
			/*** Algorithm Modification ***/
			String disabledAlgorithm = Security.getProperty("jdk.tls.disabledAlgorithms");
			System.out.println(disabledAlgorithm);
			String[] algorithms = disabledAlgorithm.split(",");

			for (int i = 0; i < algorithms.length; i++) {
				if (algorithms[i].contains("TLSv1") || algorithms[i].contains("TLSv1.1")) {
					algorithms[i] = null;
				}
			}

			String alteredAlgorithms = "";
			for (int i = 0; i < algorithms.length; i++) {
				if (algorithms[i] == null) {
					continue;
				}
				alteredAlgorithms += algorithms[i].concat(",");
			}

			Security.setProperty("jdk.tls.disabledAlgorithms", alteredAlgorithms);
			SpringApplication.run(JavasecurityApplication.class, args);

		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

}
