package com.lroperod.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lroperod.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormAnswerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormAnswerDTO.class);
        FormAnswerDTO formAnswerDTO1 = new FormAnswerDTO();
        formAnswerDTO1.setId(1L);
        FormAnswerDTO formAnswerDTO2 = new FormAnswerDTO();
        assertThat(formAnswerDTO1).isNotEqualTo(formAnswerDTO2);
        formAnswerDTO2.setId(formAnswerDTO1.getId());
        assertThat(formAnswerDTO1).isEqualTo(formAnswerDTO2);
        formAnswerDTO2.setId(2L);
        assertThat(formAnswerDTO1).isNotEqualTo(formAnswerDTO2);
        formAnswerDTO1.setId(null);
        assertThat(formAnswerDTO1).isNotEqualTo(formAnswerDTO2);
    }
}
