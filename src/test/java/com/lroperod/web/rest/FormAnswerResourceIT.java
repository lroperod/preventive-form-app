package com.lroperod.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lroperod.IntegrationTest;
import com.lroperod.domain.FormAnswer;
import com.lroperod.repository.FormAnswerRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FormAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormAnswerResourceIT {

    private static final LocalDate DEFAULT_FORM_ANSWER_LOCAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FORM_ANSWER_LOCAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/form-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormAnswerRepository formAnswerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormAnswerMockMvc;

    private FormAnswer formAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormAnswer createEntity(EntityManager em) {
        FormAnswer formAnswer = new FormAnswer().formAnswerLocalDate(DEFAULT_FORM_ANSWER_LOCAL_DATE);
        return formAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormAnswer createUpdatedEntity(EntityManager em) {
        FormAnswer formAnswer = new FormAnswer().formAnswerLocalDate(UPDATED_FORM_ANSWER_LOCAL_DATE);
        return formAnswer;
    }

    @BeforeEach
    public void initTest() {
        formAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createFormAnswer() throws Exception {
        int databaseSizeBeforeCreate = formAnswerRepository.findAll().size();
        // Create the FormAnswer
        restFormAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formAnswer)))
            .andExpect(status().isCreated());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        FormAnswer testFormAnswer = formAnswerList.get(formAnswerList.size() - 1);
        assertThat(testFormAnswer.getFormAnswerLocalDate()).isEqualTo(DEFAULT_FORM_ANSWER_LOCAL_DATE);
    }

    @Test
    @Transactional
    void createFormAnswerWithExistingId() throws Exception {
        // Create the FormAnswer with an existing ID
        formAnswer.setId(1L);

        int databaseSizeBeforeCreate = formAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormAnswers() throws Exception {
        // Initialize the database
        formAnswerRepository.saveAndFlush(formAnswer);

        // Get all the formAnswerList
        restFormAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].formAnswerLocalDate").value(hasItem(DEFAULT_FORM_ANSWER_LOCAL_DATE.toString())));
    }

    @Test
    @Transactional
    void getFormAnswer() throws Exception {
        // Initialize the database
        formAnswerRepository.saveAndFlush(formAnswer);

        // Get the formAnswer
        restFormAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, formAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formAnswer.getId().intValue()))
            .andExpect(jsonPath("$.formAnswerLocalDate").value(DEFAULT_FORM_ANSWER_LOCAL_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFormAnswer() throws Exception {
        // Get the formAnswer
        restFormAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormAnswer() throws Exception {
        // Initialize the database
        formAnswerRepository.saveAndFlush(formAnswer);

        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();

        // Update the formAnswer
        FormAnswer updatedFormAnswer = formAnswerRepository.findById(formAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedFormAnswer are not directly saved in db
        em.detach(updatedFormAnswer);
        updatedFormAnswer.formAnswerLocalDate(UPDATED_FORM_ANSWER_LOCAL_DATE);

        restFormAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormAnswer))
            )
            .andExpect(status().isOk());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
        FormAnswer testFormAnswer = formAnswerList.get(formAnswerList.size() - 1);
        assertThat(testFormAnswer.getFormAnswerLocalDate()).isEqualTo(UPDATED_FORM_ANSWER_LOCAL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFormAnswer() throws Exception {
        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();
        formAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormAnswer() throws Exception {
        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();
        formAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormAnswer() throws Exception {
        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();
        formAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formAnswer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormAnswerWithPatch() throws Exception {
        // Initialize the database
        formAnswerRepository.saveAndFlush(formAnswer);

        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();

        // Update the formAnswer using partial update
        FormAnswer partialUpdatedFormAnswer = new FormAnswer();
        partialUpdatedFormAnswer.setId(formAnswer.getId());

        partialUpdatedFormAnswer.formAnswerLocalDate(UPDATED_FORM_ANSWER_LOCAL_DATE);

        restFormAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormAnswer))
            )
            .andExpect(status().isOk());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
        FormAnswer testFormAnswer = formAnswerList.get(formAnswerList.size() - 1);
        assertThat(testFormAnswer.getFormAnswerLocalDate()).isEqualTo(UPDATED_FORM_ANSWER_LOCAL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFormAnswerWithPatch() throws Exception {
        // Initialize the database
        formAnswerRepository.saveAndFlush(formAnswer);

        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();

        // Update the formAnswer using partial update
        FormAnswer partialUpdatedFormAnswer = new FormAnswer();
        partialUpdatedFormAnswer.setId(formAnswer.getId());

        partialUpdatedFormAnswer.formAnswerLocalDate(UPDATED_FORM_ANSWER_LOCAL_DATE);

        restFormAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormAnswer))
            )
            .andExpect(status().isOk());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
        FormAnswer testFormAnswer = formAnswerList.get(formAnswerList.size() - 1);
        assertThat(testFormAnswer.getFormAnswerLocalDate()).isEqualTo(UPDATED_FORM_ANSWER_LOCAL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFormAnswer() throws Exception {
        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();
        formAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormAnswer() throws Exception {
        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();
        formAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormAnswer() throws Exception {
        int databaseSizeBeforeUpdate = formAnswerRepository.findAll().size();
        formAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formAnswer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormAnswer in the database
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormAnswer() throws Exception {
        // Initialize the database
        formAnswerRepository.saveAndFlush(formAnswer);

        int databaseSizeBeforeDelete = formAnswerRepository.findAll().size();

        // Delete the formAnswer
        restFormAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, formAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormAnswer> formAnswerList = formAnswerRepository.findAll();
        assertThat(formAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
