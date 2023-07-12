package com.marcofaccani.awssqs.service;

import com.marcofaccani.awssqs.config.props.AwsSesConfigProperties;
import com.marcofaccani.awssqs.service.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailServiceImpl implements MailService {

  private final SesClient sesClient;
  private final MailSender mailSender;
  private final AwsSesConfigProperties awsSesConfigProps;

  @Override
  public void sendMail(final String recipientEmail) {

    //TODO: insert check to verify if the email is already verified. If not, verify it
    sesClient.verifyEmailAddress(request -> request.emailAddress(awsSesConfigProps.getSender()).build());
    sesClient.verifyEmailAddress(request -> request.emailAddress(recipientEmail).build());

    final var simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(awsSesConfigProps.getSender());
    simpleMailMessage.setTo(recipientEmail);
    simpleMailMessage.setSubject("AWS SES Test");
    simpleMailMessage.setText("Hello, World!");

    mailSender.send(simpleMailMessage);
    log.info("Email sent successfully to {}", recipientEmail);
  }
}
