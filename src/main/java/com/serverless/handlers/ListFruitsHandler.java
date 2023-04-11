package com.serverless.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Fruit;
import com.serverless.service.FruitService;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Map;

import static com.serverless.Constants.*;

@Log
public class ListFruitsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final FruitService service;
    private final ObjectMapper objectMapper;

    public ListFruitsHandler() {
        this.service = new FruitService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        try {

            JsonNode requestBody = objectMapper.readTree((String) stringObjectMap.get(JSON_BODY_KEY));

            List<Fruit> fruitsByPriceRange = service.listFruitsByPriceRange(requestBody.get(MIN_PRICE_KEY).asText(), requestBody.get(MAX_PRICE_KEY).asText());

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(fruitsByPriceRange)
                    .build();
        } catch (Exception ex) {
            log.severe("Error in listing fruits: " + ex);

            Response responseBody = new Response("Error in listing fruits: ", stringObjectMap);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }
    }
}
