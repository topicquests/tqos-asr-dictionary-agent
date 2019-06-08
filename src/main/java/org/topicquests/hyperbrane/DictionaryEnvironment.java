/**
 * 
 */
package org.topicquests.hyperbrane;

import org.topicquests.hyperbrane.api.IDictionary;
import org.topicquests.os.asr.DictionaryHttpClient;
import org.topicquests.os.asr.StatisticsHttpClient;
import org.topicquests.os.asr.api.IDictionaryClient;
import org.topicquests.os.asr.api.IDictionaryEnvironment;
import org.topicquests.os.asr.api.IStatisticsClient;
import org.topicquests.support.RootEnvironment;

/**
 * @author jackpark
 *
 */
public class DictionaryEnvironment extends RootEnvironment
		implements IDictionaryEnvironment {
	private IDictionaryClient dictionaryClient;
	private IStatisticsClient statisticsClient;
	private IDictionary dictionary;

	/**
	 * @param configPath
	 * @param logConfigPath
	 */
	public DictionaryEnvironment(String configPath, String logConfigPath) {
		super(configPath, logConfigPath);
	}
	
	public String getStringProperty(String key) {
		return (String)getProperties().get(key);
	}

	@Override
	public IStatisticsClient getStatisticsClient() {
		return statisticsClient;
	}

	@Override
	public IDictionaryClient getDictionaryClient() {
		return dictionaryClient;
	}

	@Override
	public void setStatisticsClient(IStatisticsClient client) {
		statisticsClient = client;
	}

	@Override
	public void setDictionaryClient(IDictionaryClient client) {
		dictionaryClient = client;
	}

	@Override
	public IDictionary getDictionary() {
		return dictionary;
	}

	@Override
	public void initializeDictionary() {
		dictionary = new ConcordanceDictionary(this);
	}

	@Override
	public void createStatisticsClient() {
		statisticsClient = new StatisticsHttpClient(this);
	}

	@Override
	public void createDictionaryClient() {
		dictionaryClient = new DictionaryHttpClient(this);
	}

}
