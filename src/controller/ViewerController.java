package controller;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import model.Photo;
import model.PhotoModel;

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
	private Photo currentPhoto = null;
	
	double DEFAULT_HEIGHT_VALUE = 0.0;
	double DEFAULT_WIDTH_VALUE = 0.0;
	double imageHeightValue = 0.0;
	double imageWidthValue = 0.0;
	
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
        imageViewMain.setImage(originalImage);
		
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
	
	public void doCloseViewer() {
		imageViewMain.getScene().getWindow().hide();
	}
	
	public void doSavePhoto() {
		
	}
	
	public void doSavePhotoAs() {
		
	}
	
	
	public void doSliderBrightness() {
	    sliderBrightness.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();
				textFieldBrightness.setText(factor + "");
				ColorAdjust brightness = new ColorAdjust();
				brightness.setContrast(factor);
				imageViewMain.setEffect(brightness);
			}
	    	
	    });
	}
	
	public void doSliderContrast() {
	    sliderContrast.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();
				textFieldContrast.setText(factor + "");
				ColorAdjust contrast = new ColorAdjust();
				contrast.setContrast(factor);
				imageViewMain.setEffect(contrast);
			}
	    	
	    });
	}
	
	public void doSliderHue() {
	    sliderHue.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();
				textFieldHue.setText(factor + "");
				ColorAdjust hue = new ColorAdjust();
				hue.setHue(factor);
				imageViewMain.setEffect(hue);
			}
	    	
	    });
	}
	
	public void doSliderSaturation() {	    
	    sliderSaturation.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();
				textFieldSaturation.setText(factor + "");
				ColorAdjust desaturation = new ColorAdjust();
				desaturation.setSaturation(factor);
				imageViewMain.setEffect(desaturation);
			}
	    	
	    });
	}
	
	public void doSliderViewerZoom() {		
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
		double input = Double.parseDouble(textFieldBrightness.getText());
		
		if(input < -1 || input > 1) {
			textFieldBrightness.setText(null);
			Alert error = new Alert(AlertType.ERROR, "Brightness values range from -1.0 to 1.0.");
			error.showAndWait();
		} else {
			sliderBrightness.setValue(input);
			ColorAdjust brightness = new ColorAdjust();
			brightness.setBrightness(input);
			imageViewMain.setEffect(brightness);
		}
	}
	
	public void doTextFieldContrast() {
		double input = Double.parseDouble(textFieldContrast.getText());
		
		if(input < -1 || input > 1) {
			textFieldContrast.setText(null);
			Alert error = new Alert(AlertType.ERROR, "Contrast values range from -1.0 to 1.0.");
			error.showAndWait();
		} else {
			sliderContrast.setValue(input);
			ColorAdjust contrast = new ColorAdjust();
			contrast.setContrast(input);
			imageViewMain.setEffect(contrast);
		}
	}
	
	public void doTextFieldHue() {
		double input = Double.parseDouble(textFieldHue.getText());
		
		if(input < -1 || input > 1) {
			textFieldHue.setText(null);
			Alert error = new Alert(AlertType.ERROR, "Hue values range from -1.0 to 1.0.");
			error.showAndWait();
		} else {
			sliderHue.setValue(input);
			ColorAdjust hue = new ColorAdjust();
			hue.setHue(input);
			imageViewMain.setEffect(hue);
		}
	}
	
	public void doTextFieldSaturation() {
		double input = Double.parseDouble(textFieldSaturation.getText());
		
		if(input < -1 || input > 1) {
			textFieldSaturation.setText(null);
			Alert error = new Alert(AlertType.ERROR, "Saturation values range from -1.0 to 1.0.");
			error.showAndWait();
		} else {
			sliderSaturation.setValue(input);
			ColorAdjust desaturation = new ColorAdjust();
			desaturation.setSaturation(input);
			imageViewMain.setEffect(desaturation);
		}
	}
	
}
