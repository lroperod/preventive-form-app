package com.lroperod.service.mapper;

import com.lroperod.domain.*;
import com.lroperod.service.dto.QuestionAnswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionAnswer} and its DTO {@link QuestionAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionsMapper.class, FormAnswerMapper.class })
public interface QuestionAnswerMapper extends EntityMapper<QuestionAnswerDTO, QuestionAnswer> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "id")
    @Mapping(target = "formAnswer", source = "formAnswer", qualifiedByName = "id")
    QuestionAnswerDTO toDto(QuestionAnswer s);
}
