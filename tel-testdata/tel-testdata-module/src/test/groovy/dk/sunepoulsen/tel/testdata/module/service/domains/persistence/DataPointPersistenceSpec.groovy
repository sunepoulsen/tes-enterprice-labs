package dk.sunepoulsen.tel.testdata.module.service.domains.persistence

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.PersistenceException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(['ut'])
@Transactional
class DataPointPersistenceSpec extends Specification {

    @Autowired
    private TestsPersistence testsPersistence

    @Autowired
    private DataPointDataSetPersistence dataSetPersistence

    @Autowired
    private DataPointRepository repository

    @Autowired
    private DataPointPersistence sut

    void setup() {
        testsPersistence.clearDatabase()
    }

    void "Store empty set of data points"() {
        given: 'Number of data points before operation'
            long numberOfDataPoints = repository.count()

        when: 'Store empty set of data points'
            sut.createDataPoints([])

        then: 'Verify that no rows has been added'
            numberOfDataPoints == repository.count()
    }

    void "A non empty set of data points"() {
        given: 'a data set of data points'
            DataPointDataSetEntity dataSetEntity = this.dataSetPersistence.create(new DataPointDataSetEntity(
                name: 'name',
                description: 'description',
                minX: 5.0,
                maxX: 15.0,
                minY: 5.0,
                maxY: 15.0,
                minQuantity: 20,
                maxQuantity: 2000,
                status: DataPointsDataSetStatusType.NEW
            ))

        and: '5 data points for the data set of data points'
            Range<Integer> range = 0..4
            List<DataPointEntity> dataPoints = range.collect {
                return new DataPointEntity(
                    dataSet: dataSetEntity,
                    x: new Random().nextDouble(dataSetEntity.minX, dataSetEntity.maxX),
                    y: new Random().nextDouble(dataSetEntity.minY, dataSetEntity.maxY)
                )
            }

        when: 'Store the created data points'
            sut.createDataPoints(dataPoints)

        then: 'Verify the created data points'
            List<DataPointEntity> createdEntities = repository.findDataPoints(dataSetEntity.id)
            createdEntities.size() == 5
            range.each {
                DataPointEntity entity = createdEntities[it]
                assert entity.id > 0
                assert entity.dataSet.id == dataSetEntity.id
            }

    }

    @Unroll
    void "Storing data points with errors: #_testcase"() {
        given: 'a data set of data points'
            DataPointDataSetEntity dataSetEntity = this.dataSetPersistence.create(new DataPointDataSetEntity(
                name: 'name',
                description: 'description',
                minX: 5.0,
                maxX: 15.0,
                minY: 5.0,
                maxY: 15.0,
                minQuantity: 1,
                maxQuantity: 3,
                status: DataPointsDataSetStatusType.NEW
            ))

        and: 'one data point for the data set of data points'
            Range<Integer> range = 0..(_count-1)
            List<DataPointEntity> dataPoints = range.collect {
                return new DataPointEntity(
                    dataSet: dataSetEntity,
                    x: _x,
                    y: _y
                )
            }

        when: 'Store the created data points'
            sut.createDataPoints(dataPoints)

        then: 'Verify throw exception'
            PersistenceException ex = thrown(PersistenceException)
            ex.message == _message

        where:
            _testcase                       | _count | _x    | _y   | _message
            'Too many data points'          | 4      | 6.0   | 6.0  | 'Too many data points'
            'X is smaller than constraints' | 2      | -20.0 | 6.0  | 'X value is smaller than constraints'
            'X is bigger than constraints'  | 2      | 50.0  | 6.0  | 'X value is bigger than constraints'
            'Y is smaller than constraints' | 2      | 6.0   | -6.0 | 'Y value is smaller than constraints'
            'Y is bigger than constraints'  | 2      | 6.0   | 66.0 | 'Y value is bigger than constraints'
    }

    void "Storing data points to different data sets"() {
        given: '5 data set of data points'
            Range<Integer> datasetRange = 0..4
            List<DataPointDataSetEntity> datasets = datasetRange.collect {
                return this.dataSetPersistence.create(new DataPointDataSetEntity(
                    name: 'name',
                    description: 'description',
                    minX: 5.0,
                    maxX: 15.0,
                    minY: 5.0,
                    maxY: 15.0,
                    minQuantity: 1,
                    maxQuantity: 3,
                    status: DataPointsDataSetStatusType.NEW
                ))
            }

        and: 'one data point for each data set of data points'
            List<DataPointEntity> dataPoints = datasets.collect {
                return new DataPointEntity(
                    dataSet: it,
                    x: 5.0,
                    y: 5.0
                )
            }

        when: 'Store the created data points'
            sut.createDataPoints(dataPoints)

        then: 'Verify throw exception'
            PersistenceException ex = thrown(PersistenceException)
            ex.message == 'All data points must belong to the same data set'
    }

}
