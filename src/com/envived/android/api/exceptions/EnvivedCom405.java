package com.envived.android.api.exceptions;

import com.envived.android.api.EnvSocialResource;

public class EnvivedCom405 extends EnvivedComException {
	private static final long serialVersionUID = 1L;

	public EnvivedCom405(String userUri, HttpMethod method, EnvSocialResource resource, Throwable cause) {
		super(userUri, method, resource, cause);
		this.prologErrorMessage = "Communication Method Not Allowed";
	}
}
