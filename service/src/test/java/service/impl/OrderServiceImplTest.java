package service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.config.ServiceConfig;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceConfig.class)
@ContextConfiguration(classes = {OrderRepository.class, CertificateRepository.class})
@ActiveProfiles("prod")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CertificateRepository certificateRepository;

    private OrderService service;

    @BeforeEach
    void setup() {
        service = new OrderServiceImpl(orderRepository, certificateRepository);
    }

    @Test
    void whenFindById_ThenThrowEntityNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void whenMakeOrder_ThenCalculatePriceAndCreate() {
        Long id = 1L;
        Order order = new Order();
        Certificate certificate = new Certificate();
        certificate.setId(id);
        certificate.setPrice(new BigDecimal("0.00"));
        order.setCertificates(new HashSet<>(singletonList(certificate)));
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));

        when(orderRepository.save(order)).thenReturn(order);
        when(certificateRepository.findById(id)).thenReturn(Optional.of(certificate));
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        service.create(order);
        Order expected = new Order();
        expected.setCertificates(new HashSet<>(singletonList(certificate)));
        expected.setCost(certificate.getPrice());
        assertEquals(expected.getCost(), service.findById(id).getCost());
    }

    @Test
    void whenDelete_ThenDoNothing() {
        Long id = 1L;
        when(orderRepository.existsById(id)).thenReturn(true);
        when(orderRepository.findById(id)).thenReturn(Optional.of(new Order()));
        assertNotNull(service.findById(id));
        service.deleteById(id);
        verify(orderRepository).deleteById(id);
    }
}
