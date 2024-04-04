package dk.sunepoulsen.tel.deployment.tests.config

import java.time.Duration

class DeploymentConfig {

    static final String PROFILE_SYSTEM_PROPERTY = 'deployment.test.profile'

    private String profile
    private String moduleVersion
    private Boolean envEnabled
    private Boolean metricsEnabled
    private URI telTestDataUrl
    private URI telWebUrl
    private File certificateFile
    private String certificatePassword
    private Duration waitDuration

    DeploymentConfig() {
        this(System.getProperty(PROFILE_SYSTEM_PROPERTY))
    }

    DeploymentConfig(String profile) {
        this.profile = profile
        readProfile()
    }

    static DeploymentConfig instance() {
        return new DeploymentConfig()
    }

    String getProfile() {
        return profile
    }

    String getModuleVersion() {
        return moduleVersion
    }

    Boolean getEnvEnabled() {
        return envEnabled
    }

    Boolean getMetricsEnabled() {
        return metricsEnabled
    }

    URI getTelTestDataUrl() {
        return telTestDataUrl
    }

    URI getTelWebUrl() {
        return telWebUrl
    }

    File getCertificateFile() {
        return certificateFile
    }

    String getCertificatePassword() {
        return certificatePassword
    }

    Duration getWaitDuration() {
        return waitDuration
    }

    private void readProfile() {
        Properties props = new Properties()
        props.load(getClass().getResourceAsStream("/deployment-${profile}.properties"))

        this.moduleVersion = props.getProperty('version')
        this.envEnabled = Boolean.parseBoolean(props.getProperty('env.endpoint.enabled'))
        this.metricsEnabled = Boolean.parseBoolean(props.getProperty('metrics.endpoint.enabled'))
        this.telTestDataUrl = new URI(props.getProperty('tel-testdata.url'))
        this.telWebUrl = new URI(props.getProperty('tel-web.url'))
        this.certificateFile = new File(props.getProperty('ssl.key-store'))
        this.certificatePassword = props.getProperty('ssl.key-store-password')
        this.waitDuration = Duration.parse(props.getProperty('wait.duration'))
    }
}
