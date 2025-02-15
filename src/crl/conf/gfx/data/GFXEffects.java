package crl.conf.gfx.data;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import sz.util.ImageUtils;
import sz.util.Position;
import crl.action.monster.MandragoraScream;
import crl.game.Game;
import crl.ui.graphicsUI.effects.GFXAnimatedEffect;
import crl.ui.graphicsUI.effects.GFXAnimatedMeleeEffect;
import crl.ui.graphicsUI.effects.GFXAnimatedMissileEffect;
import crl.ui.graphicsUI.effects.GFXBeamEffect;
import crl.ui.graphicsUI.effects.GFXCircleBlastEffect;
import crl.ui.graphicsUI.effects.GFXDirectionalMissileEffect;
import crl.ui.graphicsUI.effects.GFXEffect;
import crl.ui.graphicsUI.effects.GFXFlashEffect;
import crl.ui.graphicsUI.effects.GFXSequentialEffect;
import crl.ui.graphicsUI.effects.GFXSplashEffect;

import static crl.action.vkiller.Bible.BIBLE_STEPS;

public class GFXEffects {
	
	private static final double
		PI_OVER_2 = Math.PI / 2;
	
	private BufferedImage IMG_EFFECTS;
	
	private BufferedImage[][] CURVED_FRAMES;
	private BufferedImage[][] STR_FRAMES;
	private Position[][] CURVED_VARS;
	
	private GFXConfiguration configuration;
	
	public GFXEffects(GFXConfiguration configuration) {
		this.configuration = configuration;
		IMG_EFFECTS = Textures.effectsImage;
		initAnimations();
		loadEffects();
	}
	
	private BufferedImage[][] readSlashes(BufferedImage source) {
		BufferedImage[][] ret = new BufferedImage[8][5];
		try {
		ret[4][0] = createSlashFrame(source, 0,72,29,12);
		ret[4][1] = createSlashFrame(source, 0,57,55,15);
		ret[4][2] = createSlashFrame(source, 0,41,85,16);
		ret[4][3] = createSlashFrame(source, 0,22,117,19);
		ret[4][4] = createSlashFrame(source, 0,0,147,22);
		
		ret[2][0] = createSlashFrame(source, 0,495,27,30);
		ret[2][1] = createSlashFrame(source, 0,435,52,60);
		ret[2][2] = createSlashFrame(source, 0,352,78,83);
		ret[2][3] = createSlashFrame(source, 0,230,112,121);
		ret[2][4] = createSlashFrame(source, 0,84,143,145);
		} catch (Exception e) {
			Game.crash("Error loading the effects", e);
		}
		
		ret[3][0] = ImageUtils.vFlip(ret[4][0]);
		ret[3][1] = ImageUtils.vFlip(ret[4][1]);
		ret[3][2] = ImageUtils.vFlip(ret[4][2]);
		ret[3][3] = ImageUtils.vFlip(ret[4][3]);
		ret[3][4] = ImageUtils.vFlip(ret[4][4]);
		
		ret[6][0] = ImageUtils.vFlip(ImageUtils.rotate(ret[4][0], PI_OVER_2));
		ret[6][1] = ImageUtils.vFlip(ImageUtils.rotate(ret[4][1], PI_OVER_2));
		ret[6][2] = ImageUtils.vFlip(ImageUtils.rotate(ret[4][2], PI_OVER_2));
		ret[6][3] = ImageUtils.vFlip(ImageUtils.rotate(ret[4][3], PI_OVER_2));
		ret[6][4] = ImageUtils.vFlip(ImageUtils.rotate(ret[4][4], PI_OVER_2));
		
		ret[1][0] = ImageUtils.hFlip(ret[6][0]);
		ret[1][1] = ImageUtils.hFlip(ret[6][1]);
		ret[1][2] = ImageUtils.hFlip(ret[6][2]);
		ret[1][3] = ImageUtils.hFlip(ret[6][3]);
		ret[1][4] = ImageUtils.hFlip(ret[6][4]);
		
		ret[0][0] = ImageUtils.vFlip(ret[2][0]);
		ret[0][1] = ImageUtils.vFlip(ret[2][1]);
		ret[0][2] = ImageUtils.vFlip(ret[2][2]);
		ret[0][3] = ImageUtils.vFlip(ret[2][3]);
		ret[0][4] = ImageUtils.vFlip(ret[2][4]);
		
		ret[5][0] = ImageUtils.hFlip(ret[0][0]);
		ret[5][1] = ImageUtils.hFlip(ret[0][1]);
		ret[5][2] = ImageUtils.hFlip(ret[0][2]);
		ret[5][3] = ImageUtils.hFlip(ret[0][3]);
		ret[5][4] = ImageUtils.hFlip(ret[0][4]);
		
		ret[7][0] = ImageUtils.hFlip(ret[2][0]);
		ret[7][1] = ImageUtils.hFlip(ret[2][1]);
		ret[7][2] = ImageUtils.hFlip(ret[2][2]);
		ret[7][3] = ImageUtils.hFlip(ret[2][3]);
		ret[7][4] = ImageUtils.hFlip(ret[2][4]);
		return ret;
	}

