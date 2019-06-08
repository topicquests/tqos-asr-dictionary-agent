/**
 * 
 */
package org.topicquests.os.asr.api;

import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 * Based on IDictionaryModel
 */
public interface IDictionaryClient {

	/**
	 * Returns the entire Dictionary as a JSONObject
	 * @return
	 */
	IResult getDictionary();
	
	/**
	 * <p>This is a multi-purpose function. It has two use cases:
	 * <ul>
	 * <li>The word is a new word, in which case it is added to the 
	 * 	 dictionary</li>
	 * <li>The word already exists</li></ul></p>
	 * <p>In both cases, the wordId is returned.<br/>
	 * <p>A return object looks like:<br/>
	 * {"cargo":"998492","isNewWord":true}<br/>
	 * where <code>isNewWord</code> will reflect whether the word is new or not</p>
	 * 
	 * @param word
	 * @return
	 */
	IResult addWord(String word);
}
