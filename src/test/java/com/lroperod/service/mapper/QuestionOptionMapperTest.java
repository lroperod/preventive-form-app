package com.lroperod.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionOptionMapperTest {

    private QuestionOptionMapper questionOptionMapper;

    @BeforeEach
    public void setUp() {
        questionOptionMapper = new QuestionOptionMapperImpl();
    }
}
