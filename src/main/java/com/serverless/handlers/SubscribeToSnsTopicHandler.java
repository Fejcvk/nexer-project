package com.serverless.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.service.SnsNotificationService;
import lombok.extern.java.Log;

import java.util.Map;

import static com.serverless.Constants.*;

@Log
public class SubscribeToSnsTopicHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {


    private final SnsNotificationService subscriptionService;
    private final String topicArn;

    public SubscribeToSnsTopicHandler() {
        topicArn = System.getenv(TOPIC_ARN_VENV_KEY);
        subscriptionService = new SnsNotificationService();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
            Map<String,String> pathParameters =  (Map<String,String>)stringObjectMap.get(PATH_PARAMETERS_KEY);
            String emailAddress = pathParameters.get(PATH_PARAMETERS_EMAIL_KEY);

            subscriptionService.subscribeToTopicWithEmail(emailAddress, topicArn);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setRawBody("Email " + emailAddress + " has just subscribed to the FruitNotificationTopic")
                    .build();

    }
}
