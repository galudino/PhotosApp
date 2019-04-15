/**
 * PhotoController.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.PhotoModel;
import model.Tag;

/**
 * Controller class to manage functionality of import.fxml
 * 
 * @version Apr 12, 2019
 * @author Gemuele Aludino
 */
public class PhotoController {
	
	int index = 0;
	
	final double DEFAULT_INSET_VALUE = 10.0;
	final double DEFAULT_HEIGHT_VALUE = 150.0;
	final double DEFAULT_WIDTH_VALUE = 200.0;
		
	double imageInsetValue = DEFAULT_INSET_VALUE;
	double imageHeightValue = DEFAULT_HEIGHT_VALUE;
	double imageWidthValue = DEFAULT_WIDTH_VALUE;
	
	Desktop desktop = Desktop.getDesktop();
	
	FileChooser fileChooser;
	File file;
	
	private Photo currentPhoto = null;
	private Album currentAlbum = null;
	private List<ImageView> currentImageViewList = null;
	
	ObservableList<Photo> photoList = FXCollections.observableArrayList();
	ObservableList<Photo> pList = FXCollections.observableArrayList();
	
	public PhotoModel model = LoginController.getModel();
	
	//@formatter:off
	@FXML Button buttonBrowse;
	@FXML Button buttonConfirmTag;
	@FXML Button buttonImportSelection;
	@FXML Button navigatorButtonBack;
	@FXML Button navigatorButtonNext;
	
	@FXML ImageView detailView;
	
	@FXML Label createdField;
	@FXML Label displayCaption;
	@FXML Label nameField;
	@FXML Label pathField;
	@FXML Label sizeField;
	
	@FXML ListView<Photo> imageQueueList;
	@FXML ListView<Tag> tagList;
	
	@FXML MenuItem fileReturnToAlbums;
	@FXML MenuItem fileOpenSelectedPhotoInViewer;
	@FXML MenuItem fileSaveAndLogout;
	@FXML MenuItem fileSaveAndExit;
	
	@FXML MenuItem viewAsThumbnails;
	@FXML MenuItem viewAsSingleImage;
	
	@FXML RadioButton radioButtonThisAlbum;
	@FXML RadioButton radioButtonSelectedAlbum;
	@FXML RadioButton radioButtonThumbnail;
	@FXML RadioButton radioButtonSimpleImage;
	
	@FXML Slider zoomSlider;
	
	@FXML SplitMenuButton albumList;
	
	
	@FXML TextField captionField;
	
	
	
	//@formatter:on
	
	@FXML
	public void initialize() {
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
	}
	
	public void doViewModeThumbnail() {
		
	}
	
	public void doViewModeSingleImage() {
		
	}
	
	public void doOpenSelectedPhotoInViewer() {
		
	}
	
	public void doImageQueueList() {
		
	}
	
	public void doTagList() {
		
	}
	
	public void doAlbumList() {
		
	}
	
	public void doRadioButtonSelectedAlbum() {
		
	}
	
	public void doRadioButtonThisAlbum() {
		
	}
	
	public void doButtonConfirmTag() {
		
	}
	
	private void setSelectedIndex(int selectedIndex) {
		index = selectedIndex;
	}
	
