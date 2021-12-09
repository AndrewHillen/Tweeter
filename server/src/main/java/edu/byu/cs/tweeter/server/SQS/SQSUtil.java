package edu.byu.cs.tweeter.server.SQS;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class SQSUtil
{
    AmazonSQS sqs;
    public SQSUtil()
    {
        sqs = AmazonSQSClientBuilder.defaultClient();
    }

    public void sendToQueue(String url, String message)
    {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(url)
                .withMessageBody(message)
                .withDelaySeconds(2);


        sqs.sendMessage(send_msg_request);
    }
}
