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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.PhotoModel;
import model.User;

/**
 * @version Mar 11, 2019
 * @author gemuelealudino
 *
 */
public class UserController {

	private PhotoModel model = LoginController.getModel();
	private User currentUser = model.getCurrentUser();
	private Photo currentPhoto = null;
	
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
	
	@FXML
	public void initialize() {
		System.out.println("Current user logged on is: " + model.getCurrentUser().getUsername());
		albumView.setItems(currentUser.getAlbumList());
		//infoData.setText(albumList.size() + " albums - " + " ### photos");
	}
	
	public void doSelectAlbum() {
        // get count of images from album first.
        // store this 
        final int imageCount = currentUser.getCurrentAlbum().getPhotoList().size();


    }
	
	public void setCaption() {
		if(captionField.getText().isEmpty() == false) {
			currentPhoto.setCaption(captionField.getText().trim());
			displayCaption.setText(captionField.getText().trim());
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please provide a caption.", ButtonType.OK);
			error.showAndWait();
		}
	}
	
	public void createTag() {
		if(tagName.getText().isEmpty() == false && tagValue.getText().isEmpty() == false) {
			if(currentPhoto.addTag(tagName.getText().trim(), tagValue.getText().trim()) == -1) {
					Alert error = new Alert(AlertType.ERROR, "Duplicate tag found. Tag not added!", ButtonType.OK);
					error.showAndWait();
			} else {
					Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Tag successfully added!", ButtonType.OK);
					success.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please provide a tag name and value.", ButtonType.OK);
			error.showAndWait();
		}
	}
	
	public void doCreate() {
		if(createAlbumName.getText().isEmpty() == false) {			
			String albumName = createAlbumName.getText().trim();
			
			if(currentUser.addAlbum(albumName) == -1) {
				Alert duplicate = new Alert(Alert.AlertType.ERROR, "Duplicate album found. Album not added!", ButtonType.OK);
				duplicate.showAndWait();
			} else {
				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Album successfully added!", ButtonType.OK);
				success.showAndWait();
				infoData.setText(albumView.getItems().size() + " albums - " + " ### photos");
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please provide an album name.", ButtonType.OK);
			error.showAndWait();
		}
	}
	
	public void doRename() { 
		int selectedIndex = albumView.getSelectionModel().getSelectedIndex();
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to rename this album?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() != ButtonType.YES)
			return;
		
		if(selectedIndex >= 0) {
			System.out.println("Selected Index: " + selectedIndex);
			currentUser.edit(selectedIndex, renameAlbumName.getText().trim());
			
			if(selectedIndex <= model.getItemCount() - 1) {
				albumView.getSelectionModel().select(selectedIndex);
			}
			
			Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Album successfully renamed!", ButtonType.OK);
			success.showAndWait();
			
			if(!success.isShowing()) {
				//set back to normal window
				
			}
		}
	}
	
	public void doDeleteAlbum() {
		int selectedIndex = albumView.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR, "There are no albums to be deleted.", ButtonType.OK);
			error.showAndWait();
			return;
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this album?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() != ButtonType.YES)
			return;
		
		if(selectedIndex >= 0) {
			System.out.println("Selected Index: " + selectedIndex);
			currentUser.deleteAlbum(selectedIndex);
			
			if(selectedIndex <= model.getItemCount() - 1) {
				albumView.getSelectionModel().select(selectedIndex);
			}
			
			Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Album successfully removed!", ButtonType.OK);
			success.showAndWait();
		}
	}
	
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
	
	public void doEditAlbum() throws IOException {	
		if(albumView.getSelectionModel().getSelectedIndex() < 0) {
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
			oldName.setText(albumView.getSelectionModel().getSelectedItem().getAlbumName());
		}
	}

	public void doLogOff() throws IOException {
		model.write();
		Parent login = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);
		Stage currentStage = (Stage) (albumView.getScene().getWindow());
		currentStage.hide();
		currentStage.setScene(loginScene);
		currentStage.setTitle("Photos -- V1.0");
		currentStage.show();
	}
	
	public void doQuit() {
		LoginController.exit();
	}
}
