package com.lroperod.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionOption.
 */
@Entity
@Table(name = "question_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestionOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "question_options_code")
    private String questionOptionsCode;

    @Column(name = "question_options_text")
    private String questionOptionsText;

    @OneToMany(mappedBy = "questionOption")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question", "form", "questionOption" }, allowSetters = true)
    private Set<Questions> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionOptionsCode() {
        return this.questionOptionsCode;
    }

    public QuestionOption questionOptionsCode(String questionOptionsCode) {
        this.setQuestionOptionsCode(questionOptionsCode);
        return this;
    }

    public void setQuestionOptionsCode(String questionOptionsCode) {
        this.questionOptionsCode = questionOptionsCode;
    }

    public String getQuestionOptionsText() {
        return this.questionOptionsText;
    }

    public QuestionOption questionOptionsText(String questionOptionsText) {
        this.setQuestionOptionsText(questionOptionsText);
        return this;
    }

    public void setQuestionOptionsText(String questionOptionsText) {
        this.questionOptionsText = questionOptionsText;
    }

    public Set<Questions> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Questions> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setQuestionOption(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setQuestionOption(this));
        }
        this.questions = questions;
    }

    public QuestionOption questions(Set<Questions> questions) {
        this.setQuestions(questions);
        return this;
    }

    public QuestionOption addQuestions(Questions questions) {
        this.questions.add(questions);
        questions.setQuestionOption(this);
        return this;
    }

    public QuestionOption removeQuestions(Questions questions) {
        this.questions.remove(questions);
        questions.setQuestionOption(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionOption)) {
            return false;
        }
        return id != null && id.equals(((QuestionOption) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionOption{" +
            "id=" + getId() +
            ", questionOptionsCode='" + getQuestionOptionsCode() + "'" +
            ", questionOptionsText='" + getQuestionOptionsText() + "'" +
            "}";
    }
}
