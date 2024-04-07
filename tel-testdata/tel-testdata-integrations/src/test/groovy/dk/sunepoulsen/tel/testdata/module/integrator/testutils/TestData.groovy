package dk.sunepoulsen.tel.testdata.module.integrator.testutils

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPoint
import dk.sunepoulsen.tes.rest.models.PaginationMetaData
import dk.sunepoulsen.tes.rest.models.PaginationModel

class TestData {

    static List<ExpectedConstraintViolation> createNullAndBankError(String propertyPath) {
        return [
            createError(propertyPath, 'must not be null'),
            createError(propertyPath, 'must not be blank')
        ]
    }

    static ExpectedConstraintViolation createError(String property, String message) {
        return new ExpectedConstraintViolation(
            propertyPath: property,
            message: message
        )
    }

    static PaginationModel<DataPoint> createEmptyDataPoints() {
        return new PaginationModel<DataPoint>(
            metadata: new PaginationMetaData(
                page: 1,
                totalPages: 1,
                totalItems: 0,
                size: 0
            ),
            results: []
        )
    }

}
