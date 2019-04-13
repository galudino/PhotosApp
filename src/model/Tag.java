/**
 * Tag.java
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

/**
 * Represents a tag for a Photo
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class Tag implements Comparable<Tag>, Serializable {

	private static final long serialVersionUID = 176039707375704571L;
	private String tagName;
	private String tagValue;

	/**
	 * Constructor that tags a tagName and tagValue field and sets it to the
	 * private fields.
	 */
	public Tag(String tagName, String tagValue) {
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	/**
	 * @param takes a Tag object and sets the fields accordingly.
	 */
	public Tag(Tag t) {
		tagName = t.tagName;
		tagValue = t.tagValue;
	}

	/**
	 * @return tagName field
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @return tagValue field
	 */
	public String getTagValue() {
		return tagValue;
	}

	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return makeKey(tagName, tagValue);
	}
	
	/**
	 * 
	 * @param tagName
	 * @param tagValue
	 * 
	 * @return
	 */
	public static String makeKey(String tagName, String tagValue) {
		return (tagName + tagValue).toLowerCase();
	}

	@Override
	public String toString() {
		return tagName + " :: " + tagValue;
	}
	
	/**
	 * @param Takes in a tag object to compare with
	 * @return a value based to see if the tag names are equal, if false it will
	 *         compare tag values.
	 */
	@Override
	public int compareTo(Tag other) {
		return (this.tagName.compareTo(other.tagName) != 0)
				? this.tagName.compareTo(other.tagName)
				: this.tagValue.compareToIgnoreCase(other.tagValue);
	}
}
