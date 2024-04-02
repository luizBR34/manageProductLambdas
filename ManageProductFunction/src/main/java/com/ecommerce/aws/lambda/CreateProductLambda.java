package com.ecommerce.aws.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ecommerce.aws.lambda.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateProductLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent createProduct(APIGatewayProxyRequestEvent request) throws JsonProcessingException {

        Product product = objectMapper.readValue(request.getBody(), Product.class);

        Table productsTable = dynamoDB.getTable(System.getenv("PRODUCTS_TABLE"));
        Item item = new Item()
                .withPrimaryKey("productId", product.getProductId())
                .withString("productName", product.getProductName())
                .withString("productDescription", product.getProductDescription())
                .withDouble("productPrice", product.getProductPrice())
                .withInt("productStock", product.getProductStock())
                .withString("productImageUrl", product.getProductImageUrl());

        productsTable.putItem(item);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(201)
                .withBody("Product Created! ID: " + product.getProductId());
    }
}
