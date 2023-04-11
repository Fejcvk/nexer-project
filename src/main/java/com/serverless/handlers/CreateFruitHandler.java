package com.serverless.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Fruit;
import com.serverless.service.FruitService;
import com.serverless.service.SnsNotificationService;
import lombok.extern.java.Log;

import java.util.Map;

import static com.serverless.Constants.*;

@Log
public class CreateFruitHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final FruitService fruitService;

    private final SnsNotificationService snsNotificationService;

    private final String topicArn;
    private final ObjectMapper objectMapper;

    private final String OPERATIONS_TEAM_EMAIL = System.getenv(OPERATIONS_TEAM_EMAIL_VENV_KEY);

    public CreateFruitHandler() {
        fruitService = new FruitService();
        objectMapper = new ObjectMapper();
        snsNotificationService = new SnsNotificationService();
        topicArn = System.getenv(TOPIC_ARN_VENV_KEY);
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {

        try {

            JsonNode requestBody = objectMapper.readTree((String) stringObjectMap.get(JSON_BODY_KEY));
            Fruit newFruit = fruitService.createFruitFromJson(requestBody);
            fruitService.save(newFruit);

            var message = "New fruit " + newFruit + " has been added to the system";

            snsNotificationService.notifyWithEmailForTopic(topicArn, OPERATIONS_TEAM_EMAIL, message);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(newFruit)
                    .build();
        } catch (Exception ex) {
            log.severe("Error in saving fruit: " + ex);

            Response responseBody = new Response("Error in saving fruit: ", stringObjectMap);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }

    }
}
