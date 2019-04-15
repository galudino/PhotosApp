/**
 * SearchController.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.PhotoModel;
import model.Tag;
import model.User;

class TagConditional {
	Tag tag1;
	Tag tag2;

	String conditional;

	public TagConditional(Tag tag1, Tag tag2, String conditional) {
		this.tag1 = tag1;
		this.tag2 = tag2;

		switch (conditional) {
		case "AND":
		case "OR":
		case "NOT":
			this.conditional = conditional;
			break;
		default:
			System.exit(0);
			break;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", tag1, conditional, tag2);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof TagConditional) {
			TagConditional tc = (TagConditional)(o);
			
			return tag1.equals(tc.tag2) && conditional.equals(tc.conditional); 
		} else {
			return false;
		}
	}
}

/**
 * Controller class to manage functionality of search.fxml
 * 
 * @version Apr 12, 2019
 * @author Gemuele Aludino
 */
public class SearchController {

	private int index = 0;

	final double DEFAULT_INSET_VALUE = 10.0;
	final double DEFAULT_HEIGHT_VALUE = 150.0;
	final double DEFAULT_WIDTH_VALUE = 200.0;

	double imageInsetValue = DEFAULT_INSET_VALUE;
	double imageHeightValue = DEFAULT_HEIGHT_VALUE;
	double imageWidthValue = DEFAULT_WIDTH_VALUE;

	boolean deleteFromDisk;
	boolean promptBeforeDelete;
	
	boolean searchScopeThisAlbum;
	boolean searchScopeSelectedAlbum;
	boolean searchScopeAllAlbums;
	
	boolean searchThisAlbum;
	boolean searchSelectedAlbum;
	boolean searchAllAlbums;
	
	boolean searchAnd;
	boolean searchOr;
	boolean searchNot;

	boolean viewModeThumbnail;
	boolean viewModeSingleImage;
	
	LocalDate dateFrom;
	LocalDate dateTo;
	long longDateFrom;
	long longDateTo;

	String stringSearchTagNameOne;
	String stringSearchTagValueOne;
	String stringSearchTagNameTwo;
	String stringSearchTagValueTwo;
	
	List<TagConditional> tagConditionalList;

	private Photo currentPhoto = null;
	private PhotoModel model = LoginController.getModel();
	private Album currentAlbum = null;
	private User currentUser = model.getCurrentUser();

	private List<ImageView> currentImageViewList = null;

	ObservableList<Photo> photoList = FXCollections.observableArrayList();
	
	//@formatter:off
	@FXML Button cancelEdit;
	@FXML Button createAlbum;
	@FXML Button editAlbum;
	@FXML Button navigatorButtonBack;
	@FXML Button navigatorButtonNext;
	@FXML Button buttonSearchNow;
	
	@FXML Button buttonCopyPhoto;
	@FXML Button buttonDeletePhoto;
	@FXML Button buttonMovePhoto;
	
	@FXML Button buttonAddCaption;
	@FXML Button buttonAddTag;
	@FXML Button buttonAddTagPair;
	
	@FXML CheckBox checkBoxDeleteFromDisk;
	@FXML CheckBox checkBoxPromptBeforeDelete;
	
	@FXML ChoiceBox<Album> albumList;
	
	@FXML DatePicker datePickerFrom;
	@FXML DatePicker datePickerTo;
	
	@FXML Hyperlink addAlbumHL;
	
	@FXML ImageView detailView;
	
	@FXML Label createdField;
	@FXML Label displayCaption;
	@FXML Label infoData;
	@FXML Label nameField;
	@FXML Label pathField;
	@FXML Label sizeField;
	
	@FXML ListView<Album> albumView;
	@FXML ListView<TagConditional> listViewTagSearch;
	@FXML ListView<Tag> tagList;
	
	@FXML MenuItem fileReturnToAlbums;
	@FXML MenuItem fileOpenSelectedPhotoInViewer;
	@FXML MenuItem fileSaveAndLogout;
	@FXML MenuItem fileSaveAndExit;

	@FXML MenuItem removeTag;
	
	@FXML MenuItem menuItemAboutPhotos;
	@FXML MenuItem menuItemHelp;
	@FXML MenuItem menuItemRemoveTagFromSearch;
	@FXML MenuItem viewAsSingleImage;
	@FXML MenuItem viewAsThumbnails;
	
	@FXML RadioButton radioButtonThumbnail;
	@FXML RadioButton radioButtonSingleImage;
	
