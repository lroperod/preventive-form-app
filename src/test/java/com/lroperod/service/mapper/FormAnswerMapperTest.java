package com.lroperod.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormAnswerMapperTest {

    private FormAnswerMapper formAnswerMapper;

    @BeforeEach
    public void setUp() {
        formAnswerMapper = new
            FormAnswerMapperImpl();
    }
}
