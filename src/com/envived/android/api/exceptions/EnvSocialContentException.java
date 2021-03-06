package com.envived.android.api.exceptions;

import com.envived.android.api.EnvSocialResource;

public class EnvSocialContentException extends EnvSocialException {
	private static final long serialVersionUID = 1L;
	
	protected String mContent;
	protected EnvSocialResource mResource;
	
	public EnvSocialContentException(String jsonContent, EnvSocialResource resource, Throwable cause) {
		super(cause);
		
		mContent = jsonContent;
		mResource = resource;
	}
	
	public String getContent() {
		return mContent;
	}

	@Override
	public String getMessage() {
		String info = "Error parsing content for resource :: " + mResource.getName();
		return info;
	}

}
