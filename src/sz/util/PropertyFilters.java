package sz.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

//import crl.debug.TimeProfile;

public class PropertyFilters {
	
	public static Color getColor(String rgba) {
		String[] components = rgba.split(",");
		if (components.length == 4)
			return new Color(inte(components[0]), inte(components[1]), inte(components[2]),inte(components[3]));
		else
			return new Color(inte(components[0]), inte(components[1]), inte(components[2]));
	}
	
	public static int inte(String n){
		return Integer.parseInt(n);
	}
	
	public static double getDouble(String n) {
		return Double.parseDouble(n);
	}
	
	public static final Font loadFont(String fontPath, int style, int ptSize) {
		// 'style' is e.g. Font.PLAIN;
		InputStream fontin = null;
		try {
			// User file? or getResource?
			// To read-as-resource from classpath (eg internal to jar etc)
			//ClassLoader cl = PropertyFilters.class.getClassLoader();
			//fontin = cl.getResourceAsStream(fontPath);
			File fontfile = new File(fontPath);
			fontin = new FileInputStream(fontfile);
			Font font = Font.createFont(Font.TRUETYPE_FONT, fontin);
			return font.deriveFont(style, ptSize);
			
		} catch (FontFormatException ffe) {
			ffe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (fontin != null) {
				try {
					fontin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	// FIXME It's taking about 10 seconds to load v5easter.ttf
	// aside from looking almost unreadable, that font messes up load time!?
	
	// FIXME This seems to called more often than it should during init?
	public static Font loadFont(String fontName, String size) {
//		TimeProfile tp = new TimeProfile("LoadFont-TP");
//		tp.start();
//		tp.mark("Loading Font ["+fontName+"]");
		int fsize = inte(size);
//		tp.mark("casted size to int");
		Font f = loadFont(fontName, Font.PLAIN, fsize);
//		tp.mark("Called loadFont. font loaded.");
//		tp.report();
		return f;
	}
	
	
	public static BufferedImage getImage(String fileName, String bounds) throws Exception {
		Rectangle r = getRectangle(bounds);
		return ImageUtils.crearImagen(fileName, r.x, r.y, r.width, r.height);
	}
	
	public static Rectangle getRectangle(String bounds){
		String [] components = bounds.split(",");
		return new Rectangle(inte(components[0]), inte(components[1]), inte(components[2]), inte(components[3]));
	}
	
	public static Position getPosition(String coord){
		String [] components = coord.split(",");
		return new Position(inte(components[0]), inte(components[1]));
	}
}
