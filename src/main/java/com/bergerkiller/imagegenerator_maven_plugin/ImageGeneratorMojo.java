package com.bergerkiller.imagegenerator_maven_plugin;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which generates a version image
 *
 * @goal generate_image
 *
 * @phase package
 */
public class ImageGeneratorMojo extends AbstractMojo {
	private static final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
	private static final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

	/**
	 * Working directory (where to put the temporary folder)
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File targetDirectory;

	/**
	 * Current version that was built
	 * 
	 * @parameter expression="${background}"
	 * @required
	 */
	private String background;

	/**
	 * Name for the output image
	 * 
	 * @parameter expression="${name}"
	 * @required
	 */
	private String name;

	/**
	 * All labels to draw onto the image
	 * 
	 * @parameter
	 */
	private List<Label> labels;

	/**
	 * Download the background, load image, paint labels and save
	 */
	public void execute() throws MojoExecutionException {
		if (background == null) {
			throw new MojoExecutionException("Please specify the <background> for the image in the configuration!");
		}
		if (name == null) {
			throw new MojoExecutionException("Please specify the <name> for the output image in the configuration!");
		}
		if (targetDirectory == null) {
			throw new MojoExecutionException("project.build.directory is not set (for some reason), what?");
		}
		File workingDirectory = new File(targetDirectory, "imagegenerator");
		workingDirectory.mkdirs();

		// Obtain the background source image
		File bgFile = new File(background);
		if (!bgFile.exists()) {
			// Parse URL
			URL website;
			try {
				website = new URL(background);
			} catch (MalformedURLException ex) {
				getLog().error("The <background> does not represent a valid URL, or the file does not exist on disk");
				return;
			}
			String name = website.getPath().substring(website.getPath().lastIndexOf('/') + 1);
			bgFile = new File(workingDirectory, website.getHost().replace('.', '_') + website.getPath().replace('/', '_'));

			// Download the background image if not already downloaded (try to parse it as an URL)
			if (!bgFile.exists()) {
				getLog().info("Downloading background image '" + name + "'...");
				try {
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream fos = new FileOutputStream(bgFile);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				} catch (Throwable t) {
					getLog().error("Could not find background file locally and finding it online failed:", t);
					return;
				}
			}
		}
		// Try to load the image
		String outputFormat = name.substring(name.lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH);
		boolean hasAlpha = outputFormat.equals("png") || outputFormat.equals("gif");
		BufferedImage image;
		int width, height;
		try {
			image = ImageIO.read(bgFile);	
			width = image.getWidth();
			height = image.getHeight();
			if (!hasAlpha) {
				// Discard of the alpha channel here
				PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
				pg.grabPixels();
				DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), width * height);
				WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
				image = new BufferedImage(RGB_OPAQUE, raster, false, null);
			}
		} catch (Exception e) {
			getLog().error("Failed to read background image:", e);
			return;
		}
		// Now let's operate on it
		if (labels != null) {
			Graphics2D g = image.createGraphics();
			try {
				for (Label label : labels) {
					label.draw(g, width, height);
				}
			} catch (Throwable t) {
				getLog().error("Failed to render image:", t);
				return;
			}
			g.dispose();
		}
		// And save it
		try {
			ImageIO.write(image, outputFormat, new File(targetDirectory, name));
		} catch (IOException e) {
			getLog().error("Failed to write resulting image to '" + name + "':", e);
			return;
		}
		// Done!
		getLog().info("Image '" + name + "' generated!");
	}
}
