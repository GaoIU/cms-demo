package com.gaoc.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5018555806093591501L;

	private int code;

	private String message;

}
