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

    private DataPointsLogicService logicService
    private DataPointsController sut

    void setup() {
        this.logicService = Mock(DataPointsLogicService)
        this.sut = new DataPointsController(this.logicService)
    }

    void "Create new Data Point Dataset successfully"() {
        given: 'a data point dataset'
            DataPointDataSet model = new DataPointDataSet(
                name: 'name'
            )

        and: 'mocked model result'
            DataPointDataSet resultDataset = new DataPointDataSet(
                id: 7
            )

        when: 'call endpoint to create dataset'
            DataPointDataSet result = sut.createDataPointDataSet(model)

        then: 'verify that the dataset is created successfully'
            noExceptionThrown()
            result.id == resultDataset.id

            1 * this.logicService.create(model) >> { resultDataset }
    }

    void "Create new Data Point Dataset resulting in a logic failure"() {
        given: 'a data point dataset'
            DataPointDataSet model = new DataPointDataSet(
                name: 'name'
            )

        when: 'call endpoint to create dataset'
            sut.createDataPointDataSet(model)

        then: 'verify that the dataset is created successfully'
            ApiConflictException ex = thrown(ApiConflictException)
            ex.serviceError.code == 'code'
            ex.serviceError.param == 'param'
            ex.serviceError.message == 'message'

            1 * this.logicService.create(model) >> {
                throw new DuplicateResourceException('code', 'param', 'message')
            }
    }

}
