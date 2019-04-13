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

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a photo in a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class Photo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2759544425727486297L;
	private static final int THUMBNAIL_W = 120;
	private static final int THUMBNAIL_H = 120;

	private String filepath;
	private String caption;
	private long datePhoto;

	private ObservableList<Tag> tagList;
	private TreeMap<String, Tag> tagMap;

	/**
	 * 
	 * @param photo
	 */
	public Photo(Photo photo) {
		this.filepath = photo.filepath;
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
		this.filepath = fileName;
		caption = "";
		datePhoto = 0;
		tagList = null;
	}

	/**
	 * 
	 * @return
	 */
	public String getFileName() {
		return filepath;
	}
	
	/**
	 * Accessor to compute and return the target filename within filepath
	 * 
	 * @author Gemuele Aludino
	 * @return substring of filepath, resultant: filename of photo
	 */
	public String getFilename() {
		return filepath.substring(filepath.lastIndexOf(File.separator) + 1);
	}
	
	/**
	 * Accessor to compute and return the target filename within filepath
	 * with no extension
	 * 
	 * @author Gemuele Aludino
	 * @return substring of filepath, resultant: filename of photo, no extension
	 */
	public String getFilenameNoExtension() {
		String filename = getFilename();
		return filename.substring(0, filename.lastIndexOf("."));
	}
	
	/**
	 * Accessor to compute and return the target filename's extension
	 * 
	 * @author Gemuele Aludino
	 * @return substring of filepath, resultant: extension of filename
	 */
	public String getFilenameExtension() {
		String filename = getFilename();
		return filename.substring(filename.lastIndexOf("."));
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
	
	public static String makeKey(String fileName) {
		return fileName.toLowerCase();
	}
	
	public String getKey(String fileName) {
		return makeKey(fileName);
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
		return filepath + " (" + datePhoto + "): \"" + caption + "\"";
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
			return filepath.contentEquals(p.filepath) ? ((datePhoto == p.datePhoto) ? ((caption.equals(p.caption)) ? true : false) : true) : true;
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
