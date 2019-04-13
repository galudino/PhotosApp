/**
 * AdminController.java
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import model.PhotoModel;
import model.User;

/**
 * Controller class to manage functionality of admin.fxml
 * 
 * @version Mar 11, 2019
 * @author Patrick Nogaj
 */
public class AdminController {

	private PhotoModel model = LoginController.getModel();

	//@formatter:off
	@FXML TextField textFieldUsername;
	@FXML TextField textFieldPassword;
	
	@FXML Button buttonConfirmAdd;
	
	@FXML TableView<User> tableViewUsers;
	@FXML TableColumn<User, String> userColumn;
	@FXML TableColumn<User, String> passwordColumn;
	//@formatter:on

	@FXML
	public void initialize() {
		userColumn.setCellValueFactory(
				new PropertyValueFactory<User, String>("username"));

		passwordColumn.setCellValueFactory(
				new PropertyValueFactory<User, String>("password"));

		tableViewUsers.setItems(model.getUserList());
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
	}

	/**
	 * Executes upon activating buttonConfirmAdd
	 */
	public void doAdd() {
		String username = textFieldUsername.getText();
		String password = textFieldPassword.getText();

		if (username.isEmpty() == false && password.isEmpty() == false) {
			/**
			 * SCENARIO 1: SUCCESS. username and password are provided
			 */

			User newUser = new User(username, password);

			if (model.addUser(username, password) == -1) {
				/**
				 * SCENARIO 1a: FAILURE. username exists in model
				 */
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Duplicate username found. User not added!");

				Alert duplicate = new Alert(Alert.AlertType.ERROR,
						"Duplicate username found. User not added!",
						ButtonType.OK);

				duplicate.showAndWait();
			} else {
				/**
				 * SCENARIO 1b: SUCCESS. username does not exist in model
				 */
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */				
				debugLog("Username " + newUser.getUsername() + " added successfully.");

				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"User successfully added!", ButtonType.OK);
				success.showAndWait();

				/**
				 * Pre-select the newly created user in tableViewUsers
				 */
				int selectedIndex = model.getIndex(newUser);
				tableViewUsers.getSelectionModel().select(selectedIndex);
				
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Selected Index: " + selectedIndex);
			}
		} else {
			/**
			 * SCENARIO 2: FAILURE. username and password are not provided
			 */

			Alert error = new Alert(AlertType.ERROR,
					"Please provide a username and password.", ButtonType.OK);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Please provide a username and password.");
			
			error.showAndWait();
		}
	}

	/**
	 * Executes upon deleting user
	 */
	public void doDelete() {
		int selectedIndex = tableViewUsers.getSelectionModel()
				.getSelectedIndex();

		if (selectedIndex < 0) {
			/**
			 * No users to delete in model
			 */
			Alert error = new Alert(Alert.AlertType.ERROR,
					"There are no users to be deleted.", ButtonType.OK);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("There are no users to be deleted.");

			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
				"Are you sure you want to delete this user?", ButtonType.YES,
				ButtonType.NO);
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		debugLog("Prompt: Are you sure you want to delete the selected user?");
		
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}

		/**
		 * Users available to delete in model
		 */
		if (selectedIndex >= 0) {
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Selected Index: " + selectedIndex);
			
			model.deleteUser(selectedIndex);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Selected user deleted.");
			
			if (selectedIndex <= model.getItemCount() - 1) {
				tableViewUsers.getSelectionModel().select(selectedIndex);
			}
		}
	}

	/**
	 * Executes upon visiting about option
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
		window.setTitle("Photos V1.0 -- About");
		window.setResizable(false);
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		debugLog("Entering about section.");

		window.show();
	}

	/**
	 * Executes upon logging out
	 * 
	 * @throws IOException if login.fxml not found
	 */
	public void doLogOff() throws IOException {
		model.write();

		Parent login = FXMLLoader
				.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);

		Stage currentStage = (Stage) (tableViewUsers.getScene().getWindow());

		currentStage.hide();
		currentStage.setScene(loginScene);
		currentStage.setTitle("Photos -- V1.0");
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		debugLog("Now logging out of adminstrator mode.");

		currentStage.show();
	}

	/**
	 * Executes upon termination
	 */
	public void doQuit() {
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		debugLog("Now quitting application.");
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
