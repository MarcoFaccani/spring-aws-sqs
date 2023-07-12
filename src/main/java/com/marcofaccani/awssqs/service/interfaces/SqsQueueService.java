package com.marcofaccani.awssqs.service.interfaces;

import java.util.Optional;

public interface SqsQueueService {

  String ERR_MSG_QUEUE_NOT_EXISTS = "Queue with name %s does not exist";
  String ERR_MSG_CREATING_QUEUE = "Error creating Queue with name %s: Error message: %s";
  String ERR_MSG_GET_QUEUE_URL_GENERAL_EX = "Error retrieving URL for queue %s. Error message: %s";

  void createQueueIfNotExists(String awsQueueName);
  Optional<String> getQueueUrl(String awsQueueName);

}
