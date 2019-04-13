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

import java.io.Serializable;
import java.util.ArrayList;

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
	private ArrayList<Photo> photoList;

	/**
	 * 
	 * @param albumName
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		albumSize = 0;
		photoList = new ArrayList<>();
	}

	/**
	 * 
	 * @param photo
	 */
	public void addPhoto(Photo photo) {
		photoList.add(photo);
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
	public ArrayList<Photo> getPhotoList() {
		return photoList;
	}

	@Override
	public String toString() {
		return albumName + " " + photoList.size();
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
