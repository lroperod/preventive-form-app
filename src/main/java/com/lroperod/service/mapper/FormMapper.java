package com.lroperod.service.mapper;

import com.lroperod.domain.*;
import com.lroperod.service.dto.FormDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Form} and its DTO {@link FormDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FormMapper extends EntityMapper<FormDTO, Form> {
    @Named("formName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "formName", source = "formName")
    FormDTO toDtoFormName(Form form);
}
