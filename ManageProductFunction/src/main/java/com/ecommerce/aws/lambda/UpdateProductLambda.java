package com.ecommerce.aws.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ecommerce.aws.lambda.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UpdateProductLambda {

    Logger logger = LoggerFactory.getLogger(UpdateProductLambda.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public APIGatewayProxyResponseEvent updateProduct(APIGatewayProxyRequestEvent request) {

        Product product = null;
        Map<String, AttributeValueUpdate> updatedProductMap = new HashMap<>();

        try {
            product = objectMapper.readValue(request.getBody(), Product.class);
            updatedProductMap.put("productName", new AttributeValueUpdate().withValue(new AttributeValue().withS(product.getProductName())));
            updatedProductMap.put("productDescription", new AttributeValueUpdate().withValue(new AttributeValue().withS(product.getProductDescription())));
            updatedProductMap.put("productPrice", new AttributeValueUpdate().withValue(new AttributeValue().withN(String.valueOf(product.getProductPrice()))));
            updatedProductMap.put("productStock", new AttributeValueUpdate().withValue(new AttributeValue().withN(String.valueOf(product.getProductStock()))));
            updatedProductMap.put("productImageUrl", new AttributeValueUpdate().withValue(new AttributeValue().withS(product.getProductImageUrl())));

        } catch (JsonProcessingException e) {
            logger.error("Exception: ", e);
            throw new RuntimeException("Error while serializing object: ", e);
        }

        Map<String, AttributeValue> keyAttribute = new HashMap<>();
        keyAttribute.put("productId", new AttributeValue().withN(String.valueOf(product.getProductId())));

        UpdateItemResult updateItemResult = dynamoDB.updateItem(new UpdateItemRequest()
                .withTableName(System.getenv("PRODUCTS_TABLE"))
                .withKey(keyAttribute)
                .withAttributeUpdates(updatedProductMap));

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Product Updated! ID: " + product.getProductId());
    }
}
