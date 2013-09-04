package com.envived.android.api.exceptions;

import com.envived.android.api.EnvSocialResource;

public class EnvivedCom401 extends EnvivedComException {
	public EnvivedCom401(String userUri, HttpMethod method, EnvSocialResource resource, Throwable cause) {
		super(userUri, method, resource, cause);
		this.prologErrorMessage = "Unauthorized Access";
	}
}
