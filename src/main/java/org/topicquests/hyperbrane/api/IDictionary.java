/*
 * Copyright 2014, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.hyperbrane.api;

import java.io.Writer;

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
	String addWord(String word);
		
	JSONObject getDictionary();
	
}
