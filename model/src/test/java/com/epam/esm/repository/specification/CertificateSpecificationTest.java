package com.epam.esm.repository.specification;

import com.epam.esm.domain.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CertificateSpecificationTest {

    @Test
    void whenCertificateHasTagsIsNull_ThenDoesNotThrowException() {
        List<String> names = null;
        assertDoesNotThrow(() -> CertificateSpecification.certificateHasTags(names));
    }

    @Test
    void whenCertificateNameEmpty_ThenDoesNotThrowException() {
        String name = "";
        assertDoesNotThrow(() -> CertificateSpecification.certificateNameLike(name));
    }

    @Test
    void whenCertificateDescriptionEmpty_ThenDoesNotThrowException() {
        String description = "";
        assertDoesNotThrow(() -> CertificateSpecification.certificateDescriptionLike(description));
    }

    @Test
    void whenCertificateStatusIsNull_ThenDoesNotThrowException() {
        Status status = null;
        assertDoesNotThrow(() -> CertificateSpecification.certificateStatusIs(status));
    }
}