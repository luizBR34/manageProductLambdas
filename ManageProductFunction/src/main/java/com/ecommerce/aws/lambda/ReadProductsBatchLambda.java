package com.ecommerce.aws.lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.ecommerce.aws.lambda.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReadProductsBatchLambda {

    Logger logger = LoggerFactory.getLogger(ReadProductsBatchLambda.class);
    public static final String PRODUCTS_TOPIC = System.getenv("PRODUCTS_TOPIC");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final AmazonSNS sns =  AmazonSNSClientBuilder.defaultClient();
    public void getS3Resource(S3Event event) {

        event.getRecords().forEach(record -> {

            S3ObjectInputStream s3ObjectInputStream = s3.getObject(record.getS3().getBucket().getName(),
                    record.getS3().getObject().getKey()).getObjectContent();

            try {
                logger.info("Reading data file from S3.");
                List<Product> productList = Arrays.asList(objectMapper.readValue(s3ObjectInputStream,
                        Product[].class));
                logger.info(productList.toString());
                s3ObjectInputStream.close();
                logger.info("Message being published to SNS.");
                publishMessageToSNS(productList);
            } catch (IOException e) {
                logger.error("Exception: ", e);
                throw new RuntimeException("Error while processing S3 event: ", e);
            }
        });

    }

    private void publishMessageToSNS(List<Product> productList) {
        productList.forEach(product -> {
            try {
                sns.publish(PRODUCTS_TOPIC, objectMapper.writeValueAsString(product));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
