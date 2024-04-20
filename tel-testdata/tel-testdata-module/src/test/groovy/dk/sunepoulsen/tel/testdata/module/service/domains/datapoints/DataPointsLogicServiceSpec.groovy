package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetStatus
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.DuplicateResourceException
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.ResourceNotFoundException
import spock.lang.Specification

class DataPointsLogicServiceSpec extends Specification {

    private DataPointDataSetPersistence dataSetPersistence
    private DataPointsTransformations transformations
    private DataPointsLogicService sut

    void setup() {
        this.dataSetPersistence = Mock(DataPointDataSetPersistence)
        this.transformations = Mock(DataPointsTransformations)
        this.sut = new DataPointsLogicService(this.dataSetPersistence, this.transformations)
    }

    void "Create new Data Point Dataset successfully"() {
        given: 'a data point dataset'
            DataPointDataSet model = new DataPointDataSet(
                name: 'name'
            )

        and: 'called model with status NEW'
            DataPointDataSet calledModel = new DataPointDataSet(
                name: 'name',
                status: DataPointDataSetStatus.NEW
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
            DataPointDataSet result = sut.create(model)

        then: 'verify that the dataset is created successfully'
            noExceptionThrown()
            result.id == resultDataset.id

            1 * this.transformations.toEntity(calledModel) >> { inputEntity }
            1 * this.dataSetPersistence.create(inputEntity) >> { outputEntity }
            1 * this.transformations.toModel(outputEntity) >> { resultDataset }
    }

    void "Get a dataset of data points: Successfully"() {
        given: 'a data point dataset'
            DataPointDataSet dataSet = new DataPointDataSet(
                name: 'name'
            )

        and: 'mocked database entity'
            DataPointDataSetEntity foundEntity = new DataPointDataSetEntity(
                name: 'name'
            )

        when: 'call endpoint to create dataset'
            DataPointDataSet result = sut.get(1L)

        then: 'verify that the dataset is created successfully'
            noExceptionThrown()
            result.id == dataSet.id

            1 * this.dataSetPersistence.get(1L) >> { foundEntity }
            1 * this.transformations.toModel(foundEntity) >> { dataSet }
    }

    void "Get a dataset of data points: Not found"() {
        when: 'call endpoint to get dataset'
            sut.get(1L)

        then: 'verify that the dataset is created successfully'
            ResourceNotFoundException ex = thrown(ResourceNotFoundException)
            ex.code == 'code'
            ex.param == 'param'
            ex.message == 'message'

            1 * this.dataSetPersistence.get(1L) >> {
                throw new ResourceNotFoundException('code', 'param', 'message')
            }
            0 * this.transformations.toModel(_)
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
            sut.create(model)

        then: 'verify that the dataset is created successfully'
            DuplicateResourceException ex = thrown(DuplicateResourceException)
            ex.code == 'code'
            ex.param == 'param'
            ex.message == 'message'

            1 * this.transformations.toEntity(model) >> { inputEntity }
            1 * this.dataSetPersistence.create(inputEntity) >> {
                throw new DuplicateResourceException('code', 'param', 'message')
            }
            0 * this.transformations.toModel(_)
    }

}
