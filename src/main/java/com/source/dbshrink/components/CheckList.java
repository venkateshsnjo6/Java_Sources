package com.source.dbshrink.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CheckList extends JPanel {

	private JList<Item> list;

	public CheckList(ListModel<Item> model) {
		super(new BorderLayout());

		list = new JList<>(model);

		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		list.setCellRenderer(new CheckRenderer());

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = list.locationToIndex(e.getPoint());
				if (index >= 0) {
					// Ensure row becomes selected
					list.setSelectedIndex(index); // <-- IMPORTANT FIX

					Item it = list.getModel().getElementAt(index);
					it.checked = !it.checked;

					list.repaint(list.getCellBounds(index, index));
				}
			}
		});

		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					int index = list.getSelectedIndex(); // current focused row
					if (index >= 0) {
						Item it = list.getModel().getElementAt(index);
						it.checked = !it.checked; // toggle
						list.repaint();
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane(list);
		add(scroll, BorderLayout.CENTER);
	}

	/** Access inner JList if needed */
	public JList<Item> getInnerList() {
		return list;
	}

	/** Returns checked keys as List */
	public List<Object> getCheckedKeyList() {
		List<Object> keys = new ArrayList<>();
		ListModel<Item> model = list.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			Item it = model.getElementAt(i);
			if (it.checked)
				keys.add(it.key);
		}
		return keys;
	}

	/** Returns checked values as List */
	public List<Object> getCheckedValueList() {
		List<Object> values = new ArrayList<>();
		ListModel<Item> model = list.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			Item it = model.getElementAt(i);
			if (it.checked)
				values.add(it.value);
		}
		return values;
	}

	/** Returns checked keys as comma-separated String */
	public String getCheckedKeys() {
		List<Object> keys = getCheckedKeyList();
		return String.join(",", keys.stream().map(Object::toString).toArray(String[]::new));
	}

	/** Returns checked values as comma-separated String */
	public String getCheckedValues() {
		List<Object> values = getCheckedValueList();
		return String.join(",", values.stream().map(Object::toString).toArray(String[]::new));
	}

	/** Returns selected keys as List (based on JList selection) */
	public List<Object> getSelectedKeyList() {
		List<Object> keys = new ArrayList<>();
		for (int index : list.getSelectedIndices()) {
			keys.add(list.getModel().getElementAt(index).key);
		}
		return keys;
	}

	/** Returns selected values as List (based on JList selection) */
	public List<Object> getSelectedValueList() {
		List<Object> values = new ArrayList<>();
		for (int index : list.getSelectedIndices()) {
			values.add(list.getModel().getElementAt(index).value);
		}
		return values;
	}

	/** Returns selected keys as comma-separated String */
	public String getSelectedKeys() {
		List<Object> keys = getCheckedKeyList();
		return String.join(",", keys.stream().map(Object::toString).toArray(String[]::new));
	}

	/** Returns selected values as comma-separated String */
	public String getSelectedValues() {
		List<Object> values = getCheckedValueList();
		return String.join(",", values.stream().map(Object::toString).toArray(String[]::new));
	}

	public void setModelItems(List<Item> items) {
		DefaultListModel<Item> model = new DefaultListModel<>();

		if (items == null || items.isEmpty()) {
			// optional: show one empty item so the list is visible
			model.addElement(new Item("", ""));
		} else {
			for (Item it : items) {
				model.addElement(it);
			}
		}

		list.setModel(model);
		list.repaint();
	}

	public void setAllChecked(boolean checked) {
		ListModel<Item> model = list.getModel();
		checked = !checked;
		for (int i = 0; i < model.getSize(); i++) {
			Item it = model.getElementAt(i);
			it.checked = checked;
		}

		if (checked) {
			int size = model.getSize();
			if (size > 0) {
				list.setSelectionInterval(0, size - 1);
			}
		} else {
			list.clearSelection();
		}

		list.repaint();
	}
}
