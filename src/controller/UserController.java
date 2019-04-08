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

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Album;
import model.PhotoModel;

/**
 * @version Mar 11, 2019
 * @author gemuelealudino
 *
 */
public class UserController {

	private PhotoModel model = LoginController.getModel();
	
	@FXML ListView<Album> albumView;
	
	@FXML
	public void initialize() {
		ObservableList<Album> aList = model.getCurrentUser().getAlbumList();
		System.out.println("Current user logged on is: " + model.getCurrentUser().getUsername());
		System.out.println(aList);
		//albumView.setItems(aList);
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
