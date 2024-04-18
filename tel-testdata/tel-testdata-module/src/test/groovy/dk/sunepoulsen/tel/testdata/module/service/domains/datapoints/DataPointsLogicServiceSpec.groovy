package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetConstraints
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetStatus
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointPersistence
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType
import dk.sunepoulsen.tes.rest.models.RangeModel
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.DuplicateResourceException
import spock.lang.Specification

class DataPointsLogicServiceSpec extends Specification {

    private static final RangeModel<Double> X_VALUES = new RangeModel<Double>(min: 150.0, max: 210.0)
    private static final RangeModel<Double> Y_VALUES = X_VALUES
    private static final RangeModel<Integer> QUANTITY = new RangeModel<Integer>(min: 2, max: 5)
    private static final DataPointDataSetConstraints CONSTRAINTS = new DataPointDataSetConstraints(
        xValues: X_VALUES,
        yValues: Y_VALUES,
        quantity: QUANTITY,
    )

    private DataPointDataSetPersistence dataSetPersistence
    private DataPointPersistence dataPointPersistence;
    private DataPointsTransformations transformations
    private DataPointsLogicService sut

    void setup() {
        this.dataSetPersistence = Mock(DataPointDataSetPersistence)
        this.dataPointPersistence = Mock(DataPointPersistence)
        this.transformations = Mock(DataPointsTransformations)
        this.sut = new DataPointsLogicService(this.dataSetPersistence, this.dataPointPersistence, this.transformations)
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

    void "Generate data points successfully"() {
        given: 'A data set of data points'
            DataPointDataSet model = new DataPointDataSet(
                id: 5L,
                name: 'name',
                description: 'description',
                constraints: CONSTRAINTS
            )

            Long capturedModelId
            List<DataPointEntity> capturedDataPoints

        when:
            sut.generateDataPoints(model)

        then:
            1 * dataSetPersistence.updateStatus(model.id, DataPointsDataSetStatusType.IN_PROGRESS)
            1 * dataSetPersistence.get(model.id) >> {
                Optional.of(new DataPointDataSetEntity(
                    id: 5L,
                    name: model.name,
                    description: model.description,
                    dataPoints: []
                ))
            }

            1 * dataPointPersistence.createDataPoints(_, _) >> { arguments ->
                capturedModelId = arguments[0]
                capturedDataPoints = arguments[1]
            }
            capturedModelId == model.id
            capturedDataPoints.size() >= model.constraints.quantity.min
            capturedDataPoints.size() < model.constraints.quantity.max

            1 * dataSetPersistence.updateStatus(model.id, DataPointsDataSetStatusType.COMPLETED)
    }

}
