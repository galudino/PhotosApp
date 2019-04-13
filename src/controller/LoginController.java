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
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Album;
import model.PhotoModel;
import model.User;

/**
 * Controller class to manage functionality of login.fxml
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class LoginController {

	public static final String DAT_FILE_PATH = "photos.dat";

	static PhotoModel model;

	//@formatter:off
	@FXML Button buttonLogin;
	@FXML TextField textFieldUsername;
	@FXML TextField textFieldPassword;
	//@formatter:on

	/**
	 * Initialization process to ensure everything is set to null including user
	 * model.
	 */
	@FXML
	public void initialize() {
		model = new PhotoModel();

		textFieldUsername.clear();
		textFieldPassword.clear();

		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
	}

	/**
	 * Login method -- obtains the information from the textfields, and attempts
	 * to map a user object.
	 * 
	 * @throws IOException: will throw an IOException if file is not found
	 *                      (FXML).
	 */
	public void doLogin() throws IOException {
		/**
		 * username and password are to be free of whitespace/delimiters
		 */
		final String username = textFieldUsername.getText().trim();
		final String password = textFieldPassword.getText().trim();

		/**
		 * @see PhotoModel.java User admin = new User("admin", "admin");
		 *      ("admin", "admin") is (username, password)
		 */
		User user = model.getUser(username);

		/**
		 * CONSOLE DIAGNOSTICS
		 */		
		debugLog(username + " and " + password + " entered");

		if (user != null) {
			/**
			 * SCENARIO 1: SUCCESS. User exists.
			 */

			if (user.getUsername().equals("admin")) {
				/**
				 * SCENARIO 1a: SUCCESS. user.username == "admin"
				 */
				if (password.equals("admin")) {
					/**
					 * SCENARIO 1a-1: SUCCESS. user.password == "admin"
					 */
					Parent adminLogin = FXMLLoader
							.load(getClass().getResource("/view/admin.fxml"));

					Scene adminScene = new Scene(adminLogin);
					Stage currentStage = (Stage) (buttonLogin.getScene()
							.getWindow());

					currentStage.hide();
					currentStage.setScene(adminScene);
					currentStage.setTitle("Photos -- Admin Tool");

					currentStage.show();

					/**
					 * CONSOLE DIAGNOSTICS
					 */
					debugLog("Now logged in as administrator with credentials "
							+ user + ".");
				} else {
					/**
					 * SCENARIO 1a-2: FAILURE. user.password != "admin"
					 */
					Alert alert = new Alert(Alert.AlertType.ERROR);

					alert.setTitle("Error");
					alert.setHeaderText("Unsuccessful Login");
					alert.setContentText("Invalid password.");

					/**
					 * CONSOLE DIAGNOSTICS
					 */
					debugLog("Failed to log in as administrator with credentials "
							+ user + ".");

					alert.showAndWait();

					textFieldUsername.setText(username);
					textFieldPassword.setText("");
				}
			} else {
				/**
				 * SCENARIO 1b: NEUTRAL. user.username != "admin"
				 */
				if (user.getPassword().equals(password)) {
					/**
					 * SCENARIO 1b-1: SUCCESS. user.password == password
					 */
					model.setCurrentUser(user);

					ObservableList<Album> aList = FXCollections
							.observableArrayList();

					for (Entry<String, Album> a : user.getAlbumMap()
							.entrySet()) {
						aList.add(a.getValue());
					}

					user.setAlbumList(aList);

					/**
					 * CONSOLE DIAGNOSTICS
					 */
					debugLog("Now logged in as standard user with credentials "
							+ user + ".");

					Parent userLogin = FXMLLoader
							.load(getClass().getResource("/view/user.fxml"));
					Scene userScene = new Scene(userLogin);

					Stage currentStage = (Stage) (buttonLogin.getScene()
							.getWindow());

					currentStage.hide();
					currentStage.setScene(userScene);
					currentStage.setTitle(
							"Photos V1.0 - Welcome " + user.getUsername());

					currentStage.show();
				} else {
					/**
					 * SCENARIO 1b-2: FAILURE. user.password != password
					 */
					Alert alert = new Alert(Alert.AlertType.ERROR);

					alert.setTitle("Error");
					alert.setHeaderText("Unsuccessful Login");
					alert.setContentText("Invalid password.");

					/**
					 * CONSOLE DIAGNOSTICS
					 */
					debugLog("Failed to log in as standard user with credentials "
							+ user + ".");

					alert.showAndWait();

					textFieldUsername.setText(username);
					textFieldPassword.setText("");
				}
			}
		} else {
			/**
			 * SCENARIO 2: User does not exist.
			 */
			Alert alert = new Alert(Alert.AlertType.ERROR);

			alert.setTitle("Error");
			alert.setHeaderText("Unsuccessful Login");
			alert.setContentText("Enter a valid username and try again.");

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Username entered does not exist.");

			alert.showAndWait();

			textFieldUsername.setText("");
			textFieldPassword.setText("");
		}
	}

	/**
	 * Grabs the model object to pass to different scenes.
	 * 
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
