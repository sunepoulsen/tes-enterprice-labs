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

class DataPointSpec extends Specification {

    private DefaultValidator validator

    void setup() {
        this.validator = new DefaultValidator()
    }

    void "Validate a DataPoint that is valid"() {
        given:
            DataPoint model = new DataPoint( x: 5.0, y: 5.0 )

        when:
            this.validator.validate(model)

        then:
            noExceptionThrown()
    }

    @Unroll
    void "Validate a DataPoint that is invalid: #_testcase"() {
        given:
            DataPoint model = new DataPoint( x: _x, y: _y )

        when:
            this.validator.validate(model, Default, OnCrudCreate)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, _errors)

        where:
            _testcase   | _x   | _y   | _errors
            'x is null' | null | 5.0  | [TestData.createError('x', 'must not be null')]
            'y is null' | 5.0  | null | [TestData.createError('y', 'must not be null')]
    }

}
