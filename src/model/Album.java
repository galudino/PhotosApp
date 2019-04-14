/**
 * Album.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents an album for a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class Album implements Comparable<Album>, Serializable {

	private static final long serialVersionUID = -6862414900620876387L;

	private int albumSize;

	private String albumName;
	
	private transient ObservableList<Photo> photoList;
	private TreeMap<String, Photo> photoMap;

	/**
	 * 
	 * @param albumName
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		photoList = FXCollections.observableArrayList();
		photoMap = new TreeMap<String, Photo>();
		albumSize = 0;
	}

	/**
	 * 
	 * @param photo
	 */
	public int addPhoto(Photo photo) {
		String photoKey = Photo.makeKey(photo.getFilepath());
		Photo temp = photoMap.get(photoKey);
		
		if(temp == null) {
			temp = new Photo(photo.getFilepath());
			photoMap.put(photoKey, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}
	
	private int indexInsertedSorted(Photo photo) {
		if (photoList.isEmpty()) {
			photoList.add(photo);
			return 0;
		} else {
			for (int i = 0; i < photoList.size(); i++) {
				if (photo.compareTo(photoList.get(i)) < 0) {
					photoList.add(i, photo);
					return i;
				}
			}
			photoList.add(photo);
			return photoList.size() - 1;
		}
	}

	@Override
	public int compareTo(Album other) {
		return this.albumName.compareToIgnoreCase(other.albumName);
	}

	/**
	 * 
	 * @return
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * 
	 * @return
	 */
	public int getAlbumSize() {
		return albumSize;
	}

	/**
	 * 
	 * @return
	 */
	public ObservableList<Photo> getPhotoList() {
		return photoList;
	}
	
	public TreeMap<String, Photo> getPhotoMap() {
		return photoMap;
	}
	
	public void setPhotoList(ObservableList<Photo> photoList) {
		this.photoList = photoList;
	}

	@Override
	public String toString() {
		return albumName;
	}

	/**
	 * 
	 * @param albumName
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * 
	 * @param albumName
	 * @return
	 */
	public static String makeKey(String albumName) {
		return albumName.toLowerCase();
	}

	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return makeKey(albumName);
	}

}
