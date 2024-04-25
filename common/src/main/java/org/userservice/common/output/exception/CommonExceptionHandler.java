package org.userservice.common.output.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.userservice.common.model.dto.ResponseErrorDto;
import org.userservice.common.model.exception.UserServiceApiException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserServiceApiException.class})
    protected ResponseEntity<ResponseErrorDto> handleUserServiceApiException(UserServiceApiException ex) {

           ResponseErrorDto body = ResponseErrorDto.builder()
                .time(LocalDateTime.now())
                .statusCode(ex.getCode())
                .errorMessage(List.of(ex.getMessage()))
                .stackTrace(List.of(Arrays.toString(ex.getStackTrace())))
                .build();

           return ResponseEntity.status(HttpStatus.valueOf(Integer.valueOf(ex.getCode()))).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<String> errorList = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

        ResponseErrorDto errorDto = ResponseErrorDto.builder()
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .errorMessage(errorList)
                .build();

        return ResponseEntity.status(HttpStatus.valueOf(errorDto.getStatusCode())).body(errorDto);
    }
}
