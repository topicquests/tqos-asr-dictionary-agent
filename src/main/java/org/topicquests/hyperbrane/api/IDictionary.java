/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.hyperbrane.api;


import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author park
 * Sentences removed from dictionary
 */
public interface IDictionary {
			
	/**
	 * Can return null if this dictionary does not have the word
	 * @param id
	 * @return
	 */
	String getWord(String id);
		
	/**
	 * Can return <code>null</code> if word doesn't exist
	 * @param word
	 * @return
	 */
	String getWordId(String word);
	
	/**
	 * Quick test; returns <code>true</code> if nothing in the dictionary
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * If word does not exist, it will be added with a new Id;
	 * Otherwise, the word's existing Id will be returned
	 * @param word
	 * @return
	 */
	IResult addWord(String word);
		
	JSONObject getDictionary();
	
}
