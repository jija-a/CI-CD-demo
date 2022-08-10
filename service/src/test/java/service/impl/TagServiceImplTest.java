package service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.config.ServiceConfig;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceConfig.class)
@ContextConfiguration(classes = {TagRepository.class})
@ActiveProfiles("prod")
class TagServiceImplTest {

    @Mock
    private TagRepository repository;

    private TagService service;

    @BeforeEach
    void setup() {
        service = new TagServiceImpl(repository);
    }

    @Test
    void whenFindById_ThenReturnEntity() {
        Long id = 1L;
        Tag expected = new Tag();
        when(repository.findById(id)).thenReturn(Optional.of(expected));
        assertEquals(expected, service.findById(id));
    }

    @Test
    void whenFindById_ThenThrowEntityNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void whenSave_ThenCreateAndReturn() {
        Long id = 1L;
        Tag expected = new Tag();
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
        when(repository.findById(id)).thenReturn(Optional.of(expected));
        when(repository.save(expected)).thenReturn(new Tag(1L, null, Collections.emptySet()));
        service.create(expected);
        assertEquals(expected, service.findById(id));
    }


    @Test
    void whenDelete_ThenRemoveAndThrowEntityNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(new Tag())).thenReturn(Optional.empty());
        when(repository.existsById(id)).thenReturn(true);
        assertNotNull(service.findById(id));
        service.delete(id);
        verify(repository).deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }
}
