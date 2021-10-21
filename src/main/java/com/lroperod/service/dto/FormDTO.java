package com.lroperod.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lroperod.domain.Form} entity.
 */
public class FormDTO implements Serializable {

    private Long id;

    @NotNull
    private String formName;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormDTO)) {
            return false;
        }

        FormDTO formDTO = (FormDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormDTO{" +
            "id=" + getId() +
            ", formName='" + getFormName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
