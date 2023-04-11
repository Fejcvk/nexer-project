package com.serverless.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;
import lombok.ToString;
import lombok.extern.java.Log;

import java.math.BigDecimal;

import static com.serverless.Constants.FRUIT_TABLE_NAME;
import static com.serverless.Constants.PRICE_INDEX_NAME;

@Data
@Log
@DynamoDBTable(tableName = FRUIT_TABLE_NAME)
@ToString
public class Fruit {
    @DynamoDBHashKey(attributeName = "name")
    private String name;
    @DynamoDBAttribute(attributeName = "description")
    private String description;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = PRICE_INDEX_NAME, attributeName = "foodType")
    private String foodType = "Fruit";

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = PRICE_INDEX_NAME, attributeName = "price")
    private BigDecimal price;
}
