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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

	private static final long serialVersionUID = -2759544425727486297L;

	private String filepath;
	private String caption;
	private long datePhoto;
	private long fileSize;
	
	private File imageFile;

	private transient ObservableList<Tag> tagList;
	private TreeMap<String, Tag> tagMap;

	/**
	 * Constructor that takes a String as the filePath.
	 * @param fileName
	 */
	public Photo(String filepath) {
		this.filepath = filepath;
		caption = "";
		datePhoto = new File(filepath).lastModified();
		fileSize = 0;
		tagList = FXCollections.observableArrayList();
		tagMap = new TreeMap<String, Tag>();
	}
	
	/**
	 * Overloaded constructor which takes a Photo object.
	 * @param photo : Photo object to copy.
	 */
	public Photo(Photo photo) {
		this.imageFile = photo.imageFile;
		this.filepath = photo.filepath;
		this.caption = photo.caption;
		this.datePhoto = photo.datePhoto;
		this.fileSize = photo.fileSize;

		tagMap = new TreeMap<String, Tag>();
		tagList = FXCollections.observableArrayList();
	}
	
	/**
	 * Overloaded constructor which takes a File object and extracts data.
	 * @param imageFile: A file object utilized to store data.
	 */
	public Photo(File imageFile) {
		this.imageFile = imageFile;
		filepath = imageFile.getAbsolutePath();
		caption = "";
		datePhoto = imageFile.lastModified();
		fileSize = 0;
		tagList = FXCollections.observableArrayList();
		tagMap = new TreeMap<String, Tag>();
	}

	/**
	 * Adds a tag to the TreeMap<String, Tag> based on index.
	 * @param tagName : String object containing the value of the tag name.
	 * @param tagValue : String object containing the value of the tag value.
	 * @return index of where it was placed in the TreeMap<String, Tag> or -1 if not added.
	 */
	public int addTag(String tagName, String tagValue) {
		String tagKey = Tag.makeKey(tagName, tagValue);
		Tag temp = tagMap.get(tagKey);
		
		System.out.println(temp);
		
		if(temp == null) {
			temp = new Tag(tagName, tagValue);
			tagMap.put(tagKey, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
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

	/**
	 * Removes a Tag from the TreeMap<String, Tag> and the ObservableList<Tag> based on index.
	 * @param index : index of where the Tag is in the list which is derived from UserController.
	 * @return true | false based on if the Tag was deleted.
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
	 * 
	 * @param time
	 * @return
	 */
    public String epochToLocalTime(long time) {
        LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return datetime.format(formatter);
    }
    
    public LocalDateTime getLocalDateTime() {
    	long time = this.datePhoto;
    	LocalDateTime thisDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    	return thisDateTime;
    }
    
    public boolean isInDateRange(LocalDate from, LocalDate to) {
    	long time = this.datePhoto;
        LocalDateTime thisDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        
        int thisMonthValue = thisDateTime.getMonthValue();
        int thisDayValue = thisDateTime.getDayOfMonth();
        int thisYearValue = thisDateTime.getYear();
        
        int fromMonthValue = from.getMonthValue();
        int fromDayValue = from.getDayOfMonth();
        int fromYearValue = from.getYear();
        
        int toMonthValue = to.getMonthValue();
        int toDayValue = to.getDayOfMonth();
        int toYearValue = to.getYear();
        

        if (thisYearValue >= fromYearValue && thisYearValue <= toYearValue) {
            if (thisMonthValue >= fromMonthValue && thisMonthValue <= toMonthValue) {            	
            	System.out.println(thisYearValue + "::" + thisMonthValue + "::" + thisDayValue);
            	
            	if (thisDayValue >= fromDayValue && thisDayValue <= toDayValue) {
            		return true;
            	} else {
            		return false;
            	}
            } else {
            	return false;
            }
        } else {
        	return false;
        }
    }
    
    public boolean matchesTagConditional(TagConditional tc) {
    	if (tc.isAnd()) {
    		return evaluateAnd(tc);
    	} else if (tc.isOr()) {
    		return evaluateOr(tc);
    	} else if (tc.isNot()) {
    		return evaluateNot(tc);
    	} else {
    		System.out.println("TagConditional does not match AND, OR, or NOT");
    		return false;
    	}
    }
    
    private boolean evaluateAnd(TagConditional tc) {
    	// check to see if tc.tag1 and tc.tag2 are in this photo.
    	boolean foundTag1 = false;
    	boolean foundTag2 = false;
    	
    	for (Tag tag : tagMap.values()) {
    		if (tc.getTag1().equals(tag)) {
    			foundTag1 = true;
    		}
    		
    		if (tc.getTag2().equals(tag)) {
    			foundTag2 = true;
    		}
    		
    		if (foundTag1 && foundTag2) {
    			break;
    		}
    	}
    	
    	return foundTag1 && foundTag2;
    }
    
    private boolean evaluateOr(TagConditional tc) {
    	// check to see if tc.tag1 OR tc.tag2 is in this photo
    	boolean foundTag1 = false;
    	boolean foundTag2 = false;
    	
    	for (Tag tag : tagMap.values()) {
    		if (tc.getTag1().equals(tag)) {
    			foundTag1 = true;
    			break;
    		}
    		
    		if (tc.getTag2().equals(tag)) {
    			foundTag2 = true;
    			break;
    		}
    	}
    	
    	return foundTag1 || foundTag2;
    }
    
    private boolean evaluateNot(TagConditional tc) {
    	// check to see if tc.tag1 and tc.tag2 or NOT in this photo
    	boolean foundTag1 = false;
    	boolean foundTag2 = false;
    	
    	for (Tag tag : tagMap.values()) {
    		if (tc.getTag1().equals(tag)) {
    			foundTag1 = true;
    		}
    		
    		if (tc.getTag2().equals(tag)) {
    			foundTag2 = true;
    			break;
    		}
    	}
    	
    	return foundTag1 && !foundTag2;
    }
    

	public static String makeKey(String fileName) {
		return fileName.toLowerCase();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getFilepath() {
		return filepath;
	}
	
	public TreeMap<String, Tag> getTagMap() {
		return tagMap;
	}
	
	public ObservableList<Tag> getTagList() {
		return tagList;
	}
	
	/**
	 * 
	 * @return
	 */
	public File getImageFile() {
		return imageFile;
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
	public String getDatePhoto() {
		return epochToLocalTime(datePhoto);
	}
	
	public long getFileSize() { 
		return fileSize;
	}
	
	public String getKey() {
		return makeKey(filepath);
	}
	
	public long getModifiedDate() {
        return datePhoto;
    }

	/**
	 * 
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public void setTagList(ObservableList<Tag> tagList) {
		this.tagList = tagList;
	}
	
	public void setDataPhoto(long time) {
		datePhoto = time;
	}
	
	public void setSizePhoto(long size) {
		fileSize = (size / 1024);
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
	

	@Override
	public String toString() {
		return filepath + " (" + epochToLocalTime(datePhoto) + "): \"" + caption + "\"";
	}


	
}
