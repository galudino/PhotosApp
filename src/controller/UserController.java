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
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import model.Album;
import model.Photo;
import model.PhotoModel;
import model.Tag;
import model.User;

/**
 * Controller class to manage functionality of user.fxml
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj, Gemuele Aludino
 */
public class UserController {
	
	private PhotoModel model = LoginController.getModel();
	private User currentUser = model.getCurrentUser();
	private Photo currentPhoto = null;
	private Album currentAlbum = null;
	
	ObservableList<Photo> photoList = FXCollections.observableArrayList();

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
	
	@FXML ListView<Tag> tagList;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	
	@FXML TextField captionField;
	@FXML Label displayCaption;
	
	@FXML Label sizeField;
	@FXML Label createdField;
	@FXML Label pathField;
	@FXML Label nameField;
	
	@FXML ImageView detailView;
	
	@FXML Label albumHeader;
	@FXML Hyperlink addHL;
	//@formatter:on
	
	int index = -1;

	@FXML
	public void initialize() {
		albumView.setItems(currentUser.getAlbumList());
		infoData.setText(currentUser.getAlbumList().size() + " albums - "  + "  photos");
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
		debugLog("Current user logged on is: " + model.getCurrentUser().getUsername());
		debugLog("Current user has albums: " + currentUser.getAlbumMap());
		debugLog("Current album:" + currentUser.getCurrentAlbum());
		debugLog("Current photo list: " + currentUser.getCurrentAlbum().getPhotoMap());
	}
	
	public void importPhoto() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(getClass().getResource("/view/import.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		addHL.getScene().getWindow().hide();
		window.setScene(scene);
		window.setTitle("Photos -- Import");
		window.setResizable(false);
		window.show();
	}

	/**
	 * Executes upon setting a caption within captionField
	 */
	public void setCaption() {
		if(currentPhoto != null) {
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
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to create a caption for.", ButtonType.OK);
			
			debugLog("ERROR: Please select a photo.");
			
			error.showAndWait();
		}
	}
	
	public void deletePhoto() {
		if(currentPhoto != null) {
			int selectedIndex = index;

			if(selectedIndex < 0) {
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
					"Are you sure you want to delete this photo?", ButtonType.YES,
					ButtonType.NO);
			
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
				debugLog("Selected Index (Photo to be deleted): " + selectedIndex);
				currentAlbum.deletePhoto(selectedIndex);

				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"Photo successfully removed!", ButtonType.OK);
				
				tilePaneImages.getChildren().remove(selectedIndex);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Photo successfully removed!");
				
				success.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to delete.", ButtonType.OK);
			
			debugLog("ERROR: Please select a photo.");
			
			error.showAndWait();
		}

	}

