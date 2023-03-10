package com.hungrybandits.rest.recipes.security;

import com.hungrybandits.rest.clients.UserProxyDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static void setSecurityContextAuthenticationObject(UserProxyDTO user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, null));
    }

    public static UserProxyDTO getUserFromSession() {
        return (UserProxyDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
