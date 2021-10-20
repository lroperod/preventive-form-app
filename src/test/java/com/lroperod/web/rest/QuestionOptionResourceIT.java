package com.lroperod.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lroperod.IntegrationTest;
import com.lroperod.domain.QuestionOption;
import com.lroperod.repository.QuestionOptionRepository;
import com.lroperod.service.dto.QuestionOptionDTO;
import com.lroperod.service.mapper.QuestionOptionMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionOptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionOptionResourceIT {

    private static final String DEFAULT_QUESTION_OPTIONS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_OPTIONS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_OPTIONS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_OPTIONS_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private QuestionOptionMapper questionOptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionOptionMockMvc;

    private QuestionOption questionOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionOption createEntity(EntityManager em) {
        QuestionOption questionOption = new QuestionOption()
            .questionOptionsCode(DEFAULT_QUESTION_OPTIONS_CODE)
            .questionOptionsText(DEFAULT_QUESTION_OPTIONS_TEXT);
        return questionOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionOption createUpdatedEntity(EntityManager em) {
        QuestionOption questionOption = new QuestionOption()
            .questionOptionsCode(UPDATED_QUESTION_OPTIONS_CODE)
            .questionOptionsText(UPDATED_QUESTION_OPTIONS_TEXT);
        return questionOption;
    }

    @BeforeEach
    public void initTest() {
        questionOption = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionOption() throws Exception {
        int databaseSizeBeforeCreate = questionOptionRepository.findAll().size();
        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);
        restQuestionOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionOption testQuestionOption = questionOptionList.get(questionOptionList.size() - 1);
        assertThat(testQuestionOption.getQuestionOptionsCode()).isEqualTo(DEFAULT_QUESTION_OPTIONS_CODE);
        assertThat(testQuestionOption.getQuestionOptionsText()).isEqualTo(DEFAULT_QUESTION_OPTIONS_TEXT);
    }

    @Test
    @Transactional
    void createQuestionOptionWithExistingId() throws Exception {
        // Create the QuestionOption with an existing ID
        questionOption.setId(1L);
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        int databaseSizeBeforeCreate = questionOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionOptions() throws Exception {
        // Initialize the database
        questionOptionRepository.saveAndFlush(questionOption);

        // Get all the questionOptionList
        restQuestionOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionOptionsCode").value(hasItem(DEFAULT_QUESTION_OPTIONS_CODE)))
            .andExpect(jsonPath("$.[*].questionOptionsText").value(hasItem(DEFAULT_QUESTION_OPTIONS_TEXT)));
    }

    @Test
    @Transactional
    void getQuestionOption() throws Exception {
        // Initialize the database
        questionOptionRepository.saveAndFlush(questionOption);

        // Get the questionOption
        restQuestionOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, questionOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionOption.getId().intValue()))
            .andExpect(jsonPath("$.questionOptionsCode").value(DEFAULT_QUESTION_OPTIONS_CODE))
            .andExpect(jsonPath("$.questionOptionsText").value(DEFAULT_QUESTION_OPTIONS_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingQuestionOption() throws Exception {
        // Get the questionOption
        restQuestionOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionOption() throws Exception {
        // Initialize the database
        questionOptionRepository.saveAndFlush(questionOption);

        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();

        // Update the questionOption
        QuestionOption updatedQuestionOption = questionOptionRepository.findById(questionOption.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionOption are not directly saved in db
        em.detach(updatedQuestionOption);
        updatedQuestionOption.questionOptionsCode(UPDATED_QUESTION_OPTIONS_CODE).questionOptionsText(UPDATED_QUESTION_OPTIONS_TEXT);
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(updatedQuestionOption);

        restQuestionOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
        QuestionOption testQuestionOption = questionOptionList.get(questionOptionList.size() - 1);
        assertThat(testQuestionOption.getQuestionOptionsCode()).isEqualTo(UPDATED_QUESTION_OPTIONS_CODE);
        assertThat(testQuestionOption.getQuestionOptionsText()).isEqualTo(UPDATED_QUESTION_OPTIONS_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingQuestionOption() throws Exception {
        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();
        questionOption.setId(count.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionOption() throws Exception {
        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();
        questionOption.setId(count.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionOption() throws Exception {
        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();
        questionOption.setId(count.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionOptionWithPatch() throws Exception {
        // Initialize the database
        questionOptionRepository.saveAndFlush(questionOption);

        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();

        // Update the questionOption using partial update
        QuestionOption partialUpdatedQuestionOption = new QuestionOption();
        partialUpdatedQuestionOption.setId(questionOption.getId());

        partialUpdatedQuestionOption.questionOptionsCode(UPDATED_QUESTION_OPTIONS_CODE).questionOptionsText(UPDATED_QUESTION_OPTIONS_TEXT);

        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionOption))
            )
            .andExpect(status().isOk());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
        QuestionOption testQuestionOption = questionOptionList.get(questionOptionList.size() - 1);
        assertThat(testQuestionOption.getQuestionOptionsCode()).isEqualTo(UPDATED_QUESTION_OPTIONS_CODE);
        assertThat(testQuestionOption.getQuestionOptionsText()).isEqualTo(UPDATED_QUESTION_OPTIONS_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateQuestionOptionWithPatch() throws Exception {
        // Initialize the database
        questionOptionRepository.saveAndFlush(questionOption);

        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();

        // Update the questionOption using partial update
        QuestionOption partialUpdatedQuestionOption = new QuestionOption();
        partialUpdatedQuestionOption.setId(questionOption.getId());

        partialUpdatedQuestionOption.questionOptionsCode(UPDATED_QUESTION_OPTIONS_CODE).questionOptionsText(UPDATED_QUESTION_OPTIONS_TEXT);

        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionOption))
            )
            .andExpect(status().isOk());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
        QuestionOption testQuestionOption = questionOptionList.get(questionOptionList.size() - 1);
        assertThat(testQuestionOption.getQuestionOptionsCode()).isEqualTo(UPDATED_QUESTION_OPTIONS_CODE);
        assertThat(testQuestionOption.getQuestionOptionsText()).isEqualTo(UPDATED_QUESTION_OPTIONS_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionOption() throws Exception {
        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();
        questionOption.setId(count.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionOptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionOption() throws Exception {
        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();
        questionOption.setId(count.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionOption() throws Exception {
        int databaseSizeBeforeUpdate = questionOptionRepository.findAll().size();
        questionOption.setId(count.incrementAndGet());

        // Create the QuestionOption
        QuestionOptionDTO questionOptionDTO = questionOptionMapper.toDto(questionOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionOptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionOptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionOption in the database
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionOption() throws Exception {
        // Initialize the database
        questionOptionRepository.saveAndFlush(questionOption);

        int databaseSizeBeforeDelete = questionOptionRepository.findAll().size();

        // Delete the questionOption
        restQuestionOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionOption> questionOptionList = questionOptionRepository.findAll();
        assertThat(questionOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
