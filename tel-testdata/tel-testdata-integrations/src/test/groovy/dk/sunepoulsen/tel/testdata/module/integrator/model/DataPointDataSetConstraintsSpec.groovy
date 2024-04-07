package dk.sunepoulsen.tel.testdata.module.integrator.model

import dk.sunepoulsen.tel.testdata.module.integrator.testutils.ConstraintViolationAssertions
import dk.sunepoulsen.tel.testdata.module.integrator.testutils.TestData
import dk.sunepoulsen.tes.rest.models.RangeModel
import dk.sunepoulsen.tes.rest.models.validation.DefaultValidator
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate
import jakarta.validation.ConstraintViolationException
import jakarta.validation.groups.Default
import spock.lang.Specification
import spock.lang.Unroll

class DataPointDataSetConstraintsSpec extends Specification {

    private static final RangeModel<Double> X_VALUES = new RangeModel<Double>(min: 150.0, max: 210.0)
    private static final RangeModel<Double> MINUS_X_VALUES = new RangeModel<Double>(min: -5.0)
    private static final RangeModel<Double> Y_VALUES = X_VALUES
    private static final RangeModel<Double> MINUS_Y_VALUES = MINUS_X_VALUES
    private static final RangeModel<Integer> QUANTITY = new RangeModel<Integer>(min: 2000, max: 5000)
    private static final RangeModel<Integer> MINUS_QUANTITY = new RangeModel<Integer>(min: -5)

    private DefaultValidator validator

    void setup() {
        this.validator = new DefaultValidator()
    }

    void "Validate a DataPointDataSetConstraints that is valid"() {
        given:
            DataPointDataSetConstraints model = new DataPointDataSetConstraints(
                xValues: X_VALUES,
                yValues: Y_VALUES,
                quantity: QUANTITY,
            )

        when:
            this.validator.validate(model, Default, OnCrudCreate)

        then:
            noExceptionThrown()
    }

    @Unroll
    void "Validate a DataPointDataSetConstraints that is invalid: #_testcase"() {
        given:
            DataPointDataSetConstraints model = new DataPointDataSetConstraints(
                xValues: _xValues,
                yValues: _yValues,
                quantity: _quantity,
            )

        when:
            this.validator.validate(model)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, _errors)

        where:
            _testcase                          | _xValues       | _yValues       | _quantity      | _errors
            'xValues is null'                  | null           | Y_VALUES       | QUANTITY       | [TestData.createError('xValues', 'must not be null')]
            'xValues.min is negative'          | MINUS_X_VALUES | Y_VALUES       | QUANTITY       | [TestData.createError('xValues.min', 'must be greater than or equal to 0')]
            'yValues is null'                  | X_VALUES       | null           | QUANTITY       | [TestData.createError('yValues', 'must not be null')]
            'yValues.min is negative'          | X_VALUES       | MINUS_Y_VALUES | QUANTITY       | [TestData.createError('yValues.min', 'must be greater than or equal to 0')]
            'quantity is null'                 | X_VALUES       | Y_VALUES       | null           | [TestData.createError('quantity', 'must not be null')]
            'quantity.min is negative is null' | X_VALUES       | Y_VALUES       | MINUS_QUANTITY | [TestData.createError('quantity.min', 'must be greater than or equal to 0')]
    }

}
