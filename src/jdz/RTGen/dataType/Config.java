
package jdz.RTGen.dataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public abstract class Config {
	private List<Field> fields = new ArrayList<>();
	@Getter private List<String> fieldNames = new ArrayList<>();

	public Config() {
		for (Field field : getClass().getDeclaredFields()) {
			if (field.getName().startsWith("this"))
				continue;
			field.setAccessible(true);
			fields.add(field);
			fieldNames.add(field.getName().toLowerCase().replaceAll("_", " "));
		}
	}

	public boolean isInteger(String name) {
		Field field = getField(name);
		return field.getType().equals(int.class) || field.getType().equals(Integer.class);
	}

	public void set(String name, float value) {
		try {
			if (isInteger(name))
				getField(name).set(this, (int) value);
			else
				getField(name).set(this, value);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public float get(String name) {
		try {
			return getField(name).getFloat(this);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private Field getField(String name) {
		int index = fieldNames.indexOf(name);
		if (index == -1)
			throw new IllegalArgumentException(name + " is not a field");
		return fields.get(index);
	}
}
