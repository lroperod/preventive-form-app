package com.lroperod.web.rest;

import com.lroperod.repository.QuestionAnswerRepository;
import com.lroperod.service.QuestionAnswerService;
import com.lroperod.service.dto.QuestionAnswerDTO;
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
 * REST controller for managing {@link com.lroperod.domain.QuestionAnswer}.
 */
@RestController
@RequestMapping("/api")
public class QuestionAnswerResource {

    private final Logger log = LoggerFactory.getLogger(QuestionAnswerResource.class);

    private static final String ENTITY_NAME = "questionAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionAnswerService questionAnswerService;

    private final QuestionAnswerRepository questionAnswerRepository;

    public QuestionAnswerResource(QuestionAnswerService questionAnswerService, QuestionAnswerRepository questionAnswerRepository) {
        this.questionAnswerService = questionAnswerService;
        this.questionAnswerRepository = questionAnswerRepository;
    }

    /**
     * {@code POST  /question-answers} : Create a new questionAnswer.
     *
     * @param questionAnswerDTO the questionAnswerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionAnswerDTO, or with status {@code 400 (Bad Request)} if the questionAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/question-answers")
    public ResponseEntity<QuestionAnswerDTO> createQuestionAnswer(@RequestBody QuestionAnswerDTO questionAnswerDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionAnswer : {}", questionAnswerDTO);
        if (questionAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionAnswerDTO result = questionAnswerService.save(questionAnswerDTO);
        return ResponseEntity
            .created(new URI("/api/question-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /question-answers/:id} : Updates an existing questionAnswer.
     *
     * @param id the id of the questionAnswerDTO to save.
     * @param questionAnswerDTO the questionAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the questionAnswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/question-answers/{id}")
    public ResponseEntity<QuestionAnswerDTO> updateQuestionAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionAnswerDTO questionAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionAnswer : {}, {}", id, questionAnswerDTO);
        if (questionAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionAnswerDTO result = questionAnswerService.save(questionAnswerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /question-answers/:id} : Partial updates given fields of an existing questionAnswer, field will ignore if it is null
     *
     * @param id the id of the questionAnswerDTO to save.
     * @param questionAnswerDTO the questionAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the questionAnswerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionAnswerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/question-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionAnswerDTO> partialUpdateQuestionAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionAnswerDTO questionAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionAnswer partially : {}, {}", id, questionAnswerDTO);
        if (questionAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionAnswerDTO> result = questionAnswerService.partialUpdate(questionAnswerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionAnswerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-answers} : get all the questionAnswers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionAnswers in body.
     */
    @GetMapping("/question-answers")
    public ResponseEntity<List<QuestionAnswerDTO>> getAllQuestionAnswers(Pageable pageable) {
        log.debug("REST request to get a page of QuestionAnswers");
        Page<QuestionAnswerDTO> page = questionAnswerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-answers/:id} : get the "id" questionAnswer.
     *
     * @param id the id of the questionAnswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionAnswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/question-answers/{id}")
    public ResponseEntity<QuestionAnswerDTO> getQuestionAnswer(@PathVariable Long id) {
        log.debug("REST request to get QuestionAnswer : {}", id);
        Optional<QuestionAnswerDTO> questionAnswerDTO = questionAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionAnswerDTO);
    }

    /**
     * {@code DELETE  /question-answers/:id} : delete the "id" questionAnswer.
     *
     * @param id the id of the questionAnswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/question-answers/{id}")
    public ResponseEntity<Void> deleteQuestionAnswer(@PathVariable Long id) {
        log.debug("REST request to delete QuestionAnswer : {}", id);
        questionAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
