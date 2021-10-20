package com.lroperod.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lroperod.IntegrationTest;
import com.lroperod.domain.QuestionAnswer;
import com.lroperod.repository.QuestionAnswerRepository;
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
 * Integration tests for the {@link QuestionAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionAnswerResourceIT {

    private static final String DEFAULT_ANSWER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionAnswerMockMvc;

    private QuestionAnswer questionAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAnswer createEntity(EntityManager em) {
        QuestionAnswer questionAnswer = new QuestionAnswer().answerCode(DEFAULT_ANSWER_CODE).answerText(DEFAULT_ANSWER_TEXT);
        return questionAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAnswer createUpdatedEntity(EntityManager em) {
        QuestionAnswer questionAnswer = new QuestionAnswer().answerCode(UPDATED_ANSWER_CODE).answerText(UPDATED_ANSWER_TEXT);
        return questionAnswer;
    }

    @BeforeEach
    public void initTest() {
        questionAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionAnswer() throws Exception {
        int databaseSizeBeforeCreate = questionAnswerRepository.findAll().size();
        // Create the QuestionAnswer
        restQuestionAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionAnswer))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getAnswerCode()).isEqualTo(DEFAULT_ANSWER_CODE);
        assertThat(testQuestionAnswer.getAnswerText()).isEqualTo(DEFAULT_ANSWER_TEXT);
    }

    @Test
    @Transactional
    void createQuestionAnswerWithExistingId() throws Exception {
        // Create the QuestionAnswer with an existing ID
        questionAnswer.setId(1L);

        int databaseSizeBeforeCreate = questionAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionAnswers() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList
        restQuestionAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].answerCode").value(hasItem(DEFAULT_ANSWER_CODE)))
            .andExpect(jsonPath("$.[*].answerText").value(hasItem(DEFAULT_ANSWER_TEXT)));
    }

    @Test
    @Transactional
    void getQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get the questionAnswer
        restQuestionAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, questionAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionAnswer.getId().intValue()))
            .andExpect(jsonPath("$.answerCode").value(DEFAULT_ANSWER_CODE))
            .andExpect(jsonPath("$.answerText").value(DEFAULT_ANSWER_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingQuestionAnswer() throws Exception {
        // Get the questionAnswer
        restQuestionAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Update the questionAnswer
        QuestionAnswer updatedQuestionAnswer = questionAnswerRepository.findById(questionAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionAnswer are not directly saved in db
        em.detach(updatedQuestionAnswer);
        updatedQuestionAnswer.answerCode(UPDATED_ANSWER_CODE).answerText(UPDATED_ANSWER_TEXT);

        restQuestionAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestionAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestionAnswer))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getAnswerCode()).isEqualTo(UPDATED_ANSWER_CODE);
        assertThat(testQuestionAnswer.getAnswerText()).isEqualTo(UPDATED_ANSWER_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionAnswer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionAnswerWithPatch() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Update the questionAnswer using partial update
        QuestionAnswer partialUpdatedQuestionAnswer = new QuestionAnswer();
        partialUpdatedQuestionAnswer.setId(questionAnswer.getId());

        partialUpdatedQuestionAnswer.answerText(UPDATED_ANSWER_TEXT);

        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionAnswer))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getAnswerCode()).isEqualTo(DEFAULT_ANSWER_CODE);
        assertThat(testQuestionAnswer.getAnswerText()).isEqualTo(UPDATED_ANSWER_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateQuestionAnswerWithPatch() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Update the questionAnswer using partial update
        QuestionAnswer partialUpdatedQuestionAnswer = new QuestionAnswer();
        partialUpdatedQuestionAnswer.setId(questionAnswer.getId());

        partialUpdatedQuestionAnswer.answerCode(UPDATED_ANSWER_CODE).answerText(UPDATED_ANSWER_TEXT);

        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionAnswer))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getAnswerCode()).isEqualTo(UPDATED_ANSWER_CODE);
        assertThat(testQuestionAnswer.getAnswerText()).isEqualTo(UPDATED_ANSWER_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionAnswer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeDelete = questionAnswerRepository.findAll().size();

        // Delete the questionAnswer
        restQuestionAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
