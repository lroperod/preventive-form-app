package com.lroperod.service;

import com.lroperod.domain.Questions;
import com.lroperod.repository.QuestionsRepository;
import com.lroperod.service.dto.QuestionsDTO;
import com.lroperod.service.mapper.QuestionsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Questions}.
 */
@Service
@Transactional
public class QuestionsService {

    private final Logger log = LoggerFactory.getLogger(QuestionsService.class);

    private final QuestionsRepository questionsRepository;

    private final QuestionsMapper questionsMapper;

    public QuestionsService(QuestionsRepository questionsRepository, QuestionsMapper questionsMapper) {
        this.questionsRepository = questionsRepository;
        this.questionsMapper = questionsMapper;
    }

    /**
     * Save a questions.
     *
     * @param questionsDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionsDTO save(QuestionsDTO questionsDTO) {
        log.debug("Request to save Questions : {}", questionsDTO);
        Questions questions = questionsMapper.toEntity(questionsDTO);
        questions = questionsRepository.save(questions);
        return questionsMapper.toDto(questions);
    }

    /**
     * Partially update a questions.
     *
     * @param questionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionsDTO> partialUpdate(QuestionsDTO questionsDTO) {
        log.debug("Request to partially update Questions : {}", questionsDTO);

        return questionsRepository
            .findById(questionsDTO.getId())
            .map(existingQuestions -> {
                questionsMapper.partialUpdate(existingQuestions, questionsDTO);

                return existingQuestions;
            })
            .map(questionsRepository::save)
            .map(questionsMapper::toDto);
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionsRepository.findAll(pageable).map(questionsMapper::toDto);
    }

    /**
     *  Get all the questions where QuestionAnswer is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionsDTO> findAllWhereQuestionAnswerIsNull() {
        log.debug("Request to get all questions where QuestionAnswer is null");
        return StreamSupport
            .stream(questionsRepository.findAll().spliterator(), false)
            .filter(questions -> questions.getQuestionAnswer() == null)
            .map(questionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one questions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionsDTO> findOne(Long id) {
        log.debug("Request to get Questions : {}", id);
        return questionsRepository.findById(id).map(questionsMapper::toDto);
    }

    /**
     * Delete the questions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Questions : {}", id);
        questionsRepository.deleteById(id);
    }
}
