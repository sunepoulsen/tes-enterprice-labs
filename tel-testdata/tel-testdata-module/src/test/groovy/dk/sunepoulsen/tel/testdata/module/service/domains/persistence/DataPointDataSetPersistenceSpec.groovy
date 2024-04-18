package dk.sunepoulsen.tel.testdata.module.service.domains.persistence

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataSetType
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(['ut'])
@Transactional
class DataPointDataSetPersistenceSpec extends Specification {

    @Autowired
    private TestsPersistence testsPersistence

    @Autowired
    private DataPointDataSetPersistence persistence

    @Autowired
    private DataPointDataSetRepository repository

    void setup() {
        testsPersistence.clearDatabase()
    }

    void "Create new data point data set"() {
        when:
            DataPointDataSetEntity result = this.persistence.create(new DataPointDataSetEntity(
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

        then:
            result.id > 0
            result.type == DataSetType.DATA_POINTS
            result.createDateTime
            result.updateDateTime
    }

    void "Update status of a data set of data points"() {
        given: 'a data set of data points'
            DataPointDataSetEntity dataSetEntity = this.persistence.create(new DataPointDataSetEntity(
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

        when: 'update a status to IN_PROGRESS'
            this.persistence.updateStatus(dataSetEntity.id, DataPointsDataSetStatusType.IN_PROGRESS)
            Optional<DataPointDataSetEntity> resultEntity = repository.findById(dataSetEntity.id)

        then:
            resultEntity.present
            resultEntity.get().id == dataSetEntity.id
            resultEntity.get().status == DataPointsDataSetStatusType.IN_PROGRESS
    }
}
