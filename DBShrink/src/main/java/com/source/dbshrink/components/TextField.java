package com.source.dbshrink.components;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextField extends JTextField {

	public enum TextCase {
		NORMAL, UPPER, LOWER
	}

	public enum TextType {
		ANY, LETTERS, NUMBERS, ALPHANUMERIC, INTEGER, DECIMAL
	}

	private TextCase textCase = TextCase.NORMAL;
	private TextType textType = TextType.ANY;
	private int maxLength = -1;
	private boolean allowSpace = true; // <<< NEW FIELD

	public TextField() {
		super();
		applyDocumentFilter();
	}

	// --- Space Allow Setter ---
	public void setAllowSpace(boolean allowSpace) {
		this.allowSpace = allowSpace;
		applyDocumentFilter();
	}

	// --- Editable property ---
	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
	}

	// --- Font customization ---
	public void setFontProperties(String fontName, int style, int size) {
		setFont(new Font(fontName, style, size));
	}

	// --- Text Case setter ---
	public void setTextCase(TextCase textCase) {
		this.textCase = textCase;
		applyDocumentFilter();
	}

	// --- Text Type setter ---
	public void setTextType(TextType textType) {
		this.textType = textType;
		applyDocumentFilter();
	}

	// --- Max Length setter ---
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		applyDocumentFilter();
	}

	// --- Verify Input When Focus Target ---
	@Override
	public void setVerifyInputWhenFocusTarget(boolean verifyInputWhenFocusTarget) {
		super.setVerifyInputWhenFocusTarget(verifyInputWhenFocusTarget);
	}

	// --- Document Filter Logic ---
	private void applyDocumentFilter() {
		((AbstractDocument) this.getDocument()).setDocumentFilter(new DocumentFilter() {

			@Override
			public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
					throws BadLocationException {
				if (text == null)
					return;
				text = filterText(text, fb, offset);
				if (maxLength < 0 || fb.getDocument().getLength() + text.length() <= maxLength)
					super.insertString(fb, offset, text, attr);
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if (text == null)
					return;
				text = filterText(text, fb, offset);
				if (maxLength < 0 || fb.getDocument().getLength() - length + text.length() <= maxLength)
					super.replace(fb, offset, length, text, attrs);
			}

			private String filterText(String text, FilterBypass fb, int offset) throws BadLocationException {
				// --- Space restriction ---
				if (!allowSpace) {
					text = text.replace(" ", "");
				}

				// --- Text type restriction ---
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

					String current = fb.getDocument().getText(0, fb.getDocument().getLength());

					// Only one decimal point allowed
					if (current.contains(".") && text.contains(".")) {
						text = text.replace(".", "");
					}

					// Minus only at start
					if (text.contains("-") && offset != 0) {
						text = text.replace("-", "");
					}
					break;

				default:
					break;
				}

				// --- Case conversion ---
				if (textCase == TextCase.UPPER) {
					text = text.toUpperCase();
				} else if (textCase == TextCase.LOWER) {
					text = text.toLowerCase();
				}

				return text;
			}
		});
	}
}
