package dk.sunepoulsen.tel.testdata.module.service.domains.persistence

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataSetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(['ut'])
class DataPointDataSetPersistenceSpec extends Specification {

    @Autowired
    private DataPointDataSetPersistence persistence

    @Autowired
    private DataPointDataSetRepository repository

    void setup() {
        this.repository.deleteAll()
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
}
