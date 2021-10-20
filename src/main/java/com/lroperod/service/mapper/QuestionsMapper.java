package com.lroperod.service.mapper;

import com.lroperod.domain.*;
import com.lroperod.service.dto.QuestionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Questions} and its DTO {@link QuestionsDTO}.
 */
@Mapper(componentModel = "spring", uses = { FormMapper.class })
public interface QuestionsMapper extends EntityMapper<QuestionsDTO, Questions> {
    @Mapping(target = "form", source = "form", qualifiedByName = "formName")
    QuestionsDTO toDto(Questions s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionsDTO toDtoId(Questions questions);

    @Named("questionText")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "questionText", source = "questionText")
    QuestionsDTO toDtoQuestionText(Questions questions);
}
