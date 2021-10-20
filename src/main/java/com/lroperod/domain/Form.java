package com.lroperod.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Form.
 */
@Entity
@Table(name = "form")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "form_name", nullable = false)
    private String formName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "form")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "form" }, allowSetters = true)
    private Set<Questions> questions = new HashSet<>();

    @OneToMany(mappedBy = "form")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "form" }, allowSetters = true)
    private Set<FormAnswer> formAnswers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Form id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormName() {
        return this.formName;
    }

    public Form formName(String formName) {
        this.setFormName(formName);
        return this;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getDescription() {
        return this.description;
    }

    public Form description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Questions> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Questions> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setForm(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setForm(this));
        }
        this.questions = questions;
    }

    public Form questions(Set<Questions> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Form addQuestions(Questions questions) {
        this.questions.add(questions);
        questions.setForm(this);
        return this;
    }

    public Form removeQuestions(Questions questions) {
        this.questions.remove(questions);
        questions.setForm(null);
        return this;
    }

    public Set<FormAnswer> getFormAnswers() {
        return this.formAnswers;
    }

    public void setFormAnswers(Set<FormAnswer> formAnswers) {
        if (this.formAnswers != null) {
            this.formAnswers.forEach(i -> i.setForm(null));
        }
        if (formAnswers != null) {
            formAnswers.forEach(i -> i.setForm(this));
        }
        this.formAnswers = formAnswers;
    }

    public Form formAnswers(Set<FormAnswer> formAnswers) {
        this.setFormAnswers(formAnswers);
        return this;
    }

    public Form addFormAnswer(FormAnswer formAnswer) {
        this.formAnswers.add(formAnswer);
        formAnswer.setForm(this);
        return this;
    }

    public Form removeFormAnswer(FormAnswer formAnswer) {
        this.formAnswers.remove(formAnswer);
        formAnswer.setForm(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Form)) {
            return false;
        }
        return id != null && id.equals(((Form) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Form{" +
            "id=" + getId() +
            ", formName='" + getFormName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