	/**
	 * Executes upon creating a tag
	 */
	public void createTag() {
		if(currentPhoto != null) {		
			if (tagName.getText().isEmpty() == false && tagValue.getText().isEmpty() == false) {
				/**
				 * SCENARIO 1: tagName and tagValue are not empty
				 */
				if (currentPhoto.addTag(tagName.getText().trim(), tagValue.getText().trim()) == -1) {
					/**
					 * SCENARIO 1a: tag entered already exists
					 */
					Alert error = new Alert(AlertType.ERROR,
							"Duplicate tag found. Tag not added!", ButtonType.OK);
					
					/**
					 * CONSOLE DIAGNOSTICS
					 */
					debugLog("Duplicate tag found. Tag not added!");
					
					debugLog(tagName.getText().trim() + " " + tagValue.getText().trim());
					
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
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to create a tag for.", ButtonType.OK);
			
			debugLog("ERROR: Please select a photo.");
			
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
				createAlbumName.getScene().getWindow().hide();
				
				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"Album successfully added!", ButtonType.OK);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Album " + albumName + " successfully added!");
				
				success.showAndWait();
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
			
			if(currentUser.edit(selectedIndex, renameAlbumName.getText().trim()) == -1) {
				Alert duplicate = new Alert(Alert.AlertType.ERROR,
						"Duplicate album found. Album not renamed!",
						ButtonType.OK);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("ERROR: Duplicate album found. Album not renamed!");
				
				duplicate.showAndWait();
			} else {
				if (selectedIndex <= model.getItemCount() - 1) {
					albumView.getSelectionModel().select(selectedIndex);
				}
				
				renameAlbumName.getScene().getWindow().hide();

				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"Album successfully renamed!", ButtonType.OK);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Album successfully renamed!");
				
				success.showAndWait();
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
                         * SCENARIO 2: Did not right click within albumView
                         */
                    	
                    	debugLog("Selection: " + item);
                    		
                    	currentAlbum = cell.getItem();
                    	
                    	albumHeader.setText(currentAlbum.getAlbumName());

                    	photoList = FXCollections.observableArrayList();
                    	
                    	for(Map.Entry<String, Photo> test2 : currentAlbum.getPhotoMap().entrySet()) {
                    		photoList.add(test2.getValue());
                    		System.out.println("Adding: " + test2.getValue());
                    	}
                    	
                    	currentAlbum.setPhotoList(photoList);
                    	
                    	if (photoList == null) {
                    		debugLog("photoList is empty!");
                    	}
                    	
                    
                    	
                    	tilePaneImages.getChildren().clear();
                    	
                    	/**
                    	 * Adds padding and insets to thumbnails.
                    	 * Ought to be adjusted later.
                    	 */
                    	tilePaneImages.setPadding(new Insets(10, 10, 10, 10));
                    	tilePaneImages.setHgap(10);
                    	
                    	for (Photo p : photoList) {
                    		ImageView iv = new ImageView(p.getFilepath());

                    		/**
                    		 * These 'magic numbers' are temporary.
                    		 * They ought to be mutable values --
                    		 * using the "zoom-lever" within the GUI
                    		 * (these are for the image thumbnails)
                    		 */
                    		iv.setFitHeight(120);
                    		iv.setFitWidth(120);
                    		iv.setPreserveRatio(true);
                    		
                    		iv.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                    			if (e.isSecondaryButtonDown()) {
                    				debugLog("Image " + p.getFilename() + " right clicked");
                    			} else {
                    				                    				
                    				setSelectedIndex(tilePaneImages.getChildren().indexOf(iv));
                    				
                    				iv.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
                    				currentPhoto = p;
                    				
                    				debugLog("Image " + p.getFilename() + " selected");
                    				debugLog(p.getFilename() + " was created on: ");
                    				nameField.setText(currentPhoto.getFilename());
                    				pathField.setText(currentPhoto.getFilepath());
                    				sizeField.setText(" KB");
                    				createdField.setText(currentPhoto.getDatePhoto());
                    				
                    				displayCaption.setText(p.getCaption());
                    				
                    				captionField.setText("");
                    				
                    				detailView.setImage(new Image(p.getFilepath()));
                    				detailView.setFitHeight(150);
                    				detailView.setFitWidth(200);
                    				detailView.setVisible(true);
                    				detailView.setPreserveRatio(true);
                            		
                    				ObservableList<Tag> tList = FXCollections.observableArrayList();
                    				
                            		for(Map.Entry<String, Tag> tag : currentPhoto.getTagMap().entrySet()) {
                            			tList.add(tag.getValue());
                            		}
                            		
                            		currentPhoto.setTagList(tList);
                            		
                            		tagList.setItems(tList);
                    				
                    			}
                    		});
                    		
                    		tilePaneImages.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                    			if(e.isPrimaryButtonDown()) {
                    				iv.setStyle(null);
                    				
                    				detailView.setImage(null);
                    				displayCaption.setText(null);
                    				nameField.setText("(No image selected)");
                    				pathField.setText("(No image selected)");
                    				sizeField.setText("(No image selected)");
                    				createdField.setText("(No image selected)");
                    				
                    				tagName.setText("");
                    				tagValue.setText("");
                    				
                    				tagList.setItems(null);
                    				
                    				currentPhoto = null;
                    			}
                    		});
                    		
                    		              		
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
	
	private void setSelectedIndex(int selectedIndex) {
		index = selectedIndex;
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