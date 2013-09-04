package com.envived.android.api.exceptions;

import com.envived.android.api.EnvSocialResource;

public class EnvivedConnectivityException extends EnvivedComException {

	private static final long serialVersionUID = 1L;

	public EnvivedConnectivityException(String userUri, HttpMethod method,
			EnvSocialResource resource, Throwable cause) {
		super(userUri, method, resource, cause);
		this.prologErrorMessage = "No network connectivity";
	}

}
