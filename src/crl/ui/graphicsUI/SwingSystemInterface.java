package crl.ui.graphicsUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.KeyStroke;

import crl.conf.gfx.data.GFXConfiguration;
import crl.game.Game;

import sz.csi.CharKey;
import sz.util.ImageUtils;
import sz.util.Position;

public class SwingSystemInterface {
	
	protected GFXConfiguration configuration;

	private boolean isFullscreen = false;
	
	private SwingInterfacePanel sip;
	private StrokeNClickInformer aStrokeInformer;
	private Position caretPosition = new Position(0,0);
	private Hashtable<String, Image> images = new Hashtable<String, Image>();
	
	private JFrame frameMain;
	private Point posClic;

	public void addMouseListener(MouseListener listener){
		frameMain.removeMouseListener(listener);
		frameMain.addMouseListener(listener);
	}
	
	public void addMouseMotionListener(MouseMotionListener listener){
		frameMain.removeMouseMotionListener(listener);
		frameMain.addMouseMotionListener(listener);
	}
	
	public void setCursor(Cursor c){
		frameMain.setCursor(c);
	}
	
	
	public void setIcon(Image icon){
		frameMain.setIconImage(icon);
	}
	
	public void setTitle(String title){
		frameMain.setTitle(title);
	}
	
	public void setVisible(boolean bal){
		frameMain.setVisible(bal);
	}

	public void showAlert(String message) {
		JOptionPane.showMessageDialog(frameMain, message, "Alert", JOptionPane.ERROR_MESSAGE);
	}
	
