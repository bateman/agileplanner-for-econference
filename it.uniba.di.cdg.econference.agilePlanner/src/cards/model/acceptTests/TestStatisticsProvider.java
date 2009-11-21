package cards.model.acceptTests;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cards.model.StoryCardModel;




public abstract class TestStatisticsProvider {
	private static final Pattern repositoryTypeRegex = Pattern.compile("(.+?)://");
	
	protected int numRuns = 0;
	protected int numPass = 0;
	protected int numFail = 0;
	protected int numExceptions = 0;
	protected String wikiText = "";
	
	private static HashMap<String, Class> providers = new HashMap<String, Class>();
	
	static{
		providers.put("fitnesse", FitnesseTestStatisticsProvider.class);
		
	}
	
	protected abstract void loadTest(StoryCardModel story);

	public int getNumExceptions() {
		return numExceptions;
	}

	public int getNumFail() {
		return numFail;
	}

	public int getNumPass() {
		return numPass;
	}

	public int getNumRuns() {
		return numRuns;
	}

	public String getWikiText() {
		return wikiText;
	}

	public void setWikiText(String wikiText) {
		this.wikiText = wikiText;
	}

	public static TestStatisticsProvider getTestStatisticsProvider(String location, StoryCardModel story){
		Matcher match = repositoryTypeRegex.matcher(location);

		if(match.find()){
			String key = match.group(1);
			

			try {
				TestStatisticsProvider provider = (TestStatisticsProvider) providers.get(key).newInstance();
				provider.loadTest(story);
				return provider;
			} catch (InstantiationException e) {
				util.Logger.singleton().error(e);
			} catch (IllegalAccessException e) {
				util.Logger.singleton().error(e);
			}
		}

		return null;
	}
	
	
	
	
}
