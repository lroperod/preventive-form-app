package com.lroperod.service;

import com.lroperod.domain.QuestionOption;
import com.lroperod.repository.QuestionOptionRepository;
import com.lroperod.service.dto.QuestionOptionDTO;
import com.lroperod.service.mapper.QuestionOptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionOption}.
 */
@Service
@Transactional
public class QuestionOptionService {

    private final Logger log = LoggerFactory.getLogger(QuestionOptionService.class);

    private final QuestionOptionRepository questionOptionRepository;

    private final QuestionOptionMapper questionOptionMapper;

    public QuestionOptionService(QuestionOptionRepository questionOptionRepository, QuestionOptionMapper questionOptionMapper) {
        this.questionOptionRepository = questionOptionRepository;
        this.questionOptionMapper = questionOptionMapper;
    }

    /**
     * Save a questionOption.
     *
     * @param questionOptionDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionOptionDTO save(QuestionOptionDTO questionOptionDTO) {
        log.debug("Request to save QuestionOption : {}", questionOptionDTO);
        QuestionOption questionOption = questionOptionMapper.toEntity(questionOptionDTO);
        questionOption = questionOptionRepository.save(questionOption);
        return questionOptionMapper.toDto(questionOption);
    }

    /**
     * Partially update a questionOption.
     *
     * @param questionOptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionOptionDTO> partialUpdate(QuestionOptionDTO questionOptionDTO) {
        log.debug("Request to partially update QuestionOption : {}", questionOptionDTO);

        return questionOptionRepository
            .findById(questionOptionDTO.getId())
            .map(existingQuestionOption -> {
                questionOptionMapper.partialUpdate(existingQuestionOption, questionOptionDTO);

                return existingQuestionOption;
            })
            .map(questionOptionRepository::save)
            .map(questionOptionMapper::toDto);
    }

    /**
     * Get all the questionOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionOptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionOptions");
        return questionOptionRepository.findAll(pageable).map(questionOptionMapper::toDto);
    }

    /**
     * Get one questionOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionOptionDTO> findOne(Long id) {
        log.debug("Request to get QuestionOption : {}", id);
        return questionOptionRepository.findById(id).map(questionOptionMapper::toDto);
    }

    /**
     * Delete the questionOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionOption : {}", id);
        questionOptionRepository.deleteById(id);
    }
}
