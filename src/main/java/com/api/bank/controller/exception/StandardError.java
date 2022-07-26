package com.api.bank.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class StandardError {

	private LocalDateTime timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
}