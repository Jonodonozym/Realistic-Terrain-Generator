
package jdz.RTGen.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageExporter {
	public static final File ROOT_FOLDER = new File("Renders");

	public static void exportPNG(BufferedImage image, File file) {
		if (!ROOT_FOLDER.exists())
			ROOT_FOLDER.mkdirs();

		if (!file.getName().endsWith(".png"))
			file = new File(file.getAbsolutePath() + ".png");

		try {
			ImageIO.write(image, "png", file);
			Logger.getGlobal().log(Level.INFO, "Saved file to " + file.getPath() + " (" + getSize(file) + ")");
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String getSize(File file) {
		int radix = 0;
		long size = file.length();
		while (size > 1024 && radix < 3) {
			size /= 1024;
			radix++;
		}

		String rString = "B";
		if (radix == 1)
			rString = "KB";
		if (radix == 2)
			rString = "MB";
		if (radix == 3)
			rString = "GB";
		return size + " " + rString;
	}
}