	public SwingSystemInterface(GFXConfiguration configuration) {
		this.configuration = configuration;
		
		frameMain = new JFrame();
		setWindowedBounds();
		
		frameMain.setUndecorated(true);
		
		sip = new SwingInterfacePanel(configuration);
		frameMain.setContentPane(sip);

		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//FIXME: Do Game-close / file-save-check funcs!
		frameMain.setBackground(Color.BLACK);
		//SZ030507 aStrokeInformer = new StrokeInformer();
		
		// ... InputMap / keymap mapping?
		aStrokeInformer = new StrokeNClickInformer();
		frameMain.addKeyListener(aStrokeInformer);
		
		ActionMap am = frameMain.getRootPane().getActionMap();
		InputMap im = frameMain.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		// bindKey(funcname, key, im, am);
		String fsname = "FULLSCREEN_KEY";
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), fsname);
		am.put(fsname, new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				toggleFullscreen();
			}
		});
		
		frameMain.addMouseListener(aStrokeInformer);
		frameMain.setFocusable(true);
		frameMain.setVisible(true);		// needed before sip.init to get non-null gfx ref.
		frameMain.requestFocusInWindow();
		
		sip.init();
		
		// if windowed, why not enable titlebar dragging like a normal win?
		// then, it can have mouse interaction with the game itself?
		// or: detect mousemoved and press/release separate from drags?
		// finicky, though. Means no drag interactions, and slight misclicks move window.
		frameMain.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				Point fl = frameMain.getLocation();
				frameMain.setLocation(
					e.getX() - posClic.x + fl.x,
					e.getY() - posClic.y + fl.y);
			}
			
			public void mouseMoved(MouseEvent e) {}
		});
		
		frameMain.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {
				posClic = e.getPoint();
			}

			public void mouseReleased(MouseEvent e) {}
		});
		
	}
	
	
	private void setWindowedBounds() {
		Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
		int ww = configuration.screenWidth,	//game screen width
			wh = configuration.screenHeight;
		frameMain.getContentPane().setPreferredSize(new Dimension(ww, wh));
		frameMain.setBounds(
			(ssize.width - ww) / 2,
			(ssize.height- wh) / 2,
			ww,
			wh);
	}

	public void enableFullscreen(JFrame win) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		
		GraphicsDevice[] gds = ge.getScreenDevices();
		if (gds.length > 1) {
			System.err.println("SwingSysInterface: Multiple graphics devices!");
		}
		
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Insets ins = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		
		//System.err.println("Go Fullscreen: Screen res is: "+screendim.width + "x"+screendim.height+" " + ins + "\nSupported modes:");
		if (!gd.isFullScreenSupported()) {
			return;
		}
		
		int targetScreenWidth = 1024;
		int targetScreenHeight = 768;	// why *this* res?
		// how about 16:9 to avoid stretching? 480p? 1080p? 2160p? With black borders?
		// FIXME : Not retaining windowed mode position. returns to centre.
		// so if moved and then fullscreened/unfullscreened, loses position.
		
		DisplayMode usableDisplayMode = getFirstUsableDisplayMode(gd, targetScreenWidth, targetScreenHeight);
		if (usableDisplayMode == null) {
			System.err.format("No suitable fullscreen display mode matched (%d x %d)\n", targetScreenWidth, targetScreenHeight);
			return;
		}
		gd.setFullScreenWindow(win);
		try {
			gd.setDisplayMode(usableDisplayMode);
		} catch (Exception e) {
			e.printStackTrace();
			gd.setFullScreenWindow(null);
		}
	}
	
	
	private static DisplayMode getFirstUsableDisplayMode(GraphicsDevice g, int targetW, int targetH) {
		DisplayMode[] modes = g.getDisplayModes();
		for (DisplayMode d: modes) {
			//System.err.format("%s x %s (%d bpp), %s Hz\n", d.getWidth(), d.getHeight(), d.getBitDepth(), d.getRefreshRate());
			if	(d.getWidth() == targetW && d.getHeight() == targetH) {
				return d;
			}
		}
		return null;	// unable to match resolution
	}
	
	//private java.awt.Rectangle windowedBounds = null;
	private Point windowedLoc = null;
	public void toggleFullscreen() {
		if (isFullscreen) {
			disableFullscreen();
			isFullscreen = false;
			return;
		}
		windowedLoc = frameMain.getLocation();
		enableFullscreen(frameMain);
		isFullscreen = true;
	}


	private synchronized void disableFullscreen() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice();
		gd.setFullScreenWindow(null);
		
		
		frameMain.dispose();
		setWindowedBounds();
		if (windowedLoc != null) {
			frameMain.setLocation(windowedLoc);
		}
		frameMain.pack();
		frameMain.setVisible(true);
		//setCursor(...);
	}
	
	
	public void cls() {
		sip.cls();
	}


	public void drawImage(String filename) {
		Image im = images.get(filename);
		if (im == null) {
			try {
				im = ImageUtils.createImage(filename);
			} catch (Exception e) {
				Game.crash("Exception trying to create image "+filename, e);
			}
			images.put(filename, im);
		}
		sip.drawImage(im);
		sip.repaint();
	}
	
	public void drawImage(Image image){
		sip.drawImage(image);
		sip.repaint();
	}
	
	public void drawImage(int scrX, int scrY, Image img) {
		sip.drawImage(scrX, scrY, img);
	}
	
	public void drawImage(int scrX, int scrY, String filename){
		Image im = images.get(filename);
		if (im == null){
			try {
				im = ImageUtils.createImage(filename);
			} catch (Exception e){
				Game.crash("Exception trying to create image "+filename, e);
			}
			images.put(filename, im);
		}
		sip.drawImage(scrX, scrY, im);
	}

	public void drawImageCC(int consoleX, int consoleY, Image img){
		drawImage(consoleX*10, consoleY*24, img);
	}

	public void drawImageCC(int consoleX, int consoleY, String img){
		drawImage(consoleX*10, consoleY*24, img);
	}
	
	
	public void refresh(){
		//invTextArea.setVisible(false);
		sip.repaint();
	}
	
	/*public void print(int x, int y, String text){
		sip.print(x*10, y*24, text);
	}*/
	
	public void printAtPixel(int x, int y, String text) {
		sip.print(x, y, text);
	}
	
	public void printAtPixel(int x, int y, String text, Color color) {
		sip.print(x, y, text, color);
	}
	
	public void printAtPixelCentered(int x, int y, String text, Color color) {
		sip.print(x, y, text, color, true);
	}

	public void print(int x, int y, String text, Color color) {
		sip.print(x*10, y*24, text, color);
	}
	
	public void waitKey (int keyCode) {
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != keyCode)
			x = inkey();
	}

	
	public synchronized CharKey inkey(){
		aStrokeInformer.informKey(Thread.currentThread());
		try {
			this.wait();
		} catch (InterruptedException ie) {}
		CharKey ret = new CharKey(aStrokeInformer.getInkeyBuffer());
		return ret;
	}
	
	public Graphics2D getGraphics2D(){
		return sip.getCurrentGraphics();
	}
	
	/*public void showTextArea(int scrX, int scrY, int scrW, int scrH, String text){
		invTextArea.setBounds(scrX, scrY, scrW, scrH);
		invTextArea.setText(text);
		invTextArea.setVisible(true);
		invTextArea.repaint();
		invTextArea.setVisible(false);
	}*/
	
	public void setFont(Font fnt){
		sip.setFontFace(fnt);
		//invTextArea.setFont(fnt);
	}
	
	public void setColor(Color color){
		sip.setColor(color);
		//invTextArea.setForeground(color);
	}
	
	//public String input(int consXPrompt,int consYPrompt,String prompt,Color promptColor, int maxLength, Color textColor){
	public String input(int xpos,int ypos, Color textColor, int maxLength){
		String ret = "";
		CharKey read = new CharKey(CharKey.NONE);
		saveBuffer();
		while (true){
			restore();
			printAtPixel(xpos, ypos, ret+"_", textColor);
			refresh();
			while (read.code == CharKey.NONE)
				read = inkey();
			if (read.code == CharKey.ENTER)
				break;
			if (read.code == CharKey.BACKSPACE){
				if (ret.equals("")){
					read.code = CharKey.NONE;
					continue;
				}
				if (ret.length() > 1)
					ret = ret.substring(0, ret.length() -1);
				else
					ret = "";
                caretPosition.x--;
				//print(caretPosition.x, caretPosition.y, " ");
            }
			else {
				if (ret.length() >= 50){
					read.code = CharKey.NONE;
					continue;
				}
				if (!read.isAlphaNumeric()){
					read.code = CharKey.NONE;
					continue;
				}
					
				String nuevo = read.toString();
				//print(caretPosition.x, caretPosition.y, nuevo, Color.WHITE);
				ret +=nuevo;
				caretPosition.x++;
			}
			read.code = CharKey.NONE;
		}
		return ret;
	}
	
	public void saveBuffer(){
		sip.saveBuffer();
	}
	
	public void saveBuffer(int buffer){
		sip.saveBuffer(buffer);
	}
	
	public void restore(){
		sip.restore();
	}
	
	public void restore(int buffer){
		sip.restore(buffer);
	}
	
	public void flash(Color c){
		sip.flash(c);
	}
	
	public void add(Component c){
		sip.add(c);
		sip.validate();
	}
	
	public void remove(Component c){
		sip.remove(c);
		sip.validate();
	}
	
	public void recoverFocus(){
		frameMain.requestFocus();
	}
}

