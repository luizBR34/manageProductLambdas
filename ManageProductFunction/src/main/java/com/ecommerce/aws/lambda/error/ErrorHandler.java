package com.ecommerce.aws.lambda.error;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    public void handler(SNSEvent event) {
        event.getRecords().forEach(record -> logger.info("Dead Letter Queue Event: " + record.toString()));
    }
}
