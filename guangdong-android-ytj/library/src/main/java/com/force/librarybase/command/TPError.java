package com.force.librarybase.command;

public class TPError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3109114244404564606L;

	private String cause;

	public void setCause(String cause) {
		this.cause = cause;
	}

	@Override
	public String toString() {
		return "TPError [getMessage()=" + getMessage() + "]";
	}
	
	@Override
	public String getMessage() {
		return cause;
	}

}