class SwingInterfacePanel extends JPanel {

	private static final long serialVersionUID = -7392757206841150146L;
	private Image bufferImage;
	private Graphics bufferGraphics;
	
	private Image backImage;
	private Graphics backGraphics;
	
	// What is this all about? Wtf are these for??
	private Image[] backImageBuffers;
	private Graphics[] backGraphicsBuffers;
	
//	private Color color;
//	private Font font;
	private FontMetrics fontMetrics;
	protected GFXConfiguration configuration;
	
	public void cls() {
		Color oldColor = bufferGraphics.getColor();
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.fillRect(0,0,configuration.screenWidth,
								configuration.screenHeight);
		bufferGraphics.setColor(oldColor);
	}
	
	public void setColor(Color color) {
	//	this.color = color;
		bufferGraphics.setColor(color);
	}
	
	public void setFontFace(Font f) {
	///	font = f;
		bufferGraphics.setFont(f);
		fontMetrics = bufferGraphics.getFontMetrics();
	}
	
	public Graphics2D getCurrentGraphics(){
		return (Graphics2D)bufferGraphics;
	}
	
	public SwingInterfacePanel(GFXConfiguration configuration){
		this.configuration = configuration;
		setLayout(null);
		//setBorder(new LineBorder(Color.GRAY));
	}
	
