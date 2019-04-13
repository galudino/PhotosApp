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
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents an end-user for a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class User implements Comparable<User>, Serializable {

	private static final long serialVersionUID = 8689422424799836920L;

	private String username;
	private String password;

	private transient ObservableList<Album> albumList;

	private TreeMap<String, Album> albumMap;

	private Album currentAlbum;

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		
		albumList = FXCollections.observableArrayList();
		albumMap = new TreeMap<String, Album>();
		
		currentAlbum = null;
	}


	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return makeKey(username);
	}

	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @return
	 */
	public ObservableList<Album> getAlbumList() {
		return albumList;
	}

	/**
	 * 
	 * @return
	 */
	public Album getCurrentAlbum() {
		return currentAlbum;
	}

	/**
	 * 
	 * @return
	 */
	public TreeMap<String, Album> getAlbumMap() {
		return albumMap;
	}

	/**
	 * 
	 * @param albumList
	 */
	public void setAlbumList(ObservableList<Album> albumList) {
		this.albumList = albumList;
	}

	/**
	 * 
	 * @param currentAlbum
	 */
	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}

	/**
	 * 
	 * @param albumName
	 * @return
	 */
	public int addAlbum(String albumName) {
		String albumKey = Album.makeKey(albumName);
		Album temp = albumMap.get(albumKey);

		if (temp == null) {
			temp = new Album(albumName);
			albumMap.put(albumKey, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public boolean deleteAlbum(int index) {
		String key = albumList.get(index).getKey();
		Album temp = albumMap.get(key);

		if (temp != null) {
			albumMap.remove(key);
			albumList.remove(index);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param album
	 * @return
	 */
	private int indexInsertedSorted(Album album) {
		if (albumList.isEmpty()) {
			albumList.add(album);
			return 0;
		} else {
			for (int i = 0; i < albumList.size(); i++) {
				if (album.compareTo(albumList.get(i)) < 0) {
					albumList.add(i, album);
					return i;
				}
			}
			albumList.add(album);
			return albumList.size() - 1;
		}
	}

	/**
	 * 
	 * @param index
	 * @param albumName
	 * @return
	 */
	public int edit(int index, String albumName) {
		String oldKey = albumList.get(index).getKey();
		String newKey = Album.makeKey(albumName);

		Album oldAlbum = albumMap.get(oldKey);

		if (oldAlbum == null || oldKey.equals(newKey)) {
			return -1;
		} else {
			Album newAlbum = albumMap.get(newKey);

			if (newAlbum == null) {
				oldAlbum.setAlbumName(albumName);

				albumMap.remove(oldKey);
				albumMap.put(newKey, oldAlbum);

				albumList.remove(index);
				return indexInsertedSorted(oldAlbum);
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public static String makeKey(String username) {
		return username.toLowerCase();
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof User)) {
			return false;
		}

		User other = (User) o;
		return this.username.equalsIgnoreCase(other.username);
	}
	
	@Override
	public int compareTo(User user) {
		return this.username.compareToIgnoreCase(user.username);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "(username) " + username + " " + "(password) " + password;
	}

}
