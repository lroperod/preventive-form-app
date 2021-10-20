package com.lroperod.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lroperod.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionOption.class);
        QuestionOption questionOption1 = new QuestionOption();
        questionOption1.setId(1L);
        QuestionOption questionOption2 = new QuestionOption();
        questionOption2.setId(questionOption1.getId());
        assertThat(questionOption1).isEqualTo(questionOption2);
        questionOption2.setId(2L);
        assertThat(questionOption1).isNotEqualTo(questionOption2);
        questionOption1.setId(null);
        assertThat(questionOption1).isNotEqualTo(questionOption2);
    }
}
