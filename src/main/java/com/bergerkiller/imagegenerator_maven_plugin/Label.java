package com.bergerkiller.imagegenerator_maven_plugin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.Field;

public class Label {
	/**
	 * Label text
	 * 
	 * @parameter
	 * @required
	 */
	private String text;

	/**
	 * X-position of the label
	 * 
	 * @parameter
	 * @required
	 */
	private int posX;

	/**
	 * Y-position of the label
	 * 
	 * @parameter
	 * @required
	 */
	private int posY;

	/**
	 * The Font to use for drawing
	 * 
	 * @parameter
	 */
	private Font font = Font.DEFAULT;

	/**
	 * The Alignment to use for the text
	 * 
	 * @parameter
	 */
	private Align align = Align.RIGHT;

	/**
	 * The colors to use for drawing (constant names or r/g/b/(a))
	 * 
	 * @parameter
	 */
	private String color = "BLACK";

	private java.awt.Color colorAwt;

	public java.awt.Color getColor() {
		if (colorAwt == null) {
			try {
			    Field field = Color.class.getField(color);
			    colorAwt = (Color) field.get(null);
			} catch (Exception e) {
			}
			if (colorAwt == null) {
				String[] parts = color.split("/");
				int r = 0, g = 0, b = 0, a = 255;
				if (parts.length >= 3) {
					r = Util.tryParseInt(parts[0], r);
					g = Util.tryParseInt(parts[1], g);
					b = Util.tryParseInt(parts[2], b);
					if (parts.length == 4) {
						a = Util.tryParseInt(parts[3], a);
					}
				}
				r = Util.clamp(r, 0, 255);
				g = Util.clamp(g, 0, 255);
				b = Util.clamp(b, 0, 255);
				a = Util.clamp(a, 0, 255);
				colorAwt = new Color(r, g, b, a);
			}
		}
		return colorAwt;
	}

	public void draw(Graphics2D graphics, int width, int height) {
		// Font
		font.apply(graphics);
		// Color
		graphics.setColor(getColor());
		// Calculate the offset to use
		int textWidth = graphics.getFontMetrics().stringWidth(text);
		int textOffset = 0;
		if (align == Align.LEFT) {
			textOffset = textWidth;
		} else if (align == Align.CENTER) {
			textOffset = textWidth / 2;
		}
		// Text
		graphics.drawString(text, posX - textOffset, posY);
	}
}