	private BufferedImage createSlashFrame(BufferedImage tileset, int baseX, int baseY, int baseWidth, int baseHeight) {
		int effectsScale = configuration.effectsScale;
		try {
			return ImageUtils.crearImagen(
				tileset,
				baseX * effectsScale,
				baseY * effectsScale,
				baseWidth * effectsScale,
				baseHeight * effectsScale);
		} catch (Exception e) {
			Game.crash("Error loading the effects",e);
			return null;
		}
	}


	private void initAnimations() {
		BufferedImage IMG_CURVEDSLASHES = Textures.curvedSlashesImage;
		BufferedImage IMG_STRSLASHES = Textures.straightSlashesImage;
		
		CURVED_FRAMES = new BufferedImage[8][5];
		CURVED_FRAMES[4][0] = createSlashFrame(IMG_CURVEDSLASHES, 0,72,29,12);
		CURVED_FRAMES[4][1] = createSlashFrame(IMG_CURVEDSLASHES, 0,57,55,15);
		CURVED_FRAMES[4][2] = createSlashFrame(IMG_CURVEDSLASHES, 0,41,85,16);
		CURVED_FRAMES[4][3] = createSlashFrame(IMG_CURVEDSLASHES, 0,22,117,19);
		CURVED_FRAMES[4][4] = createSlashFrame(IMG_CURVEDSLASHES, 0,0,147,22);
		
		CURVED_FRAMES[2][0] = createSlashFrame(IMG_CURVEDSLASHES, 0,495,27,30);
		CURVED_FRAMES[2][1] = createSlashFrame(IMG_CURVEDSLASHES, 0,435,52,60);
		CURVED_FRAMES[2][2] = createSlashFrame(IMG_CURVEDSLASHES, 0,352,78,83);
		CURVED_FRAMES[2][3] = createSlashFrame(IMG_CURVEDSLASHES, 0,230,112,121);
		CURVED_FRAMES[2][4] = createSlashFrame(IMG_CURVEDSLASHES, 0,84,143,145);
		
		CURVED_FRAMES[3][0] = ImageUtils.vFlip(CURVED_FRAMES[4][0]);
		CURVED_FRAMES[3][1] = ImageUtils.vFlip(CURVED_FRAMES[4][1]);
		CURVED_FRAMES[3][2] = ImageUtils.vFlip(CURVED_FRAMES[4][2]);
		CURVED_FRAMES[3][3] = ImageUtils.vFlip(CURVED_FRAMES[4][3]);
		CURVED_FRAMES[3][4] = ImageUtils.vFlip(CURVED_FRAMES[4][4]);
		
		CURVED_FRAMES[6][0] = ImageUtils.vFlip(ImageUtils.rotate(CURVED_FRAMES[4][0], Math.PI / 2));
		CURVED_FRAMES[6][1] = ImageUtils.vFlip(ImageUtils.rotate(CURVED_FRAMES[4][1], Math.PI / 2));
		CURVED_FRAMES[6][2] = ImageUtils.vFlip(ImageUtils.rotate(CURVED_FRAMES[4][2], Math.PI / 2));
		CURVED_FRAMES[6][3] = ImageUtils.vFlip(ImageUtils.rotate(CURVED_FRAMES[4][3], Math.PI / 2));
		CURVED_FRAMES[6][4] = ImageUtils.vFlip(ImageUtils.rotate(CURVED_FRAMES[4][4], Math.PI / 2));
		
		CURVED_FRAMES[1][0] = ImageUtils.hFlip(CURVED_FRAMES[6][0]);
		CURVED_FRAMES[1][1] = ImageUtils.hFlip(CURVED_FRAMES[6][1]);
		CURVED_FRAMES[1][2] = ImageUtils.hFlip(CURVED_FRAMES[6][2]);
		CURVED_FRAMES[1][3] = ImageUtils.hFlip(CURVED_FRAMES[6][3]);
		CURVED_FRAMES[1][4] = ImageUtils.hFlip(CURVED_FRAMES[6][4]);
		
		CURVED_FRAMES[0][0] = ImageUtils.vFlip(CURVED_FRAMES[2][0]);
		CURVED_FRAMES[0][1] = ImageUtils.vFlip(CURVED_FRAMES[2][1]);
		CURVED_FRAMES[0][2] = ImageUtils.vFlip(CURVED_FRAMES[2][2]);
		CURVED_FRAMES[0][3] = ImageUtils.vFlip(CURVED_FRAMES[2][3]);
		CURVED_FRAMES[0][4] = ImageUtils.vFlip(CURVED_FRAMES[2][4]);
		
		CURVED_FRAMES[5][0] = ImageUtils.hFlip(CURVED_FRAMES[0][0]);
		CURVED_FRAMES[5][1] = ImageUtils.hFlip(CURVED_FRAMES[0][1]);
		CURVED_FRAMES[5][2] = ImageUtils.hFlip(CURVED_FRAMES[0][2]);
		CURVED_FRAMES[5][3] = ImageUtils.hFlip(CURVED_FRAMES[0][3]);
		CURVED_FRAMES[5][4] = ImageUtils.hFlip(CURVED_FRAMES[0][4]);
		
		CURVED_FRAMES[7][0] = ImageUtils.hFlip(CURVED_FRAMES[2][0]);
		CURVED_FRAMES[7][1] = ImageUtils.hFlip(CURVED_FRAMES[2][1]);
		CURVED_FRAMES[7][2] = ImageUtils.hFlip(CURVED_FRAMES[2][2]);
		CURVED_FRAMES[7][3] = ImageUtils.hFlip(CURVED_FRAMES[2][3]);
		CURVED_FRAMES[7][4] = ImageUtils.hFlip(CURVED_FRAMES[2][4]);
		
		CURVED_VARS = new Position[8][5];
		CURVED_VARS[4][0] = new Position(32,0);
		CURVED_VARS[4][1] = new Position(32,0);
		CURVED_VARS[4][2] = new Position(32,0);
		CURVED_VARS[4][3] = new Position(32,0);
		CURVED_VARS[4][4] = new Position(32,0);
		
		CURVED_VARS[2][0] = new Position(32,-30);
		CURVED_VARS[2][1] = new Position(32,-60);
		CURVED_VARS[2][2] = new Position(32,-90);
		CURVED_VARS[2][3] = new Position(32,-113);
		CURVED_VARS[2][4] = new Position(32,-151);
		
		CURVED_VARS[3][0] = new Position(-29,0);
		CURVED_VARS[3][1] = new Position(-55,0);
		CURVED_VARS[3][2] = new Position(-85,0);
		CURVED_VARS[3][3] = new Position(-117,0);
		CURVED_VARS[3][4] = new Position(-147,0);
		
		CURVED_VARS[6][0] = new Position(16,30);
		CURVED_VARS[6][1] = new Position(16,60);
		CURVED_VARS[6][2] = new Position(16,90);
		CURVED_VARS[6][3] = new Position(16,113);
		CURVED_VARS[6][4] = new Position(16,151);
		
		CURVED_VARS[1][0] = new Position(16,-30);
		CURVED_VARS[1][1] = new Position(16,-60);
		CURVED_VARS[1][2] = new Position(16,-90);
		CURVED_VARS[1][3] = new Position(16,-113);
		CURVED_VARS[1][4] = new Position(16,-151);
		
		CURVED_VARS[0][0] = new Position(-29,-30);
		CURVED_VARS[0][1] = new Position(-55,-60);
		CURVED_VARS[0][2] = new Position(-85,-90);
		CURVED_VARS[0][3] = new Position(-117,-113);
		CURVED_VARS[0][4] = new Position(-147,-151);
		
		CURVED_VARS[5][0] = new Position(-29,30);
		CURVED_VARS[5][1] = new Position(-55,30);
		CURVED_VARS[5][2] = new Position(-85,30);
		CURVED_VARS[5][3] = new Position(-117,30);
		CURVED_VARS[5][4] = new Position(-147,30);
		
		CURVED_VARS[7][0] = new Position(32,32);
		CURVED_VARS[7][1] = new Position(32,32);
		CURVED_VARS[7][2] = new Position(32,32);
		CURVED_VARS[7][3] = new Position(32,32);
		CURVED_VARS[7][4] = new Position(32,32);
		
		STR_FRAMES = new BufferedImage[8][5];
		STR_FRAMES[4][0] = createSlashFrame(IMG_STRSLASHES, 0,72,29,12);
		STR_FRAMES[4][1] = createSlashFrame(IMG_STRSLASHES, 0,57,55,15);
		STR_FRAMES[4][2] = createSlashFrame(IMG_STRSLASHES, 0,41,85,16);
		STR_FRAMES[4][3] = createSlashFrame(IMG_STRSLASHES, 0,22,117,19);
		STR_FRAMES[4][4] = createSlashFrame(IMG_STRSLASHES, 0,0,147,22);
		
		STR_FRAMES[2][0] = createSlashFrame(IMG_STRSLASHES, 0,495,27,30);
		STR_FRAMES[2][1] = createSlashFrame(IMG_STRSLASHES, 0,435,52,60);
		STR_FRAMES[2][2] = createSlashFrame(IMG_STRSLASHES, 0,352,78,83);
		STR_FRAMES[2][3] = createSlashFrame(IMG_STRSLASHES, 0,230,112,121);
		STR_FRAMES[2][4] = createSlashFrame(IMG_STRSLASHES, 0,84,143,145);
		
		STR_FRAMES[3][0] = ImageUtils.vFlip(STR_FRAMES[4][0]);
		STR_FRAMES[3][1] = ImageUtils.vFlip(STR_FRAMES[4][1]);
		STR_FRAMES[3][2] = ImageUtils.vFlip(STR_FRAMES[4][2]);
		STR_FRAMES[3][3] = ImageUtils.vFlip(STR_FRAMES[4][3]);
		STR_FRAMES[3][4] = ImageUtils.vFlip(STR_FRAMES[4][4]);
		
		STR_FRAMES[6][0] = ImageUtils.vFlip(ImageUtils.rotate(STR_FRAMES[4][0], Math.PI / 2));
		STR_FRAMES[6][1] = ImageUtils.vFlip(ImageUtils.rotate(STR_FRAMES[4][1], Math.PI / 2));
		STR_FRAMES[6][2] = ImageUtils.vFlip(ImageUtils.rotate(STR_FRAMES[4][2], Math.PI / 2));
		STR_FRAMES[6][3] = ImageUtils.vFlip(ImageUtils.rotate(STR_FRAMES[4][3], Math.PI / 2));
		STR_FRAMES[6][4] = ImageUtils.vFlip(ImageUtils.rotate(STR_FRAMES[4][4], Math.PI / 2));
		
		STR_FRAMES[1][0] = ImageUtils.hFlip(STR_FRAMES[6][0]);
		STR_FRAMES[1][1] = ImageUtils.hFlip(STR_FRAMES[6][1]);
		STR_FRAMES[1][2] = ImageUtils.hFlip(STR_FRAMES[6][2]);
		STR_FRAMES[1][3] = ImageUtils.hFlip(STR_FRAMES[6][3]);
		STR_FRAMES[1][4] = ImageUtils.hFlip(STR_FRAMES[6][4]);
		
		STR_FRAMES[0][0] = ImageUtils.vFlip(STR_FRAMES[2][0]);
		STR_FRAMES[0][1] = ImageUtils.vFlip(STR_FRAMES[2][1]);
		STR_FRAMES[0][2] = ImageUtils.vFlip(STR_FRAMES[2][2]);
		STR_FRAMES[0][3] = ImageUtils.vFlip(STR_FRAMES[2][3]);
		STR_FRAMES[0][4] = ImageUtils.vFlip(STR_FRAMES[2][4]);
		
		STR_FRAMES[5][0] = ImageUtils.hFlip(STR_FRAMES[0][0]);
		STR_FRAMES[5][1] = ImageUtils.hFlip(STR_FRAMES[0][1]);
		STR_FRAMES[5][2] = ImageUtils.hFlip(STR_FRAMES[0][2]);
		STR_FRAMES[5][3] = ImageUtils.hFlip(STR_FRAMES[0][3]);
		STR_FRAMES[5][4] = ImageUtils.hFlip(STR_FRAMES[0][4]);
		
		STR_FRAMES[7][0] = ImageUtils.hFlip(STR_FRAMES[2][0]);
		STR_FRAMES[7][1] = ImageUtils.hFlip(STR_FRAMES[2][1]);
		STR_FRAMES[7][2] = ImageUtils.hFlip(STR_FRAMES[2][2]);
		STR_FRAMES[7][3] = ImageUtils.hFlip(STR_FRAMES[2][3]);
		STR_FRAMES[7][4] = ImageUtils.hFlip(STR_FRAMES[2][4]);
		
	}
	
