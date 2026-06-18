
package com.source.dbshrink.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source.dbshrink.DbShrinkApplication;
import com.source.dbshrink.common.Enums.CommonEnum;
import com.source.dbshrink.common.ImageUtils;
import com.source.dbshrink.components.PasswordField;
import com.source.dbshrink.components.TextField;
import com.source.dbshrink.components.TextField.TextType;
import com.source.dbshrink.config.ApplicationConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class FrmServerDetails extends JFrame implements KeyListener, ActionListener, FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelMain;
	private TextField txtServerName, txtPortNo, txtUserId;
	private PasswordField txtPassWord;
	private JButton btnConnect, btnExit;

	@Autowired
	private ApplicationConfig applicationConfig;

	public FrmServerDetails() {
		getContentPane().setPreferredSize(new Dimension(1000, 700));
		setUndecorated(true);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Server Details");
		componentCreation();
		loadInitials();
	}

	public void loadInitials() {
		txtServerName.setText("");
		txtPortNo.setText("");
		txtUserId.setText("");
		txtPassWord.setText("");

		txtServerName.requestFocus();
	}

	private void componentCreation() {
		JPanel panelContent, panelHead, panelBottom;
		JLabel lblHead, lblSrvName, lblPortNo, lblUserId, lblPassWord, lblGif;

		/*** Panel Creation ***/

		panelMain = panelCreation(0, 0, getContentPane().getWidth(), getContentPane().getHeight(),
				CommonEnum.LOGINBACKGROUND.getColor(), true);
		panelMain.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, CommonEnum.LOGINHEADER.getColor()));
		getContentPane().add(panelMain);

		panelHead = panelCreation(0, 0, getContentPane().getWidth(), 60, CommonEnum.LOGINHEADER.getColor(), true);
		panelMain.add(panelHead);

		panelBottom = panelCreation(0, getContentPane().getHeight() - 60, getContentPane().getWidth(), 60,
				CommonEnum.LOGINHEADER.getColor(), true);
		panelMain.add(panelBottom);

		panelContent = panelCreation(450, 100, 500, 500, CommonEnum.LOGINBACKGROUND.getColor(), true);
		panelContent.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, CommonEnum.LOGINHEADER.getColor()));
		panelMain.add(panelContent);

		/*** Label Creation ***/

		lblHead = labelCreation("SERVER DETAILS", 0, 20, getContentPane().getWidth(), 20, CommonEnum.HEADFONT.getFont(),
				Color.white, true);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		panelHead.add(lblHead);

		lblSrvName = labelCreation("SERVER NAME        :", 20, 30, 200, 20, CommonEnum.LABELFONT.getFont(),
				CommonEnum.LOGINHEADER.getColor(), true);
		panelContent.add(lblSrvName);

		lblPortNo = labelCreation("PORT NO                   :", lblSrvName.getX(), lblSrvName.getY() + 80,
				lblSrvName.getWidth(), lblSrvName.getHeight(), CommonEnum.LABELFONT.getFont(),
				CommonEnum.LOGINHEADER.getColor(), true);
		panelContent.add(lblPortNo);

		lblUserId = labelCreation("USER ID                    :", lblSrvName.getX(), lblPortNo.getY() + 80,
				lblSrvName.getWidth(), lblSrvName.getHeight(), CommonEnum.LABELFONT.getFont(),
				CommonEnum.LOGINHEADER.getColor(), true);
		panelContent.add(lblUserId);

		lblPassWord = labelCreation("PASSWORD            :", lblSrvName.getX(), lblUserId.getY() + 80,
				lblSrvName.getWidth(), lblSrvName.getHeight(), CommonEnum.LABELFONT.getFont(),
				CommonEnum.LOGINHEADER.getColor(), true);
		panelContent.add(lblPassWord);

		lblGif = ImageUtils.createImageLabel("/icons/DBImage.png", -200, -60, 800, 800);
		lblGif.setVisible(true);
		panelMain.add(lblGif);

		/*** TextBox Creation ***/

		txtServerName = txtBoxCreation(txtServerName, "", 200, lblSrvName.getY(), 250, 30, true,
				CommonEnum.LABELFONT.getFont(), 60, true, TextType.ANY, false, true);
		panelContent.add(txtServerName);

		txtPortNo = txtBoxCreation(txtPortNo, "", txtServerName.getX(), lblPortNo.getY(), txtServerName.getWidth(),
				txtServerName.getHeight(), true, CommonEnum.LABELFONT.getFont(), 5, true, TextType.INTEGER, false,
				true);
		panelContent.add(txtPortNo);

		txtUserId = txtBoxCreation(txtUserId, "", txtServerName.getX(), lblUserId.getY(), txtServerName.getWidth(),
				txtServerName.getHeight(), true, CommonEnum.LABELFONT.getFont(), 10, true, TextType.LETTERS, false,
				true);
		panelContent.add(txtUserId);

		txtPassWord = txtPassWordField(txtPassWord, "", txtServerName.getX(), lblPassWord.getY(),
				txtServerName.getWidth(), txtServerName.getHeight(), CommonEnum.LABELFONT.getFont(), 60, true, true);
		panelContent.add(txtPassWord);

		/*** Button Creation ***/

		btnConnect = buttonCreation(btnConnect, "CONNECT", lblPassWord.getX() + 20, lblPassWord.getY() + 100, 180, 40,
				CommonEnum.LABELFONT.getFont(), CommonEnum.LOGINHEADER.getColor(), Color.white, KeyEvent.VK_C, true);
		panelContent.add(btnConnect);

		btnExit = buttonCreation(btnExit, "EXIT", btnConnect.getX() + 250, btnConnect.getY(), btnConnect.getWidth(),
				btnConnect.getHeight(), CommonEnum.LABELFONT.getFont(), CommonEnum.LOGINHEADER.getColor(), Color.white,
				KeyEvent.VK_X, true);
		panelContent.add(btnExit);

	}

	private JButton buttonCreation(JButton button, String name, int x, int y, int width, int height, Font font,
			Color color, Color foreGround, int mnemonic, boolean visible) {
		button = new JButton(name);
		button.setBounds(x, y, width, height);
		button.setBackground(color);
		button.setForeground(foreGround);
		button.setFont(font);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setVisible(visible);
		button.addKeyListener(this);
		button.addActionListener(this);
		button.addFocusListener(this);
		button.setOpaque(true);
		button.setMnemonic(mnemonic);

		return button;
	}

	private PasswordField txtPassWordField(PasswordField passwordField, String text, int x, int y, int width,
			int height, Font font, int maxLength, boolean allowSpace, boolean visible) {
		passwordField = new PasswordField();
		passwordField.setBounds(x, y, width, height);
		passwordField.setText(text);
		passwordField.setMaxLength(maxLength);
		passwordField.setAllowSpace(allowSpace);
		passwordField.setVisible(visible);
		passwordField.setFont(font);
		passwordField.addKeyListener(this);
		passwordField.addFocusListener(this);
		passwordField.setIcons(new ImageIcon(getClass().getResource("/icons/eye_open.png")).getImage(),
				new ImageIcon(getClass().getResource("/icons/eye_closed.png")).getImage());

		return passwordField;
	}

	private JPanel panelCreation(int x, int y, int width, int height, Color backGround, boolean visible) {
		JPanel panel = new JPanel(null);
		panel.setBounds(x, y, width, height);
		panel.setBackground(backGround);
		panel.setVisible(visible);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == btnConnect) {

				enterFromServerName();
				enterFromPortNo();
				enterFromUserId();
				enterFromPassword();

				DataSource masterConn = applicationConfig.getDataSource(txtServerName.getText(),
						Integer.parseInt(txtPortNo.getText()), txtPassWord.getText(), txtUserId.getText(), "master");

				FrmDBShrink frmDBShrink = DbShrinkApplication.applicationContext.getBean(FrmDBShrink.class);
				frmDBShrink.loadInitials();
				this.dispose();
				frmDBShrink.setVisible(true);

			} else if (e.getSource() == btnExit) {

				if (JOptionPane.showConfirmDialog(panelMain, "Are You Sure to Exit?", getTitle(),
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}

			}
		} catch (Exception e2) {
			if (e2.getCause() instanceof SQLException) {
				JOptionPane.showMessageDialog(panelMain, "Check Server Details", getTitle(),
						JOptionPane.WARNING_MESSAGE);
				loadInitials();
			} else {
				JOptionPane.showMessageDialog(panelMain, e2.getMessage(), getTitle(), JOptionPane.WARNING_MESSAGE);
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (e.getSource() == txtServerName) {
					enterFromServerName();
					txtPortNo.requestFocus();
				} else if (e.getSource() == txtPortNo) {
					enterFromPortNo();
					txtUserId.requestFocus();
				} else if (e.getSource() == txtUserId) {
					enterFromUserId();
					txtPassWord.requestFocus();
				} else if (e.getSource() == txtPassWord) {
					enterFromPassword();
					btnConnect.requestFocus();
				} else if (e.getSource() == btnConnect) {
					btnConnect.doClick();
				} else if (e.getSource() == btnExit) {
					btnExit.doClick();
				}

			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (e.getSource() == btnConnect) {
					btnExit.requestFocus();
					;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (e.getSource() == btnExit) {
					btnConnect.requestFocus();
				}
			}

		} catch (Exception e2) {
			JOptionPane.showMessageDialog(panelMain, e2.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void enterFromServerName() throws Exception {
		if (txtServerName.getText().equalsIgnoreCase("")) {
			txtServerName.requestFocus();
			throw new Exception("Server Name Should not be Empty...!");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

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

	private TextField txtBoxCreation(TextField txtBox, String text, int x, int y, int widthPer, int heightPer,
			boolean editable, Font font, int maxLength, boolean inputVerifier, TextType textInputType,
			boolean allowSpace, boolean visible) {

		txtBox = new TextField();
		txtBox.setBounds(x, y, widthPer, heightPer);
		txtBox.setText(text);
		txtBox.setEditable(editable);
		txtBox.setFont(font);
		txtBox.setAllowSpace(allowSpace);
		txtBox.setMaxLength(maxLength);
		txtBox.setVerifyInputWhenFocusTarget(inputVerifier);
		txtBox.setTextType(textInputType);
		txtBox.addKeyListener(this);
		txtBox.addFocusListener(this);
		txtBox.setVisible(visible);

		return txtBox;
	}

	@Override
	public void focusGained(FocusEvent e) {
		try {
			if (e.getComponent() instanceof TextField || e.getComponent() instanceof PasswordField) {
				e.getComponent().setBackground(CommonEnum.TEXTBOXFOCUSGAIN.getColor());
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(panelMain, e2.getMessage(), getTitle(), JOptionPane.WARNING_MESSAGE);
		}

	}

	@Override
	public void focusLost(FocusEvent e) {
		try {
			if (e.getComponent() instanceof TextField || e.getComponent() instanceof PasswordField) {
				e.getComponent().setBackground(CommonEnum.TEXTBOXFOCUSLOST.getColor());
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(panelMain, e2.getMessage(), getTitle(), JOptionPane.WARNING_MESSAGE);
		}

	}

	private void enterFromPassword() throws Exception {
		if (txtPassWord.getText().equalsIgnoreCase("")) {
			txtPassWord.requestFocus();
			throw new Exception("Password Should not be Empty...!");
		}

	}

	private void enterFromUserId() throws Exception {
		if (txtUserId.getText().equalsIgnoreCase("")) {
			txtUserId.requestFocus();
			throw new Exception("UserId Should not be Empty...!");
		}
	}

	private void enterFromPortNo() throws Exception {
		if (txtPortNo.getText().equalsIgnoreCase("")) {
			txtPortNo.requestFocus();
			throw new Exception("PortNo Should not be Empty...!");
		}
	}
}
