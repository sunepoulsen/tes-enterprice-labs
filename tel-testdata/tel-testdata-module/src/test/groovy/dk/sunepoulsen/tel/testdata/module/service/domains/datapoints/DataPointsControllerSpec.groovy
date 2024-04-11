package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetConstraints
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity
import dk.sunepoulsen.tes.rest.models.RangeModel
import dk.sunepoulsen.tes.springboot.rest.exceptions.ApiConflictException
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.DuplicateResourceException
import spock.lang.Specification

class DataPointsControllerSpec extends Specification {

    private DataPointDataSetPersistence persistence
    private DataPointsTransformations transformations
    private DataPointsController sut

    void setup() {
        this.persistence = Mock(DataPointDataSetPersistence)
        this.transformations = Mock(DataPointsTransformations)
        this.sut = new DataPointsController(this.persistence, this.transformations)
    }

    void "Create new Data Point Dataset successfully"() {
        given: 'a data point dataset'
            DataPointDataSet model = new DataPointDataSet(
                name: 'name'
            )

        and: 'mocked database entities'
            DataPointDataSetEntity inputEntity = new DataPointDataSetEntity(
                id: 3
            )
            DataPointDataSetEntity outputEntity = new DataPointDataSetEntity(
                id: 7
            )

        and: 'mocked model result'
            DataPointDataSet resultDataset = new DataPointDataSet(
                id: outputEntity.id
            )

        when: 'call endpoint to create dataset'
            DataPointDataSet result = sut.createDataPointDataSet(model)

        then: 'verify that the dataset is created successfully'
            noExceptionThrown()
            result.id == resultDataset.id

            1 * this.transformations.toEntity(model) >> { inputEntity }
            1 * this.persistence.create(inputEntity) >> { outputEntity }
            1 * this.transformations.toModel(outputEntity) >> { resultDataset }
    }

    void "Create new Data Point Dataset resulting in a logic failure"() {
        given: 'a data point dataset'
            DataPointDataSet model = new DataPointDataSet(
                name: 'name'
            )

        and: 'mocked database entities'
            DataPointDataSetEntity inputEntity = new DataPointDataSetEntity(
                id: 3
            )

        when: 'call endpoint to create dataset'
            sut.createDataPointDataSet(model)

        then: 'verify that the dataset is created successfully'
            ApiConflictException ex = thrown(ApiConflictException)
            ex.serviceError.code == 'code'
            ex.serviceError.param == 'param'
            ex.serviceError.message == 'message'

            1 * this.transformations.toEntity(model) >> { inputEntity }
            1 * this.persistence.create(inputEntity) >> {
                throw new DuplicateResourceException('code', 'param', 'message')
            }
            0 * this.transformations.toModel(_)
    }

}
