# Changelog

All notable changes to this project will be documented in this file. The target-audience for this document is developers and operations.

The changelog-format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Developers should add an entry to "Unreleased work" under the appropriate subsection, describing their work _prior_ to merging to master. The entry should contain a link to the Jira-story.
Adhere to the following format:
`- [Name of Jira-story or subtask](link to Jira-story): Description of the completed work`
Example-entry:

- [TESENTLABS-1](https://sunepoulsen.atlassian.net/browse/TESENTLABS-1): Create skeleton

For release-dates, use date-format: YYYY-MM-DD

## Unreleased work

### Features

- [TESENTLABS-2](https://sunepoulsen.atlassian.net/browse/TESENTLABS-2): Create testdata backend service
- [TESENTLABS-3](https://sunepoulsen.atlassian.net/browse/TESENTLABS-3): Create frontend web skeleton
- [TESENTLABS-4](https://sunepoulsen.atlassian.net/browse/TESENTLABS-4): Create Docker Compose deployment
- [TESENTLABS-17](https://sunepoulsen.atlassian.net/browse/TESENTLABS-17): Autogenerate certificates with Gradle on execution
- [TESENTLABS-24](https://sunepoulsen.atlassian.net/browse/TESENTLABS-24): Create a deployment test
- [TESENTLABS-27](https://sunepoulsen.atlassian.net/browse/TESENTLABS-27): Manage data sets in tel-testdata

### Fixed

### Development

- [TESENTLABS-27](https://sunepoulsen.atlassian.net/browse/TESENTLABS-27): Manage data sets in tel-testdata
  - Upgrade to Gradle 8.7

### DevOps

- [TESENTLABS-13](https://sunepoulsen.atlassian.net/browse/TESENTLABS-13): Replace pipeline bash scripts with python
  scripts
- [TESENTLABS-39](https://sunepoulsen.atlassian.net/browse/TESENTLABS-39): Integrate building pipeline with Jenkins

### Security

- [TESENTLABS-37](https://sunepoulsen.atlassian.net/browse/TESENTLABS-37): Creation of docker image of tel-web should 
  be placed in a Gradle sub module 
  - Updated dependencies to latest
