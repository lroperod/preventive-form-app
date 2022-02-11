package com.lroperod.repository;

import com.lroperod.domain.FormAnswer;
import com.lroperod.domain.QuestionAnswer;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FormAnswer entity.
 */
@Repository
public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {
    @Query(
        value = "select distinct formAnswer from FormAnswer formAnswer left join fetch formAnswer.users",
        countQuery = "select count(distinct formAnswer) from FormAnswer formAnswer"
    )
    Page<FormAnswer> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct formAnswer from FormAnswer formAnswer left join fetch formAnswer.users")
    List<FormAnswer> findAllWithEagerRelationships();

    @Query("select formAnswer from FormAnswer formAnswer left join fetch formAnswer.users where formAnswer.id =:id")
    Optional<FormAnswer> findOneWithEagerRelationships(@Param("id") Long id);

}
