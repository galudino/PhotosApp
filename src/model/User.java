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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User implements Comparable<User>, Serializable {

	private static final long serialVersionUID = 8689422424799836920L;
	
	private String username;
	private String password;
	
	private transient ObservableList<Album> albumList;
	private TreeMap<String, Album> albumMap;
	private Album currentAlbum;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		albumList = FXCollections.observableArrayList();
		albumMap = new TreeMap<String, Album>();
		currentAlbum = null;
	}
	
	public static String makeKey(String username) {
		return username.toLowerCase();
	}
	
	public String getKey() {
		return makeKey(username);
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
	
	public TreeMap<String, Album> getAlbumMap() { 
		return albumMap;
	}
	
	public void setAlbumList(ObservableList<Album> albumList) {
		this.albumList = albumList;
	}
	
	@Override
	public int compareTo(User user) {
		return this.username.compareToIgnoreCase(user.username);
	}
	
	public int addAlbum(String albumName) {
		String albumKey = Album.makeKey(albumName);
		Album temp = albumMap.get(albumKey);
		
		if(temp == null) {
			temp = new Album(albumName);
			albumMap.put(albumKey, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}
	
	private int indexInsertedSorted(Album album) {
		if(albumList.isEmpty()) {
			albumList.add(album);
			return 0;
		} else {
			for(int i = 0; i < albumList.size(); i++) {
				if(album.compareTo(albumList.get(i)) < 0) {
					albumList.add(i, album);
					return i;
				}
			}
			albumList.add(album);
			return albumList.size() - 1;
		}
	}
	
	public boolean deleteAlbum(int index) {
		String key = albumList.get(index).getKey();
		Album temp = albumMap.get(key);
		
		if(temp != null) {
			albumMap.remove(key);
			albumList.remove(index);
			return true;
		} else {
			return false;
		}
	}
	
	public ObservableList<Album> load() {
		List<Album> albumLoad = new ArrayList<Album>();
		for(Map.Entry<String, Album> a : albumMap.entrySet()) {
			albumLoad.add(a.getValue());
		}
		return FXCollections.observableArrayList(albumLoad);
	}
	
	public boolean equals(Object o) {
		if(o == null || !(o instanceof User)) {
			return false;
		}
		
		User other = (User) o;
		return this.username.equalsIgnoreCase(other.username);
	}
	
}
