package dk.sunepoulsen.tel.testdata.module.integrator.model

import dk.sunepoulsen.tel.testdata.module.integrator.testutils.ConstraintViolationAssertions
import dk.sunepoulsen.tel.testdata.module.integrator.testutils.TestData
import dk.sunepoulsen.tes.rest.models.validation.DefaultValidator
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudRead
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudUpdate
import jakarta.validation.ConstraintViolationException
import spock.lang.Specification
import spock.lang.Unroll

class DataSetSpec extends Specification {

    private DefaultValidator validator

    void setup() {
        this.validator = new DefaultValidator()
    }

    @Unroll
    void "Validate with group #_testcase is valid"() {
        given:
            DataSet model = new DataSet(
                id: _id,
                name: 'name',
                description: 'description'
            )

        when:
            this.validator.validate(model, _groups)

        then:
            noExceptionThrown()

        where:
            _testcase      | _groups      | _id
            'OnCrudCreate' | OnCrudCreate | null
            'OnCrudRead'   | OnCrudRead   | 1L
            'OnCrudUpdate' | OnCrudUpdate | null
    }

    @Unroll
    void "Validate with group OnCrudCreate is invalid: #_testcase"() {
        given:
            DataSet model = new DataSet(
                id: _id,
                name: _name,
                description: _description
            )

        when:
            this.validator.validate(model, OnCrudCreate)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, _errors)

        where:
            _testcase             | _id  | _name  | _description  | _errors
            'id is not null'      | 10   | 'name' | 'description' | [TestData.createError('id', 'must be null')]
            'name is null'        | null | null   | 'description' | [TestData.createError('name', 'must not be null')]
            'description is null' | null | 'name' | null          | [TestData.createError('description', 'must not be null')]
    }

    @Unroll
    void "Validate with group OnCrudRead is invalid: #_testcase"() {
        given:
            DataSet model = new DataSet(
                id: _id,
                name: _name,
                description: _description
            )

        when:
            this.validator.validate(model, OnCrudRead)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, _errors)

        where:
            _testcase             | _id  | _name  | _description  | _errors
            'id is null'          | null | 'name' | 'description' | [TestData.createError('id', 'must not be null')]
            'name is null'        | 10   | null   | 'description' | [TestData.createError('name', 'must not be null')]
            'description is null' | 10   | 'name' | null          | [TestData.createError('description', 'must not be null')]
    }

    @Unroll
    void "Validate with group OnCrudUpdate is valid: #_testcase"() {
        given:
            DataSet model = new DataSet(
                id: null,
                name: _name,
                description: _description
            )

        when:
            this.validator.validate(model, OnCrudUpdate)

        then:
            noExceptionThrown()

        where:
            _testcase             | _name  | _description
            'All values are null' | null   | null
            'name is null'        | null   | 'description'
            'description is null' | 'name' | null
    }

    @Unroll
    void "Validate with group OnCrudUpdate is invalid: #_testcase"() {
        given:
            DataSet model = new DataSet(
                id: 10,
                name: 'name',
                description: 'description'
            )

        when:
            this.validator.validate(model, OnCrudUpdate)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, [
                TestData.createError('id', 'must be null')
            ])
    }

}
