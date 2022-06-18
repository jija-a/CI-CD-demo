package com.epam.esm.controller.rest;

import com.epam.esm.controller.util.SortTypeMapConverter;
import com.epam.esm.domain.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.model.CertificateModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateRestController {

    private final CertificateService service;

    private final ModelMapper mapper;

    @GetMapping
    @PermitAll
    public CollectionModel<CertificateModel> findAll(@RequestParam(required = false) String query,
                                                     @RequestParam(required = false, value = "tag") List<String> tagNames,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(defaultValue = "-id") String sort,
                                                     HttpServletRequest req) {

        Pageable pagingSort = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));

        Page<CertificateModel> certificates;
        if (!req.isUserInRole("ROLE_ADMIN")) {
            certificates = service.findAllActiveCertificates(query, tagNames, pagingSort)
                    .map(this::mapToCertificateModel);
        } else {
            certificates = service.findAll(query, tagNames, pagingSort)
                    .map(this::mapToCertificateModel);
        }

        certificates.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findAll(query, tagNames, page, size, sort, req)).withSelfRel().expand();
        return CollectionModel.of(certificates, link);
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<CertificateModel> findById(@PathVariable @Positive Long id,
                                                     HttpServletRequest req) {

        Certificate certificate;
        if (!req.isUserInRole("ROLE_ADMIN")) {
            certificate = service.findActiveCertificateById(id);
        } else {
            certificate = service.findById(id);
        }

        CertificateModel certificateModel = mapToCertificateModel(certificate);
        addSelfRelLink(certificateModel);
        return ResponseEntity.ok(certificateModel);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EntityModel<CertificateModel> create(@RequestBody @Valid CertificateModel certificateModel) {
        Certificate certificate = mapToCertificate(certificateModel);
        service.create(certificate);
        return EntityModel.of(mapToCertificateModel(certificate));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EntityModel<CertificateModel> update(@RequestBody @Valid CertificateModel certificateModel,
                                                @PathVariable @Positive Long id) {
        Certificate certificate = mapToCertificate(certificateModel);
        certificate = service.update(certificate, id);
        return EntityModel.of(mapToCertificateModel(certificate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
    }

    private void addSelfRelLink(CertificateModel certificateModel) {
        Link link = linkTo(this.getClass()).slash(certificateModel.getId()).withSelfRel();
        certificateModel.add(link);
    }

    private Certificate mapToCertificate(CertificateModel certificateModel) {
        return mapper.map(certificateModel, Certificate.class);
    }

    private CertificateModel mapToCertificateModel(Certificate certificate) {
        return mapper.map(certificate, CertificateModel.class);
    }
}
