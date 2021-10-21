package com.lroperod.service;

import com.lroperod.domain.QuestionAnswer;
import com.lroperod.repository.QuestionAnswerRepository;
import com.lroperod.service.dto.QuestionAnswerDTO;
import com.lroperod.service.mapper.QuestionAnswerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionAnswer}.
 */
@Service
@Transactional
public class QuestionAnswerService {

    private final Logger log = LoggerFactory.getLogger(QuestionAnswerService.class);

    private final QuestionAnswerRepository questionAnswerRepository;

    private final QuestionAnswerMapper questionAnswerMapper;

    public QuestionAnswerService(QuestionAnswerRepository questionAnswerRepository, QuestionAnswerMapper questionAnswerMapper) {
        this.questionAnswerRepository = questionAnswerRepository;
        this.questionAnswerMapper = questionAnswerMapper;
    }

    /**
     * Save a questionAnswer.
     *
     * @param questionAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionAnswerDTO save(QuestionAnswerDTO questionAnswerDTO) {
        log.debug("Request to save QuestionAnswer : {}", questionAnswerDTO);
        QuestionAnswer questionAnswer = questionAnswerMapper.toEntity(questionAnswerDTO);
        questionAnswer = questionAnswerRepository.save(questionAnswer);
        return questionAnswerMapper.toDto(questionAnswer);
    }

    /**
     * Partially update a questionAnswer.
     *
     * @param questionAnswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionAnswerDTO> partialUpdate(QuestionAnswerDTO questionAnswerDTO) {
        log.debug("Request to partially update QuestionAnswer : {}", questionAnswerDTO);

        return questionAnswerRepository
            .findById(questionAnswerDTO.getId())
            .map(existingQuestionAnswer -> {
                questionAnswerMapper.partialUpdate(existingQuestionAnswer, questionAnswerDTO);

                return existingQuestionAnswer;
            })
            .map(questionAnswerRepository::save)
            .map(questionAnswerMapper::toDto);
    }

    /**
     * Get all the questionAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionAnswers");
        return questionAnswerRepository.findAll(pageable).map(questionAnswerMapper::toDto);
    }

    /**
     * Get one questionAnswer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionAnswerDTO> findOne(Long id) {
        log.debug("Request to get QuestionAnswer : {}", id);
        return questionAnswerRepository.findById(id).map(questionAnswerMapper::toDto);
    }

    /**
     * Delete the questionAnswer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionAnswer : {}", id);
        questionAnswerRepository.deleteById(id);
    }
}
