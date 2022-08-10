package service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.config.ServiceConfig;
import com.epam.esm.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ServiceConfig.class})
@ContextConfiguration(classes = {PasswordEncoder.class})
@ActiveProfiles("prod")
class CertificateServiceImplTest {

    @Mock
    private CertificateRepository repository;

    private CertificateServiceImpl service;

    @BeforeEach
    void setup() {
        service = new CertificateServiceImpl(repository);
    }

    @Test
    void whenFindById_ThenReturnCertificate() {
        Long id = 1L;
        Optional<Certificate> expected = Optional.of(new Certificate());
        when(repository.findById(id)).thenReturn(expected);
        assertEquals(expected.get(), service.findById(id));
    }

    @Test
    void whenFindById_ThenThrowNotFoundException() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void whenSave_ThenSaveInDB() {
        Certificate expected = new Certificate();
        when(repository.findById(1L)).thenReturn(Optional.of(expected));
        when(repository.save(expected)).thenReturn(expected);

        service.create(expected);
        assertEquals(expected, service.findById(1L));
    }

    @Test
    void whenUpdate_ThenUpdateAndSave() {
        Certificate certificate = new Certificate();
        certificate.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(certificate));
        when(repository.save(certificate)).thenReturn(certificate);
        Certificate actual = service.update(certificate, certificate.getId());
        assertEquals(actual, certificate);
    }
}
