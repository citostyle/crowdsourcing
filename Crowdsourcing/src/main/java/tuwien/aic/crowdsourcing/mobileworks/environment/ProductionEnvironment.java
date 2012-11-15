package tuwien.aic.crowdsourcing.mobileworks.environment;

public class ProductionEnvironment implements Environment {

	private static final String BASE_URL = "https://work.mobileworks.com/";
	private static final String NAME = "Production";
	
	@Override
	public String getBaseUrl() {
		return BASE_URL;
	}
	
	@Override
	public String toString() {
		return NAME;
	}

}
