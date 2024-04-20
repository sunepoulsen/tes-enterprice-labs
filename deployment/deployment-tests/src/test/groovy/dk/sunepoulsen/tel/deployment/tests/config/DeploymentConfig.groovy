package dk.sunepoulsen.tel.deployment.tests.config

import dk.sunepoulsen.tes.utils.PropertyResource
import dk.sunepoulsen.tes.utils.Resources

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
        PropertyResource deploymentProperties = new PropertyResource(Resources.readResource("/deployment-${profile}.properties"))

        this.moduleVersion = deploymentProperties.property('version')
        this.envEnabled = Boolean.parseBoolean(deploymentProperties.property('env.endpoint.enabled'))
        this.metricsEnabled = Boolean.parseBoolean(deploymentProperties.property('metrics.endpoint.enabled'))
        this.telTestDataUrl = new URI(deploymentProperties.property('tel-testdata.url'))
        this.telWebUrl = new URI(deploymentProperties.property('tel-web.url'))
        this.certificateFile = new File(deploymentProperties.property('ssl.key-store'))
        this.certificatePassword = deploymentProperties.property('ssl.key-store-password')
        this.waitDuration = Duration.parse(deploymentProperties.property('wait.duration'))
    }
}
