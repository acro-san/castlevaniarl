package sz.csi.wswing;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import crl.ui.Colors;
import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.util.Position;
import sz.util.FileUtil;
import static crl.ui.Colors.*;

public class WSwingConsoleInterface implements ConsoleSystemInterface, ComponentListener {
	/** Provides Console IO.
	 * Returns keystrokes as CharKeys.
	 * Shows the characters in a Frame
	 */

	//Relations
	private SwingConsoleFrame targetFrame; //To get the keypresses from the AWT Model
	private SwingConsolePanel targetPanel; //To output characters
	private StrokeInformer aStrokeInformer; // Object to which strokes are informed

	// Attributes
	private int xpos, ypos; /** Current printing cursor position */
	private boolean autorefresh;

	// Static Attributes
	//public static Font consoleFont = new Font ("Comic Sans MS", Font.BOLD, 16);
	//public static Font consoleFont = new Font ("Terminal", Font.PLAIN, 16);
	//public static Font consoleFont = new Font ("Fixedsys Excelsior 2.00", Font.PLAIN, 16);
	public static Font consoleFont;
	public static int xdim = 80;
	public static int ydim = 25;

	private int[][] colors;
	private char[][] chars;

	private int[][] colorsBuffer;
	private char[][] charsBuffer;
	

    private Position caretPosition = new Position(0,0);

    public WSwingConsoleInterface() {
	    aStrokeInformer = new StrokeInformer();
        targetFrame = new SwingConsoleFrame();
        java.awt.Dimension initialSize = new java.awt.Dimension(1208, 1024);
        int fontSize = defineFontSize(initialSize.height, initialSize.width);
		String strConsoleFont  = loadFont();
		consoleFont = new Font (strConsoleFont, Font.PLAIN, fontSize);
		//targetFrame.setUndecorated(true);
		targetFrame.init(consoleFont, xdim, ydim);
		
		
		colors = new int[xdim][ydim];
        chars = new char[xdim][ydim];
        colorsBuffer = new int[xdim][ydim];
        charsBuffer = new char[xdim][ydim];
        
        targetPanel = targetFrame.getSwingConsolePanel();
        targetFrame.addKeyListener(aStrokeInformer);
        targetFrame.addComponentListener(this);

        
        targetFrame.setSize((int)(fontSize * xdim * 0.7 * 1.05), (int)(fontSize * ydim * 1.15));
        //targetFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        targetFrame.setLocation(0,0);
        locate (1,1);
		targetFrame.show();
    }

    public void flash(int color) {
		//targetPanel.flash(getColorFromCode(color));
	}
    public void cls() {
	    targetPanel.cls();
        /*for (int i = 0; i< xdim; i++){
            for (int j = 0; j< ydim; j++){
                targetPanel.plot(' ', i, j) ;
            }
        } /*/
    }

    public void locate(int x, int y) {
        xpos = x;
		ypos = y;
    }


	//int rcount;
    public void refresh() {
    	//System.out.println("Count "+ (rcount++) );
        targetFrame.repaint();
		/*try {
        	Thread.currentThread().sleep(1000);
        } catch (InterruptedException ie){
        } */
    }

    public void print (int x, int y, String what, int color){
		locate (x,y);
		
		Color front = getColorFromCode(color);
		for (int i = 0; i < what.length(); i++){
			if (xpos>=xdim){
				xpos = 0;
				ypos++;
			}
			if (ypos>=ydim)
				break;
            targetPanel.plot(what.charAt(i), xpos, ypos, front);
            chars[x+i][y] = what.charAt(i);
			colors[x+i][y] = color;
            xpos ++;
        }
	}


    public void print (int x, int y, char what, int color) {
    	locate (x,y);
    	if (chars[x][y] == what && colors[x][y] == color) {
    		return;
    	}
    	Color front = getColorFromCode(color);
    	targetPanel.plot(what, xpos, ypos, front);
    	colors[x][y] = color;
    	chars[x][y] = what;
    }


	public void print(int x, int y, String what) {
		print(x,y,what, Colors.WHITE);
	}

