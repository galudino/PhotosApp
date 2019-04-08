/**
 * Photo.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Photo {
	
	private static final int THUMBNAIL_W = 120;
	private static final int THUMBNAIL_H = 120;
	
	private String fileName;
	private String caption;
	private long datePhoto;
	
	private ObservableList<Tag> tagList;

	public Photo(Photo photo) {
		this.fileName = photo.fileName;
		this.caption = photo.caption;
		this.datePhoto = photo.datePhoto;
		tagList = FXCollections.observableArrayList();
		for(Tag t : photo.tagList) {
			tagList.add(new Tag(t));
		}
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getCaption() { 
		return caption;
	}
	
	public long getDatePhoto() { 
		return datePhoto;
	}
	
	//need to create image for thumbnail
	//need to create method to grab date/time from the photo
}