	private BufferedImage[] load(int frames, int xpos, int ypos) {
		int effectsScale = configuration.effectsScale;
		BufferedImage[] ret = new BufferedImage[frames];
		for (int x = 0; x < frames; x++) {
			try {
				ret[x] = ImageUtils.crearImagen(
					IMG_EFFECTS,
					(xpos+x) * 32 * effectsScale,
					ypos * 32 * effectsScale,
					32 * effectsScale,
					32 * effectsScale);
			} catch (Exception e){
				Game.crash("Error loading effect", e);
			}
		}
		return ret;
	}
	
	private BufferedImage[] loadSame(BufferedImage file, int times, int xpos, int ypos) {
		int effectsScale = this.configuration.effectsScale;
		BufferedImage[] ret = new BufferedImage[times];
		for (int x = 0; x < times; x++){
			try {
				ret[x] = ImageUtils.crearImagen(file, 
				xpos*32 * effectsScale, ypos * 32 * effectsScale, 32 * effectsScale,32 * effectsScale);
			} catch (Exception e){
				Game.crash("Error loading effect", e);
			}
		}
		return ret;
	}

	
	private BufferedImage[] loadMaterialize() {
		int effectsScale = configuration.effectsScale;
		try {
			BufferedImage[] ret = {
				ImageUtils.crearImagen(IMG_EFFECTS, 0 * effectsScale, 485 * effectsScale, 40 * effectsScale,59 * effectsScale),
				ImageUtils.crearImagen(IMG_EFFECTS, 44 * effectsScale, 485 * effectsScale, 40 * effectsScale,59 * effectsScale),
				ImageUtils.crearImagen(IMG_EFFECTS, 88 * effectsScale, 485 * effectsScale, 40 * effectsScale,59 * effectsScale)
			};
			return ret;
		} catch (Exception e){
			Game.crash("Error loading effect", e);
		}
		return null;
	}
	
