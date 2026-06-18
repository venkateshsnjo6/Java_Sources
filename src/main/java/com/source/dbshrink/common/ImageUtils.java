package com.source.dbshrink.common;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageUtils {

	@SuppressWarnings("unused")
	public static JLabel createImageLabel(String path, int x, int y, int width, int height) {
		ImageIcon icon = null;

		// Load resource from classpath
		URL resource = ImageUtils.class.getResource(path);
		if (resource != null) {
			icon = new ImageIcon(resource);
		} else {
			// Try as URL or file path
			try {
				icon = new ImageIcon(new URL(path));
			} catch (Exception e) {
				icon = new ImageIcon(path); // file path
			}
		}

		if (icon == null) {
			System.err.println("Could not load image: " + path);
			return new JLabel(); // empty label
		}

		// Resize **only if not GIF**
		if (width > 0 && height > 0 && !path.toLowerCase().endsWith(".gif")) {
			Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			icon = new ImageIcon(img);
		}

		JLabel label = new JLabel(icon);
		label.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
		return label;
	}

}
