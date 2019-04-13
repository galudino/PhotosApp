/**
 * UserController.java
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
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.Album;
import model.Photo;
import model.PhotoModel;
import model.User;

/**
 * Controller class to manage functionality of user.fxml
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj, Gemuele Aludino
 */
public class UserController {
	
	private Photo currentPhoto = null;
	private PhotoModel model = LoginController.getModel();
	
	private User currentUser = model.getCurrentUser();

	//@formatter:off
	@FXML ListView<Album> albumView;
	@FXML Hyperlink addAlbumHL;
	@FXML Label infoData;
	@FXML Button createAlbum;
	@FXML TextField createAlbumName;
	
	@FXML Button editAlbum;
	@FXML Button cancelEdit;
	@FXML TextField renameAlbumName;
	@FXML Text oldName;
	
	@FXML TilePane tilePaneImages;
	
	@FXML ListView tagList;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	
	@FXML TextField captionField;
	@FXML Label displayCaption;
	//@formatter:on

	@FXML
	public void initialize() {
		albumView.setItems(currentUser.getAlbumList());
		// infoData.setText(albumList.size() + " albums - " + " ### photos");
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
		debugLog("Current user logged on is: " + model.getCurrentUser().getUsername());
	}

	/**
	 * Executes upon setting a caption within captionField
	 */
	public void setCaption() {
		if (captionField.getText().isEmpty() == false) {
			/**
			 * SCENARIO 1: captionField is not empty.
			 */
			currentPhoto.setCaption(captionField.getText().trim());
			displayCaption.setText(captionField.getText().trim());
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("caption - \"" + captionField.getText() + "\" was set");
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
	}

	/**
	 * Executes upon creating a tag
	 */
	public void createTag() {
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
						"Duplicate tag found. Tag not added!", ButtonType.OK);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Duplicate tag found. Tag not added!");
				
				error.showAndWait();
			} else {
				/**
				 * SCENARIO 1b: tag entered does not exist
				 */
				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"Tag successfully added!", ButtonType.OK);
				
