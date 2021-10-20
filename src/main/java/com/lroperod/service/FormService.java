package com.lroperod.service;

import com.lroperod.domain.Form;
import com.lroperod.repository.FormRepository;
import com.lroperod.service.dto.FormDTO;
import com.lroperod.service.mapper.FormMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Form}.
 */
@Service
@Transactional
public class FormService {

    private final Logger log = LoggerFactory.getLogger(FormService.class);

    private final FormRepository formRepository;

    private final FormMapper formMapper;

    public FormService(FormRepository formRepository, FormMapper formMapper) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
    }

    /**
     * Save a form.
     *
     * @param formDTO the entity to save.
     * @return the persisted entity.
     */
    public FormDTO save(FormDTO formDTO) {
        log.debug("Request to save Form : {}", formDTO);
        Form form = formMapper.toEntity(formDTO);
        form = formRepository.save(form);
        return formMapper.toDto(form);
    }

    /**
     * Partially update a form.
     *
     * @param formDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FormDTO> partialUpdate(FormDTO formDTO) {
        log.debug("Request to partially update Form : {}", formDTO);

        return formRepository
            .findById(formDTO.getId())
            .map(existingForm -> {
                formMapper.partialUpdate(existingForm, formDTO);

                return existingForm;
            })
            .map(formRepository::save)
            .map(formMapper::toDto);
    }

    /**
     * Get all the forms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Forms");
        return formRepository.findAll(pageable).map(formMapper::toDto);
    }

    /**
     * Get one form by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormDTO> findOne(Long id) {
        log.debug("Request to get Form : {}", id);
        return formRepository.findById(id).map(formMapper::toDto);
    }

    /**
     * Delete the form by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Form : {}", id);
        formRepository.deleteById(id);
    }
}
