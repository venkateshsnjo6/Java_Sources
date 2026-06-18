package com.source.dbshrink.components;

import javax.swing.*;
import java.awt.*;

public class CheckRenderer extends JCheckBox implements ListCellRenderer<Item> {

	private static final long serialVersionUID = 1L;

	public CheckRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Item> list, Item item, int index, boolean isSelected,
			boolean cellHasFocus) {

		setText(String.valueOf(item.key));
		setSelected(item.checked);

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
}
