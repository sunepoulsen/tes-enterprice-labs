# Local Docker Compose Configuration

This Docker Compose Configuration starts the entire solution with all containers.

To collect all log files correctly to the local file system after the containers have been shutdown then we
need to use some special Gradle tasks to start and stop the configuration:

- `deploy`: Starts the configuration and deletes all existing log files on the local file system.
- `undeploy`: Collects the log files to the local file system and shutdown all the docker containers in
  this configuration.

## TEL TestData Module

<table>
  <tr>
    <td><b>Service</b></td>
    <td>tel-testdata-module</td>
  </tr>
  <tr>
    <td><b>Image</b></td>
    <td>tel-testdata-module:&lt;version&gt;</td>
  </tr>
  <tr>
    <td><b>Ports</b></td>
    <td>
      8080 &rarr; 29180<br/>
      9010 &rarr; 29110
    </td>
  </tr>
</table>

The following log files are collected from this container:

| **Container log file**  | **Local log file**                   |
|-------------------------|--------------------------------------|
| `/app/logs/service.log` | `build/logs/tel-testdata-module.log` |

## Tel Web Module

<table>
  <tr>
    <td><b>Service</b></td>
    <td>tel-web-module</td>
  </tr>
  <tr>
    <td><b>Image</b></td>
    <td>tel-web-module:&lt;version&gt;</td>
  </tr>
  <tr>
    <td><b>Ports</b></td>
    <td>
      8080 &rarr; 29280
    </td>
  </tr>
</table>

The following log files are collected from this container:

| **Container log file**      | **Local log file**                     |
|-----------------------------|----------------------------------------|
| `/var/log/nginx/access.log` | `build/logs/tel-web-module-access.log` |
| `/var/log/nginx/error.log`  | `build/logs/tel-web-module-error.log`  |
