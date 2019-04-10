/**
 * PhotoModel.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PhotoModel implements Serializable {

	private static final long serialVersionUID = -450152308719125499L;

	public final static String DAT_FILE_PATH = "photos.dat";
	
	private ObservableList<User> userList;
	private TreeMap<String, User> userMap;
	private User currentUser; 
	
	public PhotoModel() {
		userList = read();
		userMap = new TreeMap<String, User>();
		currentUser = null;
		
		for(User u : userList) {
			userMap.put(u.getKey(), u);
		}
	}
	
	public ObservableList<User> getUserList() {
		return userList;
	}
	
	public TreeMap<String, User> getUserMap() {
		return userMap;
	}
	
	public int getItemCount() {
		return userList.size();
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public int getIndex(User user) {
		return userList.indexOf(user);
	}
	
	public void setUserList(ObservableList<User> userList) {
		this.userList = userList;
	}
	
	public int addUser(String username, String password) {
		String key = User.makeKey(username);
		User temp = userMap.get(key);
		
		if(temp == null) {
			temp = new User(username, password);
			userMap.put(key, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}
	
	private int indexInsertedSorted(User user) {
		if(userList.isEmpty()) {
			userList.add(user);
			return 0;
		} else {
			for(int i = 0; i < userList.size(); i++) {
				if(user.compareTo(userList.get(i)) < 0) {
					userList.add(i, user);
					return i;
				}
			}
			userList.add(user);
			return userList.size() - 1;
		}
	}
	
	public boolean deleteUser(int index) {
		String key = userList.get(index).getKey();
		User temp = userMap.get(key);
		
		if(temp != null) {
			userMap.remove(key);
			userList.remove(index);
			return true;
		} else {
			return false;
		}
	}
	
	public User getUser(String username) {
		return userList.stream().filter(user->user.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
	}
	
	/**
	 * Reads the file from DAT_FILE_PATH to deserialize and load data.
	 * @return an observable list that has all the old data, or a new list with admin/stock preloaded.
	 */
	private ObservableList<User> read() {
		try {
			FileInputStream fIn = new FileInputStream(DAT_FILE_PATH);
			ObjectInputStream in = new ObjectInputStream(fIn);
			List<User> readList = (List<User>) in.readObject();
			System.out.println("User list from file: " + readList);
			return FXCollections.observableArrayList(readList);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(IOException i) {
			i.printStackTrace();
		}		
		User admin = new User("admin", "admin");
		User stock = new User("stock", "stock");
		stock.addAlbum("test");
		stock.addAlbum("test2");
		
		return FXCollections.observableArrayList(admin, stock);
	}
	
	/**
	 * Creates a serialized file with an ArrayList of all User objects. [ObservableList is not serializable]
	 */
	public void write() {	
		try {	
			FileOutputStream fOut = new FileOutputStream(DAT_FILE_PATH);
			ObjectOutputStream out = new ObjectOutputStream(fOut);
			out.writeObject(new ArrayList<User>(userList));
			out.close();
			fOut.close();
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	
}
