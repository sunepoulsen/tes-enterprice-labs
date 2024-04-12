package dk.sunepoulsen.tel.testdata.module.integrator.testutils

import jakarta.validation.ConstraintViolation

class ConstraintViolationAssertions {

    private static void verifyViolations(Set<ConstraintViolation<?>> violations, List<ExpectedConstraintViolation> expected) {
        List<ConstraintViolation<?>> actual = violations.sort { a, b ->
            a.propertyPath.toString() <=> b.propertyPath.toString() ?:
                a.message <=> b.message
        }
        expected = expected.sort { a, b ->
            a.propertyPath <=> b.propertyPath ?:
                a.message <=> b.message
        }

        assert actual.size() == expected.size()
        for (int i = 0; i < expected.size(); i++) {
            assert actual[i].propertyPath.toString() == expected[i].propertyPath
            assert actual[i].message == expected[i].message
        }
    }

}
