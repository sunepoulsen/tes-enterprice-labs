package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet
import dk.sunepoulsen.tes.springboot.rest.exceptions.ApiConflictException
import dk.sunepoulsen.tes.springboot.rest.exceptions.ApiNotFoundException
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.DuplicateResourceException
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.ResourceNotFoundException
import spock.lang.Specification

class DataPointsControllerSpec extends Specification {

    private DataPointsLogicService logicService
    private DataPointsGeneratorService dataPointsGeneratorService;
    private DataPointsController sut

    void setup() {
        this.logicService = Mock(DataPointsLogicService)
        this.dataPointsGeneratorService = Mock(DataPointsGeneratorService)
        this.sut = new DataPointsController(this.logicService, this.dataPointsGeneratorService)
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
            DataPointDataSet result = sut.create(model)

        then: 'verify that the dataset is created successfully'
            noExceptionThrown()
            result.id == resultDataset.id

            1 * this.logicService.create(model) >> { resultDataset }
            1 * this.dataPointsGeneratorService.generateDataPoints(resultDataset)
    }

    void "Create new Data Point Dataset resulting in a logic failure"() {
        given: 'a data point dataset'
            DataPointDataSet model = new DataPointDataSet(
                name: 'name'
            )

        when: 'call endpoint to create dataset'
            sut.create(model)

        then: 'verify that the dataset is created successfully'
            ApiConflictException ex = thrown(ApiConflictException)
            ex.serviceError.code == 'code'
            ex.serviceError.param == 'param'
            ex.serviceError.message == 'message'

            1 * this.logicService.create(model) >> {
                throw new DuplicateResourceException('code', 'param', 'message')
            }
            0 * this.dataPointsGeneratorService.generateDataPoints(_)
    }

    void "Get data set of data points: Success"() {
        when: 'call endpoint to get dataset'
            DataPointDataSet result = sut.get(17L)

        then: 'verify that the dataset is returned successfully'
            noExceptionThrown()
            result.id == 17L
            result.name == 'name'

            1 * this.logicService.get(17L) >> {
                new DataPointDataSet(
                    id: 17L,
                    name: 'name'
                )
            }
    }

    void "Get data set of data points: Not found"() {
        when: 'call endpoint to get dataset'
            sut.get(17L)

        then: 'verify that the dataset is returned successfully'
            ApiNotFoundException ex = thrown(ApiNotFoundException)
            ex.serviceError.code == 'code'
            ex.serviceError.param == 'param'
            ex.serviceError.message == 'message'

            1 * this.logicService.get(17L) >> {
                throw new ResourceNotFoundException('code', 'param', 'message')
            }
    }

}
