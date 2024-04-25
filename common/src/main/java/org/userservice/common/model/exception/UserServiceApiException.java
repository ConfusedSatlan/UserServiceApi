package org.userservice.common.model.exception;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;

@Getter
public class UserServiceApiException extends RuntimeException {

    protected String code;

    public UserServiceApiException(@NonNull String msg, @NonNull String errorCode) {
        super(msg);
        this.code = errorCode;
    }
}
