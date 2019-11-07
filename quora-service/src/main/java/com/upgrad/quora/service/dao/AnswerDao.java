package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager manager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        manager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerByUuid(final String uuid) {
        try {
            return manager.createNamedQuery("getAnswerByUuid", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        return manager.merge(answerEntity);
    }
/* Resolving merge conflict
I have already implemented this method and also you have used a incorrect named query, so commenting it out
    public List<AnswerEntity> getAllAnswersByUserId(Integer userId) {
        return manager.createNamedQuery("getAllAnswersByUserId", AnswerEntity.class).setParameter("user_id", userId).getResultList();
    }

I have already implemented it, also the implementation is incorrect as the method needs to return the answer for further use.
    public void deleteAnswer(AnswerEntity answerEntity) {
        manager.remove(answerEntity)
*/
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        manager.remove(answerEntity);
        return answerEntity;
    }

    public List<AnswerEntity> getAllAnswersForQuestion(final Integer questionId) {
        return manager.createNamedQuery("getAnswerForQuestion", AnswerEntity.class).setParameter("id", questionId).getResultList();

    }
}
