
package jdz.RTGen.dataType;

import java.util.logging.Logger;

import lombok.Getter;

public abstract class Configurable {
	@Getter private final String name;
	protected final Logger logger = Logger.getGlobal();

	public Configurable() {
		String[] words = getClass().getSuperclass().getSimpleName().split("(?=[A-Z])");

		String name = "";
		for (String word : words)
			name += word + " ";
		this.name = name.substring(0, name.length() - 1);
	}

	public abstract Config getConfig();

}
