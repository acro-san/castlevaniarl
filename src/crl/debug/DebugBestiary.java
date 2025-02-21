package crl.debug;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import crl.monster.MonsterData;
import crl.monster.MonsterDef;

public class DebugBestiary extends JFrame {
	
	// draw/browse monster data, with integrity checking and potential for
	// setting up animation states etc for later.
	
	// i require a cheat/debug ui for testing that lets me instantly jump to
	// levels as desired, and to set loadouts/levelups.
	
	// for example, i need to test that SickleAI is working still despite me 
	// changing its name. How can I see that? Well, I'd need to jump to the 
	// Death boss fight. I don't even know how to get to that in the game, let
	// alone.... y'know.
	
	// So: Take a leaf out of Zorbus' debug article's book.
	
	// can reference main. we'll have Main initialise and open this. once all
	// its data is loaded in.
	
	/*
	Sprites With Errors - immediately apparent by making this quick and
	filthy viewer!
	
	004: S_TIGER  : undefined?? Blank sprite. no tiger visible.
	007: S_DRAGON : error coords. Is overlapping the screaming mandrake + other
					sprites.
	010: BAT      : Appears 2x-scaled even within its own 64x64 size...
	024: SPEAR_K  : 2 pink pixels on its head makes it look like SuperSentai bug
					armour. Is that right? seems iffy.
	
	
	idea: Feature 'red outline glow' aura!
	// just: swap the alphacomp mode to colour only, draw the sprite 4x in red,
	// then draw the normal mode sprite over the top!!
	
	
	*/
	
	int currentMonsterID = 0;
	int maxSpriteW = Integer.MIN_VALUE,
		maxSpriteH = Integer.MIN_VALUE;
	boolean doOutlines = false;
	
	MonsterDataPanel mdp = null;
	
