package dk.sunepoulsen.tel.testdata.module.integrator.testutils

class TestData {

    static ExpectedConstraintViolation createError(String property, String message) {
        return new ExpectedConstraintViolation(
            propertyPath: property,
            message: message
        )
    }

}
