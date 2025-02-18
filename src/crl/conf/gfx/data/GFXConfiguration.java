package crl.conf.gfx.data;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Properties;

//import crl.debug.TimeProfile;
import crl.game.Game;
import sz.util.Position;
import sz.util.PropertyFilters;

/**
 * Settings for GFX UI
 * 
 * @author Tuukka Turto
 */
public class GFXConfiguration {

	public int
		bigTileWidth,
		
		tileWidth,	// "width of a (normal) tile"
		
		cellHeight,
		
		screenWidth,
		screenHeight,
		
		// FIXME: Overspecified. if we know screenW/H, and tileWidth... calc.
		screenWidthInTiles,
		screenHeightInTiles;
		
	public Color
		windowBackgroundColour,
		borderColourInner,
		borderColourOuter;
	
	public Font
		messageBoxFont,
		persistentMessageBoxFont;
	
	public BufferedImage
		statusScreenBackground,
		userInterfaceBackgroundImage;
	
//	public Textures textures;
	
	public double screenScale;

	public int effectsScale;
	public int viewportUserInterfaceScale;
	
	public int cameraScale;
	public Position cameraPosition;
	public Position playerLocationOnScreen;


	// *THIS* is *SLOW*. 10secs during profiling. *WHY*? 'self.time'.
	// Textures.load - i bet that has a lot to do with it.
	// Fonts - also this.
/*
	public static final TimeProfile tp;
	static {
		tp = new TimeProfile();
		tp.start();
	}
*/
	
	public void loadConfiguration(Properties p) {
//		tp.mark("Start of loadCconfiguration");
		//screenScale = 1.28;
		screenScale = PropertyFilters.getDouble(p.getProperty("SCREEN_SCALE"));
		effectsScale = PropertyFilters.inte(p.getProperty("EFFECTS_SCALE"));
		viewportUserInterfaceScale = PropertyFilters.inte(p.getProperty("VIEWPORT_UI_SCALE"));
		
		bigTileWidth = PropertyFilters.inte(p.getProperty("BIG_TILESIZE"));
		tileWidth = PropertyFilters.inte(p.getProperty("TILESIZE"));
		cellHeight = PropertyFilters.inte(p.getProperty("CELL_HEIGHT"));
		
		
		screenWidthInTiles = PropertyFilters.inte(p.getProperty("XRANGE"));
		screenHeightInTiles = PropertyFilters.inte(p.getProperty("YRANGE"));
		playerLocationOnScreen = PropertyFilters.getPosition(p.getProperty("PC_POS"));
		
		cameraScale = PropertyFilters.inte(p.getProperty("CAMERA_SCALE"));
		cameraPosition= PropertyFilters.getPosition(p.getProperty("CAMERA_POS"));
		
		windowBackgroundColour = PropertyFilters.getColor(p.getProperty("COLOR_WINDOW_BACKGROUND"));
		borderColourInner = PropertyFilters.getColor(p.getProperty("COLOR_BORDER_IN"));
		borderColourOuter = PropertyFilters.getColor(p.getProperty("COLOR_BORDER_OUT"));
		
		screenWidth = PropertyFilters.inte(p.getProperty("WINDOW_WIDTH"));
		screenHeight = PropertyFilters.inte(p.getProperty("WINDOW_HEIGHT"));
		
//		tp.mark("Done loading basic PropertyFiltered int fields");

		String msgFontName = p.getProperty("FNT_MESSAGEBOX");
		String msgFontSize = p.getProperty("FNT_MESSAGEBOX_SIZE");
//		tp.mark("Done loading MessageBox font properties ("+msgFontName+", "+msgFontSize+")");
		
		messageBoxFont = PropertyFilters.loadFont(msgFontName, msgFontSize);
//		tp.mark("Done loading messageBoxFont");
		persistentMessageBoxFont = PropertyFilters.loadFont(
			p.getProperty("FNT_PERSISTANTMESSAGEBOX"),
			p.getProperty("FNT_PERSISTANTMESSAGEBOX_SIZE"));
//		tp.mark("Done loading persistentMessageBoxFont");
		
		/*-- Load UI Images */
		try {
//			tp.mark("About to load 'StatusScreenBG' image");
			statusScreenBackground = Textures.lim(p, "IMG_STATUSSCR_BGROUND");
//			tp.mark("Done loading 'StatusScreenBG', about to load UIBG image");
			//statusScreenBackground = ImageUtils.createImage(p.getProperty("IMG_STATUSSCR_BGROUND"));
			//userInterfaceBackgroundImage = ImageUtils.createImage(p.getProperty("IMG_INTERFACE"));
			userInterfaceBackgroundImage = Textures.lim(p, "IMG_INTERFACE");
//			tp.mark("Done loading userInterfaceBackgroundImage");
		} catch (Exception e) {
			Game.crash(e.getMessage(), e);
		}
		
//		tp.mark("About to load Textures");
		//textures = new Textures(p);
		Textures.load(p);	// it's all singular static image vars, now.
//		tp.mark("Done loading textures");
//		tp.report();
	}


}
