
package jdz.RTGen.configuration;

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
			if (field.getAnnotation(Ignore.class) != null)
				continue;
			field.setAccessible(true);
			fields.add(field);
			fieldNames.add(field.getName().toLowerCase().replaceAll("_", " "));
		}
	}

	public boolean isBoolean(String name) {
		Field field = getField(name);
		return field.getType().equals(boolean.class) || field.getType().equals(Boolean.class);
	}

	public boolean isInteger(String name) {
		Field field = getField(name);
		return field.getType().equals(int.class) || field.getType().equals(Integer.class);
	}

	public boolean isFloat(String name) {
		Field field = getField(name);
		return field.getType().equals(float.class) || field.getType().equals(Float.class)
				|| field.getType().equals(double.class) || field.getGenericType().equals(double.class);
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

	public void set(String name, boolean value) {
		try {
			getField(name).set(this, value);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public int getInt(String name) {
		try {
			return getField(name).getInt(this);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public float getFloat(String name) {
		try {
			return getField(name).getFloat(this);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean getBoolean(String name) {
		try {
			return getField(name).getBoolean(this);
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean hasStep(String name) {
		return getField(name).getAnnotation(Step.class) != null;
	}

	public float getStepFloat(String name) {
		Field field = getField(name);
		if (field.getAnnotation(Step.class) == null)
			return 1.f;
		return field.getAnnotation(Step.class).value();
	}

	public int getStepInt(String name) {
		return (int) getStepFloat(name);
	}

	private Field getField(String name) {
		int index = fieldNames.indexOf(name);
		if (index == -1)
			throw new IllegalArgumentException(name + " is not a field");
		return fields.get(index);
	}

	public int numFields() {
		return fields.size();
	}
}
