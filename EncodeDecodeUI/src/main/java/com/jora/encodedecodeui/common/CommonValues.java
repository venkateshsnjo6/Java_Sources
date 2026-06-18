package com.jora.encodedecodeui.common;

import java.awt.Toolkit;

public class CommonValues {
	private static int width = Toolkit.getDefaultToolkit().getScreenSize().width * 40 / 100;
	private static int height = Toolkit.getDefaultToolkit().getScreenSize().height * 30 / 100;

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
}
