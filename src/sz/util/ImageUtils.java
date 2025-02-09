package sz.util;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static BufferedImage createImage(String filename) {
		BufferedImage im = null;
		try {
			File ifile = new File(filename);
			if (!ifile.exists()) {
				System.err.println("Image File Not Present: "+filename);
			}
			im = ImageIO.read(ifile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return im;
	}
	
	
	public static Image crearImagen(String filename, Component tracker) {	// throws Exception 
		//Debug.enterStaticMethod("IASImageUtils", "crearImagen "+filename);
		//Debug.say("estoy cargando una imagen "+filename);
		if (!FileUtil.fileExists(filename)) {
			// It's not EXCEPTIONAL! It doesn't require abandoning flow control!
			// It's just a missing image!! The program can/should find ways to
			// recover from this!!
			// Regardless of misconfigured imgs in dev environment, if a user
			// sets up alternate property files and causes problems, then there
			// should be some robust media-import function that falls back to
			// the built-in default and displays some helpful gui error for them
			// to get their mod paths correct. Throwing an exception and crashing
			// is NOT dealing with the problem.
			System.err.println("Archivo Inexistente " +filename);
			Exception e = new Exception("Archivo Inexistente " +filename);
			Debug.exitExceptionally(e);
			// throw e;
		}
		Image image = Toolkit.getDefaultToolkit().getImage(filename);
		MediaTracker mediaTracker = new MediaTracker(tracker);
		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForID(0);
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
		// Debug.exitMethod(image);
		return image;
	}
	
	
	public static BufferedImage crearImagen(String filename, int x, int y, int width, int height) { //throws Exception
		//tempImage = crearImagen(filename);
		BufferedImage tempImage = createImage(filename);
		return crearImagen(tempImage, x, y, width, height);
		//return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(tempImage.getSource(), new CropImageFilter(x, y, width, height)));
	}
	
	
	public static BufferedImage crearImagen(BufferedImage tempImage, int x, int y, int width, int height) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

		//int transparency = tempImage.getColorModel().getTransparency();
		BufferedImage ret = gc.createCompatibleImage(width,height,tempImage.getTransparency());

		Graphics2D g = ret.createGraphics();
		g.drawImage(tempImage,
			0, 0, width, height,
			x, y, x+width, y+height, null);
		g.dispose();
		return ret;
		//return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(tempImage.getSource(), new CropImageFilter(x, y, width, height)));
	}


	public static BufferedImage hFlip(BufferedImage image) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}
	
	public static BufferedImage vFlip(BufferedImage image) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}
	
	public static BufferedImage rotate(BufferedImage bufferedImage, double radians){
		AffineTransform tx = new AffineTransform();
		tx.rotate(radians, bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(bufferedImage, null);
	}
}
