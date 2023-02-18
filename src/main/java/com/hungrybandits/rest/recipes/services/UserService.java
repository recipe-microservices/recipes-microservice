package com.hungrybandits.rest.recipes.services;

import com.hungrybandits.rest.clients.UserProxyDto;
import com.hungrybandits.rest.clients.auth.AuthClient;
import com.hungrybandits.rest.clients.user.UserClient;
import com.hungrybandits.rest.exceptions.RestException;
import com.hungrybandits.rest.exceptions.dtos.ApiCallError;
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

    public UserProxyDto getUserInSession(HttpServletRequest servletRequest) {
        ResponseEntity<UserProxyDto> entity = authClient.getUserInSession(servletRequest);
        if (entity.hasBody()) {
            return entity.getBody();
        }
        throw new RestException(new ApiCallError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "No response body", null));
    }

    public UserProxyDto getUser(Long userId) {
        ResponseEntity<UserProxyDto> entity = userClient.getUser(userId);
        if (entity.hasBody()) {
            return entity.getBody();
        }
        throw new RestException(new ApiCallError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "No response body", null));
    }
}
