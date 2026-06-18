package com.jora.encodedecodeui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.formdev.flatlaf.FlatIntelliJLaf;

@SpringBootApplication(scanBasePackages = { "com.jora.encodedecodeui", "com.jora.encodedecode" })
public class EncodeDecodeUiApplication {
	public static void main(String[] args) {
		try {

			UIManager.setLookAndFeel(new FlatIntelliJLaf());
			SpringApplication app = new SpringApplication(EncodeDecodeUiApplication.class);
			app.setHeadless(false);
			app.run(args);

		} catch (UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Encode Decode", JOptionPane.ERROR_MESSAGE);
		}
	}

}
