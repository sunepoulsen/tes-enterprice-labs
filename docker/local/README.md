# Local Docker Compose Configuration

This Docker Compose Configuration starts the entire solution with all containers.

To collect all log files correctly to the local file system after the containers have been shutdown then we
need to use some special scripts to start and stop the configuration:

- `start-compose.sh`: Starts the configuration and deletes all existing log files on the local file system.
- `stop-compose.sh`: Collects the log files to the local file system and shutdown all the docker containers in 
  this configuration.

## Reporting Store

<table>
  <tr>
    <td><b>Service</b></td>
    <td>tel-testdata-module</td>
  </tr>
  <tr>
    <td><b>Image</b></td>
    <td>tel-testdata-module:1.0.0-SNAPSHOT</td>
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

| **Container log file**  | **Local log file**             |
|-------------------------|--------------------------------|
| `/app/logs/service.log` | `logs/tel-testdata-module.log` |

## Reporting Web

<table>
  <tr>
    <td><b>Service</b></td>
    <td>tel-web-module</td>
  </tr>
  <tr>
    <td><b>Image</b></td>
    <td>tel-web-module:1.0.0-SNAPSHOT</td>
  </tr>
  <tr>
    <td><b>Ports</b></td>
    <td>
      8080 &rarr; 29280
    </td>
  </tr>
</table>

The following log files are collected from this container:

| **Container log file**      | **Local log file**               |
|-----------------------------|----------------------------------|
| `/var/log/nginx/access.log` | `logs/tel-web-module-access.log` |
| `/var/log/nginx/error.log`  | `logs/tel-web-module-error.log`  |
