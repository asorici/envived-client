package com.envived.android.features.order;

import com.envived.android.api.Annotation;

public interface ISendOrderRequest {
	public void sendOrder(OrderDialogFragment orderDialog);
	public void postSendOrderRequest(String orderRequestType, Annotation orderRequest, boolean success);
}
