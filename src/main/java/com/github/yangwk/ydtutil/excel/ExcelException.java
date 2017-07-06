package com.github.yangwk.ydtutil.excel;

public class ExcelException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ExcelException() {
        super();
    }
	
	public ExcelException(String message) {
        super(message);
    }
	
	public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public ExcelException(Throwable cause) {
        super(cause);
    }
	
	
}
