package com.source.dbshrink.common;

import java.awt.Color;
import java.awt.Font;

public class Enums {
	public enum CommonEnum {

		LOGINHEADER(Color.decode("#43334C")), LOGINBACKGROUND(Color.decode("#F8F4EC")),
		TEXTBOXFOCUSGAIN(Color.decode("#F2CFAC")), TEXTBOXFOCUSLOST(Color.decode("#ffffff")),
		SRVDETAILS(Color.decode("#D3DAD9")), SRVDETAILSHEAD(Color.decode("#44444E")),
		LOGINHEADFONT(Color.decode("#F9F8F6")), HEADFONT(new Font("Arial Black", Font.BOLD, 24)),
		LABELFONT(new Font("Calibiri", Font.BOLD, 16)), LOGBTNFONT(new Font("Calibiri", Font.BOLD, 12));

		private Color color;
		private Font font;

		private CommonEnum(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}

		private CommonEnum(Font font) {
			this.font = font;
		}

		public Font getFont() {
			return font;
		}

	}

	public enum MessageType {
		Info, Error
	}

	public enum ShrinkType {
		MDF("1"), LDF("0");

		String type;

		ShrinkType(String shringType) {
			this.type = shringType;
		}

		public String getType() {
			return type;
		}

	}

}
