package com.hungrybandits.rest.recipes.exceptions;

import com.hungrybandits.rest.exceptions.CommonExceptionHandler;
import com.hungrybandits.rest.exceptions.RestException;
import com.hungrybandits.rest.exceptions.dtos.ApiCallError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends CommonExceptionHandler {
    private final Logger logger = LogManager.getLogger();

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({RestException.class})
    public ResponseEntity<ApiCallError> handleRestException(HttpServletRequest request, RestException ex)   {
        logger.error("handleRestException {}\n", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getApiCallError());

    }
}
