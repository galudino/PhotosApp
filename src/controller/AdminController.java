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
import model.PhotoModel;
import model.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @version Mar 11, 2019
 * @author gemuelealudino
 *
 */
public class AdminController {
	
	private PhotoModel model = LoginController.getModel();
	
	@FXML TextField textFieldUsername;
	@FXML TextField textFieldPassword;
	
	@FXML Button buttonConfirmAdd;
	
	@FXML TableView<User> tableViewUsers;
	@FXML TableColumn<User, String> userColumn;
	@FXML TableColumn<User, String> passwordColumn;
	
	@FXML
	public void initialize() {
		userColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
		tableViewUsers.setItems(model.getUserList());
	}
	
	public void doAdd() {
		if(textFieldUsername.getText().isEmpty() == false && textFieldPassword.getText().isEmpty() == false) {
			User newUser = new User(textFieldUsername.getText().trim(), textFieldPassword.getText().trim());
			
			if(model.addUser(textFieldUsername.getText().trim(), textFieldPassword.getText().trim()) == - 1) {
				Alert duplicate = new Alert(Alert.AlertType.ERROR, "Duplicate username found. User not added!", ButtonType.OK);
				duplicate.showAndWait();
			} else {
				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "User successfully added!", ButtonType.OK);
				success.showAndWait();
				
				System.out.println("Username: " + newUser.getUsername() + " added successfully.");
				
				int selectedIndex = model.getIndex(newUser);
				
				System.out.println("selectedIndex");
				tableViewUsers.getSelectionModel().select(selectedIndex);
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please provide a username, and password.", ButtonType.OK);
			error.showAndWait();
		}
	}
	
	public void doDelete() {
		int selectedIndex = tableViewUsers.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR, "There are no users to be deleted.", ButtonType.OK);
			error.showAndWait();
			return;
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() != ButtonType.YES)
			return;
		
		if(selectedIndex >= 0) {
			System.out.println("Selected Index: " + selectedIndex);
			model.deleteUser(selectedIndex);
			
			if(selectedIndex <= model.getItemCount() - 1) {
				tableViewUsers.getSelectionModel().select(selectedIndex);
			}
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
		window.setTitle("Photos V1.0 -- About");
		window.setResizable(false);
		window.show();
	}

	public void doLogOff() throws IOException {
		model.write();
		Parent login = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);
		Stage currentStage = (Stage) (tableViewUsers.getScene().getWindow());
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