	@FXML RadioButton radioButtonThisAlbum;
	@FXML RadioButton radioButtonSelectedAlbum;
	@FXML RadioButton radioButtonAllAlbums;
	
	@FXML RadioButton radioButtonAnd;
	@FXML RadioButton radioButtonOr;
	@FXML RadioButton radioButtonNot;
	
	@FXML Slider zoomSlider;
	
	@FXML TilePane tilePaneImages;
	
	@FXML TextField captionField;
	@FXML TextField createAlbumName;
	@FXML TextField renameAlbumName;
	@FXML TextField searchTagNameOne;
	@FXML TextField searchTagValueOne;
	@FXML TextField searchTagNameTwo;
	@FXML TextField searchTagValueTwo;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	
	@FXML Text oldName;
	//@formatter:on

	@FXML
	public void initialize() {
		/**
		 * Upon entering SearchController,
		 * 
		 * You must pass the User's album list so it can be used for either
		 * searching - the previous selected album from UserController - a
		 * user-selectable album (from their list of Albums) - all of their
		 * albums.
		 */
		
		/**
		 * Setting default values
		 */
		radioButtonSelectedAlbum.setSelected(true);
		doRadioButtonSelectedAlbum();

		radioButtonAnd.setSelected(true);
		doRadioButtonAnd();

		radioButtonThumbnail.setSelected(true);
		doViewModeThumbnail();

		initAlbumList();
		
		tagConditionalList = new ArrayList<TagConditional>();
	
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
	}

	public void initAlbumList() {
		albumList.getItems().clear();

		ObservableList<Album> currentAlbumList = model.getCurrentUser()
				.getAlbumList();

		for (Album a : currentAlbumList) {
			albumList.getItems().add(a);
		}

		albumList.setOnAction(event -> {
			radioButtonSelectedAlbum.setSelected(true);
			doRadioButtonSelectedAlbum();
			Album test = albumList.getValue();

			for (Album a : currentAlbumList) {
				if (a.equals(test)) {
					debugLog(a + " == " + test);

					model.getCurrentUser().setCurrentAlbum(test);
					currentAlbum = test;
				}
			}

			debugLog("selected album: " + currentAlbum.getAlbumName());
		});

	}

	public void doAlbumList() {
		radioButtonSelectedAlbum.setSelected(true);
		doRadioButtonSelectedAlbum();
		
		// reassign the current album to currentAlbum
		
		// get the newly assigned album from here
		//@FXML ChoiceBox<Album> albumList;
		
		Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
		currentAlbum = selectedAlbum != null ? selectedAlbum : null;
		
		debugLog("[doAlbumList]" + " newly selected album: " + currentAlbum);
	}

	public void doButtonSearchNow() {
		debugLog("[doButtonSearchNow]");

		// using the search scope,
		// determine which album is to be searched.

		// buttonSearchNow

		/**
		 * Conduct the search using:
		 * 
		 * Album scope (the current album, a selected album, or all the albums?)
		 * 
		 * LocalDate from, LocalDate to All photos must have a modified date
		 * from-to
		 * 
		 * Tag Conditionals (how will this be done?
		 * 
		 * Tag1 && Tag2 Tag3 || Tag4
		 */
	}

	public void doMenuItemRemoveTagFromSearch() {
		debugLog("[doMenuItemRemoveTagFromSearch]");

		//menuItemRemoveTagFromSearch
		
		//Remove the tag conditional from these three:
		
			//List<TagConditional> tagConditionalList;
			//@FXML ListView<TagConditional> listViewTagSearch;
				
		int selectedIndex = listViewTagSearch.getSelectionModel().getSelectedIndex();
		
		if (selectedIndex < 0) {
			debugLog("No TagConditional to remove.");
			return;
		}
		
		listViewTagSearch.getItems().remove(selectedIndex);
	
		tagConditionalList.remove(selectedIndex);
	}

