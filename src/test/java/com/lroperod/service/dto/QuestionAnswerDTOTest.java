package com.lroperod.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lroperod.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionAnswerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionAnswerDTO.class);
        QuestionAnswerDTO questionAnswerDTO1 = new QuestionAnswerDTO();
        questionAnswerDTO1.setId(1L);
        QuestionAnswerDTO questionAnswerDTO2 = new QuestionAnswerDTO();
        assertThat(questionAnswerDTO1).isNotEqualTo(questionAnswerDTO2);
        questionAnswerDTO2.setId(questionAnswerDTO1.getId());
        assertThat(questionAnswerDTO1).isEqualTo(questionAnswerDTO2);
        questionAnswerDTO2.setId(2L);
        assertThat(questionAnswerDTO1).isNotEqualTo(questionAnswerDTO2);
        questionAnswerDTO1.setId(null);
        assertThat(questionAnswerDTO1).isNotEqualTo(questionAnswerDTO2);
    }
}
