package dk.sunepoulsen.tel.testdata.module.stress.tests.simulations;

import dk.sunepoulsen.tes.utils.PropertyResource;
import dk.sunepoulsen.tes.utils.Resources;
import dk.sunepoulsen.tes.utils.exceptions.PropertyResourceException;
import dk.sunepoulsen.tes.utils.exceptions.ResourceException;
import lombok.Getter;

public class Config {

    public static final String PROFILE_SYSTEM_PROPERTY = "stress.tests.profile";

    @Getter
    private String gatlingVersion;

    @Getter
    private ScenarioConfig operationsConfig;

    Config() throws ResourceException, PropertyResourceException {
        PropertyResource props = new PropertyResource(Resources.readResource(simulationResource()));
        loadProperties(props);

        this.operationsConfig = new ScenarioConfig("admins", props);
    }

    static String simulationResource() {
        String profile = "";
        if (System.getProperty(PROFILE_SYSTEM_PROPERTY) != null) {
            profile = System.getProperty(PROFILE_SYSTEM_PROPERTY);
        }

        if (profile.isEmpty()) {
            return "/simulation.properties";
        }

        return "/simulation-" + profile + ".properties";
    }

    private void loadProperties(PropertyResource props) throws PropertyResourceException {
        gatlingVersion = props.property("gatling.version");
    }
}
