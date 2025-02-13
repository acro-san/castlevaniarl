package crl.conf.gfx.data;

import java.awt.image.BufferedImage;
import java.util.Properties;

import sz.util.Debug;
import sz.util.ImageUtils;

/**
 * Struct of all the runtime graphics assets
 * 
 * @author Tuukka Turto
 * 
 */
public class Textures {

	//static, right?
	public static BufferedImage
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
		ViewportUserInterfaceTileset,
		
		portraitsImg;
	
	public static final byte
		PRT_NONE = -1,	// instead of 'null ref' for 'no portrait' on chats.
		PRT_M1 = 0,	// or vkiller_m?
		PRT_M2 = 1,
		PRT_M3 = 2,
		PRT_M4 = 3,
		PRT_M5 = 4,
		PRT_M6 = 5,
		
		PRT_F1 = 6,
		PRT_F2 = 7,
		PRT_F3 = 8,
		PRT_F4 = 9,
		PRT_F5 = 10,
		PRT_F6 = 11,
		
		PRT_DRACULA = 12,
		PRT_DEATH = 13,
		PRT_SOLEIYU = 14,
		PRT_SOLEIYU_D = 15,
		PRT_CHRIS = 16,
		PRT_VINDELITH = 17,
		
		PRT_CLAW = 18,
		PRT_CLARA = 19,
		PRT_MELDUCK = 20,
		PRT_MAIDEN = 21;
		// and then create/init an ARRAY of images? Or rectangles for draw src?
	
	// if defining rects here and just using those, can source from portrait img
	// ...? but why is it using the 2x image in SRC form? Just *DRAW* it up2x.
	public static BufferedImage[] portraits = new BufferedImage[PRT_MAIDEN+1];
	
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
	
//	public Textures(Properties p) {
//		load(p);
//	}
	
	public static void load(Properties p) {
		//Textures t = new Textures();	// not needed.
		
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
		
		try {
			// Textures.CHAR_PORTRAITS. Refer to them by PORTRAIT INDEX ID.
			portraitsImg = ImageUtils.createImage("gfx/crl_portraits2x.gif");
			BufferedImage PRT = portraitsImg;
			
			// ummm. don't *need* to store separate images, if store xywh's...?
			
			// In order from top left to bottom right:
			portraits[PRT_M1] = ImageUtils.crearImagen(PRT, 8, 10, 84, 86);	// vkiller_m
			portraits[PRT_M2] = ImageUtils.crearImagen(PRT, 98, 10, 84, 86);	// renegade_m
			portraits[PRT_M3] = ImageUtils.crearImagen(PRT, 188, 10, 84, 86);	// vanquisher?
			portraits[PRT_M4] = ImageUtils.crearImagen(PRT, 278, 10, 84, 86);	// invoker? whichver 2 wasn't.
			portraits[PRT_M5] = ImageUtils.crearImagen(PRT, 368, 10, 84, 86);	// beastman_m
			portraits[PRT_M6] = ImageUtils.crearImagen(PRT, 458, 10, 84, 86);	// knight_m
			
			portraits[PRT_F1] = ImageUtils.crearImagen(PRT, 8, 107, 84, 86);	// vkiller_f
			portraits[PRT_F2] = ImageUtils.crearImagen(PRT, 98, 107, 84, 86);	// renegade_f
			portraits[PRT_F3] = ImageUtils.crearImagen(PRT, 188, 107, 84, 86);	// ...
			portraits[PRT_F4] = ImageUtils.crearImagen(PRT, 278, 107, 84, 86);
			portraits[PRT_F5] = ImageUtils.crearImagen(PRT, 368, 107, 84, 86);
			portraits[PRT_F6] = ImageUtils.crearImagen(PRT, 458, 107, 84, 86);
			
			portraits[PRT_DRACULA] = ImageUtils.crearImagen(PRT, 8, 205, 84, 86);
			portraits[PRT_DEATH  ] = ImageUtils.crearImagen(PRT, 98, 205, 84, 86);
			portraits[PRT_SOLEIYU] = ImageUtils.crearImagen(PRT, 188, 205, 84, 86);
			portraits[PRT_SOLEIYU_D] = ImageUtils.crearImagen(PRT, 278, 204, 84, 86);
			portraits[PRT_CHRIS] = ImageUtils.crearImagen(PRT, 368, 205, 84, 86);
			portraits[PRT_VINDELITH] = ImageUtils.crearImagen(PRT, 458, 204, 84, 86);
			
			portraits[PRT_CLAW] = ImageUtils.crearImagen(PRT, 8, 303, 84, 86);
			portraits[PRT_CLARA] = ImageUtils.crearImagen(PRT, 98, 303, 84, 86);
			portraits[PRT_MELDUCK] = ImageUtils.crearImagen(PRT, 188, 303, 84, 86);
			portraits[PRT_MAIDEN] = ImageUtils.crearImagen(PRT, 278, 303, 84, 86);
			// + 2 blank spaces... on bottom row.
			
		} catch (Exception iae) {
			Debug.byebye(iae.getMessage());
		}
		
	}

}
