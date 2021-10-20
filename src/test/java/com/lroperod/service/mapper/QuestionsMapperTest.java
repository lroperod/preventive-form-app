package com.lroperod.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionsMapperTest {

    private QuestionsMapper questionsMapper;

    @BeforeEach
    public void setUp() {
        questionsMapper = new QuestionsMapperImpl();
    }
}
