package com.ecommerce.aws.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

public class DeleteProductLambda {
    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public APIGatewayProxyResponseEvent deleteProduct(APIGatewayProxyRequestEvent request) {

        Map<String, String> queryParameters = request.getQueryStringParameters();
        String key = queryParameters.keySet().stream().findAny().orElseThrow(RuntimeException::new);

        Map<String, AttributeValue> keyAttribute = new HashMap<>();
        keyAttribute.put("productId", new AttributeValue().withN(queryParameters.get(key)));

        dynamoDB.deleteItem(new DeleteItemRequest()
                .withTableName(System.getenv("PRODUCTS_TABLE"))
                .withKey(keyAttribute));

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Product Deleted! ID: " + queryParameters.get(key));
    }
}
