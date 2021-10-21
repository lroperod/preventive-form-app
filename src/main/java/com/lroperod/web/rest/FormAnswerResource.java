package com.lroperod.web.rest;

import com.lroperod.repository.FormAnswerRepository;
import com.lroperod.service.FormAnswerService;
import com.lroperod.service.dto.FormAnswerDTO;
import com.lroperod.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lroperod.domain.FormAnswer}.
 */
@RestController
@RequestMapping("/api")
public class FormAnswerResource {

    private final Logger log = LoggerFactory.getLogger(FormAnswerResource.class);

    private static final String ENTITY_NAME = "formAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormAnswerService formAnswerService;

    private final FormAnswerRepository formAnswerRepository;

    public FormAnswerResource(FormAnswerService formAnswerService, FormAnswerRepository formAnswerRepository) {
        this.formAnswerService = formAnswerService;
        this.formAnswerRepository = formAnswerRepository;
    }

    /**
     * {@code POST  /form-answers} : Create a new formAnswer.
     *
     * @param formAnswerDTO the formAnswerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formAnswerDTO, or with status {@code 400 (Bad Request)} if the formAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-answers")
    public ResponseEntity<FormAnswerDTO> createFormAnswer(@RequestBody FormAnswerDTO formAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save FormAnswer : {}", formAnswerDTO);
        if (formAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new formAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormAnswerDTO result = formAnswerService.save(formAnswerDTO);
        return ResponseEntity
            .created(new URI("/api/form-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /form-answers/:id} : Updates an existing formAnswer.
     *
     * @param id the id of the formAnswerDTO to save.
     * @param formAnswerDTO the formAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the formAnswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-answers/{id}")
    public ResponseEntity<FormAnswerDTO> updateFormAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormAnswerDTO formAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormAnswer : {}, {}", id, formAnswerDTO);
        if (formAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormAnswerDTO result = formAnswerService.save(formAnswerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /form-answers/:id} : Partial updates given fields of an existing formAnswer, field will ignore if it is null
     *
     * @param id the id of the formAnswerDTO to save.
     * @param formAnswerDTO the formAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the formAnswerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formAnswerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/form-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormAnswerDTO> partialUpdateFormAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormAnswerDTO formAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormAnswer partially : {}, {}", id, formAnswerDTO);
        if (formAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormAnswerDTO> result = formAnswerService.partialUpdate(formAnswerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formAnswerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /form-answers} : get all the formAnswers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formAnswers in body.
     */
    @GetMapping("/form-answers")
    public ResponseEntity<List<FormAnswerDTO>> getAllFormAnswers(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of FormAnswers");
        Page<FormAnswerDTO> page;
        if (eagerload) {
            page = formAnswerService.findAllWithEagerRelationships(pageable);
        } else {
            page = formAnswerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /form-answers/:id} : get the "id" formAnswer.
     *
     * @param id the id of the formAnswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formAnswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-answers/{id}")
    public ResponseEntity<FormAnswerDTO> getFormAnswer(@PathVariable Long id) {
        log.debug("REST request to get FormAnswer : {}", id);
        Optional<FormAnswerDTO> formAnswerDTO = formAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formAnswerDTO);
    }

    /**
     * {@code DELETE  /form-answers/:id} : delete the "id" formAnswer.
     *
     * @param id the id of the formAnswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-answers/{id}")
    public ResponseEntity<Void> deleteFormAnswer(@PathVariable Long id) {
        log.debug("REST request to delete FormAnswer : {}", id);
        formAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