	public void doZoomSlider() {
		if (currentAlbum == null) {
			return;
		}
		
		zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();
				
				imageInsetValue = factor * DEFAULT_INSET_VALUE; // default 10.00
				imageHeightValue = factor * DEFAULT_HEIGHT_VALUE; // default 150.00
				imageWidthValue = factor * DEFAULT_WIDTH_VALUE; // default 200.00
				
				for (ImageView iv : currentImageViewList) {
					iv.setFitHeight(imageHeightValue);
					iv.setFitWidth(imageWidthValue);
					iv.setPreserveRatio(true);
				}

				debugLog("" + newValue.doubleValue());
				
				debugLog(imageInsetValue + " inset");
				debugLog(imageHeightValue + " height");
				debugLog(imageWidthValue + " width");
			}
			
		});
	}
	
	/**
	 * Executes upon activating navigator back button
	 */
	public void doNavigatorButtonBack() {
		/**
		 * No album was selected.
		 */
		if (currentAlbum == null) {
			return;
		}
		
		/**
		 * Album is selected but photo is not selected
		 */
		if (currentAlbum != null && currentPhoto == null) {
			return;
		}
		
		/**
		 * Album is selected and has at least 1 photo
		 */
		if (currentAlbum.getAlbumSize() != 0 && currentAlbum != null) {

			System.out.println(currentAlbum.getAlbumSize());
			/**
			 * Establish new index. If current index equals 0, set the index to
			 * the last index of the currentImageViewList. (loop to end)
			 * 
			 * Otherwise, decrement index by one to go the previous image.
			 */
			ImageView old;
			ImageView iv;

			if (index == 0) {
				old = currentImageViewList.get(index);
				index = currentImageViewList.size() - 1;
				iv = currentImageViewList.get(index);
			} else {
				old = currentImageViewList.get(index);
				--index;
				iv = currentImageViewList.get(index);
			}

			/**
			 * Add an event filter for navigatorButtonBack, for when the primary
			 * mouse button is pressed.
			 */
			navigatorButtonBack.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {
					debugLog(
							"[navigatorButtonBack] Selected index field reads: "
									+ index);

					old.setStyle(null);
					if (iv.getBoundsInParent() != null) {
						iv.setStyle(
								"-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					/**
					 * Retrieve the current photo based on the current index
					 */
					currentPhoto = photoList.get(index);

					/**
					 * Update selected image in detail pane
					 */
					detailView.setImage(
							currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					/**
					 * Update the caption section.
					 */
					displayCaption.setText(currentPhoto.getCaption());
					captionField.setText("");

					/**
					 * Update selected image's detail pane
					 */
					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText("(size)" + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					ObservableList<Tag> tList = FXCollections
							.observableArrayList();

					/**
					 * Update tags in image detail pane
					 */
					for (Map.Entry<String, Tag> tag : currentPhoto.getTagMap()
							.entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);
					tagList.setItems(tList);

				}
			});

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("[navigatorButtonBack] Image " + currentPhoto.getFilename()
					+ " was selected");

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Clicked navigator button back");
		} else {

		}
	}

	/**
	 * Executes upon activating navigator next button
	 */
	public void doNavigatorButtonNext() {
		/**
		 * No album was selected
		 */
		if (currentAlbum == null) {
			return;
		}
		
		/**
		 * Album is selected but photo is not selected
		 */
		if (currentAlbum != null && currentPhoto == null) {
			return;
		}

		/**
		 * Album is selected and has at least 1 photo
		 */
		if (currentAlbum.getAlbumSize() != 0 && currentAlbum != null) {
			/**
			 * Establish new index. If current index equals the
			 * currentImageViewList size, minus 1 (new index == last index),
			 * then set the index to zero. (loop to beginning)
			 * 
			 * Otherwise, increment index by one to go to the next image.
			 */
			ImageView old;
			ImageView iv;

			if (index == currentImageViewList.size() - 1) {
				old = currentImageViewList.get(index);
				index = 0;
				iv = currentImageViewList.get(index);
			} else {
				old = currentImageViewList.get(index);
				++index;
				iv = currentImageViewList.get(index);
			}

			/**
			 * Add an event filter for navigatorButtonNext, for when the primary
			 * mouse button is pressed.
			 */
			navigatorButtonNext.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {
					debugLog(
							"[navigatorButtonNext] Selected index field reads: "
									+ index);

					old.setStyle(null);
					if (iv.getBoundsInParent() != null) {
						iv.setStyle(
								"-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					/**
					 * Retrieve the current photo based on the current index
					 */
					currentPhoto = photoList.get(index);

					/**
					 * Update selected image in detail pane
					 */
					detailView.setImage(
							currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					/**
					 * Update the caption section.
					 */
					displayCaption.setText(currentPhoto.getCaption());
					captionField.setText("");

					/**
					 * Update selected image's detail pane
					 */
					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText("(size)" + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					ObservableList<Tag> tList = FXCollections
							.observableArrayList();

					/**
					 * Update tags in image detail pane
					 */
					for (Map.Entry<String, Tag> tag : currentPhoto.getTagMap()
							.entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);
					tagList.setItems(tList);

				}
			});

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("[navigatorButtonNext] Image " + currentPhoto.getFilename()
					+ " was selected");

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Clicked navigator button next");
		}

	}
	
	public void selectFile() {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
				new ExtensionFilter("All Files", "*.*")
		);
		
		file = fileChooser.showOpenDialog(null);
		
		if(file != null) {
			pList.add(new Photo(file));
		}
		
		imageQueueList.setItems(pList);
	}
	
	public void addSelectedPhotos() {
		Album currentAlbum = model.getCurrentUser().getCurrentAlbum();
		
		System.out.println(currentAlbum);
		
		if(currentAlbum != null) {
			for(Photo p : imageQueueList.getItems()) {

				if(currentAlbum.addPhoto(p) == -1) {
					System.out.println("failed to add..");
				} else {
					System.out.println("added successfully.");
				}
			}
		}
		
		model.write();
	}
	
	/**
	 * Executes upon selecting about section
	 * 
	 * @throws IOException if user.fxml not found
	 */
	public void doAlbum() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(getClass().getResource("/view/user.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		imageQueueList.getScene().getWindow().hide();
		window.setScene(scene);
		window.setTitle("Photos -- Import");
		window.setResizable(false);
		window.show();
	}
	
	/**
	 * Executes upon selecting about section
	 * 
	 * @throws IOException if about.fxml not found
	 */
	public void doAbout() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(getClass().getResource("/view/about.fxml"));
		loader.setController(this);
		
		Parent root = loader.load();
		
		window.initModality(Modality.NONE);
		
		Scene scene = new Scene(root);
		
		window.setScene(scene);
		window.setTitle("About");
		window.setResizable(false);
		window.show();
	}

	/**
	 * Executes upon selecting help section
	 * 
	 * @throws IOException if help.fxml not found
	 */
	public void doHelp() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(getClass().getResource("/view/help.fxml"));
		loader.setController(this);
		
		Parent root = loader.load();
		
		window.initModality(Modality.NONE);
		
		Scene scene = new Scene(root);
		
		window.setScene(scene);
		window.setTitle("Help");
		window.setResizable(false);
		window.show();
	}
	
	public void doLogOut() throws IOException {
		model.write();
		
		Parent login = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);
		Stage currentStage = (Stage) (imageQueueList.getScene().getWindow());
		
		currentStage.hide();
		currentStage.setScene(loginScene);
		currentStage.setTitle("Photos -- V1.0");
		currentStage.show();
	}
	
	public void doQuit() {
		LoginController.exit();
	}

	/**
	 * Used for console message/testing functionality/method calls
	 * 
	 * @param message String that denotes a message for the console
	 */
	public void debugLog(String message) {
		System.out.println(
				"[" + this.getClass().getSimpleName() + "] " + message);
	}
}
