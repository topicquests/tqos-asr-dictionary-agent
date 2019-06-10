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
import net.minidev.json.parser.JSONParser;

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
public class ClientDictionary  implements IDictionary {
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
	public ClientDictionary(IDictionaryEnvironment env) {
		environment = env;
		dictionaryClient = environment.getDictionaryClient();
		statisticsClient = environment.getStatisticsClient();
		bootDictionary();
	}
	
	void bootDictionary() {
		IResult r = dictionaryClient.getDictionary();
		environment.logDebug("ClientDictionary.boot "+r.getErrorString()+" | "+(r.getResultObject() != null));
		try {
			JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			String json = (String)r.getResultObject();
			dictionary = (JSONObject)p.parse(json);
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDictionary#getWord(java.lang.String)
	 */
	@Override
	public String getWord(String id) {
		if (id.equals("0"))
			return "\"";
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
		if (word.equals("\""))
			return "0";
		synchronized(dictionary) {
			JSONObject ids = getIDs();
			String lc = word.toLowerCase();
			return ids.getAsString(lc);
		}
	}

	/////////////////////////////////////////////
	// ClientDictionary should not be engaged in statistics, just its own.
	/////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see org.topicquests.concordance.api.IDictionary#addWord(java.lang.String, java.lang.String)
	 */
	@Override
	public String addWord(String theWord) {
		statisticsClient.addToKey(IASRFields.WORDS_READ);
		environment.logDebug("Dictionary.addWord "+theWord);
		if (theWord.equals("\""))
			return "0"; // default id for a quote character
		//Will get the word even if lower case
		String id = getWordId(theWord);
		environment.logDebug("Dictionary.addWord-1 "+id);
		if (id == null) {
			statisticsClient.addToKey(IASRFields.WORDS_NEW);
			IResult r = dictionaryClient.addWord(theWord);
			environment.logDebug("Dictionary.addWord-2 "+r.getErrorString()+" | "+r.getResultObject());
			JSONObject jo = null;
			String json = (String)r.getResultObject();
			try {
				JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
				jo = (JSONObject)p.parse(json);
			} catch (Exception e) {
				environment.logError(e.getMessage(), e);
				e.printStackTrace();
			}
			id = jo.getAsString("cargo");
			synchronized(dictionary) {
				getWords().put(id, theWord);
				getIDs().put(theWord.toLowerCase(), id);
			}
		}
		environment.logDebug("Dictionary.addWord-3 "+id);
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
