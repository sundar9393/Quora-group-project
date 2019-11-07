package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    public UserAuthTokenEntity signInValidationAdmin(final String accessToken) throws AuthorizationFailedException,
            UserNotFoundException {
        UserAuthTokenEntity userAuthToken = userDao.getAuthToken(accessToken);
        if(userAuthToken == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthToken.getLogutTime()!=null) {
            if(userAuthToken.getLoginTime().isBefore(userAuthToken.getLogutTime())) {
                throw new AuthorizationFailedException("ATHR-002","User is signed out");
            }
        }
        if(userAuthToken.getUser().getRole().equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }
        if (userDao.getUserByUuid(userAuthToken.getUuid()) == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }

        return userAuthToken;
    }

    public String deleteUserAsAdmin(final String uuid) throws UserNotFoundException {
        UserEntity userEntity = userDao.getUserByUuid(uuid);

        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }

        //Delete User Profile for the User
        userDao.deleteUser(userDao.getUserByUuid(uuid));
        //Return the UUID of the deleted User
        return uuid;
    }

}