	private static final int
		NUM_BACKBUFFERS = 5;
	
	public void init() {
		int w = configuration.screenWidth,
			h = configuration.screenHeight;
		bufferImage = createImage(w, h);
		bufferGraphics = bufferImage.getGraphics();
		bufferGraphics.setColor(Color.WHITE);
		backImage = createImage(w, h);
		backGraphics = backImage.getGraphics();
		backImageBuffers = new Image[NUM_BACKBUFFERS];
		backGraphicsBuffers = new Graphics[NUM_BACKBUFFERS];
		for (int i = 0 ; i < NUM_BACKBUFFERS; i++) {
			backImageBuffers[i] = createImage(w, h);
			backGraphicsBuffers[i] = backImageBuffers[i].getGraphics();
		}
	}
	
	public void drawImage(Image img) {
		bufferGraphics.drawImage(img, 0, 0,this);
	}
	
	public void drawImage(int scrX, int scrY, Image img) {
		bufferGraphics.drawImage(img, scrX, scrY,this);
	}
	
	public void print(int x, int y, String text) {
		bufferGraphics.drawString(text, x,y);
		//repaint();
	}
	
	public void print(int x, int y, String text, Color c, boolean centered) {
		if (centered) {
			int width = fontMetrics.stringWidth(text);
			x = x - (width / 2);
		}
		Color old = bufferGraphics.getColor();
		bufferGraphics.setColor(c);
		bufferGraphics.drawString(text, x, y);
		bufferGraphics.setColor(old);
	}
	
	public void print(int x, int y, String text, Color c) {
		print(x, y, text, c, false);
		//repaint();
	}
	
	public void saveBuffer() {
		backGraphics.drawImage(bufferImage,0,0,this);
	}
	
	public void saveBuffer(int buffer) {
		backGraphicsBuffers[buffer].drawImage(bufferImage,0,0,this);
	}
	
	public void restore() {
		bufferGraphics.drawImage(backImage, 0,0,this);
	}
	
	public void restore(int buffer) {
		bufferGraphics.drawImage(backImageBuffers[buffer], 0,0,this);
	}
	
	public void flash(Color c) {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bufferImage != null){
			g.drawImage(bufferImage, 0, 0, null);
		}
	}

	public Component add(Component comp) {
		return super.add(comp);
	}
	
}

class StrokeInformer implements KeyListener {
	protected int bufferCode;
	
	@Deprecated
	protected transient Thread keyListener;

	public StrokeInformer() {
		bufferCode = -1;
	}

	public void informKey(Thread toWho) {
		keyListener = toWho;
	}

	public int getInkeyBuffer( ){
		return bufferCode;
	}

	public void keyPressed(KeyEvent e) {
		bufferCode = charCode(e);
		//if (!e.isShiftDown())
		if (keyListener != null) {
			keyListener.interrupt();
		}
	}

