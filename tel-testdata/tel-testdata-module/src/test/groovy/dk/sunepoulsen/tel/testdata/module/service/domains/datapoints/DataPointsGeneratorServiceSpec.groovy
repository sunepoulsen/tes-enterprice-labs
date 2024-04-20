package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetConstraints
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointPersistence
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType
import dk.sunepoulsen.tes.rest.models.RangeModel
import spock.lang.Specification

class DataPointsGeneratorServiceSpec extends Specification {

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
    private DataPointsGeneratorService sut

    void setup() {
        this.dataSetPersistence = Mock(DataPointDataSetPersistence)
        this.dataPointPersistence = Mock(DataPointPersistence)
        this.sut = new DataPointsGeneratorService(this.dataSetPersistence, this.dataPointPersistence)
    }

    void "Generate data points: Success"() {
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
            1 * dataSetPersistence.exists(model.id) >> { true }
            1 * dataPointPersistence.createDataPoints(_, _) >> { arguments ->
                capturedModelId = arguments[0]
                capturedDataPoints = arguments[1]
            }
            capturedModelId == model.id
            capturedDataPoints.size() >= model.constraints.quantity.min
            capturedDataPoints.size() <= model.constraints.quantity.max

            1 * dataSetPersistence.updateStatus(model.id, DataPointsDataSetStatusType.COMPLETED)
    }

    void "Generate data points: Dataset not found"() {
        given: 'A data set of data points'
            DataPointDataSet model = new DataPointDataSet(
                id: 5L,
                name: 'name',
                description: 'description',
                constraints: CONSTRAINTS
            )

        when:
            sut.generateDataPoints(model)

        then:
            noExceptionThrown()
            1 * dataSetPersistence.updateStatus(model.id, DataPointsDataSetStatusType.IN_PROGRESS)
            1 * dataSetPersistence.exists(model.id) >> { false }
            0 * dataPointPersistence.createDataPoints(_, _)
            0 * dataSetPersistence.updateStatus(model.id, _)
    }

}
