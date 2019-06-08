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
package org.topicquests.hyperbrane;


import net.minidev.json.*;
import org.topicquests.hyperbrane.api.IDictionary;
import org.topicquests.os.asr.api.IDictionaryClient;
import org.topicquests.os.asr.api.IDictionaryEnvironment;
import org.topicquests.os.asr.api.IStatisticsClient;
import org.topicquests.os.asr.common.api.IASRFields;
import org.topicquests.support.api.IResult;

/**
 * @author park
 * <p>This implements a dictionary partitioned into these sections:
 * <li>words -- id/word pairs</li>
 * <li>ids -- word/id pairs</li>
 * <li>sentences -- id/sentenceList pairs</li></p>
 */
public class ConcordanceDictionary  implements IDictionary {
	private IDictionaryEnvironment environment;
	private IDictionaryClient dictionaryClient;
	private IStatisticsClient statisticsClient;
	private JSONObject dictionary;
	static final String 
		WORDS 		= "words",
		//an index of words, returning their id values
		IDS			= "ids";
		
	
	/**
	 * 
	 */
	public ConcordanceDictionary(IDictionaryEnvironment env) {
		environment = env;
		dictionaryClient = environment.getDictionaryClient();
		statisticsClient = environment.getStatisticsClient();
		bootDictionary();
	}
	
	void bootDictionary() {
		IResult r = dictionaryClient.getDictionary();
		dictionary = (JSONObject)r.getResultObject();
	}
	
	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDictionary#getWord(java.lang.String)
	 */
	@Override
	public String getWord(String id) {
		synchronized(dictionary) {
			JSONObject words = getWords();
			return (String)words.get(id);
		}

	}
	
	JSONObject getWords() {
		return (JSONObject)dictionary.get(WORDS);
	}
	
	JSONObject getIDs() {
		return (JSONObject)dictionary.get(IDS);
	}
	

	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDictionary#getId(java.lang.String)
	 */
	@Override
	public String getWordId(String word) {
		synchronized(dictionary) {
			JSONObject ids = getIDs();
			String lc = word.toLowerCase();
			return ids.getAsString(lc);
		}
	}

	/////////////////////////////////////////////
	// ConcordanceDictionary should not be engaged in statistics, just its own.
	/////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDictionary#addWord(java.lang.String, java.lang.String)
	 */
	@Override
	public String addWord(String theWord) {
		//Will get the word even if lower case
		String id = getWordId(theWord);
		statisticsClient.addToKey(IASRFields.WORDS_READ);
		if (id == null) {
			IResult r = dictionaryClient.addWord(theWord);
			JSONObject jo = (JSONObject)r.getResultObject();
			id = jo.getAsString("word");
			synchronized(dictionary) {
				getWords().put(id, theWord);
				getIDs().put(theWord.toLowerCase(), id);
			}
		}
		return id;
	}


	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDictionary#getDictionary(java.lang.String)
	 */
	@Override
	public JSONObject getDictionary() {
		return dictionary;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDictionary#isEmpty(java.lang.String)
	 */
	@Override
	public boolean isEmpty() {
		synchronized(dictionary) {
			JSONObject obj = this.getWords();
			if (obj == null)
				return false;
			return obj.isEmpty();
		}
	}

}
