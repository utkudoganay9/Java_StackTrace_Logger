package org.example.helper;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final int errorCodes;

    public CustomException(String message, int errorCodes) {
        super(String.format("%s Hata Kodu: %d", message, errorCodes));
        this.errorCodes = errorCodes;
    }
}
