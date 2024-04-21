package dk.sunepoulsen.tel.testdata.module.stress.tests.simulations;

import dk.sunepoulsen.tes.utils.PropertyResource;
import dk.sunepoulsen.tes.utils.exceptions.PropertyResourceException;
import lombok.Getter;

import java.time.Duration;

public class ScenarioConfig {

    private String propertyKeyPrefix;

    @Getter
    private Duration usersDuration;

    @Getter
    private Duration requestsPause;

    @Getter
    private Duration startupDuration;

    @Getter
    private Integer atOnceUsers;

    @Getter
    private Integer rampUsers;

    @Getter
    private Duration rampDuration;

    public ScenarioConfig(String scenarioName, PropertyResource props) throws PropertyResourceException {
        this.propertyKeyPrefix = scenarioName.toLowerCase() + ".scenario";

        loadProperties(props);
    }

    private void loadProperties(PropertyResource props) throws PropertyResourceException {
        usersDuration = Duration.parse(props.property(this.propertyKeyPrefix + ".user.duration"));
        requestsPause = Duration.parse(props.property(this.propertyKeyPrefix + ".requests.pause.duration"));
        startupDuration = Duration.parse(props.property(this.propertyKeyPrefix + ".startup.duration"));
        atOnceUsers = Integer.parseInt(props.property(this.propertyKeyPrefix + ".at.once.users"));
        rampUsers = Integer.parseInt(props.property(this.propertyKeyPrefix + ".ramp.users"));
        rampDuration = Duration.parse(props.property(this.propertyKeyPrefix + ".ramp.duration"));
    }

}
