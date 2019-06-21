/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.api;

import org.topicquests.hyperbrane.api.IDictionary;
import org.topicquests.os.asr.DictionaryHttpClient;
import org.topicquests.support.api.IEnvironment;

/**
 * @author jackpark
 *
 */
public interface IDictionaryEnvironment extends IEnvironment {
	IStatisticsClient getStatisticsClient();

	IDictionaryClient getDictionaryClient();

	String getStringProperty(String key);
	
	/**
	 * This environment options using agent providing this client
	 * @param client
	 */
	void setStatisticsClient(IStatisticsClient client);
	
	/**
	 * Use this if the using agent does not supply this client
	 */
	void createStatisticsClient();
	
	/**
	 * This environment options using agent providing this client
	 * @param client
	 */
	void setDictionaryClient(IDictionaryClient client);
	
	/**
	 * Use this if the using agent does not supply this client
	 */
	void createDictionaryClient();
	
	/**
	 * Call this after both statisticsClient and dictionaryClient are present
	 */
	void initializeDictionary();
	
	IDictionary getDictionary();
}
