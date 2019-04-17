package controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewerController extends Canvas {
	
	BufferedImage original;
	Image originalImage;
	
	@FXML
	public void initialize() {
		
	}
	
	public void reset() {
		
	}
	
	public void rotateImage(BufferedImage image, int width, int height) {
		
	}
	
	public void rotateImage() {
		BufferedImage temp;
	}
	
	public void resizeImage(int width, int height) {
		
	}
	
	public BufferedImage createBufferedImage(Image image, int width, int height, boolean tran) {
		BufferedImage temp;
		
		if(tran) 
			temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		else
			temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = temp.createGraphics();
		graphics.drawImage(temp, width, height, null);
		graphics.dispose();
		return temp;
	}
	
	public void saturate() {
	    ImageView original = new ImageView(originalImage);
	    ImageView desaturated = new ImageView(originalImage);
	    ColorAdjust desaturate = new ColorAdjust();
	    desaturate.setSaturation(-1);
	    desaturated.setEffect(desaturate);
	}
	
}
