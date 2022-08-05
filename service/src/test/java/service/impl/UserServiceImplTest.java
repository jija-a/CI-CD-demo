package service.impl;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.config.ServiceConfig;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceConfig.class)
@ContextConfiguration(classes = {UserRepository.class})
@ActiveProfiles("prod")
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    private UserService service;

    @BeforeEach
    void setup() {
        service = new UserServiceImpl(repository);
    }

    @Test
    void whenFindById_ThenReturnEntity() {
        Long id = 1L;
        User expected = new User();
        when(repository.findById(id)).thenReturn(Optional.of(expected));
        assertEquals(expected, service.findById(id));
    }

    @Test
    void whenFindById_ThenThrowEntityNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }
}