	public void locateCaret(int x, int y) {
		caretPosition.x = x;
		caretPosition.y = y;
	}

	public String input() {
		return input(9999);
	}

	public String input(int l) {
		String ret = "";
		CharKey read = new CharKey(CharKey.NONE);
		while (true) {
			while (read.code == CharKey.NONE)
				read = inkey();
			if (read.isMetaKey()){
				read.code = CharKey.NONE;
				continue;
			}
			if (read.code == CharKey.ENTER)
				return ret;
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
				print(caretPosition.x, caretPosition.y, " ");

            }
			else {
				if (ret.length() >= l){
					read.code = CharKey.NONE;
					continue;
				}
				String nuevo = read.toString();
				print(caretPosition.x, caretPosition.y, nuevo);
				ret +=nuevo;
				caretPosition.x++;
			}
			refresh();
			read.code = CharKey.NONE;
		}
	}


	public synchronized void refresh(Thread toNotify) {
		refresh();
		toNotify.interrupt();
	}


	public synchronized CharKey inkey(){
		aStrokeInformer.informKey(Thread.currentThread());
		try {
			this.wait();
		} catch (InterruptedException ie) {}
		CharKey ret = new CharKey(aStrokeInformer.getInkeyBuffer());
		return ret;
	}

	
	public void setAutoRefresh(boolean value) {
		targetPanel.setAutoUpdate(value);
	}

	public char peekChar(int x, int y) {
		return targetPanel.peekChar(x,y);
	}

	public int peekColor(int x, int y) {
		return colors[x][y];
	}

	private String loadFont() {
		BufferedReader br = null;
		try {
			br = FileUtil.getReader("font.sz");
			br.readLine();
			String fnt = br.readLine();
			br.close();
			if (!fnt.equals("NFE"))
				return fnt;


		} catch (Exception e){
			try {
				br.close();
			} catch (Exception e2){
			}
		}

		String x [] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		boolean lucida = false, courier=false;
		for (int i = 0; i < x.length; i++)
			if (x[i].equals("Lucida Console"))
				lucida = true;
			else
				if (x[i].equals("Courier New"))
					courier = true;
		if (courier)
			return "Courier New";
		else
			if (lucida)
				return "Lucida Console";
		return "Monospaced";
		
	}


	private int defineFontSize(int scrHeight, int scrWidth) {
		int byHeight = (int)(scrHeight / ydim);
		int byWidth = (int)(scrWidth/ (xdim*0.8));

		if (byHeight < byWidth)
			return byHeight;
		else
			return byWidth;
	}


	public boolean isInsideBounds(Position p) {
		return p.x>=0 && p.x <= xdim && p.y >=0 && p.y <=ydim;
	}

	public boolean isInsideBounds(int x, int y){
		return x>=0 && x <= xdim-1 && y >=0 && y <=ydim-1;
	}

	public void safeprint(int x, int y, char what, int color){
		if (isInsideBounds(x,y))
			print(x,y,what,color);
	}

	public void componentHidden(ComponentEvent e) { }

	public void componentMoved(ComponentEvent e) { }

	public void componentResized(ComponentEvent e) {
		int fontSize = defineFontSize(((Component)e.getSource()).getHeight(), ((Component)e.getSource()).getWidth());
		String strConsoleFont  = loadFont();
		consoleFont = new Font (strConsoleFont, Font.PLAIN, fontSize);
		targetFrame.setFont(consoleFont);
		targetPanel.setFont(consoleFont);
		
	}

	public void componentShown(ComponentEvent e) { }
	
	public void waitKey (int keyCode){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != keyCode)
			x = inkey();
	}

	public void restore() {
		for (int x = 0; x < colors.length; x++) {
			for (int y = 0; y < colors[0].length; y++) {
				this.print(x,y,charsBuffer[x][y], colorsBuffer[x][y]);
			}
		}
	}

	public void saveBuffer() {
		for (int i = 0; i < colors.length; i++){
			System.arraycopy(colors[i], 0, colorsBuffer[i], 0, colors[i].length-1);
			System.arraycopy(chars[i], 0, charsBuffer[i], 0, colors[i].length-1);
		}
	}	

}