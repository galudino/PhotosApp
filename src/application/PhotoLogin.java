/**
 * PhotoLogin.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import model.User;

/**
 * @version Mar 11, 2019
 * @author gemuelealudino
 *
 */
public class PhotoLogin implements ChangeListener<User> {

	@FXML AnchorPane anchorPaneMain;
	@FXML ImageView imageViewBackground;
	@FXML Pane paneUpper;
	
	@FXML Pane paneLower;
	@FXML Text textUsername;
	@FXML Text textPassword;
	@FXML Button buttonLogin;
	@FXML TextField textFieldUsername;
	@FXML TextField textFieldPassword;
	
	/* (non-Javadoc)
	 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void changed(ObservableValue<? extends User> observable, User oldValue,
			User newValue) {
		if (textFieldUsername.getText().length() > 0 && textFieldPassword.getText().length() > 0) {
			// if user types in data in textFieldUsername and textFieldPassword,
			// then enable the login button.
			
			
		}
		
	}

}
