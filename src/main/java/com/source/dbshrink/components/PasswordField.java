package com.source.dbshrink.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class PasswordField extends JPasswordField {

	public enum TextType {
		ANY, LETTERS, NUMBERS, ALPHANUMERIC, INTEGER, DECIMAL
	}

	public enum TextCase {
		NORMAL, UPPER, LOWER
	}

	private TextType textType = TextType.ANY;
	private TextCase textCase = TextCase.NORMAL;
	private boolean allowSpace = true;
	private int maxLength = -1;

	private Image iconShow; // eye-open icon
	private Image iconHide; // eye-closed icon
	private boolean showing = false; // current state

	private int iconSize = 18;
	private int iconPadding = 6;

	public PasswordField() {
		super();

		applyDocumentFilter();

		// Mouse Listener: Toggle password visibility when clicking icon
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = getWidth() - iconSize - iconPadding;
				int y = (getHeight() - iconSize) / 2;

				if (e.getX() >= x && e.getX() <= x + iconSize && e.getY() >= y && e.getY() <= y + iconSize) {

					togglePassword();
				}
			}
		});

		// Allocate space on right for icon
		setMargin(new Insets(0, 0, 0, iconSize + 10));
	}

	// ===== ICONS ============================================================

	public void setIcons(Image show, Image hide) {
		this.iconShow = show;
		this.iconHide = hide;
		repaint();
	}

	private void togglePassword() {
		showing = !showing;
		setEchoChar(showing ? (char) 0 : '•');
		repaint();
	}

	// ===== SETTERS ==========================================================

	public void setAllowSpace(boolean allowSpace) {
		this.allowSpace = allowSpace;
		applyDocumentFilter();
	}

	public void setTextType(TextType type) {
		this.textType = type;
		applyDocumentFilter();
	}

	public void setTextCase(TextCase textCase) {
		this.textCase = textCase;
		applyDocumentFilter();
	}

	public void setMaxLength(int max) {
		this.maxLength = max;
		applyDocumentFilter();
	}

	// ===== DOCUMENT FILTER ==================================================

	private void applyDocumentFilter() {
		((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {

			@Override
			public void insertString(FilterBypass fb, int ofs, String str, AttributeSet a) throws BadLocationException {
				if (str == null)
					return;
				str = filter(str, fb, ofs);
				if (maxLength < 0 || fb.getDocument().getLength() + str.length() <= maxLength)
					super.insertString(fb, ofs, str, a);
			}

			@Override
			public void replace(FilterBypass fb, int ofs, int len, String str, AttributeSet a)
					throws BadLocationException {
				if (str == null)
					return;
				str = filter(str, fb, ofs);
				if (maxLength < 0 || fb.getDocument().getLength() - len + str.length() <= maxLength)
					super.replace(fb, ofs, len, str, a);
			}

			private String filter(String text, FilterBypass fb, int offset) throws BadLocationException {

				if (!allowSpace)
					text = text.replace(" ", "");

				switch (textType) {
				case LETTERS:
					text = text.replaceAll("[^a-zA-Z]", "");
					break;

				case NUMBERS:
					text = text.replaceAll("[^0-9]", "");
					break;

				case ALPHANUMERIC:
					text = text.replaceAll("[^a-zA-Z0-9]", "");
					break;

				case INTEGER:
					text = text.replaceAll("[^-0-9]", "");
					if (text.contains("-") && offset != 0)
						text = text.replace("-", "");
					break;

				case DECIMAL:
					text = text.replaceAll("[^-0-9.]", "");
					String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
					if (cur.contains(".") && text.contains("."))
						text = text.replace(".", "");
					if (text.contains("-") && offset != 0)
						text = text.replace("-", "");
					break;

				default:
					break;
				}

				if (textCase == TextCase.UPPER)
					text = text.toUpperCase();

				if (textCase == TextCase.LOWER)
					text = text.toLowerCase();

				return text;
			}
		});
	}

	// ===== PAINT ICON =======================================================

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (iconShow == null || iconHide == null)
			return;

		Image img = showing ? iconHide : iconShow;

		int x = getWidth() - iconSize - iconPadding;
		int y = (getHeight() - iconSize) / 2;

		g.drawImage(img, x, y, iconSize, iconSize, this);
	}
}
