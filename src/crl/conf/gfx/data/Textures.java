package crl.conf.gfx.data;

import java.awt.image.BufferedImage;
import java.util.Properties;

import sz.util.ImageUtils;

/**
 * Struct of all the runtime graphics assets
 * 
 * @author Tuukka Turto
 * 
 */
public class Textures {	// but it's not 'configuration'. it's the actual bitmap data.

	public BufferedImage
		ShadowImage,
		CharactersImage,
		BigMonstersImage,
		MonstersImage,
		ItemsImage,
		ItemIconsImage,
		
		TerrainImage,
		DarkTerrainImage,
		NightTerrainImage,
		DarkNightTerrainImage,
		
		FeaturesImage,
		FeatureIconsImage,
		EffectsImage,
		
		effectsImage,
		slashesImage,
		curvedSlashesImage,
		straightSlashesImage,
		
		// what not 'image' in the name of these ones?
		UserInterfaceTileset,
		ViewportUserInterfaceTileset;
	
	
	// 'lim' == Load Image. Shorthand. Adding this mainly to get file-specific
	// (useful) error outputs when s file's not found.
	public static final BufferedImage lim(Properties p, String propertyName) {
		String propValue = p.getProperty(propertyName);
		BufferedImage im = null;
		try {
			im = ImageUtils.createImage(propValue);
		} catch (Exception e) {
			System.err.println("Problem with loading image ("+propValue+" requested as property: "+propertyName+" from .properties file: ???");
			e.printStackTrace();
		}
		return im;
	}
	
	public Textures(Properties p) {
		load(p);
	}
	
	public void load(Properties p) {
		ShadowImage = lim(p,"TILES_SHADOW");
		CharactersImage = lim(p, "TILES_CHARACTERS");
		BigMonstersImage = lim(p, "TILES_BIG_MONSTERS");
		MonstersImage = lim(p, "TILES_MONSTERS");
		ItemsImage = lim(p, "TILES_ITEMS");
		ItemIconsImage = lim(p, "TILES_ITEM_ICONS");

		TerrainImage = lim(p, "TILES_TERRAIN");
		DarkTerrainImage = lim(p, "TILES_DARK_TERRAIN");
		NightTerrainImage = lim(p, "TILES_NIGHT_TERRAIN");
		DarkNightTerrainImage = lim(p, "TILES_DARK_NIGHT_TERRAIN");

		FeaturesImage = lim(p, "TILES_FEATURES");
		FeatureIconsImage = lim(p, "TILES_FEATURE_ICONS");
		EffectsImage = lim(p, "TILES_EFFECTS");

		effectsImage = lim(p, "SFX_EFFECTS");
		slashesImage = lim(p, "SFX_SLASHES");
		curvedSlashesImage = lim(p, "SFX_CURVED_SLASHES");
		straightSlashesImage = lim(p, "SFX_STRAIGHT_SLASHES");

		UserInterfaceTileset = lim(p, "UI_TILES");
		ViewportUserInterfaceTileset = lim(p, "VIEWPORT_UI_TILES");
	}

}
