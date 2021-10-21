package com.lroperod.service.mapper;

import com.lroperod.domain.*;
import com.lroperod.service.dto.FormAnswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormAnswer} and its DTO {@link FormAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, FormMapper.class })
public interface FormAnswerMapper extends EntityMapper<FormAnswerDTO, FormAnswer> {
    @Mapping(target = "users", source = "users", qualifiedByName = "loginSet")
    @Mapping(target = "form", source = "form", qualifiedByName = "formName")
    FormAnswerDTO toDto(FormAnswer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormAnswerDTO toDtoId(FormAnswer formAnswer);

    @Mapping(target = "removeUser", ignore = true)
    FormAnswer toEntity(FormAnswerDTO formAnswerDTO);
}
