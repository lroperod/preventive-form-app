package com.lroperod.repository;

import com.lroperod.domain.FormAnswer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FormAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {}
