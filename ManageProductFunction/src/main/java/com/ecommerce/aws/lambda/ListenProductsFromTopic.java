package com.ecommerce.aws.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.ecommerce.aws.lambda.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListenProductsFromTopic {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public static final String PRODUCTS_TABLE = System.getenv("PRODUCTS_TABLE");
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
    public void listenTopic(SNSEvent event) {

        event.getRecords().forEach(snsRecord -> {
            try {
                Product product = objectMapper.readValue(snsRecord.getSNS().getMessage(), Product.class);
                saveProduct(product);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void saveProduct(Product product) {

        Table productsTable = dynamoDB.getTable(PRODUCTS_TABLE);
        Item item = new Item()
                .withPrimaryKey("productId", product.getProductId())
                .withString("productName", product.getProductName())
                .withString("productDescription", product.getProductDescription())
                .withDouble("productPrice", product.getProductPrice())
                .withInt("productStock", product.getProductStock())
                .withString("productImageUrl", product.getProductImageUrl());

        productsTable.putItem(item);
    }
}
