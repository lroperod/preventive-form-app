package com.lroperod.service;

import com.lroperod.domain.FormAnswer;
import com.lroperod.repository.FormAnswerRepository;
import com.lroperod.service.dto.FormAnswerDTO;
import com.lroperod.service.mapper.FormAnswerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FormAnswer}.
 */
@Service
@Transactional
public class FormAnswerService {

    private final Logger log = LoggerFactory.getLogger(FormAnswerService.class);

    private final FormAnswerRepository formAnswerRepository;

    private final FormAnswerMapper formAnswerMapper;

    public FormAnswerService(FormAnswerRepository formAnswerRepository, FormAnswerMapper formAnswerMapper) {
        this.formAnswerRepository = formAnswerRepository;
        this.formAnswerMapper = formAnswerMapper;
    }

    /**
     * Save a formAnswer.
     *
     * @param formAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    public FormAnswerDTO save(FormAnswerDTO formAnswerDTO) {
        log.debug("Request to save FormAnswer : {}", formAnswerDTO);
        FormAnswer formAnswer = formAnswerMapper.toEntity(formAnswerDTO);
        formAnswer = formAnswerRepository.save(formAnswer);
        return formAnswerMapper.toDto(formAnswer);
    }

    /**
     * Partially update a formAnswer.
     *
     * @param formAnswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FormAnswerDTO> partialUpdate(FormAnswerDTO formAnswerDTO) {
        log.debug("Request to partially update FormAnswer : {}", formAnswerDTO);

        return formAnswerRepository
            .findById(formAnswerDTO.getId())
            .map(existingFormAnswer -> {
                formAnswerMapper.partialUpdate(existingFormAnswer, formAnswerDTO);

                return existingFormAnswer;
            })
            .map(formAnswerRepository::save)
            .map(formAnswerMapper::toDto);
    }

    /**
     * Get all the formAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormAnswers");
        return formAnswerRepository.findAll(pageable).map(formAnswerMapper::toDto);
    }

    /**
     * Get all the formAnswers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FormAnswerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return formAnswerRepository.findAllWithEagerRelationships(pageable).map(formAnswerMapper::toDto);
    }

    /**
     * Get one formAnswer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormAnswerDTO> findOne(Long id) {
        log.debug("Request to get FormAnswer : {}", id);
        return formAnswerRepository.findOneWithEagerRelationships(id).map(formAnswerMapper::toDto);
    }

    /**
     * Delete the formAnswer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FormAnswer : {}", id);
        formAnswerRepository.deleteById(id);
    }
}
