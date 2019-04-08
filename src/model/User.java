/**
 * User.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User implements Comparable<User>, Serializable {

	private static final long serialVersionUID = 8689422424799836920L;
	
	private String username;
	private String password;
	
	private transient ObservableList<Album> albumList;
	
	private ArrayList<Album> theAlbumList;
	private Album currentAlbum;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		albumList = FXCollections.observableArrayList();
		theAlbumList = null;
		currentAlbum = null;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String toString() {
		return username + " " + password;
	}
	
	public ObservableList<Album> getAlbumList() {
		return albumList;
	}
	
	public Album getCurrentAlbum() {
		return currentAlbum;
	}

	@Override
	public int compareTo(User user) {
		return this.username.compareToIgnoreCase(user.username);
	}
	
	public Album addAlbum(String username) {
		Album album = new Album(username);
		
		boolean isFound = IntStream.range(0, albumList.size()).anyMatch(i -> albumList.get(i).getAlbumName().equalsIgnoreCase(album.getAlbumName()));
	
		if(!isFound) {
			albumList.add(album);
			return album;
		} else {
			return null;
		}
	}
	
	public boolean equals(Object o) {
		if(o == null || !(o instanceof User)) {
			return false;
		}
		
		User other = (User) o;
		return this.username.equalsIgnoreCase(other.username);
	}
	
	public static String makeKey(String username) {
		return username.toLowerCase();
	}
	
	public String getKey() {
		return makeKey(username);
	}
	
}
