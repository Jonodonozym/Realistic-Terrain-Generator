
package jdz.RTGen;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {
	public static void init() {
		Logger logger = Logger.getGlobal();
		logger.setUseParentHandlers(false);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(getFormatter());
		logger.addHandler(handler);
	}

	public static SimpleFormatter getFormatter() {
		return new SimpleFormatter() {
			private static final String format = "[%1$tT] [%2$-7s] %3$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
						lr.getMessage());
			}
		};
	}

}
