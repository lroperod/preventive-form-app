package com.lroperod.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.lroperod.domain.FormAnswer} entity.
 */
public class FormAnswerDTO implements Serializable {

    private Long id;

    private LocalDate formAnswerLocalDate;

    private Set<UserDTO> users = new HashSet<>();

    private FormDTO form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFormAnswerLocalDate() {
        return formAnswerLocalDate;
    }

    public void setFormAnswerLocalDate(LocalDate formAnswerLocalDate) {
        this.formAnswerLocalDate = formAnswerLocalDate;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormAnswerDTO)) {
            return false;
        }

        FormAnswerDTO formAnswerDTO = (FormAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormAnswerDTO{" +
            "id=" + getId() +
            ", formAnswerLocalDate='" + getFormAnswerLocalDate() + "'" +
            ", users=" + getUsers() +
            ", form=" + getForm() +
            "}";
    }
}