	public void doButtonAddTagPair() {
		debugLog("[doButtonAddTagPair]");

		/**
		 * Grab the strings from the TextFields
		 */
		stringSearchTagNameOne = searchTagNameOne.getText();
		stringSearchTagValueOne = searchTagValueOne.getText();
		stringSearchTagNameTwo = searchTagValueTwo.getText();
		stringSearchTagValueTwo = searchTagValueTwo.getText();

		/**
		 * Create tag1 and tag2 based off of the strings grabbed
		 * from TextFields
		 */
		Tag tag1 = new Tag(stringSearchTagNameOne, stringSearchTagValueOne);
		Tag tag2 = new Tag(stringSearchTagNameTwo, stringSearchTagValueTwo);

		/**
		 * Determine the condition based on the RadioButtons
		 * activated and the boolean values set.
		 */
		String condition = "";
		if (searchAnd) {
			condition = "AND";
		} else if (searchOr) {
			condition = "OR";
		} else if (searchNot) {
			condition = "NOT";
		}
		
		/**
		 * Create the tag conditional
		 */
		TagConditional conditional = new TagConditional(tag1, tag2, condition);
		
		/**
		 * Add the conditional to the List<TagConditional>
		 * and ObservableList<TagConditional>
		 */
		tagConditionalList.add(conditional);
		//tagConditionalObservableList.add(conditional);
				
		/**
		 * Add the ObservableList<TagConditional>
		 * to the ListView interface
		 */
		listViewTagSearch.setItems(FXCollections.observableArrayList(tagConditionalList));
		
		debugLog(conditional + "was added");
	}

	public void doRadioButtonAnd() {
		if (radioButtonAnd.isSelected()) {
			radioButtonOr.setSelected(false);
			radioButtonNot.setSelected(false);

			searchAnd = true;
			searchOr = false;
			searchNot = false;
		}

		debugLog("[doRadioButtonAnd]");
	}

	public void doRadioButtonOr() {
		if (radioButtonOr.isSelected()) {
			radioButtonAnd.setSelected(false);
			radioButtonNot.setSelected(false);

			searchOr = true;
			searchAnd = false;
			searchNot = false;
		}

		debugLog("[doRadioButtonOr]");
	}

	public void doRadioButtonNot() {
		if (radioButtonNot.isSelected()) {
			radioButtonAnd.setSelected(false);
			radioButtonOr.setSelected(false);

			searchNot = true;
			searchAnd = false;
			searchOr = false;
		}

		debugLog("[doRadioButtonNot]");
	}

	public void doSearchTagNameOne() {
		debugLog("[doSearchTagNameOne]");

		// searchTagNameOne
	}

	public void doSearchTagValueOne() {
		debugLog("[doSearchTagValueOne");

		// searchTagValueOne
	}

	public void doSearchTagNameTwo() {
		debugLog("[doSearchTagNameTwo]");

		// searchTagNameTwo
	}

	public void doSearchTagValueTwo() {
		debugLog("[doSearchTagValueTwo]");

		// searchTagValueTwo
	}

	public void doDatePickerFrom() {
		debugLog("[doDatePickerFrom]");

		dateFrom = datePickerFrom.getValue();
		longDateFrom = 0;	// TODO must figure out how to convert LocalDate to long

		debugLog(dateFrom + " was retrieved" + " (long value) " + longDateFrom);
	}

	public void doDatePickerTo() {
		debugLog("[doDatePickerTo]");

		dateTo = datePickerTo.getValue();
		longDateTo = 0; // TODO must figure out how to convert LocalDate to long
		
		debugLog(dateFrom + " was retrieved" + " (long value) " + longDateTo);
	}

	public void doRadioButtonThisAlbum() {
		if (radioButtonThisAlbum.isSelected()) {
			radioButtonSelectedAlbum.setSelected(false);
			radioButtonAllAlbums.setSelected(false);

			searchScopeThisAlbum = true;
			searchScopeSelectedAlbum = false;
			searchScopeAllAlbums = false;
		}

		debugLog("[doRadioButtonThisAlbum]");
	}

	public void doRadioButtonSelectedAlbum() {
		if (radioButtonSelectedAlbum.isSelected()) {
			radioButtonThisAlbum.setSelected(false);
			radioButtonAllAlbums.setSelected(false);

			searchScopeSelectedAlbum = true;
			searchScopeThisAlbum = false;
			searchScopeAllAlbums = false;
		}

		debugLog("[doRadioButtonSelectedAlbum]");
	}

	public void doRadioButtonAllAlbums() {
		if (radioButtonAllAlbums.isSelected()) {
			radioButtonThisAlbum.setSelected(false);
			radioButtonSelectedAlbum.setSelected(false);

			searchScopeAllAlbums = true;
			searchScopeThisAlbum = false;
			searchScopeSelectedAlbum = false;
		}

		debugLog("[doRadioButtonAllAlbums]");
	}