	private BufferedImage[] loadVanish() {
		int effectsScale = this.configuration.effectsScale;
		try {
			BufferedImage[] ret = {
				ImageUtils.crearImagen(IMG_EFFECTS, 129 * effectsScale, 485 * effectsScale, 40 * effectsScale,59 * effectsScale),
				ImageUtils.crearImagen(IMG_EFFECTS, 172 * effectsScale, 485 * effectsScale, 40 * effectsScale,59 * effectsScale),
				ImageUtils.crearImagen(IMG_EFFECTS, 215 * effectsScale, 485 * effectsScale, 40 * effectsScale,59 * effectsScale)
			};
			return ret;
		} catch (Exception e) {
			Game.crash("Error loading effect", e);
		}
		return null;
	}
	
	// Shorthand Frame-Sequence loaders. An alternative would be: numerical indexing of graphic sheets pre-tiled for easy access?
	// assumption: these are loading a sequence of 2,8,4,or 1 frame(s), at the given (16x16?) tile x,y position in the src sheet?
	private Image[] load2(int xpos, int ypos) {
		return load(2, xpos, ypos);
	}
	
	private Image[] load8(int xpos, int ypos) {
		return load(8, xpos, ypos);
	}
	
	private Image[] load4(int xpos, int ypos) {
		return load(4, xpos, ypos);
	}
	
