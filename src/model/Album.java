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

public class Album implements Comparable<Album>, Serializable {

	private static final long serialVersionUID = -6862414900620876387L;
	private String albumName;
	
	private int albumSize;
	
	private ArrayList<Photo> photoList;
	
	public Album(String albumName) {
		this.albumName = albumName;
		albumSize = 0;
		photoList = new ArrayList<>();
	}
	
	public void addPhoto(Photo photo) {
		photoList.add(photo);
	}
	
	@Override
	public int compareTo(Album arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getAlbumName() {
		return albumName;
	}
	
	public int getAlbumSize() { 
		return albumSize;
	}
	
	public String toString() {
		return albumName + " " + photoList.size();
	}
	
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	public static String makeKey(String albumName) {
		return albumName.toLowerCase();
	}
	
	public String getKey() {
		return makeKey(albumName);
	}

}
