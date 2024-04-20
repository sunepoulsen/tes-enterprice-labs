# Design of endpoints

### Data model

Data models for the endpoints are documented in with OpenApi that is accessible by the `tel-testdata` 
service. The documentation can be accessed here:

```
https://<host>:<port>/swagger-ui.html
```

## Data points

Data points are test data of pair of X- and Y-coordinates.


### Create data sets of data points

Data set of data points can be created like with this endpoint:

```
POST /datasets/data-points
```

Creation of a dataset of data points are done like this:

```mermaid
sequenceDiagram
    actor Caller
    participant SprintBoot as Spring Boot
    participant Controller
    participant Logic as Logic Service
    participant DataPointsGenerator as Data Points <br/>Generator Service
    participant Persistence
    participant Repository as Spring Boot<br/>Repository

    Caller ->>+ SprintBoot: Request
    SprintBoot ->> SprintBoot: Validation
    SprintBoot ->>+ Controller: create(DataPointDataSet)
    Controller ->>+ Logic: create(DataPointDataSetEntity)
    Logic ->>+ Persistence: create(DataPointDataSetEntity)
    Persistence ->>+ Repository: save(DataPointDataSetEntity)
    Repository ->> Repository: Update time columns
    Repository ->>- Persistence: Created DataPointDataSetEntity
    Persistence -->>- Logic: DataPointDataSetEntity
    Logic -->>- Controller: DataPointDataSetEntity
    par Return response
        Controller -->>- SprintBoot: Created DataPointDataSet
        SprintBoot -->>- Caller: Response: ACCEPTED
    and Generate data points
        SprintBoot ->>+ DataPointsGenerator: generateDataPoints
        DataPointsGenerator ->>+ Persistence: updateStatus(IN_PROGRESS)
        Persistence -->>- DataPointsGenerator: void
        DataPointsGenerator ->>+ Persistence: createDataPoints(DataPoint*)
        Persistence ->>+ Repository: saveAll(DataPointEntity*)
        Repository ->> Repository: Update time columns
        Repository ->>- Persistence: void
        Persistence -->>- DataPointsGenerator: void
        DataPointsGenerator ->>+ Persistence: updateStatus(COMPLETED)
        Persistence -->>- DataPointsGenerator: void
        DataPointsGenerator -->>- SprintBoot: void
    end
```

### Get data sets of data points

Data set of data points can be received with this endpoint:

```
GET /datasets/data-points/{id}
```

Retrieving a dataset of data points are done like this:

```mermaid
sequenceDiagram
    actor Caller
    participant SprintBoot as Spring Boot
    participant Controller
    participant Logic as Logic Service
    participant Persistence
    participant Repository as Spring Boot<br/>Repository
    Caller ->>+ SprintBoot: Request
    SprintBoot ->> SprintBoot: Validation
    SprintBoot ->>+ Controller: get(Long)
    Controller ->>+ Logic: get(Long)
    Logic ->>+ Persistence: get(Long)
    Persistence ->>+ Repository: findById(Long)
    Repository ->>- Persistence: DataPointDataSetEntity
    Persistence -->>- Logic: DataPointDataSetEntity
    Logic -->>- Controller: DataPointDataSet
    Controller -->>- SprintBoot: DataPointDataSet
    SprintBoot -->>- Caller: Response: OK
```
