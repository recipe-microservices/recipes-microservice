package com.hungrybandits.rest.recipes.aop;

import com.hungrybandits.rest.clients.UserProxyDTO;
import com.hungrybandits.rest.exceptions.ApiAccessException;
import com.hungrybandits.rest.exceptions.ApiOperationException;
import com.hungrybandits.rest.recipes.annotations.VerifyUser;
import com.hungrybandits.rest.recipes.security.SecurityUtils;
import com.hungrybandits.rest.recipes.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Slf4j
@AllArgsConstructor
@Component
public class UserSessionInspectorAspect {

    private final UserService userService;

    @Pointcut("@annotation(com.hungrybandits.rest.recipes.annotations.VerifyUser)")
    public void loggingPointCut(){}

    @Before("loggingPointCut()")
    public void before(ProceedingJoinPoint joinPoint) throws ApiAccessException {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        VerifyUser verifyUserClassLevel = joinPoint.getTarget().getClass().getAnnotation(VerifyUser.class);
        String[] rolesClassLevel = (verifyUserClassLevel != null) ? verifyUserClassLevel.roles() : new String[0];
        Method method = methodSignature.getMethod();
        VerifyUser verifyUserMethodLevel = method.getAnnotation(VerifyUser.class);
        String[] rolesMethodLevel = (verifyUserMethodLevel != null) ? verifyUserMethodLevel.roles(): new String[0];
        String[] roles = new String[rolesClassLevel.length + rolesMethodLevel.length];
        System.arraycopy(rolesClassLevel, 0, roles, 0, rolesClassLevel.length);
        System.arraycopy(rolesMethodLevel, 0, roles, rolesClassLevel.length, rolesMethodLevel.length);
        Object httpServletReqArg =  Arrays.stream(args).filter((arg) -> arg instanceof HttpServletRequest).findAny().orElse(null);

        if (httpServletReqArg != null) {
            UserProxyDTO user = this.userService.checkAuthorizationAndGetUserInSession(roles, (HttpServletRequest) httpServletReqArg);
            SecurityUtils.setSecurityContextAuthenticationObject(user);
        }

        throw new ApiOperationException("HttpServletRequest is not provided");
    }
}
