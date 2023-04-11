package com.serverless.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import lombok.extern.java.Log;

@Log
public class SnsNotificationService {

    private AmazonSNS amazonSNSClient;

    private final String EMAIL_PROTOCOL = "email";

    public SnsNotificationService() {
        amazonSNSClient = AmazonSNSClientBuilder.defaultClient();
    }

    public void subscribeToTopicWithEmail(String emailAddress, String topicArn) {
        log.info("Subscribing with email " + emailAddress + "to FruitNotificationTopic");
        var subscribeRequest = new SubscribeRequest(topicArn, EMAIL_PROTOCOL, emailAddress);
        amazonSNSClient.subscribe(subscribeRequest);
    }

    public void notifyWithEmailForTopic(String topicArn, String email, String message) {
        log.info("Publishing info about new fruit to operations");
        var publishRequest = new PublishRequest(topicArn, message, email);
        amazonSNSClient.publish(publishRequest);
    }

}
