package com.lroperod.web.rest;

import com.lroperod.repository.QuestionsRepository;
import com.lroperod.service.QuestionsService;
import com.lroperod.service.dto.QuestionsDTO;
import com.lroperod.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.lroperod.domain.Questions}.
 */
@RestController
@RequestMapping("/api")
public class QuestionsResource {

    private final Logger log = LoggerFactory.getLogger(QuestionsResource.class);

    private static final String ENTITY_NAME = "questions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionsService questionsService;

    private final QuestionsRepository questionsRepository;

    public QuestionsResource(QuestionsService questionsService, QuestionsRepository questionsRepository) {
        this.questionsService = questionsService;
        this.questionsRepository = questionsRepository;
    }

    /**
     * {@code POST  /questions} : Create a new questions.
     *
     * @param questionsDTO the questionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionsDTO, or with status {@code 400 (Bad Request)} if the questions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    public ResponseEntity<QuestionsDTO> createQuestions(@RequestBody QuestionsDTO questionsDTO) throws URISyntaxException {
        log.debug("REST request to save Questions : {}", questionsDTO);
        if (questionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new questions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionsDTO result = questionsService.save(questionsDTO);
        return ResponseEntity
            .created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questions/:id} : Updates an existing questions.
     *
     * @param id the id of the questionsDTO to save.
     * @param questionsDTO the questionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionsDTO,
     * or with status {@code 400 (Bad Request)} if the questionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questions/{id}")
    public ResponseEntity<QuestionsDTO> updateQuestions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionsDTO questionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Questions : {}, {}", id, questionsDTO);
        if (questionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionsDTO result = questionsService.save(questionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing questions, field will ignore if it is null
     *
     * @param id the id of the questionsDTO to save.
     * @param questionsDTO the questionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionsDTO,
     * or with status {@code 400 (Bad Request)} if the questionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionsDTO> partialUpdateQuestions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionsDTO questionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Questions partially : {}, {}", id, questionsDTO);
        if (questionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionsDTO> result = questionsService.partialUpdate(questionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionsDTO>> getAllQuestions(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("questionanswer-is-null".equals(filter)) {
            log.debug("REST request to get all Questionss where questionAnswer is null");
            return new ResponseEntity<>(questionsService.findAllWhereQuestionAnswerIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Questions");
        Page<QuestionsDTO> page = questionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * TODO: Create a getQuestionsByFormId
     */
    @GetMapping("/questions/form/{idForm}")
    public ResponseEntity<List<QuestionsDTO>> getQuestionsByFormId(Pageable pageable, @PathVariable(name = "idForm") Long formId) {
        // IRA AL SERVICIO A BUSCAR LAS PREGUNTAS POR ID
        log.debug("REST request to get a page of Questions from a form");
        Page<QuestionsDTO> page = questionsService.findAllByFormId(formId,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());

        //QuestionsDTO questiones = new QuestionsDTO();
    }

    /**
     * {@code GET  /questions/:id} : get the "id" questions.
     *
     * @param id the id of the questionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionsDTO> getQuestions(@PathVariable Long id) {
        log.debug("REST request to get Questions : {}", id);
        Optional<QuestionsDTO> questionsDTO = questionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionsDTO);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" questions.
     *
     * @param id the id of the questionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestions(@PathVariable Long id) {
        log.debug("REST request to delete Questions : {}", id);
        questionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
