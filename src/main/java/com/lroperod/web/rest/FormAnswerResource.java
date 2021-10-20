package com.lroperod.web.rest;

import com.lroperod.domain.FormAnswer;
import com.lroperod.repository.FormAnswerRepository;
import com.lroperod.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lroperod.domain.FormAnswer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FormAnswerResource {

    private final Logger log = LoggerFactory.getLogger(FormAnswerResource.class);

    private static final String ENTITY_NAME = "formAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormAnswerRepository formAnswerRepository;

    public FormAnswerResource(FormAnswerRepository formAnswerRepository) {
        this.formAnswerRepository = formAnswerRepository;
    }

    /**
     * {@code POST  /form-answers} : Create a new formAnswer.
     *
     * @param formAnswer the formAnswer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formAnswer, or with status {@code 400 (Bad Request)} if the formAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-answers")
    public ResponseEntity<FormAnswer> createFormAnswer(@RequestBody FormAnswer formAnswer) throws URISyntaxException {
        log.debug("REST request to save FormAnswer : {}", formAnswer);
        if (formAnswer.getId() != null) {
            throw new BadRequestAlertException("A new formAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormAnswer result = formAnswerRepository.save(formAnswer);
        return ResponseEntity
            .created(new URI("/api/form-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /form-answers/:id} : Updates an existing formAnswer.
     *
     * @param id the id of the formAnswer to save.
     * @param formAnswer the formAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formAnswer,
     * or with status {@code 400 (Bad Request)} if the formAnswer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-answers/{id}")
    public ResponseEntity<FormAnswer> updateFormAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormAnswer formAnswer
    ) throws URISyntaxException {
        log.debug("REST request to update FormAnswer : {}, {}", id, formAnswer);
        if (formAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormAnswer result = formAnswerRepository.save(formAnswer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formAnswer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /form-answers/:id} : Partial updates given fields of an existing formAnswer, field will ignore if it is null
     *
     * @param id the id of the formAnswer to save.
     * @param formAnswer the formAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formAnswer,
     * or with status {@code 400 (Bad Request)} if the formAnswer is not valid,
     * or with status {@code 404 (Not Found)} if the formAnswer is not found,
     * or with status {@code 500 (Internal Server Error)} if the formAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/form-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormAnswer> partialUpdateFormAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormAnswer formAnswer
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormAnswer partially : {}, {}", id, formAnswer);
        if (formAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormAnswer> result = formAnswerRepository
            .findById(formAnswer.getId())
            .map(existingFormAnswer -> {
                if (formAnswer.getFormAnswerLocalDate() != null) {
                    existingFormAnswer.setFormAnswerLocalDate(formAnswer.getFormAnswerLocalDate());
                }

                return existingFormAnswer;
            })
            .map(formAnswerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formAnswer.getId().toString())
        );
    }

    /**
     * {@code GET  /form-answers} : get all the formAnswers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formAnswers in body.
     */
    @GetMapping("/form-answers")
    public List<FormAnswer> getAllFormAnswers() {
        log.debug("REST request to get all FormAnswers");
        return formAnswerRepository.findAll();
    }

    /**
     * {@code GET  /form-answers/:id} : get the "id" formAnswer.
     *
     * @param id the id of the formAnswer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formAnswer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-answers/{id}")
    public ResponseEntity<FormAnswer> getFormAnswer(@PathVariable Long id) {
        log.debug("REST request to get FormAnswer : {}", id);
        Optional<FormAnswer> formAnswer = formAnswerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(formAnswer);
    }

    /**
     * {@code DELETE  /form-answers/:id} : delete the "id" formAnswer.
     *
     * @param id the id of the formAnswer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-answers/{id}")
    public ResponseEntity<Void> deleteFormAnswer(@PathVariable Long id) {
        log.debug("REST request to delete FormAnswer : {}", id);
        formAnswerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
