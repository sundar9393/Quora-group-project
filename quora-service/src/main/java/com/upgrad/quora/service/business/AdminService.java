package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminService {
    @Autowired
    private UserDao userDao;

    public UserAuthTokenEntity signInValidationAdmin(final String accessToken) throws AuthorizationFailedException {
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

        return userAuthToken;
    }

}
