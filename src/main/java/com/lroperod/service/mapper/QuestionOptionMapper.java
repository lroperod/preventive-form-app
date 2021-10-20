package com.lroperod.service.mapper;

import com.lroperod.domain.*;
import com.lroperod.service.dto.QuestionOptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionOption} and its DTO {@link QuestionOptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionsMapper.class })
public interface QuestionOptionMapper extends EntityMapper<QuestionOptionDTO, QuestionOption> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "questionText")
    QuestionOptionDTO toDto(QuestionOption s);
}
