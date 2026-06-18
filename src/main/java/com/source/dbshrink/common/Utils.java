package com.source.dbshrink.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.source.dbshrink.common.Enums.MessageType;
import com.source.dbshrink.form.FrmDBShrink;

public class Utils {
	private static int errCount = 0;

	public static void writeLog(MessageType messageType, String msg, Class<?> logclass) {
		Logger logger = LogManager.getLogger(logclass);
		if (MessageType.Info == messageType) {
			if (null != FrmDBShrink.txtInfoLog) {
				logger.info(msg);
				FrmDBShrink.txtInfoLog.append(msg.concat("\n"));
				FrmDBShrink.txtInfoLog.setCaretPosition(FrmDBShrink.txtInfoLog.getText().length());
			}
		} else if (MessageType.Error == messageType) {
			if (null != FrmDBShrink.txtErrorLogArea) {
				++errCount;
				logger.error(msg);
				FrmDBShrink.btnErrorLog.setText("Error(" + errCount + ")");
				FrmDBShrink.txtErrorLogArea.append(msg.concat("\n"));
				FrmDBShrink.txtErrorLogArea.setCaretPosition(FrmDBShrink.txtErrorLogArea.getText().length());
			}
		}

	}

	public static void inizializeLog() {
		FrmDBShrink.txtInfoLog.setText("");
		FrmDBShrink.txtErrorLogArea.setText("");
		errCount = 0;
		FrmDBShrink.btnErrorLog.setText("Error");
	}

}
