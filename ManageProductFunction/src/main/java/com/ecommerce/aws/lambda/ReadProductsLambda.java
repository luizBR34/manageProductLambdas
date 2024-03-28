package com.ecommerce.aws.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ecommerce.aws.lambda.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReadProductsLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public APIGatewayProxyResponseEvent readProducts(APIGatewayProxyRequestEvent request) throws JsonProcessingException {

        ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv("PRODUCTS_TABLE")));
        List<Product> products = scanResult.getItems().stream().map(item -> new Product(Integer.parseInt(item.get("productId").getN()),
                        item.get("productName").getS(),
                        item.get("productDescription").getS(),
                        Double.parseDouble(item.get("productPrice").getN()),
                        Integer.parseInt(item.get("productStock").getN()),
                        item.get("productImageUrl").getS()))
                .toList();

        String jsonOutput = objectMapper.writeValueAsString(products);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonOutput);
    }
}