	private Image[] load1(int xpos, int ypos) {
		return load(1, xpos, ypos);
	}
	
	// *STATIC*? Riiight?
	public GFXEffect[] effects;
	
	
	protected void loadEffects() {
		effects = new GFXEffect[] {
			// Animated Missile Effects
			new GFXAnimatedMissileEffect("SFX_CHARGE_BALL", load2(0,0), 50),
			new GFXAnimatedEffect("SFX_HOMING_BALL",load2(2,0), 60),
			new GFXDirectionalMissileEffect("SFX_METEORBALL", load8(8,0), 30),
			new GFXDirectionalMissileEffect("SFX_ITEMBREAKBIBLE", load8(8,0), 30),
			new GFXAnimatedMissileEffect("SFX_LIT_SPELL", load2(0,1), 25),
			new GFXAnimatedEffect("SFX_RED_HIT",load2(2,1), 100),
			new GFXAnimatedEffect("SFX_WHITE_HIT",load2(4,1), 50),
			new GFXAnimatedEffect("SFX_QUICK_WHITE_HIT",load2(4,1), 10),
			new GFXDirectionalMissileEffect("SFX_RENEGADE_BOD",load8(8,1), 20),
			new GFXSequentialEffect("SFX_BIBLE", BIBLE_STEPS, load1(2,2), 5),
			new GFXAnimatedMissileEffect("SFX_CAT",load1(3,2), 55),
			
			new GFXAnimatedMissileEffect("SFX_BIRD",load2(4,2), 20),
			new GFXAnimatedMissileEffect("SFX_DRAGONFIRE",load2(6,2), 30),
			new GFXDirectionalMissileEffect("SFX_SUMMON_SPIRIT", load8(8,2), 45),
			new GFXAnimatedMissileEffect("SFX_CROSS", load2(0,3), 40),
			new GFXAnimatedMissileEffect("SFX_HOLY",load1(2,3), 20),
			new GFXAnimatedMissileEffect("SFX_EGG",load1(3,3), 20),
			new GFXDirectionalMissileEffect("SFX_FIREBALL", load8(0,4), 30),
			new GFXDirectionalMissileEffect("SFX_ICEBALL", load8(0,5), 30),
			new GFXDirectionalMissileEffect("SFX_WHIP_FIREBALL", load8(0,4), 30),
			new GFXDirectionalMissileEffect("SFX_FLAMESSHOOT", load8(0,4), 30),
			new GFXAnimatedEffect("SFX_EGG_BLAST",load2(14,4), 20),
			new GFXAnimatedMissileEffect("SFX_AXE", load8(0,11), 30),
			
			new GFXAnimatedEffect("SFX_HOLY_RAINDROP",load2(4,0), 25),
			// En Target.java : 60 "SFX_"+weaponDef.getID()
			// En MonsterMissile.java : 48 "SFX_MONSTER_ID_"+aMonster.getID()
			
			//Directional Missile Effects
			new GFXDirectionalMissileEffect("SFX_SOUL_STEAL", load8(8,2), 30),
			new GFXDirectionalMissileEffect("SFX_SLEEP_SPELL", load8(8,2), 30), /*Pending*/
			new GFXDirectionalMissileEffect("SFX_TELEPORT", load8(8,2), 30), /*Pending*/
			new GFXDirectionalMissileEffect("SFX_SHADETELEPORT", load8(8,2), 30), /*Pending*/
			new GFXDirectionalMissileEffect("SFX_WHITE_DAGGER", load8(0,8), 5),
			new GFXDirectionalMissileEffect("SFX_THROWN_DAGGER", load8(0,8), 5),
			new GFXDirectionalMissileEffect("SFX_SILVER_DAGGER", load8(0,9), 5),
			new GFXDirectionalMissileEffect("SFX_GOLD_DAGGER", load8(0,10), 5),
			new GFXDirectionalMissileEffect("SFX_SOULSSTRIKE", load8(8,2), 30),
			//En MonsterMissile.java : 51 "SFX_MONSTER_ID_"+aMonster.getID()
			
			//Beam Effects
			new GFXBeamEffect("SFX_FLAME_SPELL", load1(3,6), 46),
			new GFXBeamEffect("SFX_ICE_SPELL", load1(11,14), 46),
			new GFXBeamEffect("SFX_ENERGY_BEAM", load1(11,14), 15), /*Pending*/
			//En MonsterMissile.java : 42 "SFX_MONSTER_ID_"+aMonster.getID()
			
			//Splash Effects
			
			new GFXAnimatedEffect("SFX_MATERIALIZE",loadMaterialize(),30,-4,-27), 
			new GFXAnimatedEffect("SFX_VANISH",loadVanish(),30,-4,-27),
			new GFXSplashEffect("SFX_SHADOW_APOCALYPSE",load2(10,15),70), /*load 5 frames*/
			new GFXSplashEffect("SFX_SHADOW_EXTINCTION",load2(12,15),70), /*load 4 frames*/
			new GFXSplashEffect("SFX_HOLY_FLAME",load2(12,14),20), /*load 3 frames*/
			new GFXSplashEffect("SFX_MANDRAGORA_SCREAM",loadSame(Textures.MonstersImage, MandragoraScream.SCREAM_RANGE, 8,6),120),
			new GFXSplashEffect("SFX_HOLY_RAINSPLASH",new Image[]{load1(4,0)[0], load1(5,0)[0],load1(4,0)[0],load1(5,0)[0]},50), /*load 3 frames*/
			new GFXSplashEffect("SFX_DOVE_BLAST",load8(8,13),20),
			new GFXSplashEffect("SFX_CRYSTAL_BLAST",load2(10,14),30), /*load 4 frames*/
			
			// OK. These circleblast effects need the gfxConfiguration...
			// purely so they get a ref to find out the gfx 'Normal Tile Size'
			// later (at render time). That's baloney. must be able to get that
			// conf at render time, somehow.
			new GFXCircleBlastEffect("SFX_BOSS_DEATH",Color.YELLOW,10), /*pending*/
			new GFXCircleBlastEffect("SFX_ROSARY_BLAST",Color.WHITE,10), /*pending*/
			new GFXCircleBlastEffect("SFX_KILL_CHRIS",Color.MAGENTA,20), /*pending*/
			new GFXCircleBlastEffect("SFX_MORPH_SOLEIYU",Color.BLACK,20), /*pending*/
			
			new GFXCircleBlastEffect("SFX_SOUL_FLAME",Color.RED,20), /*pending*/
			new GFXCircleBlastEffect("SFX_SOUL_BLAST",Color.WHITE,20), /*pending*/
			
			
			//Melee Effects
			//En MonsterMissile.java  : 45 "SFX_MONSTER_ID_"+aMonster.getID()
			//En Attack.java : 116 "SFX_"+weaponDef.getID()
			
			//Sequential Effects
			
			
			//Tile Effects
			//new GFXIconEffect("SFX_SHADOW_FLARE",load1(8,15)[0], 150),
			new GFXDirectionalMissileEffect("SFX_SHADOW_FLARE", load8(8,1), 20),
			new GFXDirectionalMissileEffect("SFX_HELLFIRE", load8(8,1), 20),
			
			new GFXAnimatedEffect("SFX_MONSTER_CRAWLING",load2(10,16), 20),
			
			//Tile Missile Effects
			new GFXAnimatedMissileEffect("SFX_MISSILE_HOMING_BALL",load2(2,0), 20),
			
//			En Attack.java : 128 "SFX_"+weaponDef.getID()
			
			//Flash Effects
			new GFXFlashEffect("SFX_THUNDER_FLASH", Color.WHITE),
			
			//Weapons
			/*new GFXBeamMissileEffect("SFX_WP_AGUEN", "/\\", ConsoleSystemInterface.BLUE, 46),*/

			new GFXAnimatedMeleeEffect("SFX_WP_BASELARD",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_STAFF",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_MACE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_SHORT_SWORD",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_GAUNTLET",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_SHORT_SPEAR",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_FLAIL",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_RINGS",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_WHIP",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_SCIMITAR",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_ROD",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_KNUCKLES",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_LONG_SPEAR",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_SABRE",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_GLADIUS",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_COMBAT_RINGS",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_BATTLE_SPEAR",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_CUTLASS",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_ESTOC",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_COMBAT_GAUNTLET",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_MORNING_STAR",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_HALBERD",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_COMBAT_KNIFE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_KATANA",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_BROADSWORD",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_SPIKY_KNUCKLES",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_SPIKED_RINGS",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_RAPIER",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_FALCHION",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_BASTARDSWORD",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_DUALBLADE_SPEAR",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_MARTIAL_ARMBAND",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_BLADE_RINGSET",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_CLAYMORE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_LEATHER_WHIP",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_THORN_WHIP",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_CHAIN_WHIP",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_VKILLERW",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_LIT_WHIP",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_FLAME_WHIP",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_HOLBEIN_DAGGER",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_SHOTEL",STR_FRAMES, CURVED_VARS, 30),
			createAnimatedMeleeEffect("SFX_WP_FLAMBERGE", "gfx/crl_slashes-fire.gif", 20),
			new GFXAnimatedMeleeEffect("SFX_WP_WEREBANE",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_ETHANOS_BLADE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_MABLUNG",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_HADOR",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_HARPER",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_TULKAS_FIST",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_ALCARDE_SPEAR",STR_FRAMES, CURVED_VARS, 30),
			createAnimatedMeleeEffect("SFX_WP_FIREBRAND", "gfx/crl_slashes-fire.gif", 20),
			new GFXAnimatedMeleeEffect("SFX_WP_ICEBRAND",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_VORPAL_BLADE",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_GURTHANG",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_OSAFUNE",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_MORMEGIL",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_GRAM",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_KAISER_KNUCKLE",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_TERMINUS",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_MOURNEBLADE",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_CRISSAEGRIM",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_MASAMUNE",CURVED_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_WP_HAMMER_JUSTICE",CURVED_FRAMES, CURVED_VARS, 30),

			
			new GFXDirectionalMissileEffect("SFX_WP_HANDGUN", load8(8,6), 10),
			new GFXDirectionalMissileEffect("SFX_WP_SILVER_HANDGUN", load8(8,6), 10),
			new GFXDirectionalMissileEffect("SFX_WP_REVOLVER", load8(8,6), 10),
			new GFXDirectionalMissileEffect("SFX_WP_BOW", load8(0,14), 10),
			new GFXDirectionalMissileEffect("SFX_WP_CROSSBOW", load8(0,14), 10),
			
			new GFXAnimatedMissileEffect("SFX_WP_SHURIKEN", load2(0,3), 30),
			new GFXAnimatedMissileEffect("SFX_WP_CHAKRAM", load2(0,3), 30),
			new GFXAnimatedMissileEffect("SFX_WP_BUFFALO_STAR", load2(0,3), 30),
			new GFXDirectionalMissileEffect("SFX_WP_BWAKA_KNIFE", load8(0,8), 5),
			new GFXDirectionalMissileEffect("SFX_WP_THROWING_KNIFE", load8(0,8), 5),

			//Monsters
			new GFXAnimatedMissileEffect("SFX_ROTATING_BONE", load4(8,3), 20),
			new GFXAnimatedMeleeEffect("SFX_AXE_MELEE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_SPEAR_MELEE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_GIANT_BONE_MELEE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_BLADE_SOLDIER_MELEE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_BONE_HALBERD_MELEE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMeleeEffect("SFX_LIZARD_SWORDSMAN_MELEE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMissileEffect("SFX_SUMMON_CAT_WITCH",load1(3,2), 55),
			new GFXAnimatedMissileEffect("SFX_FIRE_BEAM", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_NOVA_BEAM", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_ARK_BEAM", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_POISON_CLOUD", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMeleeEffect("SFX_FLAME_SWORD_MELEE",STR_FRAMES, CURVED_VARS, 30),
			new GFXAnimatedMissileEffect("SFX_FLAME_WAVE", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_TOXIC_POWDER", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_STONE_BEAM", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_STUN_BEAM", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_ANCIENT_BEAM", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_PURPLE_FLAME_BEAM", load8(8,1), 30),/*Provisional*/
			new GFXAnimatedMissileEffect("SFX_ROTATING_AXE", load8(0,12), 40),
			new GFXDirectionalMissileEffect("SFX_MAGIC_MISSILE", load8(8,5), 20),
			new GFXAnimatedMissileEffect("SFX_BIG_FIREBALL", load8(8,1), 30),
			new GFXDirectionalMissileEffect("SFX_FLAMING_BARREL", load8(8,1), 30), /*Provisional*/
			new GFXDirectionalMissileEffect("SFX_BULLET", load8(8,6), 10),
			new GFXDirectionalMissileEffect("SFX_SEED", load8(8,7), 30),
			new GFXDirectionalMissileEffect("SFX_WINDING_SHARD", load8(8,8), 30),
			new GFXAnimatedMissileEffect("SFX_MONSTER_ID_ZELDO", load8(8,9), 30),
			new GFXAnimatedMissileEffect("SFX_WHIRLING_SICKLE", load8(8,9), 30),
			new GFXAnimatedMissileEffect("SFX_SPINNING_DISK", load2(12,4), 50),
			new GFXAnimatedMissileEffect("SFX_SPINNING_SWORD", load8(8,11), 40),
			new GFXDirectionalMissileEffect("SFX_ARROW", load8(0,14), 10),
			
			new GFXDirectionalMissileEffect("SFX_THROWN_SPEAR", load8(0,13), 10),
			new GFXDirectionalMissileEffect("SFX_THROWN_SWORD", load8(0,13), 10),
		};
	}


	private GFXAnimatedMeleeEffect createAnimatedMeleeEffect(String id, String file, int delay) {
		try {
			return new GFXAnimatedMeleeEffect(id,readSlashes(ImageUtils.createImage(file)), CURVED_VARS, delay);
		} catch (Exception e) {
			Game.crash("Error loading the animated effects", e);
			return null;
		}
	}
	
	/*
	public GFXEffect[] getEffects() {
		return effects;
	}
	*/

}