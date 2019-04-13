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

import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a photo in a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class Photo {

	private static final int THUMBNAIL_W = 120;
	private static final int THUMBNAIL_H = 120;

	private String fileName;
	private String caption;
	private long datePhoto;

	private ObservableList<Tag> tagList;
	private TreeMap<String, Tag> tagMap;

	/**
	 * 
	 * @param photo
	 */
	public Photo(Photo photo) {
		this.fileName = photo.fileName;
		this.caption = photo.caption;
		this.datePhoto = photo.datePhoto;

		tagList = FXCollections.observableArrayList();

		for (Tag t : photo.tagList) {
			tagList.add(new Tag(t));
		}
	}

	/**
	 * 
	 * @param fileName
	 */
	public Photo(String fileName) {
		this.fileName = fileName;
		caption = "";
		datePhoto = 0;
		tagList = null;
	}

	/**
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 
	 * @return
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * 
	 * @return
	 */
	public long getDatePhoto() {
		return datePhoto;
	}

	/**
	 * 
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 
	 * @param tagName
	 * @param tagValue
	 * @return
	 */
	public int addTag(String tagName, String tagValue) {
		String tagKey = Tag.makeKey(tagName, tagValue);
		Tag temp = tagMap.get(tagKey);
		
		if(temp != null) {
			temp = new Tag(tagName, tagValue);
			tagMap.put(tagKey, temp);
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
	public boolean deleteTag(int index) {
		String key = tagList.get(index).getKey();
		Tag temp = tagMap.get(key);
		
		if(temp != null) {
			tagMap.remove(key);
			tagList.remove(index);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Places the tag in the appropriate spot in the tagList.
	 * @param tag
	 * @return an index for where the Tag will be placed in the list.
	 */
	private int indexInsertedSorted(Tag tag) {
		if (tagList.isEmpty()) {
			tagList.add(tag);
			return 0;
		} else {
			for (int i = 0; i < tagList.size(); i++) {
				if (tag.compareTo(tagList.get(i)) < 0) {
					tagList.add(i, tag);
					return i;
				}
			}
			tagList.add(tag);
			return tagList.size() - 1;
		}
	}

	@Override
	public String toString() {
		return fileName + " (" + datePhoto + "): \"" + caption + "\"";
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Photo) {
			Photo p = (Photo) (o);

			/**
			 * If filenames are equal:
			 * 	Compare dates. If dates are equal:
			 * 		Compare captions. If captions equal:
			 * 			return true;
			 * 	    Else return false.
			 *  Else return false.
			 * Else return false.
			 */
			return fileName.contentEquals(p.fileName) ? ((datePhoto == p.datePhoto) ? ((caption.equals(p.caption)) ? true : false) : true) : true;
		} else {
			return false;
		}

	}
	
	/**
	 * 
	 * @param o
	 * @return
	 */
	public int compareTo(Object o) {
		// TODO compareTo
		
		if (o != null && o instanceof Photo) {
			Photo p = (Photo)(o);
			
			return 0;
		} else {
			return 0;
		}
	}

	// need to create image for thumbnail
	// need to create method to grab date/time from the photo
}
