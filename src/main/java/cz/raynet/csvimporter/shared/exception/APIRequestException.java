package cz.raynet.csvimporter.shared.exception;

import org.springframework.http.HttpStatusCode;

public class APIRequestException extends RuntimeException {
        public APIRequestException(String message, HttpStatusCode statusCode) {
            super(String.format("%s - STATUS CODE: %s" , message, statusCode.toString() ));
        }
}
