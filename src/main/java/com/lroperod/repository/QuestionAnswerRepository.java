package com.lroperod.repository;

import java.util.List;

import com.lroperod.domain.FormAnswer;
import com.lroperod.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
    /**
     *
     * TODO: Create a method to save form's answer
     */
    List<QuestionAnswer> saveAnswerForm(QuestionAnswer questionAnswer);

}
