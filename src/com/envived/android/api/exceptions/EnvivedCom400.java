package com.envived.android.api.exceptions;

import com.envived.android.api.EnvSocialResource;

public class EnvivedCom400 extends EnvivedComException {
	public EnvivedCom400(String userUri, HttpMethod method, EnvSocialResource resource, Throwable cause) {
		super(userUri, method, resource, cause);
		this.prologErrorMessage = "Bad Content Message or URI params";
	}
}