	public void doViewModeThumbnail() {
		if (radioButtonThumbnail.isSelected()) {
			radioButtonSingleImage.setSelected(false);

			viewModeThumbnail = true;
			viewModeSingleImage = false;
		}

		debugLog("[doViewModeThumbnail]");
	}

	public void doViewModeSingleImage() {
		if (radioButtonSingleImage.isSelected()) {
			radioButtonThumbnail.setSelected(false);

			viewModeSingleImage = true;
			viewModeThumbnail = false;
		}

		debugLog("[doViewModeSingleImage]");
	}

	public void doOpenSelectedPhotoInViewer() {
		debugLog("[doOpenSelectedPhotoInViewer]");
	}

	public void movePhoto() {
		debugLog("Move photo button pressed");
	}

	public void copyPhoto() {
		debugLog("Copy photo button pressed");
	}

	public void deletePhoto() {
		if (currentPhoto != null) {
			int selectedIndex = index;

			if (selectedIndex < 0) {
				Alert error = new Alert(Alert.AlertType.ERROR,
						"There are no photos to be deleted.", ButtonType.OK);

				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("There are no photos to be deleted.");

				error.showAndWait();
				return;
			}

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
					"Are you sure you want to delete this photo?",
					ButtonType.YES, ButtonType.NO);

			alert.showAndWait();

			/**
			 * User no longer wants to delete the selected album
			 */
			if (alert.getResult() != ButtonType.YES) {
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Quit from deleting photo.");
				return;
			}

			/**
			 * Photo is found within list
			 */
			if (selectedIndex >= 0) {
				debugLog("Selected Index (Photo to be deleted): "
						+ selectedIndex);
				currentAlbum.deletePhoto(selectedIndex);

				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"Photo successfully removed!", ButtonType.OK);

				tilePaneImages.getChildren().remove(selectedIndex);

				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Photo successfully removed!");

				updateInfoData();

				success.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to delete.", ButtonType.OK);

			debugLog("ERROR: Please select a photo.");

			error.showAndWait();
		}

	}

	public void doCheckBoxDeleteFromDisk() {
		deleteFromDisk = deleteFromDisk ? false : true;
		
		debugLog("Delete from disk checked -- delete from disk enabled: " + deleteFromDisk);
	}

	public void doCheckBoxPromptBeforeDelete() {
		promptBeforeDelete = promptBeforeDelete ? false : true;
		
		debugLog("Prompt before delete checked -- prompt before delete enabled: " + promptBeforeDelete);
	}

	public void removeTag() {
		int selectedIndex = tagList.getSelectionModel().getSelectedIndex();

		if (selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR,
					"There are no tags to be deleted.", ButtonType.OK);

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("There are no tags to be deleted.");

			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
				"Are you sure you want to delete this tag?", ButtonType.YES,
				ButtonType.NO);

		alert.showAndWait();

		/**
		 * User no longer wants to delete the selected tag
		 */
		if (alert.getResult() != ButtonType.YES) {
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Quit from deleting tag.");
			return;
		}

		/**
		 * Tag is found within list
		 */
		if (selectedIndex >= 0) {
			debugLog("Selected Index (tag to be deleted): " + selectedIndex);
			currentPhoto.deleteTag(selectedIndex);

			Alert success = new Alert(Alert.AlertType.CONFIRMATION,
					"Tag successfully removed!", ButtonType.OK);

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("ATag successfully removed!");

			success.showAndWait();
		}
	}

	/**
	 * Executes upon selecting add album mechanism
	 * 
	 * @throws IOException if add_album.fxml not found
	 */
	public void doAddAlbum() throws IOException {
		/*
		 * Stage window = new Stage(); FXMLLoader loader = new FXMLLoader();
		 * 
		 * loader.setLocation(getClass().getResource("/view/add_album.fxml"));
		 * loader.setController(this);
		 * 
		 * Parent root = loader.load();
		 * 
		 * window.initModality(Modality.APPLICATION_MODAL); Scene scene = new
		 * Scene(root);
		 * 
		 * window.setScene(scene); window.setTitle("Photos -- Add Album");
		 * window.setResizable(false); window.show();
		 * 
		 * updateInfoData();
		 */

		debugLog("[doAddAlbum] (from results)");
	}

	/**
	 * Runs each time there is an update to the user's albums and/or photos.
	 */
	public void updateInfoData() {
		int albumCount = currentUser.getAlbumList().size();
		int totalPhotoCount = 0;
		long totalByteCount = 0;

		for (Album a : currentUser.getAlbumMap().values()) {
			totalPhotoCount += a.getAlbumSize();

			for (Photo p : a.getPhotoMap().values()) {
				totalByteCount += p.getFileSize();
				debugLog("" + totalByteCount);
			}
		}

		String output = String.format("%d albums - %d photos - %d KB",
				albumCount, totalPhotoCount, totalByteCount);

		infoData.setText(output);
	}

	/**
	 * Executes upon creating a tag
	 */
	public void createTag() {
		if (currentPhoto != null) {
			if (tagName.getText().isEmpty() == false
					&& tagValue.getText().isEmpty() == false) {
				/**
				 * SCENARIO 1: tagName and tagValue are not empty
				 */
				if (currentPhoto.addTag(tagName.getText().trim(),
						tagValue.getText().trim()) == -1) {
					/**
					 * SCENARIO 1a: tag entered already exists
					 */
					Alert error = new Alert(AlertType.ERROR,
							"Duplicate tag found. Tag not added!",
							ButtonType.OK);

					/**
					 * CONSOLE DIAGNOSTICS
					 */
					debugLog("Duplicate tag found. Tag not added!");

					debugLog(tagName.getText().trim() + " "
							+ tagValue.getText().trim());

					error.showAndWait();
				} else {
					/**
					 * SCENARIO 1b: tag entered does not exist
					 */

					Alert success = new Alert(Alert.AlertType.CONFIRMATION,
							"Tag successfully added!", ButtonType.OK);

					debugLog("Tag " + tagName.getText()
							+ " was successfully added!");

					success.showAndWait();
				}
			} else {
				/**
				 * SCENARIO 2: either tagName or tagValue or both are empty
				 */
				Alert error = new Alert(AlertType.ERROR,
						"Please provide a tag name and value.", ButtonType.OK);

				debugLog("ERROR: Please provide a tag name and value.");

				error.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to create a tag for.",
					ButtonType.OK);

			debugLog("ERROR: Please select a photo.");

			error.showAndWait();
		}
	}

	/**
	 * Executes upon setting a caption within captionField
	 */
	public void setCaption() {
		if (currentPhoto != null) {
			if (captionField.getText().isEmpty() == false) {
				/**
				 * SCENARIO 1: captionField is not empty.
				 */
				currentPhoto.setCaption(captionField.getText().trim());
				displayCaption.setText(captionField.getText().trim());

				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog(
						"caption - \"" + captionField.getText() + "\" was set");
			} else {
				/**
				 * SCENARIO 2: captionField is an empty string.
				 */
				Alert error = new Alert(AlertType.ERROR,
						"Please provide a caption.", ButtonType.OK);

				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("caption provided was an empty string.");

				error.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to create a caption for.",
					ButtonType.OK);

			debugLog("ERROR: Please select a photo.");

			error.showAndWait();
		}
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
		// model.write();

		Parent login = FXMLLoader
				.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);
		// Stage currentStage = (Stage) (imageQueueList.getScene().getWindow());

		// currentStage.hide();
		// currentStage.setScene(loginScene);
		// currentStage.setTitle("Photos -- V1.0");
		// currentStage.show();
	}

	public void doQuit() {
		LoginController.exit();
	}

	public void doAlbum() throws IOException {
		/*
		 * Stage window = new Stage(); FXMLLoader loader = new FXMLLoader();
		 * 
		 * loader.setLocation(getClass().getResource("/view/user.fxml")); Parent
		 * root = loader.load(); Scene scene = new Scene(root);
		 * addAlbumHL.getScene().getWindow().hide(); window.setScene(scene);
		 * window.setTitle("Photos -- Import"); window.setResizable(false);
		 * window.show();
		 */

		debugLog("doAlbum");
	}

	public void doZoomSlider() {
		if (currentAlbum == null || currentImageViewList == null) {
			return;
		}

		zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();

				imageInsetValue = factor * DEFAULT_INSET_VALUE; // default 10.00
				imageHeightValue = factor * DEFAULT_HEIGHT_VALUE; // default
																	// 150.00
				imageWidthValue = factor * DEFAULT_WIDTH_VALUE; // default
																// 200.00

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

	private void setSelectedIndex(int selectedIndex) {
		index = selectedIndex;
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