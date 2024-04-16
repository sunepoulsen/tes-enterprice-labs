package dk.sunepoulsen.tel.testdata.module.integrator.model

import dk.sunepoulsen.tel.testdata.module.integrator.testutils.ConstraintViolationAssertions
import dk.sunepoulsen.tel.testdata.module.integrator.testutils.TestData
import dk.sunepoulsen.tes.rest.models.RangeModel
import dk.sunepoulsen.tes.rest.models.validation.DefaultValidator
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudRead
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudUpdate
import jakarta.validation.ConstraintViolationException
import jakarta.validation.groups.Default
import spock.lang.Specification
import spock.lang.Unroll

class DataPointDataSetSpec extends Specification {

    private static final RangeModel<Double> X_VALUES = new RangeModel<Double>(min: 150.0, max: 210.0)
    private static final RangeModel<Double> Y_VALUES = X_VALUES
    private static final RangeModel<Integer> QUANTITY = new RangeModel<Integer>(min: 2000, max: 5000)
    private static final DataPointDataSetConstraints CREATION_CONSTRAINTS = new DataPointDataSetConstraints(
        xValues: X_VALUES,
        yValues: Y_VALUES,
        quantity: QUANTITY,
    )

    private DefaultValidator validator

    void setup() {
        this.validator = new DefaultValidator()
    }

    void "Validate with group OnCrudCreate is valid"() {
        given:
            DataPointDataSet model = new DataPointDataSet(
                name: 'name',
                description: 'description',
                constraints: CREATION_CONSTRAINTS
            )

        when:
            this.validator.validate(model, Default, OnCrudCreate)

        then:
            noExceptionThrown()
    }

    @Unroll
    void "Validate with group OnCrudCreate is invalid: #_testcase"() {
        given:
            DataPointDataSet model = new DataPointDataSet(
                name: 'name',
                description: 'description',
                constraints: new DataPointDataSetConstraints(
                    xValues: _xValues,
                    yValues: _yValues,
                    quantity: _quantity,
                ),
                status: _status
            )

        when:
            this.validator.validate(model, Default, OnCrudCreate)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, _errors)

        where:
            _testcase                     | _xValues | _yValues | _quantity | _status                            | _errors
            'constraints.xValues is null' | null     | Y_VALUES | QUANTITY  | null                               | [TestData.createError('constraints.xValues', 'must not be null')]
            'status is not null'          | X_VALUES | Y_VALUES | QUANTITY  | DataPointDataSetStatus.IN_PROGRESS | [TestData.createError('status', 'must be null')]
    }

    void "Validate with group OnCrudRead is valid"() {
        given:
            DataPointDataSet model = new DataPointDataSet(
                id: 17,
                name: 'name',
                description: 'description',
                constraints: CREATION_CONSTRAINTS,
                status: DataPointDataSetStatus.NEW
            )

        when:
            this.validator.validate(model, Default, OnCrudRead)

        then:
            noExceptionThrown()
    }

    @Unroll
    void "Validate with group OnCrudRead is invalid: #_testcase"() {
        given:
            DataPointDataSet model = new DataPointDataSet(
                id: 17,
                name: 'name',
                description: 'description',
                constraints: _constraints,
                status: DataPointDataSetStatus.NEW
            )

        when:
            this.validator.validate(model, Default, OnCrudRead)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, _errors)

        where:
            _testcase                     | _constraints | _errors
            'constraints is null' | null         | [TestData.createError('constraints', 'must not be null')]
    }

    void "Validate with group OnCrudUpdate is valid"() {
        given:
            DataPointDataSet model = new DataPointDataSet(
                constraints: CREATION_CONSTRAINTS
            )

        when:
            this.validator.validate(model, Default, OnCrudUpdate)

        then:
            noExceptionThrown()
    }

    void "Validate with group OnCrudUpdate is invalid"() {
        given:
            DataPointDataSet model = new DataPointDataSet(
                constraints: CREATION_CONSTRAINTS,
                status: DataPointDataSetStatus.IN_PROGRESS
            )

        when:
            this.validator.validate(model, Default, OnCrudUpdate)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, [
                TestData.createError('status', 'must be null')
            ])
    }

}
