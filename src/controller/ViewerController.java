package controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
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
	
	@FXML ImageView imageViewMain;

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
	
	@FXML TilePane tilePaneImage;
	
	//@formatter:on
	
	private PhotoModel model = LoginController.getModel();
	private User currentUser = model.getCurrentUser();
	private Photo currentPhoto = null;
	
	double DEFAULT_HEIGHT_VALUE = 0.0;
	double DEFAULT_WIDTH_VALUE = 0.0;
	double imageHeightValue = 0.0;
	double imageWidthValue = 0.0;
	
	BufferedImage original;
	BufferedImage editedImage;
	Image originalImage;
	
	@FXML
	public void initialize() {
		
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


		
		displayImage();
	}
	
	private void displayImage() {
		File test = new File(currentPhoto.getFilepath());
		originalImage = new Image(test.toURI().toString());
		ImageView iv = new ImageView(originalImage);
        imageViewMain.setImage(originalImage);
        
		int x = (int) originalImage.getWidth();
		int y = (int) originalImage.getHeight();
		
		original = createBufferedImage(originalImage, x, y, true);
		editedImage = original;
		
		DEFAULT_HEIGHT_VALUE = originalImage.getHeight();
		DEFAULT_WIDTH_VALUE = originalImage.getWidth();
		
		imageHeightValue = DEFAULT_HEIGHT_VALUE;
		imageWidthValue = DEFAULT_WIDTH_VALUE;
	}
	
	public void doTilePaneImage() {
		
	}
	
	public void doImageViewMain() {
		
	}
	
	public void doFlipHorizontal() {
        imageViewMain.setScaleX(imageViewMain.getScaleX() * -1);
    }

    public void doFlipVertical() {
        imageViewMain.setScaleY(imageViewMain.getScaleY() * -1);
    }
		
	public void doRotate180() {
        imageViewMain.setRotate(imageViewMain.getRotate() + 180);
    }

    public void doRotate270() {
        imageViewMain.setRotate(imageViewMain.getRotate() + 270);
    }

    public void doRotate90() {
        imageViewMain.setRotate(imageViewMain.getRotate() + 90);
    }
	
	public BufferedImage rotateImage(BufferedImage src, int rotationAngle) {		
		double theta = (Math.PI * 2) / 360 * rotationAngle;
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage dest;
		
		if (rotationAngle == 90 || rotationAngle == 270) {
			dest = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
		} else {
			dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		}

		Graphics2D graphics2D = dest.createGraphics();

		if (rotationAngle == 90) {
			graphics2D.translate((height - width) / 2, (height - width) / 2);
			graphics2D.rotate(theta, height / 2, width / 2);
		} else if (rotationAngle == 270) {
			graphics2D.translate((width - height) / 2, (width - height) / 2);
			graphics2D.rotate(theta, height / 2, width / 2);
		} else {
			graphics2D.translate(0, 0);
			graphics2D.rotate(theta, width / 2, height / 2);
		}
		graphics2D.drawRenderedImage(src, null);
		return dest;
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
		//@FXML Slider sliderViewerZoom;
		
		sliderViewerZoom.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();
				
				imageHeightValue = factor * DEFAULT_HEIGHT_VALUE;
				imageWidthValue = factor * DEFAULT_WIDTH_VALUE;
				
				imageViewMain.setFitHeight(imageHeightValue);
				imageViewMain.setFitWidth(imageWidthValue);
				imageViewMain.setPreserveRatio(true);
				
			}
			
		});
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