	private int charCode(KeyEvent x) {
		int code = x.getKeyCode();
		if(x.isControlDown()) {
			return CharKey.CTRL;
		}
		if (code >= KeyEvent.VK_A && code <= KeyEvent.VK_Z) {
			if (x.getKeyChar() >= 'a'){
				int diff = KeyEvent.VK_A - CharKey.a;
				return code-diff;
			} else {
				int diff = KeyEvent.VK_A - CharKey.A;
				return code-diff;
			}
		}

		switch (x.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			return CharKey.SPACE;
		case KeyEvent.VK_COMMA:
			return CharKey.COMMA;
		case KeyEvent.VK_PERIOD: 
			return CharKey.DOT;
		case KeyEvent.VK_DELETE:
			return CharKey.DELETE;
		case KeyEvent.VK_NUMPAD0:
			return CharKey.N0;
		case KeyEvent.VK_NUMPAD1:
			return CharKey.N1;
		case KeyEvent.VK_NUMPAD2:
			return CharKey.N2;
		case KeyEvent.VK_NUMPAD3:
			return CharKey.N3;
		case KeyEvent.VK_NUMPAD4:
			return CharKey.N4;
		case KeyEvent.VK_NUMPAD5:
			return CharKey.N5;
		case KeyEvent.VK_NUMPAD6:
			return CharKey.N6;
		case KeyEvent.VK_NUMPAD7:
			return CharKey.N7;
		case KeyEvent.VK_NUMPAD8:
			return CharKey.N8;
		case KeyEvent.VK_NUMPAD9:
			return CharKey.N9;
		case KeyEvent.VK_1:
			return CharKey.N1;
		case KeyEvent.VK_2:
			return CharKey.N2;
		case KeyEvent.VK_3:
			return CharKey.N3;
		case KeyEvent.VK_4:
			return CharKey.N4;
		case KeyEvent.VK_5:
			return CharKey.N5;
		case KeyEvent.VK_6:
			return CharKey.N6;
		case KeyEvent.VK_7:
			return CharKey.N7;
		case KeyEvent.VK_8:
			return CharKey.N8;
		case KeyEvent.VK_9:
			return CharKey.N9;
		case KeyEvent.VK_F1:
			return CharKey.F1;
		case KeyEvent.VK_F2:
			return CharKey.F2;
		case KeyEvent.VK_F3:
			return CharKey.F3;
		case KeyEvent.VK_F4:
			return CharKey.F4;
		case KeyEvent.VK_F5:
			return CharKey.F5;
		case KeyEvent.VK_F6:
			return CharKey.F6;
		case KeyEvent.VK_F7:
			return CharKey.F7;
		case KeyEvent.VK_F8:
			return CharKey.F8;
		case KeyEvent.VK_F9:
			return CharKey.F9;
		case KeyEvent.VK_F10:
			return CharKey.F10;
		case KeyEvent.VK_F11:
			return CharKey.F11;
		case KeyEvent.VK_F12:
			return CharKey.F12;
		case KeyEvent.VK_ENTER:
			return CharKey.ENTER;
		case KeyEvent.VK_BACK_SPACE:
			return CharKey.BACKSPACE;
		case KeyEvent.VK_ESCAPE:
			return CharKey.ESC;
		case KeyEvent.VK_UP:
			return CharKey.UARROW;
		case KeyEvent.VK_DOWN:
			return CharKey.DARROW;
		case KeyEvent.VK_LEFT:
			return CharKey.LARROW;
		case KeyEvent.VK_RIGHT:
			return CharKey.RARROW;
		}
		
		if (x.getKeyChar() == '.') {
			return CharKey.DOT;
		}
		if (x.getKeyChar() == '?') {
			return CharKey.QUESTION;
		}
		return -1;
	}


	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}

class StrokeNClickInformer extends StrokeInformer implements MouseListener {
	public void mousePressed(MouseEvent e) {
		if (keyListener != null) {
			bufferCode = CharKey.NONE;
			keyListener.interrupt();
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	
	public void mouseExited(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) {
		
	}

	
}