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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Photo;
import model.PhotoModel;

/**
 * Controller class to manage functionality of import.fxml
 * 
 * @version Apr 12, 2019
 * @author Gemuele Aludino
 */
public class PhotoController {
	
	public PhotoModel model = LoginController.getModel();
	
	// TODO fields
	
	ObservableList<Photo> pList = FXCollections.observableArrayList();
	
	@FXML ListView imageQueueList;
	@FXML SplitMenuButton albumList;
	
	FileChooser fileChooser;
	File file;
	Desktop desktop = Desktop.getDesktop();
	
	@FXML
	public void initialize() {
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
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
	
	/**
	 * Executes upon selecting about section
	 * 
	 * @throws IOException if about.fxml not found
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
