package com.bergerkiller.imagegenerator_maven_plugin;

import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class Font {
	public static final Font DEFAULT = new Font();

	/**
	 * @parameter
	 */
	private String name = "TimesRoman";

	/**
	 * @parameter
	 */
	private double size = 12.0;

	/**
	 * @parameter
	 */
	private boolean italic = false;

	/**
	 * @parameter
	 */
	private boolean bold = false;

	/**
	 * @parameter
	 */
	private boolean underline = false;

	/**
	 * @parameter
	 */
	private boolean strikethrough = false;

	public void apply(Graphics2D graphics) {
		Map<TextAttribute, Object> attr = new HashMap<TextAttribute, Object>();
		attr.put(TextAttribute.FAMILY, name);
		attr.put(TextAttribute.SIZE, size);
		if (italic) {
			attr.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		}
		if (bold) {
			attr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		}
		if (underline) {
			attr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		}
		if (strikethrough) {
			attr.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		}
		graphics.setFont(new java.awt.Font(attr));
	}
}
