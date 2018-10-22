
package jdz.RTGen.application;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import jdz.RTGen.LoggerConfig;

public class Console extends JScrollPane {
	private static final long serialVersionUID = 7276038748388399889L;

	private final JTextArea textArea;

	public Console() {
		super(new JTextArea(12, 80));
		textArea = (JTextArea) getViewport().getView();
		textArea.setBackground(Color.WHITE);
		textArea.setForeground(Color.BLACK);
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));

		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		PrintStream stream = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				textArea.append(String.valueOf((char) b));
			}
		});

		System.setOut(stream);

		Logger.getGlobal().addHandler(new StreamHandler() {
			private SimpleFormatter formatter = LoggerConfig.getFormatter();

			@Override
			public void publish(LogRecord record) {
				flush();
				textArea.append(formatter.format(record));
			}
		});
	}


}
