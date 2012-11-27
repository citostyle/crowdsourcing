
package tuwien.aic.crowdsourcing.mobileworks.environment;

public class SandboxEnvironment implements Environment {

    private static final String BASE_URL = "https://sandbox.mobileworks.com/";
    private static final String NAME = "Sandbox";

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public String toString() {
        return NAME;
    }

}
