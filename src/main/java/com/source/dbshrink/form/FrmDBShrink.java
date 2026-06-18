package com.source.dbshrink.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.source.dbshrink.DbShrinkApplication;
import com.source.dbshrink.common.Enums.CommonEnum;
import com.source.dbshrink.common.Enums.ShrinkType;
import com.source.dbshrink.common.Utils;
import com.source.dbshrink.components.CheckList;
import com.source.dbshrink.components.Item;
import com.source.dbshrink.config.ApplicationConfig;
import com.source.dbshrink.logic.LogicDbShrink;

import jakarta.annotation.PostConstruct;

@Component
public class FrmDBShrink extends JFrame implements KeyListener, ActionListener, FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelMain;
	private JCheckBox chkDatabase;
	private JLabel lblSrvName, lblPortNo, lblUserId, lblTime;
	private CheckList dbCheckList;
	private JButton btnMDF, btnLDF, btnBack;
	public static JButton btnInfoLog, btnErrorLog;
	public static JTextArea txtInfoLog, txtErrorLogArea;
	private JScrollPane scrInfoLog, scrErrorLog;

	@Value("${shrink.mdfShrink:N}")
	private String mdfEnable;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private LogicDbShrink logicDbShrink;

	public FrmDBShrink() {

		getContentPane().setPreferredSize(new Dimension(1000, 700));
		setUndecorated(true);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("DATABASE SHRINK");

	}

	@PostConstruct
	public void init() {
		componentCreation();
		componentListener();

	}

	private void componentListener() {
		chkDatabase.addActionListener(e -> {
			if (!chkDatabase.isSelected()) {
				dbCheckList.setAllChecked(true);
			} else {
				dbCheckList.setAllChecked(false);
			}
		});

	}

	public void loadInitials() {
		try {
			lblSrvName.setText("SERVER NAME       :  " + applicationConfig.getServerName());
			lblPortNo.setText("PORT NO                  :  " + applicationConfig.getPortNo());
			lblUserId.setText("USER NAME            :  " + applicationConfig.getUserName());

			logicDbShrink.loadDatabase(dbCheckList);
			Utils.inizializeLog();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panelMain, e.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Scheduled(cron = "* * * * * *")
	private void setTime() {
		lblTime.setText("TIME                          :  "
				+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")).toUpperCase());
	}

	private void componentCreation() {
		JPanel panelLog, panelHead, panelBottom, panelSrvDetails;
		JLabel lblHead, lblGif;

		/*** Panel Creation ***/

		panelMain = panelCreation(0, 0, getContentPane().getWidth(), getContentPane().getHeight(),
				CommonEnum.SRVDETAILS.getColor(), true);
		panelMain.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, CommonEnum.SRVDETAILSHEAD.getColor()));
		getContentPane().add(panelMain);

		panelHead = panelCreation(0, 0, getContentPane().getWidth(), 60, CommonEnum.SRVDETAILSHEAD.getColor(), true);
		panelMain.add(panelHead);

		panelBottom = panelCreation(0, getContentPane().getHeight() - 60, getContentPane().getWidth(), 60,
				CommonEnum.SRVDETAILSHEAD.getColor(), true);
		panelMain.add(panelBottom);

		panelLog = panelCreation(10, 350, panelMain.getWidth() - 20, 260, CommonEnum.SRVDETAILS.getColor(), true);
		panelLog.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, CommonEnum.LOGINHEADER.getColor()));
		panelMain.add(panelLog);

		panelSrvDetails = panelCreation(550, 80, 350, 250, CommonEnum.SRVDETAILS.getColor(), true);
		panelSrvDetails.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, CommonEnum.SRVDETAILSHEAD.getColor()));
		panelMain.add(panelSrvDetails);

		/*** Label Creation ***/

		lblHead = labelCreation("DATABASE SHRINK", 0, 20, getContentPane().getWidth(), 20,
				CommonEnum.HEADFONT.getFont(), Color.white, true);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		panelHead.add(lblHead);

		lblSrvName = labelCreation("SERVER NAME       :", 10, 20, getContentPane().getWidth(), 20,
				CommonEnum.LABELFONT.getFont(), CommonEnum.SRVDETAILSHEAD.getColor(), true);
		lblSrvName.setHorizontalAlignment(SwingConstants.LEFT);
		panelSrvDetails.add(lblSrvName);

		lblPortNo = labelCreation("PORT NO                  :", 10, lblSrvName.getY() + 50, getContentPane().getWidth(),
				20, CommonEnum.LABELFONT.getFont(), CommonEnum.SRVDETAILSHEAD.getColor(), true);
		lblPortNo.setHorizontalAlignment(SwingConstants.LEFT);
		panelSrvDetails.add(lblPortNo);

		lblUserId = labelCreation("USER NAME            :", 10, lblPortNo.getY() + 50, getContentPane().getWidth(), 20,
				CommonEnum.LABELFONT.getFont(), CommonEnum.SRVDETAILSHEAD.getColor(), true);
		lblUserId.setHorizontalAlignment(SwingConstants.LEFT);
		panelSrvDetails.add(lblUserId);

		lblTime = labelCreation("TIME                          :", 10, lblUserId.getY() + 50,
				getContentPane().getWidth(), 20, CommonEnum.LABELFONT.getFont(), CommonEnum.SRVDETAILSHEAD.getColor(),
				true);
		lblTime.setHorizontalAlignment(SwingConstants.LEFT);
		panelSrvDetails.add(lblTime);

		/*** CheckBox Creation ***/

		chkDatabase = checkBoxCreation("Database", 50, 80, 100, 30, CommonEnum.LABELFONT.getFont(),
				CommonEnum.SRVDETAILSHEAD.getColor(), true);
		panelMain.add(chkDatabase);

		DefaultListModel<Item> model = new DefaultListModel<>();

		model.addElement(new Item("", ""));
		dbCheckList = new CheckList(model);
		dbCheckList.setBounds(chkDatabase.getX() + 130, chkDatabase.getY(), 250, 120);
		dbCheckList.addKeyListener(this);
		panelMain.add(dbCheckList);

		/*** Button Creation ***/

		btnMDF = buttonCreation(btnMDF, "MDF", chkDatabase.getX(), dbCheckList.getY() + 140, 180, 40,
				CommonEnum.LABELFONT.getFont(), CommonEnum.SRVDETAILSHEAD.getColor(), Color.white, KeyEvent.VK_M, true,
				mdfEnable.equalsIgnoreCase("D") ? true : false);
		panelMain.add(btnMDF);

		btnLDF = buttonCreation(btnLDF, "LDF", btnMDF.getX() + 200, btnMDF.getY(), btnMDF.getWidth(),
				btnMDF.getHeight(), CommonEnum.LABELFONT.getFont(), CommonEnum.SRVDETAILSHEAD.getColor(), Color.white,
				KeyEvent.VK_L, true, true);
		panelMain.add(btnLDF);

		btnBack = buttonCreation(btnBack, "BACK", btnMDF.getX(), btnMDF.getY() + 60, 380, btnMDF.getHeight(),
				CommonEnum.LABELFONT.getFont(), CommonEnum.SRVDETAILSHEAD.getColor(), Color.white, KeyEvent.VK_B, true,
				true);
		panelMain.add(btnBack);

		btnInfoLog = buttonCreation(btnInfoLog, "INFO", 0, 0, 75, 20, CommonEnum.LOGBTNFONT.getFont(),
				CommonEnum.SRVDETAILSHEAD.getColor(), Color.white, KeyEvent.VK_I, true, true);
		panelLog.add(btnInfoLog);

		btnErrorLog = buttonCreation(btnErrorLog, "ERROR", btnInfoLog.getX() + btnInfoLog.getWidth() + 5,
				btnInfoLog.getY(), btnInfoLog.getWidth(), btnInfoLog.getHeight(), CommonEnum.LOGBTNFONT.getFont(),
				CommonEnum.SRVDETAILSHEAD.getColor(), Color.white, KeyEvent.VK_E, true, true);
		panelLog.add(btnErrorLog);

		/*** TextArea Creation ***/

		txtInfoLog = txtAreaCreation(txtInfoLog, 5, (btnInfoLog.getY() + btnInfoLog.getHeight()) + 2,
				panelLog.getWidth() - 10, panelLog.getHeight() - 30, CommonEnum.SRVDETAILS.getColor(), false, true,
				true);

		txtErrorLogArea = txtAreaCreation(txtErrorLogArea, 5, (btnInfoLog.getY() + btnInfoLog.getHeight()) + 2,
				panelLog.getWidth() - 10, panelLog.getHeight() - 30, CommonEnum.SRVDETAILS.getColor(), false, true,
				true);

		/*** Scrollpane Creation ***/

		scrInfoLog = scrollPaneCreation(scrInfoLog, txtInfoLog, txtInfoLog.getX(), txtInfoLog.getY(),
				txtInfoLog.getWidth(), txtInfoLog.getHeight(), Color.black, true, false);
		panelLog.add("Messages", scrInfoLog);

		scrErrorLog = scrollPaneCreation(scrErrorLog, txtErrorLogArea, txtErrorLogArea.getX(), txtErrorLogArea.getY(),
				txtErrorLogArea.getWidth(), txtErrorLogArea.getHeight(), Color.black, true, false);
		panelLog.add("Messages", scrErrorLog);

	}

	private JScrollPane scrollPaneCreation(JScrollPane scrollPane, JTextArea txtArea, int x, int y, int width,
			int height, Color backGround, boolean visible, boolean opaque) {
		scrollPane = new JScrollPane(txtArea);
		scrollPane.setBounds(x, y, width, height);
		scrollPane.setBackground(backGround);
		scrollPane.setVisible(visible);
		scrollPane.setOpaque(opaque);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		return scrollPane;
	}

	private JTextArea txtAreaCreation(JTextArea txtArea, int x, int y, int width, int height, Color backGround,
			boolean editable, boolean visible, boolean lineWrap) {
		txtArea = new JTextArea();
		txtArea.setBounds(x, y, width, height);
		txtArea.setBackground(backGround);
		txtArea.setEditable(editable);
		txtArea.setVisible(visible);
		txtArea.setLineWrap(lineWrap);
		return txtArea;
	}

	private JCheckBox checkBoxCreation(String name, int x, int y, int width, int height, Font font, Color foreGround,
			boolean visible) {
		JCheckBox checkBox = new JCheckBox(name);
		checkBox.setBounds(x, y, width, height);
		checkBox.setFont(font);
		checkBox.setForeground(foreGround);
		checkBox.addKeyListener(this);
		checkBox.addFocusListener(this);
		checkBox.setBackground(CommonEnum.SRVDETAILS.getColor());
		return checkBox;
	}

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			if (e.getSource() == btnMDF) {

				if (dbCheckList.getSelectedKeys().equalsIgnoreCase("")) {
					throw new Exception("Select Atleast One DB Name");
				}

				shrinkExecution(dbCheckList.getSelectedKeys(), ShrinkType.MDF.getType());
			} else if (e.getSource() == btnLDF) {

				if (dbCheckList.getSelectedKeys().equalsIgnoreCase("")) {
					throw new Exception("Select Atleast One DB Name");
				}

				shrinkExecution(dbCheckList.getSelectedKeys(), ShrinkType.LDF.getType());

			} else if (e.getSource() == btnBack) {
				FrmServerDetails frmServerDetails = DbShrinkApplication.applicationContext
						.getBean(FrmServerDetails.class);
				this.dispose();
				frmServerDetails.setVisible(true);
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(panelMain, e2.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
		}

	}

	private void shrinkExecution(String dbNames, String type) throws Exception {
		Utils.inizializeLog();
		btnInfoLog.doClick();
		btnMDF.setEnabled(false);
		btnLDF.setEnabled(false);

		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				logicDbShrink.shrink(dbNames, type);
				JOptionPane.showMessageDialog(panelMain, "DB Shrinked Successfully", getTitle(),
						JOptionPane.INFORMATION_MESSAGE);
				btnMDF.setEnabled(mdfEnable.equalsIgnoreCase("D") ? true : false);
				btnLDF.setEnabled(true);
			}
		});

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (e.getSource() == chkDatabase) {
					dbCheckList.requestFocus();
				} else if (e.getSource() == dbCheckList) {
					if (btnMDF.isEnabled()) {
						btnMDF.requestFocus();
					} else {
						btnLDF.requestFocus();
					}
				} else if (e.getSource() == btnMDF) {
					btnMDF.doClick();
				} else if (e.getSource() == btnLDF) {
					btnLDF.doClick();
				} else if (e.getSource() == btnBack) {
					btnBack.doClick();
				}

			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (e.getSource() == btnMDF) {
					btnLDF.requestFocus();
				} else if (e.getSource() == chkDatabase) {
					dbCheckList.requestFocus();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (e.getSource() == btnLDF) {
					btnMDF.requestFocus();
				} else if (e.getSource() == dbCheckList) {
					chkDatabase.requestFocus();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (e.getSource() == btnMDF || e.getSource() == btnLDF) {
					btnBack.requestFocus();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (e.getSource() == btnMDF || e.getSource() == btnLDF) {
					dbCheckList.requestFocus();
				} else if (e.getSource() == btnBack) {
					btnMDF.requestFocus();
				}
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(panelMain, e2.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	private JButton buttonCreation(JButton button, String name, int x, int y, int width, int height, Font font,
			Color color, Color foreGround, int mnemonic, boolean visible, boolean enable) {
		button = new JButton(name);
		button.setBounds(x, y, width, height);
		button.setBackground(color);
		button.setForeground(foreGround);
		button.setFont(font);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setVisible(visible);
		button.setEnabled(enable);
		button.addKeyListener(this);
		button.addActionListener(this);
		button.addFocusListener(this);
		button.setOpaque(true);
		button.setMnemonic(mnemonic);

		return button;
	}

	private JPanel panelCreation(int x, int y, int width, int height, Color backGround, boolean visible) {
		JPanel panel = new JPanel(null);
		panel.setBounds(x, y, width, height);
		panel.setBackground(backGround);
		panel.setVisible(visible);
		return panel;
	}

	private JLabel labelCreation(String lblName, int x, int y, int widthPer, int heightPer, Font font, Color color,
			boolean visible) {
		JLabel label = new JLabel(lblName);
		label.setBounds(x, y, widthPer, heightPer);
		label.setFont(font);
		label.setForeground(color);
		label.setVisible(visible);

		return label;

	}

}
