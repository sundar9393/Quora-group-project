package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
Service class that interact with DAO classes
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity addQuestion(QuestionEntity question, final String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = authenticationService.signInValidation(accessToken);
        question.setUser(userAuthToken.getUser());

      return questionDao.createQuestion(question);
    }

    public List<QuestionEntity> getAllQuestions(final String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = authenticationService.signInValidation(accessToken);
        return questionDao.getAllQuestions();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionContent(final String uuid, final String accessToken, final  String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthToken = authenticationService.signInValidation(accessToken);
        QuestionEntity existingQuestion = isQuestionPresent(uuid);
        if(existingQuestion.getUser().getId() != userAuthToken.getUser().getId()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        existingQuestion.setContent(content);
        return questionDao.editQuestionContent(existingQuestion);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String uuid, final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthToken = authenticationService.signInValidation(accessToken);
        QuestionEntity existingQuestion = isQuestionPresent(uuid);
        if(existingQuestion.getUser().getId() != userAuthToken.getUser().getId()) {
            throw new AuthorizationFailedException("ATHR-003","Only the question owner or admin can delete the question");
        }
        return questionDao.deleteQuestion(existingQuestion);
    }


    public List<QuestionEntity> getAllQuestionsByUser(final String uuid, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity authTokenEntity = authenticationService.signInValidation(accessToken);
        UserEntity userEntity = userDao.getUserByUuid(uuid);
        if(userEntity == null) {
           throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(userEntity.getId());
    }


    //Writing the logic in a method to avoid boiler plate code
    private QuestionEntity isQuestionPresent(final String uuid) throws InvalidQuestionException {
        QuestionEntity existingQuestion = questionDao.getQuestionByUuid(uuid);
        if(existingQuestion == null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        }
        return existingQuestion;
    }



}
