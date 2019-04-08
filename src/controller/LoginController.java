/**
 * LoginController.java
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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.PhotoModel;
import model.User;

public class LoginController {
	
	public final static String DAT_FILE_PATH = "photos.dat";
	
	static PhotoModel model;
	
	@FXML Button buttonLogin;
	@FXML TextField textFieldUsername;
	@FXML TextField textFieldPassword;
	
	/**
	 * Initialization process to ensure everything is set to null including user model.
	 */
	@FXML
	public void initialize() {
		model = new PhotoModel();
		textFieldUsername.clear();
		textFieldPassword.clear();
	}

	/**
	 * Login method -- obtains the information from the textfields, and attempts to map a user object.
	 * @throws IOException: will throw an IOException if file is not found (FXML).
	 */
	public void doLogin() throws IOException {
		String username = textFieldUsername.getText().trim();
		String password = textFieldPassword.getText().trim();
		
		User user = model.getUser(username);
		System.out.println(user);
		
		if(user != null) {
			if(username.equals("admin")) {
				if(password.equals("admin")) {
					Parent adminLogin = FXMLLoader.load(getClass().getResource("/view/admin.fxml"));
					Scene adminScene = new Scene(adminLogin);
					Stage currentStage = (Stage) (buttonLogin.getScene().getWindow());
					currentStage.hide();
					currentStage.setScene(adminScene);
					currentStage.setTitle("Photos -- Admin Tool");
					currentStage.show();
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Unsuccessful Login");
					alert.setContentText("Invalid password.");
					alert.showAndWait();
					textFieldUsername.setText(username);
					textFieldPassword.setText("");
				}
			} else {
				if(password.equals(user.getPassword())) {
					model.setCurrentUser(user);
					Parent userLogin = FXMLLoader.load(getClass().getResource("/view/user.fxml"));
					Scene userScene = new Scene(userLogin);
					Stage currentStage = (Stage) (buttonLogin.getScene().getWindow());
					currentStage.hide();
					currentStage.setScene(userScene);
					currentStage.setTitle("Photos V1.0 - Welcome " + user.getUsername());
					currentStage.show();
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Unsuccessful Login");
					alert.setContentText("Invalid password.");
					alert.showAndWait();
					textFieldUsername.setText(username);
					textFieldPassword.setText("");
				}
			}
		} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Unsuccessful Login");
				alert.setContentText("Invalid username or password.");
				alert.showAndWait();
				textFieldUsername.setText("");
				textFieldPassword.setText("");
		}
	}
	
	/**
	 * Grabs the model object to pass to different scenes.
	 * @return a model object
	 */
	public static PhotoModel getModel() {
		return model;
	}
	
	/**
	 * Method to exit the program.
	 */
	public static void exit() {
		model.write();
		Platform.exit();
	}

}
