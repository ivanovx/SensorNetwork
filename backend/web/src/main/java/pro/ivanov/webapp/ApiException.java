package pro.ivanov.webapp;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ApiException(String message, HttpStatus httpStatus, LocalDateTime timestamp) { }