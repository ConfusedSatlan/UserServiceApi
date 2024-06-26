package org.userservice.common.output.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ResponseErrorDto> handleNotFound(Exception ex) {
        ResponseErrorDto body = ResponseErrorDto.builder()
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .errorMessage(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(body);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<ResponseErrorDto> handleNullPointerException(NullPointerException ex) {
        ResponseErrorDto body = ResponseErrorDto.builder()
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.name())
                .errorMessage(List.of(ex.getMessage()))
                .stackTrace(List.of(Arrays.toString(ex.getStackTrace())))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(body);
    }

    @ExceptionHandler(value = {UserServiceApiException.class})
    protected ResponseEntity<ResponseErrorDto> handleUserServiceApiException(UserServiceApiException ex) {

           ResponseErrorDto body = ResponseErrorDto.builder()
                .time(LocalDateTime.now())
                .statusCode(ex.getCode())
                .errorMessage(List.of(ex.getMessage()))
                .stackTrace(List.of(Arrays.toString(ex.getStackTrace())))
                .build();
           return ResponseEntity.status(Integer.valueOf(ex.getCode())).body(body);
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

    @Override
    protected @NonNull ResponseEntity<Object> createResponseEntity(Object body, HttpHeaders headers,
                                                                   HttpStatusCode statusCode, WebRequest request) {
        if (body instanceof ResponseErrorDto error) {
            ResponseEntity<Object> responseWithExceptionDetails = buildResponseEntity(error);
            log.debug("[EXCEPTION]: create response with exception details: {}", responseWithExceptionDetails.getBody());
            return responseWithExceptionDetails;

        } else if (body instanceof ProblemDetail problem) {
            if (problem.getStatus() == 0) {
                problem.setStatus(statusCode.value());
            }
            ResponseEntity<Object> responseWithExceptionDetails = buildResponseEntity(problem);
            log.debug("[EXCEPTION]: create response with exception details: {}", responseWithExceptionDetails.getBody());
            return responseWithExceptionDetails;
        }

        return super.createResponseEntity(body, headers, statusCode, request);
    }

    private ResponseEntity<Object> buildResponseEntity(ResponseErrorDto responseErrorDto) {
        HttpStatus status = HttpStatus.valueOf(responseErrorDto.getStatusCode());

        return ResponseEntity.status(status).body(responseErrorDto);
    }

    private ResponseEntity<Object> buildResponseEntity(ProblemDetail problemDetail) {
        ResponseErrorDto responseErrorDto = new ResponseErrorDto();
        HttpStatus status = HttpStatus.resolve(problemDetail.getStatus());

        responseErrorDto.setTime(LocalDateTime.now());
        responseErrorDto.setStatusCode(status.name());
        if (problemDetail.getDetail() != null) {
            responseErrorDto.setErrorMessage(List.of(problemDetail.getDetail()));
        }

        return ResponseEntity.status(status).body(responseErrorDto);
    }
}
