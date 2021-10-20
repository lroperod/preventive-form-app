package com.lroperod.web.rest;

import com.lroperod.domain.QuestionOption;
import com.lroperod.repository.QuestionOptionRepository;
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
 * REST controller for managing {@link com.lroperod.domain.QuestionOption}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuestionOptionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionOptionResource.class);

    private static final String ENTITY_NAME = "questionOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionOptionRepository questionOptionRepository;

    public QuestionOptionResource(QuestionOptionRepository questionOptionRepository) {
        this.questionOptionRepository = questionOptionRepository;
    }

    /**
     * {@code POST  /question-options} : Create a new questionOption.
     *
     * @param questionOption the questionOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionOption, or with status {@code 400 (Bad Request)} if the questionOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/question-options")
    public ResponseEntity<QuestionOption> createQuestionOption(@RequestBody QuestionOption questionOption) throws URISyntaxException {
        log.debug("REST request to save QuestionOption : {}", questionOption);
        if (questionOption.getId() != null) {
            throw new BadRequestAlertException("A new questionOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionOption result = questionOptionRepository.save(questionOption);
        return ResponseEntity
            .created(new URI("/api/question-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /question-options/:id} : Updates an existing questionOption.
     *
     * @param id the id of the questionOption to save.
     * @param questionOption the questionOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionOption,
     * or with status {@code 400 (Bad Request)} if the questionOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/question-options/{id}")
    public ResponseEntity<QuestionOption> updateQuestionOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionOption questionOption
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionOption : {}, {}", id, questionOption);
        if (questionOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionOption result = questionOptionRepository.save(questionOption);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionOption.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /question-options/:id} : Partial updates given fields of an existing questionOption, field will ignore if it is null
     *
     * @param id the id of the questionOption to save.
     * @param questionOption the questionOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionOption,
     * or with status {@code 400 (Bad Request)} if the questionOption is not valid,
     * or with status {@code 404 (Not Found)} if the questionOption is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/question-options/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionOption> partialUpdateQuestionOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionOption questionOption
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionOption partially : {}, {}", id, questionOption);
        if (questionOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionOption> result = questionOptionRepository
            .findById(questionOption.getId())
            .map(existingQuestionOption -> {
                if (questionOption.getQuestionOptionsCode() != null) {
                    existingQuestionOption.setQuestionOptionsCode(questionOption.getQuestionOptionsCode());
                }
                if (questionOption.getQuestionOptionsText() != null) {
                    existingQuestionOption.setQuestionOptionsText(questionOption.getQuestionOptionsText());
                }

                return existingQuestionOption;
            })
            .map(questionOptionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionOption.getId().toString())
        );
    }

    /**
     * {@code GET  /question-options} : get all the questionOptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionOptions in body.
     */
    @GetMapping("/question-options")
    public List<QuestionOption> getAllQuestionOptions() {
        log.debug("REST request to get all QuestionOptions");
        return questionOptionRepository.findAll();
    }

    /**
     * {@code GET  /question-options/:id} : get the "id" questionOption.
     *
     * @param id the id of the questionOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/question-options/{id}")
    public ResponseEntity<QuestionOption> getQuestionOption(@PathVariable Long id) {
        log.debug("REST request to get QuestionOption : {}", id);
        Optional<QuestionOption> questionOption = questionOptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(questionOption);
    }

    /**
     * {@code DELETE  /question-options/:id} : delete the "id" questionOption.
     *
     * @param id the id of the questionOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/question-options/{id}")
    public ResponseEntity<Void> deleteQuestionOption(@PathVariable Long id) {
        log.debug("REST request to delete QuestionOption : {}", id);
        questionOptionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
