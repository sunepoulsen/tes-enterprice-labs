package dk.sunepoulsen.tel.testdata.module.ct

import dk.sunepoulsen.tel.testdata.module.integrator.TelTestDataIntegrator
import dk.sunepoulsen.tel.testdata.module.integrator.TelTestDataTestsIntegrator
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetConstraints
import dk.sunepoulsen.tes.docker.containers.TESBackendContainer
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientBadRequestException
import dk.sunepoulsen.tes.rest.models.RangeModel
import dk.sunepoulsen.tes.rest.models.ServiceValidationError
import dk.sunepoulsen.tes.rest.models.ServiceValidationErrorModel
import spock.lang.Specification

/**
 * Component tests of endpoints to manage data sets of data points.
 */
class DataPointDataSetSpec extends Specification {

    private TESBackendContainer container
    private TelTestDataTestsIntegrator testsSut
    private TelTestDataIntegrator sut

    void setup() {
        this.container = DeploymentSpockExtension.telTestDataBackendContainer()
        this.testsSut = DeploymentSpockExtension.telTestDataBackendTestsIntegrator()
        this.sut = DeploymentSpockExtension.telTestDataBackendIntegrator()

        this.testsSut.deletePersistence()
    }

    void "POST /datasets/data-points: ACCEPTED"() {
        given: 'Service is available'
            this.container.isHostAccessible()

        and: 'a body with validation errors'
            DataPointDataSet dataset = new DataPointDataSet(
                name: 'name',
                description: 'description',
                constraints: new DataPointDataSetConstraints(
                    xValues: new RangeModel<BigDecimal>(min: 5.0, max: 10.0),
                    yValues: new RangeModel<BigDecimal>(min: 5.0, max: 10.0),
                    quantity: new RangeModel<Integer>(min: 5, max: 10),
                )
            )

        when: 'Call POST /datasets/data-points'
            DataPointDataSet result = sut.createDataPointDataSet(dataset).blockingGet()

        then: 'Verify that we receive a ACCEPTED response and the right body'
            result.id > 0
            result.name == dataset.name
            result.description == dataset.description
            result.constraints == dataset.constraints
    }

    void "POST /datasets/data-points: BAD REQUEST"() {
        given: 'Service is available'
            this.container.isHostAccessible()

        and: 'a body with validation errors'
            DataPointDataSet dataset = new DataPointDataSet(
                name: 'name',
                description: 'description',
                constraints: new DataPointDataSetConstraints(
                    xValues: new RangeModel<BigDecimal>(min: 5.0, max: 10.0),
                    yValues: new RangeModel<BigDecimal>(min: 5.0, max: -10.0),
                    quantity: new RangeModel<Integer>(min: 5, max: 10),
                )
            )

        when: 'Call POST /datasets/data-points'
            sut.createDataPointDataSet(dataset).blockingGet()

        then: 'Verify that we receive a bad request'
            ClientBadRequestException ex = thrown(ClientBadRequestException)

        and: 'verify error message'
            ex.serviceError.code == null
            ex.serviceError.param == null
            ex.serviceError.message == 'Unable to process request because of validation errors'

        and: 'verify that we receive a service validation error model'
            ex.serviceError instanceof ServiceValidationErrorModel
            ServiceValidationErrorModel validationErrorModel = (ServiceValidationErrorModel)ex.serviceError

        and: 'verify validation errors'
            validationErrorModel.validationErrors == [
                new ServiceValidationError(
                    param: 'constraints.yValues',
                    message: 'max must be equal or greater than min'
                ),
                new ServiceValidationError(
                    param: 'constraints.yValues.max',
                    message: 'must be greater than or equal to 0'
                )
            ]
    }

}
