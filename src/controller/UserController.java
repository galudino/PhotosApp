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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
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
	
	@FXML ListView<Album> albumView;
	@FXML Hyperlink addAlbumHL;
	
	@FXML
	public void initialize() {
		ObservableList<Album> albumList = FXCollections.observableArrayList(currentUser.load());
		System.out.println("Current user logged on is: " + model.getCurrentUser().getUsername());
		albumView.setItems(albumList);
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
		window.setTitle("Help");
		window.setResizable(false);
		window.show();
	}
	
	//does not work properly.
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
	
	//functions as it should.
	public void doQuit() {
		LoginController.exit();
	}
}
