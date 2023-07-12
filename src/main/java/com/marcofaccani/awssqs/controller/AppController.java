package com.marcofaccani.awssqs.controller;

import com.marcofaccani.awssqs.model.WireTransferMessage;
import com.marcofaccani.awssqs.channel.outbound.SqsWriterServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
@RequiredArgsConstructor
public class AppController {

  private final SqsWriterServiceImpl sqsWriterServiceImpl;

  /*
    Validation on the payload is out of scope for this project.
    Controller is used only to allow you to push messages to the queue programmatically without opening AWS SQS Dashboard
   */
  @PostMapping("/send")
  public ResponseEntity<HttpStatus> sendMessageToSqsQueue(@RequestBody final WireTransferMessage wireTransferMessage) {
    sqsWriterServiceImpl.sendMessage(wireTransferMessage);
    return ResponseEntity.ok().build();
  }

}
