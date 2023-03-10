package com.hungrybandits.rest.recipes.services;

import com.hungrybandits.rest.clients.UserProxyDTO;
import com.hungrybandits.rest.clients.auth.AuthClient;
import com.hungrybandits.rest.clients.user.UserClient;
import com.hungrybandits.rest.exceptions.ApiAccessException;
import com.hungrybandits.rest.exceptions.RestException;
import com.hungrybandits.rest.exceptions.dtos.ApiCallError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    private final AuthClient authClient;
    private final UserClient userClient;

    public UserService(AuthClient authClient, UserClient userClient) {
        this.authClient = authClient;
        this.userClient = userClient;
    }

    public UserProxyDTO checkAuthorizationAndGetUserInSession(String[] roles, HttpServletRequest servletRequest) throws ApiAccessException {
        ResponseEntity<UserProxyDTO> entity = authClient.checkAuthorizationAndReturnUser(roles, servletRequest.getHeader(HttpHeaders.AUTHORIZATION));
        if (entity.hasBody()) {
            return entity.getBody();
        }
        throw new RestException(new ApiCallError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "No response body", null));
    }

    public UserProxyDTO getUser(Long userId) {
        ResponseEntity<UserProxyDTO> entity = userClient.getUser(userId);
        if (entity.hasBody()) {
            return entity.getBody();
        }
        throw new RestException(new ApiCallError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "No response body", null));
    }
}
