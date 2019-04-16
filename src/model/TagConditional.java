/**
 * TagConditional.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model;



/**
 * @version Apr 16, 2019
 * @author gemuelealudino
 *
 */
public class TagConditional {
	Tag tag1;
	Tag tag2;

	String conditional;

	public TagConditional(Tag tag1, Tag tag2, String conditional) {
		this.tag1 = tag1;
		this.tag2 = tag2;

		switch (conditional) {
		case "AND":
		case "OR":
		case "NOT":
			this.conditional = conditional;
			break;
		default:
			System.exit(0);
			break;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", tag1, conditional, tag2);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof TagConditional) {
			TagConditional tc = (TagConditional)(o);
			
			return tag1.equals(tc.tag2) && conditional.equals(tc.conditional); 
		} else {
			return false;
		}
	}
}
