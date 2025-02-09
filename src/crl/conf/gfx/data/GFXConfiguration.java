package crl.conf.gfx.data;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import crl.game.Game;

import sz.util.ImageUtils;
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
		
		halfTileWidth,	// "width of half tile" ?!
		normalTileWidth,	//"width of normal tile" ?!
		
		cellHeight,
		
		screenWidth,
		screenHeight,
	
		screenWidthInTiles,
		screenHeightInTiles;
		
	public Color
		windowBackgroundColour,
		borderColourInner,
		borderColourOuter;
	
	public Font
		messageBoxFont,
		persistantMessageBoxFont;
	
	public BufferedImage
		statusScreenBackground,
		userInterfaceBackgroundImage;
	
	// TODO Rename this var 'textures'.
	public Textures textures;
	
	public double screenScale;

	public int effectsScale;
	public int viewportUserInterfaceScale;
	
	public int cameraScale;
	public Position cameraPosition;
	public Position playerLocationOnScreen;


	public void LoadConfiguration(Properties p) {
		//screenScale = 1.28;
		screenScale = PropertyFilters.getDouble(p.getProperty("SCREEN_SCALE"));
		effectsScale = PropertyFilters.inte(p.getProperty("EFFECTS_SCALE"));
		viewportUserInterfaceScale = PropertyFilters.inte(p.getProperty("VIEWPORT_UI_SCALE"));
		bigTileWidth = PropertyFilters.inte(p.getProperty("BIG_TILESIZE"));
		normalTileWidth = PropertyFilters.inte(p.getProperty("TILESIZE"));
		halfTileWidth = PropertyFilters.inte(p.getProperty("HALF_TILESIZE"));
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
		
		try {
			messageBoxFont = PropertyFilters.getFont(
				p.getProperty("FNT_MESSAGEBOX"),
				p.getProperty("FNT_MESSAGEBOX_SIZE"));
			persistantMessageBoxFont = PropertyFilters.getFont(
				p.getProperty("FNT_PERSISTANTMESSAGEBOX"),
				p.getProperty("FNT_PERSISTANTMESSAGEBOX_SIZE"));
			
		} catch (FontFormatException ffe) {
			Game.crash("Error loading the font", ffe);
		} catch (IOException ioe) {
			Game.crash("Error loading the font", ioe);
		}
		
		/*-- Load UI Images */
		try {
			statusScreenBackground = Textures.lim(p, "IMG_STATUSSCR_BGROUND");
			//statusScreenBackground = ImageUtils.createImage(p.getProperty("IMG_STATUSSCR_BGROUND"));
			//userInterfaceBackgroundImage = ImageUtils.createImage(p.getProperty("IMG_INTERFACE"));
			userInterfaceBackgroundImage = Textures.lim(p, "IMG_INTERFACE");
			
		} catch (Exception e) {
			Game.crash(e.getMessage(), e);
		}
		
		textures = new Textures(p);
	}


}
