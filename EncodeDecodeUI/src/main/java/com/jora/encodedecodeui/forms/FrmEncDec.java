package com.jora.encodedecodeui.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.springframework.stereotype.Component;

import com.jora.encodedecode.service.EncryptionDecryptionService;
import com.jora.encodedecodeui.EncodeDecodeUiApplication;
import com.jora.encodedecodeui.common.CommonValues;

@Component
public class FrmEncDec extends JFrame implements KeyListener, ActionListener, FocusListener {

	private JButton btnEncode, btnDecode, btnViceVersa;

	private JTextField txtPlain, txtFinal;

	private final EncryptionDecryptionService encryptionDecryptionService;

	private static final long serialVersionUID = 1L;

	public FrmEncDec(EncryptionDecryptionService encryptionDecryptionService) throws Exception {
		this.encryptionDecryptionService = encryptionDecryptionService;

		int frameWidth = CommonValues.getWidth();
		int frameHeight = CommonValues.getHeight();

		setTitle("Encode Decode");
		setSize(frameWidth, frameHeight);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);
		setIconImage(new ImageIcon(EncodeDecodeUiApplication.class.getResource("/icons/encrypted.png")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		componentCreation();
		componentListener();
		setVisible(true);
	}

	private void componentListener() throws Exception {
		txtPlain.addKeyListener(this);
		btnEncode.addKeyListener(this);
		btnDecode.addKeyListener(this);
		btnViceVersa.addKeyListener(this);

		btnEncode.addActionListener(this);
		btnDecode.addActionListener(this);
		btnViceVersa.addActionListener(this);

		txtPlain.addFocusListener(this);
		txtFinal.addFocusListener(this);
	}

	private void componentCreation() throws Exception {
		int frameWidth = getWidth();
		int frameHeight = getHeight();

		JLabel lblPlain, lblEncrypt;

		JPanel panelMain = new JPanel();
		panelMain.setLayout(null);
		panelMain.setBounds(0, 0, frameWidth, frameHeight);
		add(panelMain);

		int panelHeightGap = (panelMain.getHeight() * 4 / 100);

		lblPlain = new JLabel("Plain Text:");
		lblPlain.setBounds(frameWidth * 5 / 100, frameHeight * 10 / 100, frameWidth * 30 / 100, frameHeight * 6 / 100);

		txtPlain = new JTextField();
		txtPlain.setBounds(lblPlain.getX(), lblPlain.getY() + lblPlain.getHeight() + panelHeightGap,
				frameWidth * 87 / 100, frameHeight * 10 / 100);

		lblEncrypt = new JLabel("Final Text:");
		lblEncrypt.setBounds(txtPlain.getX(),
				txtPlain.getY() + txtPlain.getHeight() + (panelMain.getHeight() * 4 / 100), lblPlain.getWidth(),
				lblPlain.getHeight());

		txtFinal = new JTextField();
		txtFinal.setBounds(lblEncrypt.getX(), lblEncrypt.getY() + lblEncrypt.getHeight() + panelHeightGap,
				txtPlain.getWidth(), txtPlain.getHeight());
		txtFinal.setEditable(false);

		btnViceVersa = new JButton("↓↑");
		btnViceVersa.setBounds((frameWidth * 45 / 100), txtFinal.getY() + txtFinal.getHeight() + (3 * panelHeightGap),
				frameWidth * 10 / 100, frameHeight * 10 / 100);

		btnEncode = new JButton("Encrypt ↓");
		btnEncode.setBounds(frameWidth * 20 / 100, btnViceVersa.getY(), frameWidth * 25 / 100, frameHeight * 10 / 100);
		btnEncode.setMnemonic(KeyEvent.VK_E);

		btnDecode = new JButton("Decrypt ↑");
		btnDecode.setBounds(frameWidth * 55 / 100, btnEncode.getY(), frameWidth * 25 / 100, frameHeight * 10 / 100);
		btnDecode.setMnemonic(KeyEvent.VK_D);

		panelMain.add(lblPlain);
		panelMain.add(txtPlain);
		panelMain.add(lblEncrypt);
		panelMain.add(txtFinal);
		panelMain.add(btnEncode);
		panelMain.add(btnDecode);
		panelMain.add(btnViceVersa);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (e.getSource() == txtPlain) {
					btnEncode.requestFocus();
				} else if (e.getSource() == btnEncode) {
					btnEncode.doClick();
				} else if (e.getSource() == btnDecode) {
					btnDecode.doClick();
				} else if (e.getSource() == btnViceVersa) {
					btnViceVersa.doClick();
				}
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(this, e2.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == btnEncode) {
				if (txtPlain.getText().isBlank()) {
					JOptionPane.showMessageDialog(this, "Please Enter the text", getTitle(),
							JOptionPane.INFORMATION_MESSAGE);
					txtPlain.requestFocus();
					return;
				}
				txtFinal.setText(encryptionDecryptionService.encrypt(txtPlain.getText()));
			} else if (e.getSource() == btnDecode) {
				if (txtPlain.getText().isBlank()) {
					JOptionPane.showMessageDialog(this, "Please Enter the text", getTitle(),
							JOptionPane.INFORMATION_MESSAGE);
					txtPlain.requestFocus();
					return;
				}
				txtFinal.setText(encryptionDecryptionService.decrypt(txtPlain.getText()));
			} else if (e.getSource() == btnViceVersa) {
				String plainText=txtPlain.getText();
				txtPlain.setText(txtFinal.getText());
				txtFinal.setText(plainText);
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(this, e2.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		try {
			if (e.getSource() == txtPlain) {
				SwingUtilities.invokeLater(() -> {
					txtPlain.selectAll();
				});
			} else if (e.getSource() == txtFinal) {
				SwingUtilities.invokeLater(() -> {
					txtFinal.selectAll();
				});
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(this, e2.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}

}
