package controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Photo;
import model.PhotoModel;
import model.User;

public class ViewerController extends Canvas {
	
	//@formatter:off
	@FXML Button buttonFlipHorizontal;
	@FXML Button buttonFlipVertical;
	@FXML Button buttonRotate180;
	@FXML Button buttonRotate270;
	@FXML Button buttonRotate90;
	
	@FXML MenuItem menuItemCloseViewer;
	@FXML MenuItem menuItemFlipHorizontal;
	@FXML MenuItem menuItemFlipVertical;
	@FXML MenuItem menuItemRedo;
	@FXML MenuItem menuItemRevert;
	@FXML MenuItem menuItemRotate180;
	@FXML MenuItem menuItemRotate270;
	@FXML MenuItem menuItemRotate90;
	@FXML MenuItem menuItemSavePhoto;
	@FXML MenuItem menuItemSavePhotoAs;
	@FXML MenuItem menuItemUndo;
	
	@FXML Slider sliderBrightness;
	@FXML Slider sliderContrast;
	@FXML Slider sliderHue;
	@FXML Slider sliderSaturation;
	@FXML Slider sliderViewerZoom;
	
	@FXML TextField textFieldBrightness;
	@FXML TextField textFieldContrast;
	@FXML TextField textFieldHue;
	@FXML TextField textFieldSaturation;
	
	//@formatter:on
	
	private PhotoModel model = LoginController.getModel();
	private User currentUser = model.getCurrentUser();
	private Photo currentPhoto = null;

	
	BufferedImage original;
	Image originalImage;
	
	@FXML
	public void initialize() {
		// upon initialization:
		// get the selected photo to appear in the viewer
		// collect the data for:
		//brightness
		//saturation
		//hue
		//contrast
		
		if (UserController.currentSelectedPhoto != null) {
			currentPhoto = UserController.currentSelectedPhoto;
		}
		
		if (PhotoController.currentSelectedPhoto != null) {
			currentPhoto = PhotoController.currentSelectedPhoto;
		}
		
		if (SearchController.currentSelectedPhoto != null) {
			currentPhoto = SearchController.currentSelectedPhoto;
		}
		
		System.out.println("Now in viewer: " + currentPhoto);

		
	}
	
	public void doFlipHorizontal() {
		
	}
	
	public void doFlipVertical() {
		
	}
	
	public void doRotate180() {
		
	}
	
	public void doRotate270() {
		
	}
	
	public void doRotate90() {
		
	}
	
	public void doCloseViewer() {
		
	}
	
	public void doUndo() {
		
	}
	
	public void doRedo() {
		
	}
	
	public void doRevert() {
		
	}
	
	public void doSavePhoto() {
		
	}
	
	public void doSavePhotoAs() {
		
	}
	
	
	public void doSliderBrightness() {
		
	}
	
	public void doSliderContrast() {
		
	}
	
	public void doSliderHue() {
		
	}
	
	public void doSliderSaturation() {
		
	}
	
	public void doSliderViewerZoom() {
		
	}
	
	public void doTextFieldBrightness() {
		
	}
	
	public void doTextFieldContrast() {
		
	}
	
	public void doTextFieldHue() {
		
	}
	
	public void doTextFieldSaturation() {
		
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
