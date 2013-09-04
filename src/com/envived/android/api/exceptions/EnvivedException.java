package com.envived.android.api.exceptions;

public abstract class EnvivedException extends Exception {
	private static final long serialVersionUID = 1L;

	protected EnvivedException(Throwable cause) {
		if (cause != null) {
			initCause(cause);
		}
	}
	
	@Override
	public abstract String getMessage();
}