				debugLog("Tag " + tagName.getText() + " was successfully added!");
				
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
	}

	/**
	 * Executes upon creating an album
	 */
	public void doCreate() {
		if (createAlbumName.getText().isEmpty() == false) {
			/**
			 * SCENARIO 1: album name provided
			 */
			String albumName = createAlbumName.getText().trim();

			if (currentUser.addAlbum(albumName) == -1) {
				/**
				 * SCENARIO 1a: album name exists within albumList
				 */
				Alert duplicate = new Alert(Alert.AlertType.ERROR,
						"Duplicate album found. Album not added!",
						ButtonType.OK);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("ERROR: Duplicate album found. Album not added!");
				
				duplicate.showAndWait();
			} else {
				/**
				 * SCENARIO 1b: album name does not exist within albumList
				 */
				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"Album successfully added!", ButtonType.OK);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Album " + albumName + " successfully added!");
				
				success.showAndWait();
				
				infoData.setText(albumView.getItems().size() + " albums - "
						+ " ### photos");
			}
		} else {
			/**
			 * SCENARIO 2: album name not provided
			 */
			Alert error = new Alert(AlertType.ERROR,
					"Please provide an album name.", ButtonType.OK);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("ERROR: Please provide an album name.");
			
			error.showAndWait();
		}
	}

	/**
	 * Executes upon renaming an album
	 */
	public void doRename() {
		int selectedIndex = albumView.getSelectionModel().getSelectedIndex();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
				"Are you sure you want to rename this album?", ButtonType.YES,
				ButtonType.NO);
		
		alert.showAndWait();

		/**
		 * User no longer wants to rename album (did not select YES)
		 */
		if (alert.getResult() != ButtonType.YES) {
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Quit from renaming album.");
			return;
		}

		/**
		 * Album is found within list
		 */
		if (selectedIndex >= 0) {
			debugLog("Selected Index (album to be renamed): " + selectedIndex);
			
			currentUser.edit(selectedIndex, renameAlbumName.getText().trim());

			if (selectedIndex <= model.getItemCount() - 1) {
				albumView.getSelectionModel().select(selectedIndex);
			}

			Alert success = new Alert(Alert.AlertType.CONFIRMATION,
					"Album successfully renamed!", ButtonType.OK);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Album successfully renamed!");
			
			success.showAndWait();

			if (!success.isShowing()) {
				// set back to normal window
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Will set back to normal window.");
			}
		}
	}
	
	/**
	 * Executes upon selection of an album within albumView
	 */
	public void doSelectAlbum() {
        albumView.setCellFactory(lv -> {
            ListCell<Album> cell = new ListCell<Album>() {
                @Override
                protected void updateItem(Album item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if ((!cell.isEmpty())) {
                	/**
                	 * SCENARIO: Selected cell is not empty.
                	 */
                	
                    Album item = cell.getItem();
                    
                    if (event.isSecondaryButtonDown()) {
                        /**
                         * SCENARIO 1: Right clicked selection within albumView
                         */
                    	
                    	/**
                    	 * CONSOLE DIAGNOSTICS
                    	 */
                    	debugLog("Right click selection: " + item);
                    } else {
                        /**
                         * SCENARIO 2: Did not right click  within albumView
                         */
                    	
                    	debugLog("Selection: " + item);
                    	
                    	
                    	
                    	Album album = cell.getItem();
                    	List<Photo> photoList = album.getPhotoList();
                    	
                    	if (photoList.isEmpty()) {
                    		debugLog("photoList is empty!");
                    	}
                    	
                    	for (Photo p : photoList) {
                    		ImageView iv = new ImageView(p.getFileName());
                    		
                    		/**
                    		 * These 'magic numbers' are temporary.
                    		 * They ought to be mutable values --
                    		 * using the "zoom-lever" within the GUI
                    		 * (these are for the image thumbnails)
                    		 */
                    		iv.setFitHeight(120);
                    		iv.setFitWidth(120);
                    		
                    		iv.setPreserveRatio(true);
                    		
                    		tilePaneImages.getChildren().add(iv);
                    		
                    		/**
                    		 * CONSOLE DIAGNOSTICS
                    		 */
                    		debugLog("Photo " + p + " added to tilePaneImages");
                    	}
                    }
                }
            });
            
            return cell;
        });
    }
	
	/**
	 * Executes upon selecting add album mechanism
	 * 
	 * @throws IOException if add_album.fxml not found
	 */
	public void doAddAlbum() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(getClass().getResource("/view/add_album.fxml"));
		loader.setController(this);
		
		Parent root = loader.load();
		
		window.initModality(Modality.APPLICATION_MODAL);
		
		Scene scene = new Scene(root);
		
		window.setScene(scene);
		window.setTitle("Photos -- Add Album");
		window.setResizable(false);
		window.show();
	}

	/**
	 * Executes upon selecting edit album mechanism
	 * 
	 * @throws IOException if edit_album.fxml not found
	 */
	public void doEditAlbum() throws IOException {
		if (albumView.getSelectionModel().getSelectedIndex() < 0) {
			return;
		} else {
			Stage window = new Stage();
			FXMLLoader loader = new FXMLLoader();
			
			loader.setLocation(getClass().getResource("/view/edit_album.fxml"));
			loader.setController(this);
			
			Parent root = loader.load();
			window.initModality(Modality.APPLICATION_MODAL);
			
			Scene scene = new Scene(root);
			
			window.setScene(scene);
			window.setTitle("Photos -- Edit Album");
			window.setResizable(false);
			window.show();
			
			oldName.setText(albumView.getSelectionModel().getSelectedItem()
					.getAlbumName());
		}
	}

	/**
	 * Executes upon deleting album
	 */
	public void doDeleteAlbum() {
		int selectedIndex = albumView.getSelectionModel().getSelectedIndex();
		
		/**
		 * No albums are found to be deleted.
		 */
		if (selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR,
					"There are no albums to be deleted.", ButtonType.OK);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("There are no albums to be deleted.");
			
			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
				"Are you sure you want to delete this album?", ButtonType.YES,
				ButtonType.NO);
		
		alert.showAndWait();

		/**
		 * User no longer wants to delete the selected album
		 */
		if (alert.getResult() != ButtonType.YES) {
			/**
			 * CONSOLE DIAGNOSTICS 
			 */
			debugLog("Quit from deleting album.");
			return;
		}

		/**
		 * Album is found within list
		 */
		if (selectedIndex >= 0) {
			debugLog("Selected Index (album to be deleted): " + selectedIndex);
			currentUser.deleteAlbum(selectedIndex);

			if (selectedIndex <= model.getItemCount() - 1) {
				albumView.getSelectionModel().select(selectedIndex);
			}

			Alert success = new Alert(Alert.AlertType.CONFIRMATION,
					"Album successfully removed!", ButtonType.OK);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Album successfully removed!");
			
			success.showAndWait();
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

	/**
	 * Executes upon logging off
	 * 
	 * @throws IOException if login.fxml not found
	 */
	public void doLogOff() throws IOException {
		model.write();
		
		Parent login = FXMLLoader
				.load(getClass().getResource("/view/login.fxml"));
		
		Scene loginScene = new Scene(login);
		
		Stage currentStage = (Stage) (albumView.getScene().getWindow());
		
		currentStage.hide();
		currentStage.setScene(loginScene);
		currentStage.setTitle("Photos -- V1.0");
		currentStage.show();
	}

	/**
	 * Executes upon terminating program
	 */
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
