package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String questionUuid, final String accessToken, AnswerEntity answer) throws AuthorizationFailedException, InvalidQuestionException {
        //Signin validation
        UserAuthTokenEntity userAuthToken = authenticationService.signInValidation(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionUuid);
        if(questionEntity == null) {
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        answer.setUser(userAuthToken.getUser());
        answer.setQuestion(questionEntity);
        return answerDao.createAnswer(answer);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final String answerUuid, final String accessToken, final String content) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthTokenEntity userAuthToken = authenticationService.signInValidation(accessToken);
        AnswerEntity answerEntity = answerDao.getAnswerByUuid(answerUuid);
        if(answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if(userAuthToken.getUser().getId() != answerEntity.getUser().getId()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        answerEntity.setAnswerContent(content);
        return answerDao.editAnswer(answerEntity);
    }
}
