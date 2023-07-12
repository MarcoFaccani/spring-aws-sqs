package com.marcofaccani.awssqs.service.interfaces;

import com.marcofaccani.awssqs.model.WireTransferMessage;

public interface SqsWriterService {

  String ERR_MSG_SEND_MESSAGE_TO_SQS = "Error sending message to SQS. Error message: %s";

  void sendMessage(WireTransferMessage wireTransferMessage);

}
