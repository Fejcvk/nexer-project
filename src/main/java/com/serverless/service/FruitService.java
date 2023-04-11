package com.serverless.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.model.Fruit;
import com.serverless.repository.DynamoDBAdapter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.serverless.Constants.PRICE_INDEX_NAME;

@Log
public class FruitService {

    DynamoDBAdapter adapter;
    AmazonDynamoDB client;
    DynamoDBMapper mapper;

    AmazonSNS snsClient;

    ObjectMapper objectMapper;

    public FruitService() {
        this.adapter = DynamoDBAdapter.getInstance();
        this.client = this.adapter.getClient();
        this.mapper = this.adapter.createDbMapper(DynamoDBMapperConfig.builder().build());
        this.snsClient = AmazonSNSClient.builder().build();
        objectMapper = new ObjectMapper();
    }
    @SneakyThrows
    public Fruit createFruitFromJson(JsonNode payload) {
        log.info("Creating fruit form json " + payload.asText());
        return objectMapper.treeToValue(payload, Fruit.class);
    }

    public void save(Fruit fruit) {
        log.info("Saving new fruit: " + fruit.toString());
        this.mapper.save(fruit);
    }

    public List<Fruit> listFruitsByPriceRange(String minPrice, String maxPrice) {

        var attributeValueMap = new HashMap<String, AttributeValue>();
        attributeValueMap.put(":food_type", new AttributeValue().withS("Fruit"));
        attributeValueMap.put(":min_price", new AttributeValue().withN(minPrice));
        attributeValueMap.put(":max_price", new AttributeValue().withN(maxPrice));

        var queryExpression = new DynamoDBQueryExpression<Fruit>()
                .withIndexName(PRICE_INDEX_NAME)
                .withConsistentRead(false)
                .withKeyConditionExpression("foodType = :food_type and price between :min_price and :max_price")
                .withExpressionAttributeValues(attributeValueMap);

        PaginatedQueryList<Fruit> result = this.mapper.query(Fruit.class, queryExpression);

        return new ArrayList<>(result);
    }
}
