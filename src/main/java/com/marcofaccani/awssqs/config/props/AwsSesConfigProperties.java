package com.marcofaccani.awssqs.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.aws.ses")
public class AwsSesConfigProperties {

  private String sender;

}
