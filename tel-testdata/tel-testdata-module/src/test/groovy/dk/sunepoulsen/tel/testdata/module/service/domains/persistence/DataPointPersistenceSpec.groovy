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
            sut.createDataPoints(1L, [])

        then: 'Verify that no rows has been added'
            numberOfDataPoints == repository.count()
    }

    void "A non empty set of data points"() {
        given: 'a data set of data points'
            long numberOfDataPoints = repository.count()
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
                    x: new Random().nextDouble(dataSetEntity.minX, dataSetEntity.maxX),
                    y: new Random().nextDouble(dataSetEntity.minY, dataSetEntity.maxY)
                )
            }

        when: 'Store the created data points'
            sut.createDataPoints(dataSetEntity.id, dataPoints)

        then: 'Verify the created data points'
            numberOfDataPoints == repository.count() - 5
            List<DataPointEntity> createdEntities = repository.findDataPoints(dataSetEntity.id)
            createdEntities.size() == 5
            range.each {
                DataPointEntity entity = createdEntities[it]
                assert entity.id > 0
                assert entity.dataSet.id == dataSetEntity.id
            }

    }

    void "A non empty set of data points to a data set that does not exist"() {
        given: 'Get total number of data points'
            long numberOfDataPoints = repository.count()

        and: '5 data points for the data set of data points'
            Range<Integer> range = 0..4
            List<DataPointEntity> dataPoints = range.collect {
                return new DataPointEntity(
                    x: 5.0,
                    y: 3.5
                )
            }

        when: 'Store the created data points'
            sut.createDataPoints(1L, dataPoints)

        then: 'Verify that no data points has been created'
            numberOfDataPoints == repository.count()
            repository.findDataPoints(1L) == []
    }

    @Unroll
    void "Storing data points with errors: #_testcase"() {
        given: 'a data set of data points'
            long numberOfDataPoints = repository.count()
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
                    x: _x,
                    y: _y
                )
            }

        when: 'Store the created data points'
            sut.createDataPoints(dataSetEntity.id, dataPoints)

        then: 'Verify throw exception'
            numberOfDataPoints == repository.count()
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

}
