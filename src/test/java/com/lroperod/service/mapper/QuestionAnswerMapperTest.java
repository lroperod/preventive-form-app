package com.lroperod.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionAnswerMapperTest {

    private QuestionAnswerMapper questionAnswerMapper;

    @BeforeEach
    public void setUp() {
        questionAnswerMapper = new QuestionAnswerMapperImpl();
    }
}