	public DebugBestiary() {
		super("Debug MonsterData");
		currentMonsterID = 0;
		// find out max monster sprite size...
		int numMons = MonsterData.DEFS_ORDERED.size();
		maxSpriteW = Integer.MIN_VALUE;
		maxSpriteH = Integer.MIN_VALUE;
		int pw, ph;
		for (int m=0; m<numMons; m++) {
			MonsterDef md = MonsterData.DEFS_ORDERED.get(m);
			crl.ui.Appearance ap = md.appearance;
			crl.ui.graphicsUI.GFXAppearance app = (crl.ui.graphicsUI.GFXAppearance)ap;
			BufferedImage monImage = (BufferedImage)app.getImage();
			pw = monImage.getWidth();
			ph = monImage.getHeight();
			if (pw > maxSpriteW) { maxSpriteW = pw; }
			if (ph > maxSpriteH) { maxSpriteH = ph; }
		}
		
		mdp = new MonsterDataPanel();
		mdp.setPreferredSize(new Dimension(800, 600));
		mdp.setBackground(Color.DARK_GRAY);
		setContentPane(mdp);
		
		
		// keybind left/right arrow.
		ActionMap am = getRootPane().getActionMap();
		InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "bestiary_previous");
		am.put("bestiary_previous", actPrev);
		
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "bestiary_next");
		am.put("bestiary_next", actNext);
		
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private AbstractAction actPrev = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent ae) {
			currentMonsterID--;
			if (currentMonsterID < 0) {
				currentMonsterID = 0;
			}
			repaint();
		}
	},
	
	actNext = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent ae) {
			currentMonsterID++;
			int numMons = MonsterData.DEFS_ORDERED.size();
			if (currentMonsterID >= numMons) {
				currentMonsterID = numMons-1;
			}
			repaint();
		}
	};
	
	
	
	class MonsterDataPanel extends JPanel {
		
		public MonsterDataPanel() {
			super();
			// mouse controls or whatnot. buttons/comboboxes for selecting?
			// keybind L/R arrows? idk. just whatever to view whatever.
			// show name for starters. fancy font later.
			setLayout(null);
			JButton prev = new JButton(" < ");
			prev.setAction(actPrev);
			prev.setText(" < ");
			
			JButton next = new JButton(" < ");
			next.setAction(actNext);
			next.setText(" > ");
			// max monster id is...???
			add(prev);
			prev.setBounds(10, 10, 90, 36);
			
			add(next);
			next.setBounds(120, 10, 90, 36);
			
			JCheckBox toggleRedOutline = new JCheckBox("Red Outline", false);
			toggleRedOutline.setBackground(new Color(0, true));
			toggleRedOutline.setForeground(Color.WHITE);	// since it's on dark grey bg.
			toggleRedOutline.setFocusPainted(false);
			
			add(toggleRedOutline);
			toggleRedOutline.setBounds(240, 10, 120, 36);
			toggleRedOutline.addActionListener(ae -> {
				boolean nowEnabled = toggleRedOutline.isSelected();
				if (nowEnabled != doOutlines) {
					doOutlines = nowEnabled;
					repaint();
				}
			});
		}
		
		public void paintComponent(Graphics g1) {
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			
			// selected Monster ID. Left and right arrow keys to cycle through
			// them, or something like that.
			
			// then just list off stats, show anim frames eventually, etc.
			
			g.setColor(Color.WHITE);
			
			MonsterDef md = MonsterData.DEFS_ORDERED.elementAt(currentMonsterID);
			if (md == null) {
				// draw an errmsg in red, though.
				return;
			}
			
			final int LINE_HEIGHT = 24;
			//array/list of stats we wanna lay out? with their field names.?
			
			
			// appearance... draw sprite. etc etc. Copy bestiary screen from
			// Aria? Or another?
			String nameIsh = md.ID;
			int maxMID = MonsterData.DEFS_ORDERED.size();
			String idAndName = String.format("No. %03d  /  %03d :    %s\n", currentMonsterID+1, maxMID, nameIsh);
			g.drawString(idAndName, 50, 90);
			
			// Oh MAN, is that Aria screen poorly designed! text all different sizes,
			// layouts unaligned. blech. Name underneath picture.!
			
			crl.ui.Appearance ap = md.appearance;
			crl.ui.graphicsUI.GFXAppearance monAppearance = (crl.ui.graphicsUI.GFXAppearance)ap;
			BufferedImage monImage = (BufferedImage)monAppearance.getImage();
			
			g.drawString(idAndName, 50, 90);
			
			// Sprite size? Sprite Centering?
			int imgX = 50,
				imgY = 120,
				sprW = monImage.getWidth(),
				sprH = monImage.getHeight();
			
			g.setColor(Color.MAGENTA.darker());
			g.drawRect(imgX, imgY, maxSpriteW, maxSpriteH);
			int midx = maxSpriteW / 2,
				midy = maxSpriteH / 2;
			int sx = imgX + midx - (sprW / 2),
				sy = imgY + midy - (sprH / 2);
			
			g.setColor(Color.CYAN);
			g.drawRect(sx, sy, sprW, sprH);
			
			if (doOutlines) {
				//gfx env create compat?
				BufferedImage redOut = new BufferedImage(sprW+6, sprH+6, BufferedImage.TYPE_INT_ARGB);
				BufferedImage outlinedSprite = new BufferedImage(sprW+6, sprH+6, BufferedImage.TYPE_INT_ARGB);
				Graphics2D og = redOut.createGraphics();
				int ow = sprW+6,
					oh = sprH+6;
				og.drawImage(monImage, 3, 3, null);	//within the 2nd. ok?
				og.setComposite(AlphaComposite.SrcIn);//?
				og.setColor(Color.RED);
				og.fillRect(0, 0, ow, oh);
				og.setPaintMode();
				
				// Poop! the sprites are all 2x2 scaled on IMPORT atm!
				
				og = outlinedSprite.createGraphics();
				og.drawImage(redOut, -1, 0, null);
				og.drawImage(redOut, +1, 0, null);
				og.drawImage(redOut, 0, -1, null);
				og.drawImage(redOut, 0, +1, null);
				
				og.drawImage(monImage, 3, 3, null);
				g.drawImage(outlinedSprite, sx-3, sy-3, null);
				
				
			} else {
				g.drawImage(monImage, sx, sy, null);
			}
			
			g.drawString(String.format("Sprite size: %d x %d", sprW, sprH), imgX, imgY + maxSpriteH + 20);
			// put a border around it and all, etc.
			
			g.setColor(Color.WHITE);
			
			int dataX = imgX + maxSpriteW + 30;	// nice whitespace gaps...
			int dataY = imgY + LINE_HEIGHT - 12;
			
			// Stats 'Table'.
			g.drawString(md.description, dataX, dataY);	// is actually the "readable name"
			dataY += LINE_HEIGHT;
			
			g.drawString("HP / "+md.maxHP, dataX, dataY);
			dataY += LINE_HEIGHT;
			
			g.drawString(md.longDescription, dataX, dataY);
			dataY += LINE_HEIGHT;
			
			statline(g, dataX, dataY, "Min level: ", md.minLevel);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "Max level: ", md.maxLevel);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "Atk: ", md.attack);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "AtkCost: ", md.attackCost);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "AutoRespawns: ", md.autorespawnCount);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "BloodContent: ", md.bloodContent);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "EvadeChance: ", md.evadeChance);
			dataY += LINE_HEIGHT;
			g.drawString("EvadeMessage: " + md.evadeMessage, dataX, dataY);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "Leaping: ", md.leaping);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "Score: ", md.score);
			dataY += LINE_HEIGHT;
			
			String boolz = String.format("Flies: %b, Swims: %b, Ethereal: %b, Undead: %b",
					md.canFly, md.canSwim, md.isEthereal, md.isUndead);
			g.drawString(boolz, dataX, dataY);
			dataY += LINE_HEIGHT;
			
			statline(g, dataX, dataY, "SightRange: ", md.sightRange);
			dataY += LINE_HEIGHT;
			statline(g, dataX, dataY, "WalkCost: ", md.walkCost);	// speed? Per turn whatnot
			dataY += LINE_HEIGHT;
			g.drawString("SFX when damaged: " + md.wavOnHit, dataX, dataY);
			dataY += LINE_HEIGHT;
			
			// what about hex plots, or graphs for averages of stats across ALL
			// monsters? might those be useful at all?
			
			
			
			// ability icons? leaping, undead, whatnot...? little boxes, with pictures?
			// weakness against specific weapon types? Would make char class selection more interesting.
			
			// collected its soul [tickmark] ? (Aria has this, obviously).
			// What about: Number Killed.!? And if you've not seen it, ??? it out/dont show...?
			
		}

		private void statline(Graphics2D g, int x, int y, String label, int value) {
			int lw = g.getFontMetrics().stringWidth(label);
			
			g.drawString(label, x, y);
			if (value == 0) {
				g.drawString(""+value, x+lw, y);
				return;
			}
			Color c = g.getColor();
			g.setColor(Color.CYAN.darker());
			g.drawString(""+value, x+lw, y);
			g.setColor(c);
		}
	}
	
}
