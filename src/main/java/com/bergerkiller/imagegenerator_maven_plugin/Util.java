package com.bergerkiller.imagegenerator_maven_plugin;

public class Util {
	/**
	 * Returns the default value if the input value is null
	 * 
	 * @param value to fix
	 * @param def to return if the value is null
	 * @return the value or the default
	 */
	public static <T> T fixNull(T value, T def) {
		return value == null ? def : value;
	}

	public static int tryParseInt(String text, int def) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException ex) {
			return def;
		}
	}

	public static int clamp(int value, int min, int max) {
		return value < min ? min : (value > max ? max : value);
	}
}
