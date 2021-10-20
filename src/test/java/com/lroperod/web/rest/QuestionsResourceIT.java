package com.lroperod.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lroperod.IntegrationTest;
import com.lroperod.domain.Questions;
import com.lroperod.domain.enumeration.QuestionType;
import com.lroperod.repository.QuestionsRepository;
import com.lroperod.service.dto.QuestionsDTO;
import com.lroperod.service.mapper.QuestionsMapper;
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
 * Integration tests for the {@link QuestionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionsResourceIT {

    private static final String DEFAULT_QUESTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_TEXT = "BBBBBBBBBB";

    private static final QuestionType DEFAULT_QUESTION_TYPE = QuestionType.TEXT;
    private static final QuestionType UPDATED_QUESTION_TYPE = QuestionType.NUMBER;

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionsMockMvc;

    private Questions questions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createEntity(EntityManager em) {
        Questions questions = new Questions()
            .questionCode(DEFAULT_QUESTION_CODE)
            .questionText(DEFAULT_QUESTION_TEXT)
            .questionType(DEFAULT_QUESTION_TYPE);
        return questions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createUpdatedEntity(EntityManager em) {
        Questions questions = new Questions()
            .questionCode(UPDATED_QUESTION_CODE)
            .questionText(UPDATED_QUESTION_TEXT)
            .questionType(UPDATED_QUESTION_TYPE);
        return questions;
    }

    @BeforeEach
    public void initTest() {
        questions = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestions() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();
        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);
        restQuestionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate + 1);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestionCode()).isEqualTo(DEFAULT_QUESTION_CODE);
        assertThat(testQuestions.getQuestionText()).isEqualTo(DEFAULT_QUESTION_TEXT);
        assertThat(testQuestions.getQuestionType()).isEqualTo(DEFAULT_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void createQuestionsWithExistingId() throws Exception {
        // Create the Questions with an existing ID
        questions.setId(1L);
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionCode").value(hasItem(DEFAULT_QUESTION_CODE)))
            .andExpect(jsonPath("$.[*].questionText").value(hasItem(DEFAULT_QUESTION_TEXT)))
            .andExpect(jsonPath("$.[*].questionType").value(hasItem(DEFAULT_QUESTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get the questions
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL_ID, questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questions.getId().intValue()))
            .andExpect(jsonPath("$.questionCode").value(DEFAULT_QUESTION_CODE))
            .andExpect(jsonPath("$.questionText").value(DEFAULT_QUESTION_TEXT))
            .andExpect(jsonPath("$.questionType").value(DEFAULT_QUESTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuestions() throws Exception {
        // Get the questions
        restQuestionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions
        Questions updatedQuestions = questionsRepository.findById(questions.getId()).get();
        // Disconnect from session so that the updates on updatedQuestions are not directly saved in db
        em.detach(updatedQuestions);
        updatedQuestions.questionCode(UPDATED_QUESTION_CODE).questionText(UPDATED_QUESTION_TEXT).questionType(UPDATED_QUESTION_TYPE);
        QuestionsDTO questionsDTO = questionsMapper.toDto(updatedQuestions);

        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestionCode()).isEqualTo(UPDATED_QUESTION_CODE);
        assertThat(testQuestions.getQuestionText()).isEqualTo(UPDATED_QUESTION_TEXT);
        assertThat(testQuestions.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionsWithPatch() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions using partial update
        Questions partialUpdatedQuestions = new Questions();
        partialUpdatedQuestions.setId(questions.getId());

        partialUpdatedQuestions.questionCode(UPDATED_QUESTION_CODE).questionType(UPDATED_QUESTION_TYPE);

        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestions))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestionCode()).isEqualTo(UPDATED_QUESTION_CODE);
        assertThat(testQuestions.getQuestionText()).isEqualTo(DEFAULT_QUESTION_TEXT);
        assertThat(testQuestions.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateQuestionsWithPatch() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions using partial update
        Questions partialUpdatedQuestions = new Questions();
        partialUpdatedQuestions.setId(questions.getId());

        partialUpdatedQuestions.questionCode(UPDATED_QUESTION_CODE).questionText(UPDATED_QUESTION_TEXT).questionType(UPDATED_QUESTION_TYPE);

        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestions))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestionCode()).isEqualTo(UPDATED_QUESTION_CODE);
        assertThat(testQuestions.getQuestionText()).isEqualTo(UPDATED_QUESTION_TEXT);
        assertThat(testQuestions.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeDelete = questionsRepository.findAll().size();

        // Delete the questions
        restQuestionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, questions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
